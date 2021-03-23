package io.github.jvcmarcenes.tca.alchemy;

import io.github.jvcmarcenes.tca.TCA;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;

import java.util.Hashtable;

public class Aspects {

  private static final Hashtable<ResourceLocation, AspectGroup> ITEM_ASPECTS = new Hashtable<>();
  private static final Hashtable<ResourceLocation, AspectGroup> ENTITY_ASPECTS = new Hashtable<>();

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

  public static final String AER = "aer";
  public static final String ALKIMIA = "alkimia";
  public static final String ALIENIS = "alienis";
  public static final String AQUA = "aqua";
  public static final String BESTIA = "bestia";
  public static final String COGNITIO = "cognitio";
  public static final String DESIDERIUM = "desiderium";
  public static final String EXAMINIS = "examinis";
  public static final String GELUM = "gelum";
  public static final String HERBA = "herba";
  public static final String HUMANIS = "humanis";
  public static final String IGNIS = "ignis";
  public static final String INSTRUMENTUM = "instrumentum";
  public static final String LUX = "lux";
  public static final String MACHINA = "machina";
  public static final String METALLUM = "metallum";
  public static final String MORTUS = "mortus";
  public static final String MOTUS = "motus";
  public static final String ORDO = "ordo";
  public static final String PERDITIO = "perditio";
  public static final String POTENTIA = "potentia";
  public static final String PRAECANTATIO = "praecantatio";
  public static final String SENSUS = "sensus";
  public static final String TENEBRAE = "tenebrae";
  public static final String TERRA = "terra";
  public static final String VICTUS = "victus";
  public static final String VITIUM = "vitium";
  public static final String VITREUS = "vitreus";

  public static final String NONE = "";
}
