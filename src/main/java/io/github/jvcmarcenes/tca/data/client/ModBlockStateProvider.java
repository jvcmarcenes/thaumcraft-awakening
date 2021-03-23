package io.github.jvcmarcenes.tca.data.client;

import io.github.jvcmarcenes.tca.TCA;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockStateProvider extends BlockStateProvider {

  public ModBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
    super(gen, TCA.MOD_ID, exFileHelper);
  }

  @Override
  protected void registerStatesAndModels() {

  }
}
