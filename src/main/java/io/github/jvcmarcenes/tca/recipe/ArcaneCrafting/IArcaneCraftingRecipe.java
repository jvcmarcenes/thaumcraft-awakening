package io.github.jvcmarcenes.tca.recipe.ArcaneCrafting;

import io.github.jvcmarcenes.tca.recipe.ModRecipeTypes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;

public interface IArcaneCraftingRecipe extends IRecipe<ArcaneCraftingInventory> {

    default IRecipeType<?> getType() {
        return ModRecipeTypes.ARCANE;
    }

    int getVisCost();
    String getRequiredAspect();
}
