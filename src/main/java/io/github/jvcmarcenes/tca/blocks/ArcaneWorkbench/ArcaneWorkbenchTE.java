package io.github.jvcmarcenes.tca.blocks.ArcaneWorkbench;

import io.github.jvcmarcenes.tca.alchemy.IAspectStorage;
import io.github.jvcmarcenes.tca.init.ModBlocks;
import io.github.jvcmarcenes.tca.items.AspectPhial;
import io.github.jvcmarcenes.tca.items.VisStorageItem.VisStorage;
import io.github.jvcmarcenes.tca.recipe.ArcaneCrafting.ArcaneCraftingInventory;
import io.github.jvcmarcenes.tca.util.ICraftingTE;
import io.github.jvcmarcenes.tca.init.ModTileEntityTypes;
import io.github.jvcmarcenes.tca.recipe.ArcaneCrafting.IArcaneCraftingRecipe;
import io.github.jvcmarcenes.tca.recipe.ModRecipeTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

//TODO for some reason the arcane recipes aren't working, I suppose the error is in ArcaneShapedRecipe#match
public class ArcaneWorkbenchTE extends TileEntity implements INamedContainerProvider, ICraftingTE {

  public static final int CRAFT_MATRIX_SLOT_START = 0;
  public static final int CRAFT_MATRIX_SLOT_END = 8;
  public static final int OUTPUT_SLOT = 9;
  public static final int CRYSTAL_SLOT = 10;
  public static final int PHIAL_SLOT = 11;

  public final ItemStackHandler inventory = new ItemStackHandler(12) {
    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
      if (slot == CRYSTAL_SLOT) return stack.getItem() instanceof VisStorage;
      if (slot == PHIAL_SLOT) return stack.getItem() instanceof AspectPhial;
      return true;
    }

