package io.github.jvcmarcenes.tca.blocks.Crucible;

import java.util.List;
import java.util.Optional;

import io.github.jvcmarcenes.tca.alchemy.AspectGroup;
import io.github.jvcmarcenes.tca.alchemy.Aspects;
import io.github.jvcmarcenes.tca.init.ModTileEntityTypes;
import io.github.jvcmarcenes.tca.recipe.ModRecipeTypes;
import io.github.jvcmarcenes.tca.recipe.Alchemy.AlchemyRecipe;
import io.github.jvcmarcenes.tca.recipe.Alchemy.AlchemyRecipeInventory;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;

//TODO implement boiling
public class CrucibleTE extends TileEntity implements ITickableTileEntity {

  public final AspectGroup aspects = new AspectGroup();

  public CrucibleTE() { super(ModTileEntityTypes.CRUCIBLE.get()); }
  
  @Override
  public void tick() {
    if (getBlockState().get(Crucible.LEVEL) == 0) return;

    double x = pos.getX();
    double y = pos.getY();
    double z = pos.getZ();
    AxisAlignedBB aabb = new AxisAlignedBB(x, y + 1, z, x + 1, y + 1.2, z + 1);
    List<ItemEntity> items = getWorld().getEntitiesWithinAABB(ItemEntity.class, aabb, entity -> !Aspects.get(entity.getItem()).hasNone());
    items.forEach(item -> {
      item.remove();
      meltItem(item.getItem());
    });
  }

  public void meltItem(ItemStack in) {
    if (in.isEmpty()) return;

    boolean wasCatalyst = tryCraft(in);
    if (!wasCatalyst) aspects.addAll(Aspects.get(in));
  }

  public void meltItem(ItemStack in, PlayerEntity player) {
    meltItem(in);

    StringTextComponent msg = new StringTextComponent("> " + aspects.toString());
    msg.setStyle(Style.EMPTY.setColor(Color.fromInt(0xbf80ff)));
    player.sendMessage(msg, player.getUniqueID());
  }

  private boolean tryCraft(ItemStack in) {
    AlchemyRecipeInventory inv = new AlchemyRecipeInventory(aspects, in.getItem());

    assert world != null;
    Optional<AlchemyRecipe> recipe = world.getRecipeManager().getRecipe(ModRecipeTypes.ALCHEMY, inv, world);
    if (!recipe.isPresent()) return false;

    aspects.drainAll(recipe.get().getRequiredAspects());
    world.setBlockState(pos, getBlockState().with(Crucible.LEVEL, getBlockState().get(Crucible.LEVEL) - 1));
    //TODO play sound

    world.addEntity(new ItemEntity(world, getPos().getX(), getPos().getY() + 1, getPos().getZ(), recipe.get().getCraftingResult(inv)));

    return true;
  }
}
