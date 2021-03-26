package io.github.jvcmarcenes.tca.init;

import io.github.jvcmarcenes.tca.TCA;
import io.github.jvcmarcenes.tca.recipe.Alchemy.AlchemyRecipe;
import io.github.jvcmarcenes.tca.recipe.ArcaneCrafting.ArcaneShapedRecipe;
import io.github.jvcmarcenes.tca.recipe.ArcaneCrafting.ArcaneShapelessRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModRecipeSerializers {

    public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, TCA.MOD_ID);

    public static final RegistryObject<IRecipeSerializer<ArcaneShapedRecipe>> ARCANE_SHAPED;
    public static final RegistryObject<IRecipeSerializer<ArcaneShapelessRecipe>> ARCANE_SHAPELESS;
    public static final RegistryObject<IRecipeSerializer<AlchemyRecipe>> ALCHEMY;

    static {
        ARCANE_SHAPED = RECIPE_SERIALIZERS.register("arcane_shaped", ArcaneShapedRecipe.Serializer::new);
        ARCANE_SHAPELESS = RECIPE_SERIALIZERS.register("arcane_shapeless", ArcaneShapelessRecipe.Serializer::new);
        ALCHEMY = RECIPE_SERIALIZERS.register("alchemy", AlchemyRecipe.Serializer::new);
    }
}
