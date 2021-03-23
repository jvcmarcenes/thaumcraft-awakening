package io.github.jvcmarcenes.tca.data.server;

import io.github.jvcmarcenes.tca.TCA;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockTagsProvider extends BlockTagsProvider {
    public ModBlockTagsProvider(DataGenerator generatorIn, ExistingFileHelper existingFileHelper) {
        super(generatorIn, TCA.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerTags() {

    }
}
