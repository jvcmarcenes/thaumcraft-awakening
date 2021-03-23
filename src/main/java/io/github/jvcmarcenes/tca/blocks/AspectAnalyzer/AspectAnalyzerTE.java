package io.github.jvcmarcenes.tca.blocks.AspectAnalyzer;

import io.github.jvcmarcenes.tca.init.ModBlocks;
import io.github.jvcmarcenes.tca.init.ModTileEntityTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class AspectAnalyzerTE extends TileEntity implements INamedContainerProvider {

    public AspectAnalyzerTE() {
        super(ModTileEntityTypes.ASPECT_ANALYZER.get());
    }

    public final ItemStackHandler item = new ItemStackHandler(1);

    @Override
    public ITextComponent getDisplayName() {
        return ModBlocks.ASPECT_ANALYZER.get().getTranslatedName();
    }

    @Nullable @Override
    public Container createMenu(int windowId, PlayerInventory playerInv, PlayerEntity player) {
        return new AspectAnalyzerContainer(windowId, playerInv, this);
    }
}
