package io.github.jvcmarcenes.tca.recipe;

import io.github.jvcmarcenes.tca.TCA;
import io.github.jvcmarcenes.tca.recipe.Alchemy.AlchemyRecipe;
import io.github.jvcmarcenes.tca.recipe.ArcaneCrafting.IArcaneCraftingRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TCA.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModRecipeTypes {

    public static IRecipeType<IArcaneCraftingRecipe> ARCANE;
    public static IRecipeType<AlchemyRecipe> ALCHEMY;


    @SubscribeEvent
    public static void registerRecipeTypes(RegistryEvent.Register<IRecipeSerializer<?>> e) {
        ARCANE = IRecipeType.register("arcane_crafting");
        ALCHEMY = IRecipeType.register("alchemy");
    }
}
