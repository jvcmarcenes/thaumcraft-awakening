package io.github.jvcmarcenes.tca.data.server;

import io.github.jvcmarcenes.tca.alchemy.Aspects;
import io.github.jvcmarcenes.tca.init.ModBlocks;
import io.github.jvcmarcenes.tca.init.ModItems;
import io.github.jvcmarcenes.tca.recipe.Alchemy.AlchemyRecipe;
import io.github.jvcmarcenes.tca.recipe.Alchemy.AlchemyRecipeBuilder;
import io.github.jvcmarcenes.tca.recipe.ArcaneCrafting.ArcaneShapedRecipeBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.data.*;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider {
  public ModRecipeProvider(DataGenerator generatorIn) {
    super(generatorIn);
  }

  @Override
  protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {

    ShapedRecipeBuilder.shapedRecipe(ModBlocks.TABLE.get())
      .key('s', tag(ItemTags.WOODEN_SLABS))
      .key('p', tag(ItemTags.PLANKS))
      .patternLine("sss")
      .patternLine("p p")
      .addCriterion("has_item", hasItem(ItemTags.WOODEN_SLABS))
      .build(consumer);

    ShapelessRecipeBuilder.shapelessRecipe(ModItems.SALIS_MUNDUS.get())
      .addIngredient(Items.REDSTONE)
      .addIngredient(Items.GLOWSTONE_DUST)
      .addIngredient(ModItems.VIS_CRYSTAL.get())
      .addCriterion("has_item", hasItem(ModItems.VIS_CRYSTAL.get()))
      .build(consumer);

    ArcaneShapedRecipeBuilder.create(Items.DIAMOND)
      .key('d', Blocks.DIRT)
      .key('g', Blocks.GOLD_BLOCK)
      .patternLine("ddd")
      .patternLine("dgd")
      .patternLine("ddd")
      .visCost(100)
      .addCriterion("has_item", hasItem(Blocks.GOLD_BLOCK))
      .build(consumer, "diamond_from_arcane");

    ArcaneShapedRecipeBuilder.create(ModItems.THAUMOMETER.get())
      .key('g', Items.GOLD_INGOT)
      .key('p', Items.GLASS_PANE)
      .patternLine(" g ")
      .patternLine("gpg")
      .patternLine(" g ")
      .visCost(15)
      .aspect(Aspects.ORDO)
      .addCriterion("has_item", hasItem(Items.GOLD_INGOT))
      .build(consumer);

    AlchemyRecipeBuilder.create(Items.DIAMOND, 1)
      .aspect(Aspects.VITREUS, 50)
      .aspect(Aspects.IGNIS, 20)
      .aspect(Aspects.POTENTIA, 10)
      .catalyst(Items.COAL)
      .addCriterion("has_item", hasItem(Items.COAL))
      .build(consumer, "diamond_from_alchemy");

    AlchemyRecipeBuilder.create(ModBlocks.NITOR.get(), 1)
      .aspect(Aspects.LUX, 10)
      .aspect(Aspects.IGNIS, 10)
      .aspect(Aspects.POTENTIA, 5)
      .catalyst(Items.GLOWSTONE_DUST)
      .addCriterion("has_item", hasItem(Items.GLOWSTONE_DUST))
      .build(consumer);
  }

  private static Ingredient tag(ITag<Item> itemTag) {
    return Ingredient.fromTag(itemTag);
  }
}
