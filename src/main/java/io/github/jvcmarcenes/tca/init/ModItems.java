package io.github.jvcmarcenes.tca.init;

import io.github.jvcmarcenes.tca.TCA;
import io.github.jvcmarcenes.tca.items.SalisMundus.SalisMundus;
import io.github.jvcmarcenes.tca.items.AspectPhial;
import io.github.jvcmarcenes.tca.items.Thaumometer;
import io.github.jvcmarcenes.tca.items.VisStorageItem.VisStorage;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, TCA.MOD_ID);

    public static final RegistryObject<Thaumometer> THAUMOMETER = ITEMS.register("thaumometer", () -> new Thaumometer(def(1)));
    public static final RegistryObject<VisStorage> VIS_CRYSTAL = ITEMS.register("vis_crystal", () -> new VisStorage(def(1), VisStorage.VIS_CRYSTAL));
    public static final RegistryObject<VisStorage> VIS_CELL = ITEMS.register("vis_cell", () -> new VisStorage(def(1), VisStorage.VIS_CELL));
    public static final RegistryObject<VisStorage> CREATIVE_CELL = ITEMS.register("creative_cell", () -> new VisStorage(def(1), new VisStorage.Properties(1000, 1000)));
    public static final RegistryObject<SalisMundus> SALIS_MUNDUS = ITEMS.register("salis_mundus", () -> new SalisMundus(def()));
    public static final RegistryObject<AspectPhial> ASPECT_PHIAL = ITEMS.register("aspect_phial", () -> new AspectPhial(def()));

    private static Item.Properties def() { return new Item.Properties().group(TCA.ITEM_GROUP); }
    private static Item.Properties def(int stacksize) { return new Item.Properties().group(TCA.ITEM_GROUP).maxStackSize(stacksize); }
}
