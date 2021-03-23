package io.github.jvcmarcenes.tca.blocks;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;

public class AuraTap extends Block {

    public AuraTap() {
        super(
            AbstractBlock.Properties.create(Material.ROCK)
                .hardnessAndResistance(3.0f)
                .harvestTool(ToolType.PICKAXE)
        );
    }

}
