package io.github.jvcmarcenes.tca.data.server;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import io.github.jvcmarcenes.tca.TCA;
import io.github.jvcmarcenes.tca.data.server.loot.ModBlockLootTables;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.LootTableProvider;
import net.minecraft.loot.*;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ModLootTables extends LootTableProvider {

    public ModLootTables(DataGenerator dataGeneratorIn) {
        super(dataGeneratorIn);
    }

    private final List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>>
        lootTablegenerators = ImmutableList.of(
            Pair.of(ModBlockLootTables::new, LootParameterSets.BLOCK)
        );

    @Override
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> getTables() {
        return lootTablegenerators;
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationTracker validationtracker) {
        final Set<ResourceLocation> modLootTableIds =
            LootTables
                .getReadOnlyLootTables()
                .stream()
                .filter(lootTable -> lootTable.getNamespace().equals(TCA.MOD_ID))
                .collect(Collectors.toSet());

        for (ResourceLocation id : Sets.difference(modLootTableIds, map.keySet()))
            validationtracker.addProblem("Missing mod loot table: " + id);

        map.forEach((id, lootTable) ->
            LootTableManager.validateLootTable(validationtracker, id, lootTable));
    }

    @Override
    public String getName() {
        return TCA.MOD_ID + "_loottables";
    }
}
