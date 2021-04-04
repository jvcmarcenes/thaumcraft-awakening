package io.github.jvcmarcenes.tca.data.client;

import io.github.jvcmarcenes.tca.TCA;
import io.github.jvcmarcenes.tca.init.ModBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockStateProvider extends BlockStateProvider {

  ExistingFileHelper efh;

  public ModBlockStateProvider(DataGenerator gen, ExistingFileHelper efh) {
    super(gen, TCA.MOD_ID, efh);
    this.efh = efh;
  }

  @Override
  protected void registerStatesAndModels() {

    simpleBlock(ModBlocks.ARCANE_WORKBENCH.get(), existingFile("block/arcane_workbench"));

    simpleBlock(ModBlocks.GREATWOOD_PLANKS.get());
    simpleBlock(ModBlocks.SILVERWOOD_PLANKS.get());

    logBlock(ModBlocks.GREATWOOD_LOG.get());
    logBlock(ModBlocks.SILVERWOOD_LOG.get());

  }

  private ModelFile existingFile(String loc) {
    return new ModelFile.ExistingModelFile(modLoc(loc), efh);
  }
}
