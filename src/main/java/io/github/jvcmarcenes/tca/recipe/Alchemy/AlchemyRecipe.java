package io.github.jvcmarcenes.tca.recipe.Alchemy;

import java.util.concurrent.atomic.AtomicReference;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import io.github.jvcmarcenes.tca.alchemy.AspectGroup;
import io.github.jvcmarcenes.tca.init.ModRecipeSerializers;
import io.github.jvcmarcenes.tca.recipe.ModRecipeTypes;
import io.github.jvcmarcenes.tca.util.JsonHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class AlchemyRecipe implements IRecipe<AlchemyRecipeInventory> {

  private final AspectGroup aspects;
  private final Item catalyst;
  private final ItemStack output;

  private final ResourceLocation id;

  public AlchemyRecipe(ResourceLocation id, AspectGroup aspects, Item catalyst, ItemStack output) {
    this.id = id;
    this.aspects = aspects;
    this.catalyst = catalyst;
    this.output = output;
  }

  @Override
  public boolean matches(AlchemyRecipeInventory inv, World world) {
    AtomicReference<Boolean> ret = new AtomicReference<>(true);
    aspects.forEach((aspect, amount) -> {
      ret.set(ret.get() && amount <= inv.getAmountOfAspect(aspect));
    });
    return ret.get() && catalyst == inv.getCatalyst();
  }

  public AspectGroup getRequiredAspects() { return aspects; }

  public Item getCatalyst() { return catalyst; }

  @Override
  public ItemStack getCraftingResult(AlchemyRecipeInventory inv) { return output.copy(); }

  @Override
  public boolean canFit(int width, int height) { return true; }

  @Override
  public ItemStack getRecipeOutput() { return output; }

  @Override
  public ResourceLocation getId() { return id; }

  @Override
  public IRecipeSerializer<?> getSerializer() { return ModRecipeSerializers.ALCHEMY.get(); }

  @Override
  public IRecipeType<?> getType() { return ModRecipeTypes.ALCHEMY; }

  public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<AlchemyRecipe> {
  
    @Override
    public AlchemyRecipe read(ResourceLocation recipeId, JsonObject json) {
      JsonArray jsonAspects = JSONUtils.getJsonArray(json, "aspects", new JsonArray());

      AspectGroup aspects = new AspectGroup();
      jsonAspects.forEach((element) -> {
        String aspect = JSONUtils.getString(element.getAsJsonObject(), "name");
        int amount = JSONUtils.getInt(element.getAsJsonObject(), "amount");
        aspects.add(aspect, amount);
      });

      Item catalyst = JsonHelper.deserializeItem(json, "catalyst");
      ItemStack output = JsonHelper.deserializeItemStack(JSONUtils.getJsonObject(json, "output"));

      return new AlchemyRecipe(recipeId, aspects, catalyst, output);
    }
  
    @Override
    public AlchemyRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
      int size = buffer.readVarInt();

      AspectGroup aspects = new AspectGroup();
      for (int i = 0; i < size; i++)
        aspects.add(buffer.readString(), buffer.readVarInt());

      Item catalyst = buffer.readItemStack().getItem();
      ItemStack output = buffer.readItemStack();

      return new AlchemyRecipe(recipeId, aspects, catalyst, output);
    }
      
    @Override
    public void write(PacketBuffer buffer, AlchemyRecipe recipe) {
      buffer.writeVarInt(recipe.aspects.size());

      recipe.aspects.forEach((aspect, amount) -> {
        buffer.writeString(aspect);
        buffer.writeVarInt(amount);
      });

      buffer.writeItemStack(new ItemStack(recipe.catalyst));
      buffer.writeItemStack(recipe.output);
    }
  }
}
