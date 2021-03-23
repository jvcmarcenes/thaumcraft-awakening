package io.github.jvcmarcenes.tca.data.server;

import io.github.jvcmarcenes.tca.TCA;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModItemTagsProvider extends ItemTagsProvider {
    public ModItemTagsProvider(DataGenerator generatorIn, BlockTagsProvider blockTagsProvider, ExistingFileHelper existingFileHelper) {
        super(generatorIn, blockTagsProvider, TCA.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerTags() {

    }
}
