package io.github.jvcmarcenes.tca.init;

import java.util.function.Function;

import io.github.jvcmarcenes.tca.TCA;
import io.github.jvcmarcenes.tca.items.SalisMundus.SalisMundus;
import io.github.jvcmarcenes.tca.items.ScribingTools;
import io.github.jvcmarcenes.tca.items.Thaumometer;
import io.github.jvcmarcenes.tca.items.EssentiaStorage.AspectPhial;
import io.github.jvcmarcenes.tca.items.EssentiaStorage.JarBlockItem;
import io.github.jvcmarcenes.tca.items.EssentiaStorage.JarItemISTER;
import io.github.jvcmarcenes.tca.items.VisStorageItem.VisStorage;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {
  
  public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, TCA.MOD_ID);
  
  public static final RegistryObject<Item> THAUMONOMICON = register("thaumonomicon", Item::new, def(1));
  
  public static final RegistryObject<Thaumometer> THAUMOMETER = register("thaumometer", Thaumometer::new, def(1));
  public static final RegistryObject<VisStorage> VIS_CRYSTAL = register("vis_crystal", p -> new VisStorage(p, VisStorage.VIS_CRYSTAL), def(1));
  public static final RegistryObject<VisStorage> VIS_CELL = register("vis_cell", p -> new VisStorage(p, VisStorage.VIS_CELL), def(1));
  public static final RegistryObject<VisStorage> CREATIVE_CELL = register("creative_cell", p -> new VisStorage(p, new VisStorage.Properties(1000, 1000)), def(1));
  public static final RegistryObject<SalisMundus> SALIS_MUNDUS = register("salis_mundus", SalisMundus::new);
  public static final RegistryObject<AspectPhial> ASPECT_PHIAL = register("aspect_phial", AspectPhial::new);
  public static final RegistryObject<ScribingTools> SCRIBING_TOOLS = register("scribing_tools", ScribingTools::new ,def(1).maxDamage(128));
  
  public static final RegistryObject<BlockItem> NITOR = register("nitor", p -> new BlockItem(ModBlocks.NITOR.get(), p));
  public static final RegistryObject<JarBlockItem> ESSENTIA_JAR = register("essentia_jar", p -> new JarBlockItem(ModBlocks.JAR.get(), p), def(16).setISTER(() -> JarItemISTER::new));
  
  private static <T extends Item> RegistryObject<T> register(String name, Function<Item.Properties, T> factory) {
    return register(name, factory, def());
  }

  private static <T extends Item> RegistryObject<T> register(String name, Function<Item.Properties, T> factory, Item.Properties props) {
    return ITEMS.register(name, () -> factory.apply(props));
  }
  
  //private static Item.Properties defNoTab() { return new Item.Properties(); }
  //private static Item.Properties defNoTab(int stackSize) { return new Item.Properties().maxStackSize(stackSize); }
  private static Item.Properties def() { return new Item.Properties().group(TCA.ITEM_GROUP); }
  private static Item.Properties def(int stacksize) { return new Item.Properties().group(TCA.ITEM_GROUP).maxStackSize(stacksize); }
}
