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
        
    }

    private ItemModelBuilder builder(String name) {
        ModelFile itemGenerated = getExistingFile(mcLoc("item/generated"));
        return getBuilder(name).parent(itemGenerated).texture("layer0", "item/" + name);
    }
}
