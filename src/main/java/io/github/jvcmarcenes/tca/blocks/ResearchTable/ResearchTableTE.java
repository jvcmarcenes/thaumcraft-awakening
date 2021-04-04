package io.github.jvcmarcenes.tca.blocks.ResearchTable;

import io.github.jvcmarcenes.tca.init.ModTileEntityTypes;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.ItemStackHandler;

public class ResearchTableTE extends TileEntity {

  public static final int SCRIBING_SLOT = 0;

  public final ItemStackHandler inventory = new ItemStackHandler(1);

  public ResearchTableTE() {
    super(ModTileEntityTypes.RESEARCH_TABLE.get());
  }
  
}
