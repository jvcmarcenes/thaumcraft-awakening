package io.github.jvcmarcenes.tca.world.aura;

import io.github.jvcmarcenes.tca.TCA;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeRegistry;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.BiomeDictionary;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Set;

public class  BiomeAuraAffinity {

  private static final Hashtable<BiomeDictionary.Type, Float> map = new Hashtable<>();

  public static float getAuraAffinity(BiomeDictionary.Type key) {
    if (map.containsKey(key)) return map.get(key);

    TCA.LOGGER.warn("No affinity mapped to Biome Type: " + key.getName());
    return 1.0f;
  }

  public static float getAuraAffinity(Set<BiomeDictionary.Type> types) {
    return types.stream()
      .map(BiomeAuraAffinity::getAuraAffinity)
      .reduce(1.0f, (res, n) -> res * n);
  }

  public static float getAuraAffinity(RegistryKey<Biome> biome) {
    return getAuraAffinity(BiomeDictionary.getTypes(biome));
  }

  public static float getAuraAffinityForChunk(Chunk chunk) {
    return (float)Arrays.stream(chunk.getBiomes().getBiomeIds())
      .mapToDouble(id -> getAuraAffinity(BiomeRegistry.getKeyFromID(id)))
      .average().orElse(1.0);
  }

  public static void registerBiomeAuraAffinities() {
    register(BiomeDictionary.Type.BEACH, 1.0f);
    register(BiomeDictionary.Type.COLD, 1.091f);
    register(BiomeDictionary.Type.CONIFEROUS, 1.07f);
    register(BiomeDictionary.Type.DEAD, 0.8f);
    register(BiomeDictionary.Type.DENSE, 1.08f);
    register(BiomeDictionary.Type.DRY, 0.84f);
    register(BiomeDictionary.Type.END, 1.0f);
    register(BiomeDictionary.Type.FOREST, 1.19f);
    register(BiomeDictionary.Type.HILLS, 1.1f);
    register(BiomeDictionary.Type.HOT, 0.95f);
    register(BiomeDictionary.Type.JUNGLE, 1.0f);
    register(BiomeDictionary.Type.LUSH, 1.12f);
    register(BiomeDictionary.Type.MAGICAL, 1.4f);
    register(BiomeDictionary.Type.MESA, 0.9f);
    register(BiomeDictionary.Type.MODIFIED, 1.0f);
    register(BiomeDictionary.Type.MOUNTAIN, 1.1f);
    register(BiomeDictionary.Type.MUSHROOM, 1.15f);
    register(BiomeDictionary.Type.NETHER, 0.85f);
    register(BiomeDictionary.Type.OCEAN, 0.9f);
    register(BiomeDictionary.Type.OVERWORLD, 1.0f);
    register(BiomeDictionary.Type.PLAINS, 1.1f);
    register(BiomeDictionary.Type.PLATEAU, 1.0f);
    register(BiomeDictionary.Type.RARE, 1.0f);
    register(BiomeDictionary.Type.RIVER, 1.0f);
    register(BiomeDictionary.Type.SANDY, 0.9f);
    register(BiomeDictionary.Type.SAVANNA, 0.78f);
    register(BiomeDictionary.Type.SNOWY, 1.12f);
    register(BiomeDictionary.Type.SPARSE, 1.0f);
    register(BiomeDictionary.Type.SPOOKY, 1.13f);
    register(BiomeDictionary.Type.SWAMP, 1.1f);
    register(BiomeDictionary.Type.VOID, 1.0f);
    register(BiomeDictionary.Type.WASTELAND, 0.8f);
    register(BiomeDictionary.Type.WATER, 1.0f);
    register(BiomeDictionary.Type.WET, 1.1f);
  }

  public static void register(BiomeDictionary.Type key, float aff) {
    map.put(key, aff);
  }

  public void printBiomeAffinities() {
    for (Field field : Biomes.class.getDeclaredFields()) {
      RegistryKey<Biome> key;
      try {
        key = (RegistryKey<Biome>)field.get(null);
        float biomeAffinity =
          BiomeDictionary.getTypes(key).stream()
            .map(BiomeAuraAffinity::getAuraAffinity)
            .reduce(1.0f, (res, n) -> res * n);
        TCA.LOGGER.info("Aura Affinity for Biome: " + key.getLocation() + " is of " + biomeAffinity);
      } catch (Exception e) {
        TCA.LOGGER.error("oh no");
      }
    }
  }
}
