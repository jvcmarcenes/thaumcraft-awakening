package io.github.jvcmarcenes.tca.data.server.loot;

import io.github.jvcmarcenes.tca.TCA;
import io.github.jvcmarcenes.tca.init.ModBlocks;
import io.github.jvcmarcenes.tca.init.ModItems;
import net.minecraft.block.Block;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ModBlockLootTables extends BlockLootTables {

    @Override
    protected void addTables() {
        ModBlocks.BLOCKS.getEntries().forEach((entry) -> registerDropSelfLootTable(entry.get()));

        registerDropping(ModBlocks.CRYSTAL_BLOCK.get(), ModItems.VIS_CRYSTAL.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return StreamSupport
            .stream(ForgeRegistries.BLOCKS.spliterator(), false)
            .filter(entry ->
                entry.getRegistryName() != null
                && entry.getRegistryName().getNamespace().equals(TCA.MOD_ID)
            ).collect(Collectors.toSet());
    }
}
