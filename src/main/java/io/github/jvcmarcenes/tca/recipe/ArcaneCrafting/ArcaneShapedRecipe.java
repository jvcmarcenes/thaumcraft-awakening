package io.github.jvcmarcenes.tca.recipe.ArcaneCrafting;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.*;
import io.github.jvcmarcenes.tca.TCA;
import io.github.jvcmarcenes.tca.init.ModRecipeSerializers;
import io.github.jvcmarcenes.tca.util.JsonHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;
import java.util.Set;

public class ArcaneShapedRecipe implements IArcaneCraftingRecipe {
  static int MAX_WIDTH = 3;
  static int MAX_HEIGHT = 3;

  private final int recipeWidth;
  private final int recipeHeight;

  private final NonNullList<Ingredient> recipeItems;
  private final ItemStack recipeOutput;
  private final int visCost;
  private final String aspect;

  private final ResourceLocation id;

  public ArcaneShapedRecipe(ResourceLocation idIn, int recipeWidthIn, int recipeHeightIn, NonNullList<Ingredient> recipeItemsIn, ItemStack recipeOutputIn, int visCost, String aspect) {        this.id = idIn;
    this.recipeWidth = recipeWidthIn;
    this.recipeHeight = recipeHeightIn;
    this.recipeItems = recipeItemsIn;
    this.recipeOutput = recipeOutputIn;
    this.visCost = visCost;
    this.aspect = aspect;
  }

  @Override
  public String getRequiredAspect() { return aspect; }

  @Override 
  public int getVisCost() { return visCost; }

  @Override
  public boolean matches(ArcaneCraftingInventory inv, World worldIn) {
    boolean matrixMatch = matrixMatch(inv);
    return matrixMatch && aspect.equals(inv.getAspect());
  }

  private boolean matrixMatch(ArcaneCraftingInventory inv) {
    for(int i = 0; i <= inv.getWidth() - this.recipeWidth; ++i) {
      for(int j = 0; j <= inv.getHeight() - this.recipeHeight; ++j) {
        if (this.checkMatch(inv, i, j, true)) return true;
        if (this.checkMatch(inv, i, j, false)) return true;
      }
    }

    return false;
  }

  private boolean checkMatch(ArcaneCraftingInventory craftingInventory, int width, int height, boolean mirror) {
    for(int i = 0; i < craftingInventory.getWidth(); ++i) {
      for(int j = 0; j < craftingInventory.getHeight(); ++j) {
        int k = i - width;
        int l = j - height;
        Ingredient ingredient = Ingredient.EMPTY;
        if (k >= 0 && l >= 0 && k < this.recipeWidth && l < this.recipeHeight) {
          if (mirror)
            ingredient = this.recipeItems.get(this.recipeWidth - k - 1 + l * this.recipeWidth);
          else
            ingredient = this.recipeItems.get(k + l * this.recipeWidth);
        }

        if (!ingredient.test(craftingInventory.getStackInSlot(i + j * craftingInventory.getWidth())))
          return false;
      }
    }

    return true;
  }

  @Override
  public ItemStack getCraftingResult(ArcaneCraftingInventory inv) {
    return getRecipeOutput().copy();
  }

  @Override
  public boolean canFit(int width, int height) {
    return width >= recipeWidth && height >= recipeHeight;
  }

  @Override
  public ItemStack getRecipeOutput() { return recipeOutput; }

  @Override
  public ResourceLocation getId() { return id; }

  @Override
  public IRecipeSerializer<?> getSerializer() { return ModRecipeSerializers.ARCANE_SHAPED.get(); }

  // Json Deserialization Helpers
  private static NonNullList<Ingredient> deserializeIngredients(String[] pattern, Map<String, Ingredient> keys, int patternWidth, int patternHeight) {
    NonNullList<Ingredient> nonnulllist = NonNullList.withSize(patternWidth * patternHeight, Ingredient.EMPTY);
    Set<String> set = Sets.newHashSet(keys.keySet());
    set.remove(" ");

    for(int i = 0; i < pattern.length; ++i)
      for(int j = 0; j < pattern[i].length(); ++j) {
        String s = pattern[i].substring(j, j + 1);
        Ingredient ingredient = keys.get(s);
        if (ingredient == null) throw new JsonSyntaxException("Pattern references symbol '" + s + "' but it's not defined in the key");

        set.remove(s);
        nonnulllist.set(j + patternWidth * i, ingredient);
      }

    if (!set.isEmpty()) throw new JsonSyntaxException("Key defines symbols that aren't used in pattern: " + set);
    else return nonnulllist;
  }

