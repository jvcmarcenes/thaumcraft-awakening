package io.github.jvcmarcenes.tca.recipe.ArcaneCrafting;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
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

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class ArcaneShapedRecipeBuilder {

  private final Item result;
  private final int count;
  private int visCost;
  private String aspect;
  private final List<String> pattern = Lists.newArrayList();
  private final Map<Character, Ingredient> key = Maps.newLinkedHashMap();
  private final Advancement.Builder advancementBuilder = Advancement.Builder.builder();

  public ArcaneShapedRecipeBuilder(IItemProvider result, int count) {
    this.result = result.asItem();
    this.count = count;
    this.visCost = 0;
    this.aspect = Aspects.NONE;
  }

  public static ArcaneShapedRecipeBuilder create(IItemProvider resultIn) {
    return create(resultIn, 1);
  }

  public static ArcaneShapedRecipeBuilder create(IItemProvider result, int count) {
    return new ArcaneShapedRecipeBuilder(result, count);
  }

  public ArcaneShapedRecipeBuilder key(Character symbol, ITag<Item> tagIn) {
    return this.key(symbol, Ingredient.fromTag(tagIn));
  }

  public ArcaneShapedRecipeBuilder key(Character symbol, IItemProvider itemIn) {
    return this.key(symbol, Ingredient.fromItems(itemIn));
  }

  public ArcaneShapedRecipeBuilder key(Character symbol, Ingredient ingredientIn) {
    if (key.containsKey(symbol))
      throw new IllegalArgumentException("Symbol '" + symbol + "' is already defined!");
    else if (symbol == ' ')
      throw new IllegalArgumentException("Symbol ' ' (whitespace) is reserved and cannot be defined");
    else {
      key.put(symbol, ingredientIn);
      return this;
    }
  }

  public ArcaneShapedRecipeBuilder patternLine(String patternIn) {
    if (!pattern.isEmpty() && patternIn.length() != pattern.get(0).length())
      throw new IllegalArgumentException("Pattern must be the same width on every line!");
    else {
      pattern.add(patternIn);
      return this;
    }
  }

  public ArcaneShapedRecipeBuilder visCost(int amount) {
    this.visCost = amount;
    return this;
  }

  public ArcaneShapedRecipeBuilder aspect(String aspect) {
    this.aspect = aspect;
    return this;
  }

  public ArcaneShapedRecipeBuilder addCriterion(String name, ICriterionInstance criterionIn) {
    advancementBuilder.withCriterion(name, criterionIn);
    return this;
  }

  public void build(Consumer<IFinishedRecipe> consumerIn) {
    build(consumerIn, ForgeRegistries.ITEMS.getKey(result));
  }

  public void build(Consumer<IFinishedRecipe> consumerIn, String save) {
    ResourceLocation resourcelocation = ForgeRegistries.ITEMS.getKey(result);

    if ((new ResourceLocation(save)).equals(resourcelocation))
      throw new IllegalStateException("Shaped Recipe " + save + " should remove its 'save' argument");
    else
      build(consumerIn, new ResourceLocation(save));
  }

  public void build(Consumer<IFinishedRecipe> consumerIn, ResourceLocation id) {
    this.validate(id);
    this.advancementBuilder.withParentId(new ResourceLocation("recipes/root")).withCriterion("has_the_recipe", RecipeUnlockedTrigger.create(id)).withRewards(AdvancementRewards.Builder.recipe(id)).withRequirementsStrategy(IRequirementsStrategy.OR);
    consumerIn.accept(new ArcaneShapedRecipeBuilder.Result(id, result, count, visCost, aspect, pattern, key, advancementBuilder, new ResourceLocation(id.getNamespace(), "recipes/" + result.getGroup().getPath() + "/" + id.getPath())));
  }

  private void validate(ResourceLocation id) {
    if (pattern.isEmpty())
      throw new IllegalStateException("No pattern is defined for shaped recipe " + id + "!");
    else {
      Set<Character> set = Sets.newHashSet(key.keySet());
      set.remove(' ');

      for(String s : this.pattern)
        for(int i = 0; i < s.length(); ++i) {
          char c0 = s.charAt(i);
          if (!key.containsKey(c0) && c0 != ' ')
            throw new IllegalStateException("Pattern in recipe " + id + " uses undefined symbol '" + c0 + "'");

          set.remove(c0);
        }

        if (!set.isEmpty())
          throw new IllegalStateException("Ingredients are defined but not used in pattern for recipe " + id);
        else if (pattern.size() == 1 && pattern.get(0).length() == 1)
          throw new IllegalStateException("Shaped recipe " + id + " only takes in a single item - should it be a shapeless recipe instead?");
        else if (advancementBuilder.getCriteria().isEmpty())
          throw new IllegalStateException("No way of obtaining recipe " + id);
    }
  }

  public class Result implements IFinishedRecipe {
    private final ResourceLocation id;
    private final Item result;
    private final int count;
    private final int visCost;
    private final String aspect;
    private final List<String> pattern;
    private final Map<Character, Ingredient> key;
    private final Advancement.Builder advancementBuilder;
    private final ResourceLocation advancementId;

    public Result(ResourceLocation idIn, Item resultIn, int countIn, int visCost, String aspect, List<String> patternIn, Map<Character, Ingredient> keyIn, Advancement.Builder advancementBuilderIn, ResourceLocation advancementIdIn) {
      this.id = idIn;
      this.result = resultIn;
      this.count = countIn;
      this.visCost = visCost;
      this.aspect = aspect;
      this.pattern = patternIn;
      this.key = keyIn;
      this.advancementBuilder = advancementBuilderIn;
      this.advancementId = advancementIdIn;
    }

    public void serialize(JsonObject json) {
      json.addProperty("vis_cost", visCost);
      json.addProperty("aspect", aspect);

      JsonArray patternLines = new JsonArray();
      for(String s : this.pattern) patternLines.add(s);
      json.add("pattern", patternLines);

      JsonObject jsonobject = new JsonObject();
      for(Map.Entry<Character, Ingredient> entry : this.key.entrySet())
        jsonobject.add(String.valueOf(entry.getKey()), entry.getValue().serialize());
      json.add("key", jsonobject);

      JsonObject jsonobject1 = new JsonObject();
      jsonobject1.addProperty("item", ForgeRegistries.ITEMS.getKey(this.result).toString());
      if (this.count > 1)
        jsonobject1.addProperty("count", this.count);
      json.add("result", jsonobject1);
    }

    public IRecipeSerializer<?> getSerializer() {
      return ModRecipeSerializers.ARCANE_SHAPED.get();
    }

    public ResourceLocation getID() {
      return id;
    }

    @Nullable
    public JsonObject getAdvancementJson() {
      return advancementBuilder.serialize();
    }

    @Nullable
    public ResourceLocation getAdvancementID() {
      return advancementId;
    }
  }
}
