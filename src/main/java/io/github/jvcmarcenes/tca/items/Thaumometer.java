package io.github.jvcmarcenes.tca.items;

import io.github.jvcmarcenes.tca.world.aura.AuraChunk;
import io.github.jvcmarcenes.tca.world.aura.AuraWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

public class Thaumometer extends Item {

    public Thaumometer(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (world.isRemote) return ActionResult.resultPass(stack);

        AuraChunk ac = AuraWorld.getAuraChunkAt(world, player.getPosition());

        ITextComponent msg = new StringTextComponent("Aura: <" + ac.getMax() + ", " + ac.getVis() + ", " + ac.getFlux() + ">");
        player.sendMessage(msg, player.getUniqueID());

        return ActionResult.resultPass(stack);
    }
}