  @VisibleForTesting
  static String[] shrink(String... toShrink) {
    int i = Integer.MAX_VALUE;
    int j = 0;
    int k = 0;
    int l = 0;

    for(int i1 = 0; i1 < toShrink.length; ++i1) {
      String s = toShrink[i1];
      i = Math.min(i, firstNonSpace(s));
      int j1 = lastNonSpace(s);
      j = Math.max(j, j1);
      if (j1 < 0) {
        if (k == i1) k++;
        l++;
      } else l = 0;
    }

    if (toShrink.length == l) return new String[0];
    else {
      String[] astring = new String[toShrink.length - l - k];

      for(int k1 = 0; k1 < astring.length; ++k1)
        astring[k1] = toShrink[k1 + k].substring(i, j + 1);

      return astring;
    }
  }

  private static int firstNonSpace(String str) {
    int i;
    for(i = 0; i < str.length() && str.charAt(i) == ' '; ++i) {}
    return i;
  }

  private static int lastNonSpace(String str) {
    int i;
    for(i = str.length() - 1; i >= 0 && str.charAt(i) == ' '; --i) {}
    return i;
  }

  private static String[] patternFromJson(JsonArray jsonArr) {
    String[] astring = new String[jsonArr.size()];

    if (astring.length > MAX_HEIGHT)
      throw new JsonSyntaxException("Invalid pattern: too many rows, " + MAX_HEIGHT + " is maximum");
    else if (astring.length == 0)
      throw new JsonSyntaxException("Invalid pattern: empty pattern not allowed");
    else {
      for (int i = 0; i < astring.length; ++i) {
        String s = JSONUtils.getString(jsonArr.get(i), "pattern[" + i + "]");
        if (s.length() > MAX_WIDTH)
          throw new JsonSyntaxException("Invalid pattern: too many columns, " + MAX_WIDTH + " is maximum");

        if (i > 0 && astring[0].length() != s.length())
          throw new JsonSyntaxException("Invalid pattern: each row must be the same width");

        astring[i] = s;
      }

      return astring;
    }
  }

  private static Map<String, Ingredient> deserializeKey(JsonObject json) {
    Map<String, Ingredient> map = Maps.newHashMap();

    for(Map.Entry<String, JsonElement> entry : json.entrySet()) {
      if (entry.getKey().length() != 1)
        throw new JsonSyntaxException("Invalid key entry: '" + (String)entry.getKey() + "' is an invalid symbol (must be 1 character only).");

      if (" ".equals(entry.getKey()))
        throw new JsonSyntaxException("Invalid key entry: ' ' is a reserved symbol.");

      map.put(entry.getKey(), Ingredient.deserialize(entry.getValue()));
    }

    map.put(" ", Ingredient.EMPTY);
    return map;
  }

  public static ItemStack deserializeItem(JsonObject object) {
    String s = JSONUtils.getString(object, "item");

    TCA.LOGGER.info("deserializing item: " + s);

    Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(s));

    int i = JSONUtils.getInt(object, "count", 1);
    return new ItemStack(item, i);
  }

  public static class Serializer extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<ArcaneShapedRecipe> {

    public ArcaneShapedRecipe read(ResourceLocation id, JsonObject json) {
      Map<String, Ingredient> map = ArcaneShapedRecipe.deserializeKey(JSONUtils.getJsonObject(json, "key"));
      String[] matrix = ArcaneShapedRecipe.shrink(ArcaneShapedRecipe.patternFromJson(JSONUtils.getJsonArray(json, "pattern")));
      int width = matrix[0].length();
      int height = matrix.length;
      NonNullList<Ingredient> ingredients = ArcaneShapedRecipe.deserializeIngredients(matrix, map, width, height);
      int visCost = JSONUtils.getInt(json, "vis_cost");
      String aspect = JSONUtils.getString(json, "aspect");
      ItemStack output = JsonHelper.deserializeItemStack(JSONUtils.getJsonObject(json, "result"));
      return new ArcaneShapedRecipe(id, width, height, ingredients, output, visCost, aspect);
    }

    public ArcaneShapedRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
      int width = buffer.readVarInt();
      int height = buffer.readVarInt();

      NonNullList<Ingredient> recipeItems = NonNullList.withSize(width * height, Ingredient.EMPTY);
      for(int k = 0; k < recipeItems.size(); ++k) recipeItems.set(k, Ingredient.read(buffer));

      int visCost = buffer.readVarInt();
      String aspect = buffer.readString();

      ItemStack itemstack = buffer.readItemStack();

      return new ArcaneShapedRecipe(recipeId, width, height, recipeItems, itemstack, visCost, aspect);
    }

    public void write(PacketBuffer buffer, ArcaneShapedRecipe recipe) {
      buffer.writeVarInt(recipe.recipeWidth);
      buffer.writeVarInt(recipe.recipeHeight);

      for(Ingredient ingredient : recipe.recipeItems) {
        ingredient.write(buffer);
      }

      buffer.writeVarInt(recipe.visCost);
      buffer.writeString(recipe.aspect);

      buffer.writeItemStack(recipe.recipeOutput);
    }
  }
}