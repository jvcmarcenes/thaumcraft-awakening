package io.github.jvcmarcenes.tca.blocks.ArcaneWorkbench;

import io.github.jvcmarcenes.tca.init.ModBlocks;
import io.github.jvcmarcenes.tca.init.ModContainerTypes;
import io.github.jvcmarcenes.tca.items.EssentiaStorage.AspectPhial;
import io.github.jvcmarcenes.tca.items.VisStorageItem.VisStorage;
import io.github.jvcmarcenes.tca.util.CraftingOutputSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntReferenceHolder;
import net.minecraftforge.items.SlotItemHandler;

public class ArcaneWorkbenchContainer extends Container {

  public final ArcaneWorkbenchTE tileEntity;
  private final IWorldPosCallable canInteractWithCallable;

  public ArcaneWorkbenchContainer(final int windowId, PlayerInventory playerInv, final PacketBuffer data) {
    this(windowId, playerInv, (ArcaneWorkbenchTE)playerInv.player.world.getTileEntity(data.readBlockPos()));
  }

  public ArcaneWorkbenchContainer(final int windowId, PlayerInventory playerInventory, ArcaneWorkbenchTE tileEntity) {
    super(ModContainerTypes.ARCANE_WORKBENCH.get(), windowId);

    this.tileEntity = tileEntity;
    this.canInteractWithCallable = IWorldPosCallable.of(tileEntity.getWorld(), tileEntity.getPos());

    trackInt(new IntReferenceHolder() {
      @Override public int get() { return tileEntity.getVisStorageCharge(); }
      @Override public void set(int value) { tileEntity.setVisStorageCharge(value); }
    });

    

    //Craft Matrix
    final int craftGap = 24;
    final int craftMatStartX = 68;
    final int craftMatStartY = 40;
    for (int row = 0; row < 3; row++) for (int column = 0; column < 3; column++)
      this.addSlot(new SlotItemHandler(tileEntity.inventory,
        row * 3 + column,
        craftMatStartX + column * craftGap,
        craftMatStartY + row * craftGap
      ));

    //Output Slot
    this.addSlot(new CraftingOutputSlot(tileEntity.inventory, ArcaneWorkbenchTE.OUTPUT_SLOT, 188, 64, tileEntity));

    //Vis Storage Slot
    this.addSlot(new SlotItemHandler(tileEntity.inventory, ArcaneWorkbenchTE.CRYSTAL_SLOT, 7, 115));

    //Aspect Phial Slot
    this.addSlot(new SlotItemHandler(tileEntity.inventory, ArcaneWorkbenchTE.PHIAL_SLOT, 188, 24));

    //Player Inventory
    final int invGap = 18;
    final int playerInvStartX = 22;
    final int playerInvStartY = 153;
    final int playerHotbarY = playerInvStartY + invGap * 3 + 4;
    for (int row = 0; row < 3; row++) for (int column = 0; column < 9; column++)
      this.addSlot(new Slot(playerInventory,
        9 + (row * 9) + column,
        playerInvStartX + (column * invGap),
        playerInvStartY + (row * invGap)
      ));
    for (int column = 0; column < 9; column++)
      this.addSlot(new Slot(playerInventory,
        column,
        playerInvStartX + (column * invGap),
        playerHotbarY
      ));
  }

  @Override
  public boolean canInteractWith(PlayerEntity player) {
    return isWithinUsableDistance(canInteractWithCallable, player, ModBlocks.ARCANE_WORKBENCH.get());
  }

  @Override
  public ItemStack transferStackInSlot(PlayerEntity player, int index) {
    ItemStack stack = ItemStack.EMPTY;
    Slot slot = this.inventorySlots.get(index);

    if (slot == null || !slot.getHasStack()) return stack;

    ItemStack slotStack = slot.getStack();
    stack = slotStack.copy();

    if (index >= tileEntity.inventory.getSlots()) {
      if (slotStack.getItem() instanceof VisStorage && !mergeItemStack(slotStack, ArcaneWorkbenchTE.CRYSTAL_SLOT, ArcaneWorkbenchTE.CRYSTAL_SLOT + 1, false))
        return ItemStack.EMPTY;
      if (slotStack.getItem() instanceof AspectPhial && !mergeItemStack(slotStack, ArcaneWorkbenchTE.PHIAL_SLOT, ArcaneWorkbenchTE.PHIAL_SLOT + 1, false))
        return ItemStack.EMPTY;
      if (!mergeItemStack(slotStack, 0, 9, false))
        return ItemStack.EMPTY;
    } else if (index == ArcaneWorkbenchTE.OUTPUT_SLOT) {
      return tileEntity.onCraftMany(player.inventory);
    } else {
      if (!mergeItemStack(slotStack, tileEntity.inventory.getSlots(), inventorySlots.size(), false))
        return ItemStack.EMPTY;
    }

    if (slotStack.isEmpty()) slot.putStack(ItemStack.EMPTY);
    else slot.onSlotChanged();

    return stack;
  }

}
