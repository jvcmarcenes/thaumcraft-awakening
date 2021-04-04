package io.github.jvcmarcenes.tca.init;

import io.github.jvcmarcenes.tca.TCA;
import io.github.jvcmarcenes.tca.blocks.ArcaneWorkbench.ArcaneWorkbench;
import io.github.jvcmarcenes.tca.blocks.Crucible.Crucible;
import io.github.jvcmarcenes.tca.blocks.EssentiaJar.EssentiaJar;
import io.github.jvcmarcenes.tca.blocks.ResearchTable.ResearchTable;
import io.github.jvcmarcenes.tca.blocks.CrystalBlock;
import net.minecraft.block.Block;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.AbstractBlock.Properties;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Hashtable;
import java.util.function.Supplier;

public class ModBlocks {
  
  public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, TCA.MOD_ID);
  
  public static final RegistryObject<Block> GREATWOOD_PLANKS = register("greatwood_planks", () -> new Block(Properties.create(Material.WOOD).hardnessAndResistance(2.0f).harvestTool(ToolType.AXE).sound(SoundType.WOOD)));
  public static final RegistryObject<RotatedPillarBlock> GREATWOOD_LOG = register("greatwood_log", () -> new RotatedPillarBlock(Properties.create(Material.WOOD).hardnessAndResistance(3.0f).harvestTool(ToolType.AXE).sound(SoundType.WOOD)));
  public static final RegistryObject<Block> SILVERWOOD_PLANKS = register("silverwood_planks", () -> new Block(Properties.create(Material.WOOD).hardnessAndResistance(2.0f).harvestTool(ToolType.AXE).sound(SoundType.WOOD)));
  public static final RegistryObject<RotatedPillarBlock> SILVERWOOD_LOG = register("silverwood_log", () -> new RotatedPillarBlock(Properties.create(Material.WOOD).hardnessAndResistance(3.0f).harvestTool(ToolType.AXE).sound(SoundType.WOOD)));

  public static final RegistryObject<Block> TABLE;
  public static final RegistryObject<Block> STONE_TABLE;
  
  public static final RegistryObject<Block> NITOR;
  
  public static final RegistryObject<ResearchTable> RESEARCH_TABLE = registerNoItem("research_table", ResearchTable::new);
  public static final RegistryObject<ArcaneWorkbench> ARCANE_WORKBENCH = register("arcane_workbench", ArcaneWorkbench::new);
  public static final RegistryObject<Crucible> CRUCIBLE = register("crucible", Crucible::new);
  public static final RegistryObject<EssentiaJar> JAR  = registerNoItem("essentia_jar", EssentiaJar::new);
  
  public static final RegistryObject<CrystalBlock> CRYSTAL_BLOCK;
  
  static {
    TABLE = register("table_wood", () -> new Block(Properties.create(Material.WOOD).harvestTool(ToolType.AXE).hardnessAndResistance(1.5f).notSolid()));
    STONE_TABLE = register("table_stone", () -> new Block(Properties.create(Material.ROCK).harvestTool(ToolType.PICKAXE).hardnessAndResistance(3.0f).notSolid()));
    
    NITOR = registerNoItem("nitor", () -> new Block(Properties.create(Material.FIRE).doesNotBlockMovement().hardnessAndResistance(0.1f).setLightLevel(state -> 15)));
    
    CRYSTAL_BLOCK = register("crystal_block", () -> new CrystalBlock(Properties.create(Material.GLASS).hardnessAndResistance(0.0f)));
  }
  
  private static <T extends Block> RegistryObject<T> registerNoItem(String name, Supplier<T> block) {
    return BLOCKS.register(name, block);
  }
  
  private static final Hashtable<RegistryObject<?>, RegistryObject<BlockItem>> BLOCK_ITEMS = new Hashtable<>();

  public RegistryObject<BlockItem> getBlockItem(RegistryObject<Block> block) {
    return BLOCK_ITEMS.get(block);
  }

  private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> block) {
    RegistryObject<T> ret = registerNoItem(name, block);
    // RegistryObject<BlockItem> item = 
      ModItems.ITEMS.register(name, () -> new BlockItem(ret.get(), new Item.Properties().group(TCA.ITEM_GROUP)));
    // BLOCK_ITEMS.put(ret, item);
    return ret;
  }
}
