package io.github.jvcmarcenes.tca.data.client;

import io.github.jvcmarcenes.tca.TCA;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockModelProvider extends BlockModelProvider {

  public ModBlockModelProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
    super(gen, TCA.MOD_ID, exFileHelper);
  }

  @Override
  protected void registerModels() {

    

  }
  
}
