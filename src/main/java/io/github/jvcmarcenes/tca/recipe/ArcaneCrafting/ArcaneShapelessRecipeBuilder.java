package io.github.jvcmarcenes.tca.recipe.ArcaneCrafting;

import java.util.List;
import java.util.function.Consumer;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import io.github.jvcmarcenes.tca.alchemy.Aspects;
import io.github.jvcmarcenes.tca.init.ModRecipeSerializers;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.IRequirementsStrategy;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class ArcaneShapelessRecipeBuilder {
  
  private final Item result;
  private final int count;
  private final List<Ingredient> ingredients = Lists.newArrayList();

  private int visCost = 0;
  private String aspect = Aspects.NONE;

  private final Advancement.Builder advancementBuilder = Advancement.Builder.builder();

  public ArcaneShapelessRecipeBuilder(IItemProvider result, int count) {
    this.result = result.asItem();
    this.count = count;
  }

  public static ArcaneShapelessRecipeBuilder create(IItemProvider result) {
    return new ArcaneShapelessRecipeBuilder(result, 1);
  }

  public static ArcaneShapelessRecipeBuilder create(IItemProvider result, int count) {
    return new ArcaneShapelessRecipeBuilder(result, count);
  }

  public ArcaneShapelessRecipeBuilder visCost(int visCost) {
    this.visCost = visCost;
    return this;
  }

  public ArcaneShapelessRecipeBuilder aspect(String aspect) {
    this.aspect = aspect;
    return this;
  }

  public ArcaneShapelessRecipeBuilder addIngredient(ITag<Item> tagIn) {
    return this.addIngredient(Ingredient.fromTag(tagIn));
  }

  public ArcaneShapelessRecipeBuilder addIngredient(IItemProvider itemIn) {
    return this.addIngredient(itemIn, 1);
  }

  public ArcaneShapelessRecipeBuilder addIngredient(IItemProvider itemIn, int quantity) {
    for(int i = 0; i < quantity; ++i)
        this.addIngredient(Ingredient.fromItems(itemIn));

    return this;
  }

  public ArcaneShapelessRecipeBuilder addIngredient(Ingredient ingredientIn) {
    return this.addIngredient(ingredientIn, 1);
  }

  public ArcaneShapelessRecipeBuilder addIngredient(Ingredient ingredientIn, int quantity) {
    for(int i = 0; i < quantity; ++i)
      this.ingredients.add(ingredientIn);

    return this;
  }

  public ArcaneShapelessRecipeBuilder addCriterion(String name, ICriterionInstance criterionIn) {
    this.advancementBuilder.withCriterion(name, criterionIn);
    return this;
  }

  public void build(Consumer<IFinishedRecipe> consumerIn) {
    this.build(consumerIn, ForgeRegistries.ITEMS.getKey(this.result));
  }

  public void build(Consumer<IFinishedRecipe> consumerIn, String save) {
    ResourceLocation resourcelocation = ForgeRegistries.ITEMS.getKey(this.result);
    if ((new ResourceLocation(save)).equals(resourcelocation))
      throw new IllegalStateException("Shapeless Recipe " + save + " should remove its 'save' argument");
    else
      this.build(consumerIn, new ResourceLocation(save));
  }

  public void build(Consumer<IFinishedRecipe> consumerIn, ResourceLocation id) {
    this.validate(id);
    this.advancementBuilder.withParentId(new ResourceLocation("recipes/root")).withCriterion("has_the_recipe", RecipeUnlockedTrigger.create(id)).withRewards(AdvancementRewards.Builder.recipe(id)).withRequirementsStrategy(IRequirementsStrategy.OR);
    consumerIn.accept(new ArcaneShapelessRecipeBuilder.Result(id, result, count, ingredients, visCost, aspect, advancementBuilder, new ResourceLocation(id.getNamespace(), "recipes/" + this.result.getGroup().getPath() + "/" + id.getPath())));
  }

  private void validate(ResourceLocation id) {
    if (this.advancementBuilder.getCriteria().isEmpty())
      throw new IllegalStateException("No way of obtaining recipe " + id);
  }

  public static class Result implements IFinishedRecipe {
    private final ResourceLocation id;
    private final Item result;
    private final int count;
    private final List<Ingredient> ingredients;
    private final int visCost;
    private final String aspect;

    private final Advancement.Builder advancementBuilder;
    private final ResourceLocation advancementId;

    public Result(ResourceLocation id, Item result, int count, List<Ingredient> ingredients, int visCost, String aspect, Advancement.Builder advancementBuilderIn, ResourceLocation advancementIdIn) {
      this.id = id;
      this.result = result;
      this.count = count;
      this.ingredients = ingredients;
      this.visCost = visCost;
      this.aspect = aspect;

      this.advancementBuilder = advancementBuilderIn;
      this.advancementId = advancementIdIn;
    }

    public void serialize(JsonObject json) {
      JsonArray jsonIngredients = new JsonArray();

      for(Ingredient ingredient : ingredients)
        jsonIngredients.add(ingredient.serialize());
      json.add("ingredients", jsonIngredients);

      json.addProperty("visCost", visCost);
      json.addProperty("aspect", aspect);

      JsonObject jsonobject = new JsonObject();
      jsonobject.addProperty("item", ForgeRegistries.ITEMS.getKey(result).toString());
      jsonobject.addProperty("count", count);

      json.add("output", jsonobject);
    }

    public IRecipeSerializer<?> getSerializer() {
       return ModRecipeSerializers.ARCANE_SHAPELESS.get();
    }

    public ResourceLocation getID() { return id; }

    @Nullable
    public JsonObject getAdvancementJson() { return advancementBuilder.serialize(); }

    @Nullable
    public ResourceLocation getAdvancementID() { return advancementId; }

  }
}
