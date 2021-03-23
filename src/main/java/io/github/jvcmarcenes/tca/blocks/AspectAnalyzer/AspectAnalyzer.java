package io.github.jvcmarcenes.tca.blocks.AspectAnalyzer;

import io.github.jvcmarcenes.tca.alchemy.AspectGroup;
import io.github.jvcmarcenes.tca.alchemy.Aspects;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponent;
import net.minecraft.world.World;

public class AspectAnalyzer extends Block {

    public AspectAnalyzer() {
        super(AbstractBlock.Properties.create(Material.ROCK).hardnessAndResistance(3.0f));
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        /*
        if (!world.isRemote) {
            final TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity instanceof AspectAnalyzerTE)
                NetworkHooks.openGui((ServerPlayerEntity)player, (AspectAnalyzerTE)tileEntity, pos);
        }
         */

        if (!world.isRemote) {
            ItemStack stack = player.getHeldItem(hand);
            AspectGroup aspects = Aspects.get(stack);
            TextComponent msg = new StringTextComponent(aspects.toString());
            msg.setStyle(Style.EMPTY.setColor(Color.fromInt(0xbf80ff)));
            player.sendMessage(msg, player.getUniqueID());
        }

        return ActionResultType.SUCCESS;
    }
}
