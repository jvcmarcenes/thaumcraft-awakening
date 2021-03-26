package io.github.jvcmarcenes.tca.recipe.ArcaneCrafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.github.jvcmarcenes.tca.init.ModRecipeSerializers;
import io.github.jvcmarcenes.tca.util.JsonHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.RecipeMatcher;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class ArcaneShapelessRecipe implements IArcaneCraftingRecipe {

  private final NonNullList<Ingredient> ingredients;
  private final ItemStack output;
  private final int visCost;
  private final String aspect;

  private final ResourceLocation id;

  public ArcaneShapelessRecipe(ResourceLocation id, NonNullList<Ingredient> ingredients, ItemStack output, int visCost, String aspect) {
    this.id = id;
    this.ingredients = ingredients;
    this.output = output;
    this.visCost = visCost;
    this.aspect = aspect;
  }

  @Override
  public int getVisCost() { return visCost; }

  @Override
  public String getRequiredAspect() { return aspect; }

  @Override
  public boolean matches(ArcaneCraftingInventory inv, World worldIn) {
    java.util.List<ItemStack> inputs = new java.util.ArrayList<>();
    int i = 0;

    for(int j = 0; j < inv.getSizeInventory(); ++j) {
      ItemStack itemstack = inv.getStackInSlot(j);
      if (!itemstack.isEmpty()) {
        ++i;
        inputs.add(itemstack);
      }
    }

    return i == ingredients.size() && RecipeMatcher.findMatches(inputs,  ingredients) != null && aspect.equals(inv.getAspect());
  }

  @Override
  public boolean canFit(int width, int height) {
    return width * height <= ingredients.size();
  }

  @Override
  public ItemStack getRecipeOutput() { return output; }

  @Override
  public ItemStack getCraftingResult(ArcaneCraftingInventory inv) { return output.copy(); }

  @Override
  public ResourceLocation getId() { return id; }

  @Override
  public IRecipeSerializer<?> getSerializer() { return ModRecipeSerializers.ARCANE_SHAPELESS.get(); }

  public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<ArcaneShapelessRecipe> {

    @Override
    public ArcaneShapelessRecipe read(ResourceLocation recipeId, JsonObject json) {
      
      JsonArray jsonIngredients = JSONUtils.getJsonArray(json, "ingredients");
      NonNullList<Ingredient> ingredients = NonNullList.create();
      for (JsonElement element : jsonIngredients)
        ingredients.add(Ingredient.deserialize(element));

      int visCost = JSONUtils.getInt(json, "visCost");
      String aspect = JSONUtils.getString(json, "aspect");

      ItemStack output = JsonHelper.deserializeItemStack(JSONUtils.getJsonObject(json, "output"));

      return new ArcaneShapelessRecipe(recipeId, ingredients, output, visCost, aspect);
    }

    @Override
    public ArcaneShapelessRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
      int size = buffer.readVarInt();

      NonNullList<Ingredient> ingredients = NonNullList.withSize(size, Ingredient.EMPTY);
      for (int i = 0; i < size; i++) ingredients.set(size, Ingredient.read(buffer));

      int visCost = buffer.readVarInt();
      String aspect = buffer.readString();

      ItemStack output = buffer.readItemStack();

      return new ArcaneShapelessRecipe(recipeId, ingredients, output, visCost, aspect);
    }

    @Override
    public void write(PacketBuffer buffer, ArcaneShapelessRecipe recipe) {
      buffer.writeVarInt(recipe.ingredients.size());
      for (Ingredient ingredient : recipe.ingredients)
        ingredient.write(buffer);

      buffer.writeVarInt(recipe.visCost);
      buffer.writeString(recipe.aspect);

      buffer.writeItemStack(recipe.output);
    }
  }
}
