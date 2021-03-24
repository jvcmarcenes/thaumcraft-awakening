package io.github.jvcmarcenes.tca.alchemy;

import io.github.jvcmarcenes.tca.TCA;
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
    Item item = stack.getItem();
    return item instanceof IAspectStorage ? 
      IAspectStorage.getStoredAspect(stack) == "" ? 
        AspectGroup.EMPTY
        : aspects().with(IAspectStorage.getStoredAspect(stack), ((IAspectStorage)item).getStoredAmount())
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

  private static String createAspect(String aspect) {
    ASPECTS.add(aspect);
    return aspect;
  }

  public static final String AER = createAspect("aer");
  public static final String ALKIMIA = createAspect("alkimia");
  public static final String ALIENIS = createAspect("alienis");
  public static final String AQUA = createAspect("aqua");
  public static final String BESTIA = createAspect("bestia");
  public static final String COGNITIO = createAspect("cognitio");
  public static final String DESIDERIUM = createAspect("desiderium");
  public static final String EXAMINIS = createAspect("examinis");
  public static final String GELUM = createAspect("gelum");
  public static final String HERBA = createAspect("herba");
  public static final String HUMANIS = createAspect("humanis");
  public static final String IGNIS = createAspect("ignis");
  public static final String INSTRUMENTUM = createAspect("instrumentum");
  public static final String LUX = createAspect("lux");
  public static final String MACHINA = createAspect("machina");
  public static final String METALLUM = createAspect("metallum");
  public static final String MORTUS = createAspect("mortus");
  public static final String MOTUS = createAspect("motus");
  public static final String ORDO = createAspect("ordo");
  public static final String PERDITIO = createAspect("perditio");
  public static final String POTENTIA = createAspect("potentia");
  public static final String PRAECANTATIO = createAspect("praecantatio");
  public static final String SENSUS = createAspect("sensus");
  public static final String TENEBRAE = createAspect("tenebrae");
  public static final String TERRA = createAspect("terra");
  public static final String VICTUS = createAspect("victus");
  public static final String VITIUM = createAspect("vitium");
  public static final String VITREUS = createAspect("vitreus");

  public static final String NONE = createAspect("");
}
