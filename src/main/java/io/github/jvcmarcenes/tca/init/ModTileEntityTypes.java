package io.github.jvcmarcenes.tca.init;

import io.github.jvcmarcenes.tca.TCA;
import io.github.jvcmarcenes.tca.blocks.ArcaneWorkbench.ArcaneWorkbenchTE;
import io.github.jvcmarcenes.tca.blocks.Crucible.CrucibleTE;
import io.github.jvcmarcenes.tca.blocks.EssentiaJar.EssentiaJarTE;
import io.github.jvcmarcenes.tca.blocks.ResearchTable.ResearchTableTE;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ModTileEntityTypes {

  public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, TCA.MOD_ID);

  public static final RegistryObject<TileEntityType<ArcaneWorkbenchTE>> ARCANE_WORKBENCH = register("arcane_workbench", ArcaneWorkbenchTE::new, ModBlocks.ARCANE_WORKBENCH);
  public static final RegistryObject<TileEntityType<CrucibleTE>> CRUCIBLE = register("crucible", CrucibleTE::new, ModBlocks.CRUCIBLE);
  public static final RegistryObject<TileEntityType<EssentiaJarTE>> JAR = register("essentia_jar", EssentiaJarTE::new, ModBlocks.JAR);
  public static final RegistryObject<TileEntityType<ResearchTableTE>> RESEARCH_TABLE = register("research_table", ResearchTableTE::new, ModBlocks.RESEARCH_TABLE);

  private static <T extends TileEntity, B extends Block> RegistryObject<TileEntityType<T>> register(String name, Supplier<T> factory, Supplier<B> block) {
    return TILE_ENTITY_TYPES.register(name, () -> TileEntityType.Builder.create(factory, block.get()).build(null));
  }
}
