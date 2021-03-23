package io.github.jvcmarcenes.tca.recipe.Alchemy;

import java.util.function.Consumer;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import io.github.jvcmarcenes.tca.alchemy.AspectGroup;
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
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class AlchemyRecipeBuilder {
 
    private final Item result;
    private final int count;

    private Item catalyst;
    private final AspectGroup aspects = new AspectGroup();

    private final Advancement.Builder advancementBuilder = Advancement.Builder.builder();

    public AlchemyRecipeBuilder(IItemProvider result, int count) {
      this.result = result.asItem();
      this.count = count;
    }

    public static AlchemyRecipeBuilder create(IItemProvider result) {
      return create(result, 1);
    }

    public static AlchemyRecipeBuilder create(IItemProvider result, int count) {
      return new AlchemyRecipeBuilder(result, count);
    }

    public AlchemyRecipeBuilder aspect(String aspect, int amount) {
      aspects.add(aspect, amount);
      return this;
    }

    public AlchemyRecipeBuilder catalyst(IItemProvider catalyst) {
      this.catalyst = catalyst.asItem();
      return this;
    }

    public AlchemyRecipeBuilder catalyst(Ingredient catalyst) {
      //this.catalyst = catalyst.item
      return this;
    }

    public AlchemyRecipeBuilder addCriterion(String name, ICriterionInstance criterionIn) {
      advancementBuilder.withCriterion(name, criterionIn);
      return this;
    }

    public void build(Consumer<IFinishedRecipe> consumerIn) {
      build(consumerIn, ForgeRegistries.ITEMS.getKey(result));
    }

    public void build(Consumer<IFinishedRecipe> consumerIn, String save) {
      ResourceLocation resourcelocation = ForgeRegistries.ITEMS.getKey(result);

      if ((new ResourceLocation(save)).equals(resourcelocation))
        throw new IllegalStateException("Alchemy Recipe " + save + " should remove its 'save' argument");
      else
        build(consumerIn, new ResourceLocation(save));
    }

  public void build(Consumer<IFinishedRecipe> consumerIn, ResourceLocation id) {
    this.validate(id);
    this.advancementBuilder.withParentId(new ResourceLocation("recipes/root"))
      .withCriterion("has_the_recipe", RecipeUnlockedTrigger.create(id))
      .withRewards(AdvancementRewards.Builder.recipe(id))
      .withRequirementsStrategy(IRequirementsStrategy.OR);

    ResourceLocation advancementId = new ResourceLocation(id.getNamespace(), "recipes/" + result.getGroup().getPath() + "/" + id.getPath());
    consumerIn.accept(new AlchemyRecipeBuilder.Result(id, result, count, aspects, catalyst, advancementBuilder, advancementId));
  }

  private void validate(ResourceLocation id) {
    if (aspects.hasNone())
      throw new IllegalStateException("No aspects required for shaped recipe " + id + "!");
    else if (catalyst == null)
      throw new IllegalStateException("No catalyst set for shaped recipe " + id + "!");
    else if (advancementBuilder.getCriteria().isEmpty())
      throw new IllegalStateException("No way of obtaining recipe " + id);
  }

  public class Result implements IFinishedRecipe {
    private final ResourceLocation id;
    private final Item result;
    private final int count;
    private final AspectGroup aspects;
    private final Item catalyst;

    private final Advancement.Builder advancementBuilder;
    private final ResourceLocation advancementId;

    public Result(ResourceLocation id, Item result, int count, AspectGroup aspects, Item catalyst, Advancement.Builder advancementBuilder, ResourceLocation advancementId) {
      this.id = id;
      this.result = result;
      this.count = count;
      this.aspects = aspects;
      this.catalyst = catalyst;

      this.advancementBuilder = advancementBuilder;
      this.advancementId = advancementId;
    }

    @Override
    public void serialize(JsonObject json) {
      JsonArray jsonAspects = new JsonArray();
      aspects.forEach((aspect, amount) -> {
        JsonObject obj = new JsonObject();
        obj.addProperty("name", aspect);
        obj.addProperty("amount", amount);
        jsonAspects.add(obj);
      });
      json.add("aspects", jsonAspects);

      json.addProperty("catalyst", ForgeRegistries.ITEMS.getKey(catalyst).toString());

      JsonObject jsonOutput = new JsonObject();
      jsonOutput.addProperty("item", ForgeRegistries.ITEMS.getKey(result).toString());
      jsonOutput.addProperty("count", count);
      json.add("output", jsonOutput);
    }

    @Override
    public IRecipeSerializer<?> getSerializer() { return ModRecipeSerializers.ALCHEMY.get(); }

    @Override
    public ResourceLocation getID() { return id; }

    @Override
    public JsonObject getAdvancementJson() { return advancementBuilder.serialize(); }

    @Override
    public ResourceLocation getAdvancementID() { return advancementId; }
  }
}
