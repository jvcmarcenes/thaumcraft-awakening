package io.github.jvcmarcenes.tca.blocks.AspectAnalyzer;

import io.github.jvcmarcenes.tca.init.ModBlocks;
import io.github.jvcmarcenes.tca.init.ModContainerTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IWorldPosCallable;
import net.minecraftforge.items.SlotItemHandler;


public class AspectAnalyzerContainer extends Container {

    private final IWorldPosCallable canInteractWithCallable;

    public AspectAnalyzerContainer(int windowId, PlayerInventory playerInv, PacketBuffer data) {
        this(windowId, playerInv, (AspectAnalyzerTE)playerInv.player.world.getTileEntity(data.readBlockPos()));
    }

    public AspectAnalyzerContainer(int windowId, PlayerInventory playerInv, AspectAnalyzerTE te) {
        super(ModContainerTypes.ASPECT_ANALYZER.get(), windowId);

        this.canInteractWithCallable = IWorldPosCallable.of(te.getWorld(), te.getPos());

        final int slotSizePlus2 = 18;

        this.addSlot(new SlotItemHandler(te.item, 0, 0, 0));

        //Player Inventory
        final int playerInvStartX = 8;
        final int playerInvStartY = 84;
        final int playerHotbarY = playerInvStartY + slotSizePlus2 * 3 + 4;
        for (int row = 0; row < 3; row++) for (int column = 0; column < 9; column++)
            this.addSlot(new Slot(playerInv,
                9 + (row * 9) + column,
                playerInvStartX + (column * slotSizePlus2),
                playerInvStartY + (row * slotSizePlus2)
            ));
        for (int column = 0; column < 9; column++)
            this.addSlot(new Slot(playerInv,
                column,
                playerInvStartX + (column * slotSizePlus2),
                playerHotbarY
            ));
    }

    @Override
    public boolean canInteractWith(PlayerEntity player) {
        return isWithinUsableDistance(canInteractWithCallable, player, ModBlocks.ASPECT_ANALYZER.get());
    }
}
