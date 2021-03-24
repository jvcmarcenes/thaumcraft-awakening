package io.github.jvcmarcenes.tca.init;

import io.github.jvcmarcenes.tca.TCA;
import io.github.jvcmarcenes.tca.blocks.ArcaneWorkbench.ArcaneWorkbench;
import io.github.jvcmarcenes.tca.blocks.Crucible.Crucible;
import io.github.jvcmarcenes.tca.blocks.CrystalBlock;
import net.minecraft.block.Block;
import net.minecraft.block.AbstractBlock.Properties;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, TCA.MOD_ID);

    public static final RegistryObject<Block> TABLE;

    public static final RegistryObject<Block> NITOR;

    public static final RegistryObject<ArcaneWorkbench> ARCANE_WORKBENCH;
    public static final RegistryObject<Crucible> CRUCIBLE;

    public static final RegistryObject<CrystalBlock> CRYSTAL_BLOCK;

    static {
        TABLE = register("table", () -> new Block(Properties.create(Material.WOOD).harvestTool(ToolType.AXE).hardnessAndResistance(1.0f)));

        NITOR = register("nitor", () -> new Block(Properties.create(Material.FIRE).doesNotBlockMovement().hardnessAndResistance(0.1f).setLightLevel(state -> 15)));

        ARCANE_WORKBENCH = register("arcane_workbench", ArcaneWorkbench::new);
        CRUCIBLE = register("crucible", Crucible::new);

        CRYSTAL_BLOCK = register("crystal_block", () -> new CrystalBlock(Properties.create(Material.GLASS).hardnessAndResistance(0.0f)));
    }

    private static <T extends Block> RegistryObject<T> registerNoItem(String name, Supplier<T> block) {
        return BLOCKS.register(name, block);
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> block) {
        RegistryObject<T> ret = registerNoItem(name, block);
        ModItems.ITEMS.register(name, () -> new BlockItem(ret.get(), new Item.Properties().group(TCA.ITEM_GROUP)));
        return ret;
    }
}
