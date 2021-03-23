package io.github.jvcmarcenes.tca.init;

import io.github.jvcmarcenes.tca.TCA;
import io.github.jvcmarcenes.tca.blocks.ArcaneWorkbench.ArcaneWorkbenchTE;
import io.github.jvcmarcenes.tca.blocks.AspectAnalyzer.AspectAnalyzerTE;
import io.github.jvcmarcenes.tca.blocks.Crucible.CrucibleTE;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ModTileEntityTypes {

  public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, TCA.MOD_ID);

  public static final RegistryObject<TileEntityType<ArcaneWorkbenchTE>> ARCANE_WORKBENCH;
  public static final RegistryObject<TileEntityType<AspectAnalyzerTE>> ASPECT_ANALYZER;
  public static final RegistryObject<TileEntityType<CrucibleTE>> CRUCIBLE;

  static {
    ARCANE_WORKBENCH = TILE_ENTITY_TYPES.register("arcane_workbench", () ->
      build(ArcaneWorkbenchTE::new, ModBlocks.ARCANE_WORKBENCH.get()));
    ASPECT_ANALYZER = TILE_ENTITY_TYPES.register("aspect_analyzer", () ->
      build(AspectAnalyzerTE::new, ModBlocks.ASPECT_ANALYZER.get()));
    CRUCIBLE = TILE_ENTITY_TYPES.register("crucible", () ->
      build(CrucibleTE::new, ModBlocks.CRUCIBLE.get()));
  }

  public static <T extends TileEntity> TileEntityType<T> build(Supplier<T> factory, Block... blocks) {
    return TileEntityType.Builder.create(factory, blocks).build(null);
  }
}
