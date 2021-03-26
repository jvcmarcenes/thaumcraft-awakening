package io.github.jvcmarcenes.tca.data.client;

import io.github.jvcmarcenes.tca.TCA;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {
  public ModItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
    super(generator, TCA.MOD_ID, existingFileHelper);
  }

  @Override
  protected void registerModels() {
      
    blockItem("crucible");
    blockItem("arcane_workbench");

    builder("salis_mundus");
    getBuilder("aspect_phial")
      .parent(getExistingFile(mcLoc("item/generated")))
      .texture("layer0", "item/phial")
      .texture("layer1", "item/phial_overlay");
  }

  private ItemModelBuilder builder(String name) {
    ModelFile itemGenerated = getExistingFile(mcLoc("item/generated"));
    return getBuilder(name).parent(itemGenerated).texture("layer0", "item/" + name);
  }

  private ItemModelBuilder blockItem(String name) {
    return withExistingParent(name, modLoc(name));
  }
}
