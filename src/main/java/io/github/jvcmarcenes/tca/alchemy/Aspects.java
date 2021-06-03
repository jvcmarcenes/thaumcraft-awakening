package io.github.jvcmarcenes.tca.alchemy;

import io.github.jvcmarcenes.tca.TCA;
import io.github.jvcmarcenes.tca.items.EssentiaStorage.IAspectStorage;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

import java.util.Hashtable;

public class Aspects {

  private static final Hashtable<ResourceLocation, AspectGroup> ITEM_ASPECTS = new Hashtable<>();
  private static final Hashtable<ResourceLocation, AspectGroup> ENTITY_ASPECTS = new Hashtable<>();

  public static final NonNullList<String> ASPECTS = NonNullList.create();
  public static final Hashtable<String, Integer> ASPECT_COLORS = new Hashtable<>();

  public static void registerItemAspects() {
    register(Items.STONE, aspects().with(TERRA, 5));
    register(Items.OBSIDIAN, aspects().with(TERRA, 10).with(IGNIS, 5));
    register(Items.IRON_INGOT, aspects().with(METALLUM, 14));
    register(Items.REDSTONE, aspects().with(POTENTIA, 12));
    register(Items.GLASS, aspects().with(VITREUS, 6));
    register(Items.QUARTZ, aspects().with(ORDO, 8));
    register(Items.RED_TULIP, aspects().with(HERBA, 12).with(VICTUS, 6));
    register(Items.APPLE, aspects().with(VICTUS, 6).with(HERBA, 8));
    register(Items.GLOWSTONE_DUST, aspects().with(LUX, 10));
    register(Items.COAL, aspects().with(POTENTIA, 10).with(IGNIS, 8));

    TCA.LOGGER.info("Item Aspects:");
    ITEM_ASPECTS.forEach((key, aspects) -> {
      TCA.LOGGER.info(key + "> " + aspects);
    });
  }

  public static void registerEntityAspects() {
    register(EntityType.CHICKEN, aspects().with(VICTUS, 10).with(AER, 4));
    register(EntityType.MAGMA_CUBE, aspects().with(VICTUS, 8).with(IGNIS, 15));
    
    TCA.LOGGER.info("EntityAspects:");
    ENTITY_ASPECTS.forEach((key, aspects) -> {
      TCA.LOGGER.info(key + "> " + aspects);
    });
  }

  public static AspectGroup get(ItemStack stack) {
    if (stack.isEmpty()) return AspectGroup.EMPTY;

    Item item = stack.getItem();
    return item instanceof IAspectStorage ? 
      IAspectStorage.getStoredAspect(stack).equals(Aspects.NONE) ? 
        AspectGroup.EMPTY
        : aspects().with(IAspectStorage.getStoredAspect(stack), ((IAspectStorage)item).getStoredAmount(stack))
      : ITEM_ASPECTS.containsKey(item.getRegistryName()) ? 
        ITEM_ASPECTS.get(item.getRegistryName()) 
        : AspectGroup.EMPTY;
  }

  public static AspectGroup get(EntityType<?> entity) {
    return ENTITY_ASPECTS.containsKey(entity.getRegistryName())
      ? ENTITY_ASPECTS.get(entity.getRegistryName()) : AspectGroup.EMPTY;
  }

  public static void register(Item item, AspectGroup aspects) {
    ITEM_ASPECTS.put(item.getRegistryName(), aspects);
  }

  public static void register(EntityType<?> entity, AspectGroup aspects) {
    ENTITY_ASPECTS.put(entity.getRegistryName(), aspects);
  }

  private static AspectGroup aspects() { return new AspectGroup(); }

  private static String createAspect(String aspect, int color) {
    ASPECTS.add(aspect);
    ASPECT_COLORS.put(aspect, color);
    return aspect;
  }

  public static final String AER = createAspect("aer", 0xffe25e);
  public static final String ALKIMIA = createAspect("alkimia", 0x51e0a2);
  public static final String ALIENIS = createAspect("alienis", 0x7918ba);
  public static final String AURAM = createAspect("auram", 0xe398d9);
  public static final String AQUA = createAspect("aqua", 0x27a4e3);
  public static final String BESTIA = createAspect("bestia", 0xb58326);
  public static final String COGNITIO = createAspect("cognitio", 0xe8cbb7);
  public static final String DESIDERIUM = createAspect("desiderium", 0xd1c217);
  //public static final String EXAMINIS = createAspect("examinis");
  public static final String GELUM = createAspect("gelum", 0xc8e2e8);
  public static final String HERBA = createAspect("herba", 0x27a614);
  public static final String HUMANIS = createAspect("humanis", 0xebd1b2);
  public static final String IGNIS = createAspect("ignis", 0xde581f);
  public static final String INSTRUMENTUM = createAspect("instrumentum", 0x151aab);
  public static final String LUX = createAspect("lux", 0xd9cd4e);
  public static final String MACHINA = createAspect("machina", 0x757575);
  public static final String METALLUM = createAspect("metallum", 0xc4c4c4);
  public static final String MORTUS = createAspect("mortus", 0x961a1a);
  public static final String MOTUS = createAspect("motus", 0xcfcfcf);
  public static final String ORDO = createAspect("ordo", 0xf4f4f4);
  public static final String PERDITIO = createAspect("perditio", 0x1f1f1f);
  public static final String POTENTIA = createAspect("potentia", 0xa0d9d1);
  public static final String PRAECANTATIO = createAspect("praecantatio", 0x9324e6);
  public static final String SENSUS = createAspect("sensus", 0x0fa8a8);
  public static final String SPIRITUS = createAspect("spiritus", 0xf8f8f8);
  public static final String TENEBRAE = createAspect("tenebrae", 0x1f1f1f);
  public static final String TERRA = createAspect("terra", 0x319b15);
  public static final String VICTUS = createAspect("victus", 0xe60e0e);
  public static final String VITIUM = createAspect("vitium", 0x4c147a);
  public static final String VITREUS = createAspect("vitreus", 0x84cab8);

  public static final String NONE = "";
}