    @Override
    protected void onContentsChanged(int slot) {
      super.onContentsChanged(slot);
      if (slot == OUTPUT_SLOT || slot == CRYSTAL_SLOT) return;
      updateInventory();
    }
  };

  public int visCost = 0;

  public ArcaneWorkbenchTE() {
    super(ModTileEntityTypes.ARCANE_WORKBENCH.get());
  }

  private class InvHolder {
    public final ItemStackHandler itemStackHandler;
    public final int width;
    public final int height;

    public InvHolder(ItemStackHandler itemStackHandler, int width, int height) {
      this.itemStackHandler = itemStackHandler;
      this.width = width;
      this.height = height;
    }
  }

  private InvHolder getCraftMatrix() {
    List<List<ItemStack>> craftMatrix = new ArrayList<>();

    for (int i = 0; i < 3; i++) {
      craftMatrix.add(new ArrayList<>());
      for (int j = 0; j < 3; j++) {
        craftMatrix.get(i).add(inventory.getStackInSlot(i * 3 + j));
      }
    }

    Supplier<Integer> s = () -> 0;
    boolean l = false;
    while (true) {
      if (craftMatrix.size() <= 0) break;
      if (craftMatrix.get(s.get()).stream().map(ItemStack::isEmpty).reduce(true, (acc, cur) -> acc && cur))
        craftMatrix.remove(s.get().intValue());
      else {
        if (l) break;
        else l = true;
        s = () -> craftMatrix.size() - 1;
      }
    }

    if (craftMatrix.size() <= 0) return new InvHolder(new ItemStackHandler(), 0, 0);

    s = () -> 0;
    l = false;
    while (true) {
      if (craftMatrix.get(craftMatrix.size() - 1).size() <= 0) break;

      boolean acc = true;
      for (int i = 0; i < craftMatrix.size(); i++) acc = acc && craftMatrix.get(i).get(s.get()).isEmpty();

      if (acc)
        for (int i = 0; i < craftMatrix.size(); i++) craftMatrix.get(i).remove(s.get().intValue());
      else {
        if (l) break;
        else l = true;
        s = () -> craftMatrix.get(craftMatrix.size() - 1).size() - 1;
      }
    }

    int width = craftMatrix.get(0).size();
    int height = craftMatrix.size();
    int size = width * height;

    List<ItemStack> flat = new ArrayList<>();
    craftMatrix.forEach(flat::addAll);

    ItemStackHandler itemStackHandler = new ItemStackHandler(size);
    for (int i = 0; i < size; i++) itemStackHandler.setStackInSlot(i, flat.get(i));

    return new InvHolder(itemStackHandler, width, height);
  }

  private CraftingInventoryWrapper getCraftingInventory() {
    InvHolder inv = getCraftMatrix();

    return new CraftingInventoryWrapper(inv.itemStackHandler, inv.width, inv.height);
  }

  private ArcaneCraftingInventory getArcaneCraftingInventory() {
    InvHolder inv = getCraftMatrix();
    String aspect = IAspectStorage.getStoredAspect(inventory.getStackInSlot(PHIAL_SLOT));
    return new ArcaneCraftingInventory(inv.itemStackHandler, aspect, inv.width, inv.height);
  }

  private Optional<ICraftingRecipe> getRecipe() {
      assert world != null;
      return world.getRecipeManager().getRecipe(IRecipeType.CRAFTING, getCraftingInventory(), world);
  }

  private Optional<IArcaneCraftingRecipe> getArcaneRecipe() {
      assert world != null;
      return world.getRecipeManager().getRecipe(ModRecipeTypes.ARCANE, getArcaneCraftingInventory(), world);
  }

  private ItemStack getRecipeResult() {
    IArcaneCraftingRecipe arcaneRecipe = getArcaneRecipe().orElse(null);
    if (arcaneRecipe != null) {
      visCost = arcaneRecipe.getVisCost();
      return arcaneRecipe.getCraftingResult(getArcaneCraftingInventory());
    }

    ICraftingRecipe recipe = getRecipe().orElse(null);
    if (recipe != null) {
      visCost = 0;
      return recipe.getCraftingResult(getCraftingInventory());
    }

    return ItemStack.EMPTY;
  }

  public int getVisStorageCharge() {
    ItemStack crystal = inventory.getStackInSlot(CRYSTAL_SLOT);
    return crystal.isEmpty() ? 0 : VisStorage.Helper.getCurrentVis(crystal);
  }
  public void setVisStorageCharge(int v) { VisStorage.Helper.setCurrentVis(inventory.getStackInSlot(CRYSTAL_SLOT), v); }
  public int drainVisStorageCharge(int a) { return VisStorage.Helper.drainVis(inventory.getStackInSlot(CRYSTAL_SLOT), a, false); }

  private void updateInventory() {
    inventory.setStackInSlot(OUTPUT_SLOT, getRecipeResult());
  }

  private void shrinkStackInSlot(int slot) {
    ItemStack slotStack = inventory.getStackInSlot(slot);

    ItemStack container = slotStack.hasContainerItem() ? slotStack.getContainerItem() : ItemStack.EMPTY;

    slotStack.shrink(1);

    if (inventory.getStackInSlot(slot).isEmpty() && !container.isEmpty())
      inventory.setStackInSlot(slot, container);
  }

  @Override
  public boolean canCraft(PlayerEntity player) {
    return getVisStorageCharge() >= visCost;
  }

  @Override
  public ItemStack craft() {
    ItemStack output = inventory.getStackInSlot(OUTPUT_SLOT);

    for (int _slot = CRAFT_MATRIX_SLOT_START; _slot <= CRAFT_MATRIX_SLOT_END; _slot++)
      shrinkStackInSlot(_slot);

    drainVisStorageCharge(visCost);
    inventory.getStackInSlot(PHIAL_SLOT).shrink(1);
    updateInventory();

    return output;
  }

  public ItemStack onCraftMany(PlayerInventory playerInv) {
    int amount = 64;
    for (int _slot = CRAFT_MATRIX_SLOT_START; _slot <= CRAFT_MATRIX_SLOT_END; _slot++){
      ItemStack stack = inventory.getStackInSlot(_slot);
      if (stack.isEmpty()) continue;
      amount = amount > stack.getCount() ? stack.getCount() : amount;
    }

    for(int i = 0; i < amount; i++) {
      if (!canCraft(playerInv.player)) break;

      ItemStack output = craft();
      if (!playerInv.addItemStackToInventory(output)) return output;
    }

    return ItemStack.EMPTY;
  }

  @Override
  public ITextComponent getDisplayName() {
    return new TranslationTextComponent(ModBlocks.ARCANE_WORKBENCH.get().getTranslationKey());
  }

  @Override
  public Container createMenu(int windowId, PlayerInventory playerInv, PlayerEntity player) {
      return new ArcaneWorkbenchContainer(windowId, playerInv, this);
    }
}
