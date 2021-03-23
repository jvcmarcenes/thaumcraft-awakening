package io.github.jvcmarcenes.tca.items.SalisMundus;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SalisMundus extends Item {

    public SalisMundus(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        World world = context.getWorld();
        PlayerEntity player = context.getPlayer();
        BlockPos pos = context.getPos();

        for (ISalisMundusTrigger trigger : ISalisMundusTrigger.triggers) {
            if (!trigger.validate(world, player, pos)) continue;

            trigger.execute(world, player, pos, context);

            if (!player.abilities.isCreativeMode)
                player.getHeldItem(context.getHand()).shrink(1);
            return ActionResultType.SUCCESS;
        }

        return super.onItemUse(context);
    }
}
