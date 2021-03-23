package io.github.jvcmarcenes.tca.items.VisStorageItem;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class VisStorage extends Item {

    private final int visCap;
    private final int currentVis;

    private static final String VIS_CAP_TAG = "visCap";
    private static final String CURRENT_VIS_TAG = "currentVis";

    public VisStorage(Item.Properties properties, Properties visProperties) {
        super(properties);
        this.visCap = visProperties.visCap;
        this.currentVis = visProperties.currentVis;
    }

    @Override
    public void onCreated(ItemStack stack, World worldIn, PlayerEntity playerIn) {
        super.onCreated(stack, worldIn, playerIn);

        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt(VIS_CAP_TAG, visCap);
        nbt.putInt(CURRENT_VIS_TAG, currentVis);

        stack.setTag(nbt);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new StringTextComponent("Vis: " + Helper.getCurrentVis(stack)));
    }

    public static class Properties {
        public final int visCap;
        public final int currentVis;

        public Properties(int visCap, int currentVis) {
            this.visCap = visCap;
            this.currentVis = currentVis;
        }
    }

    public static final Properties VIS_CRYSTAL = new Properties(50, 50);
    public static final Properties VIS_CELL = new Properties(100, 0);

    public static class Helper {

        public static int getVisCap(ItemStack stack) {
            return stack.hasTag() ? stack.getTag().getInt(VIS_CAP_TAG) : 0;
        }
        public static void setVisCap(ItemStack stack, int value) {
            if (stack.hasTag()) stack.getTag().putInt(VIS_CAP_TAG, Math.max(0, value));
        }

        public static int getCurrentVis(ItemStack stack) {
            return stack.hasTag() ? stack.getTag().getInt(CURRENT_VIS_TAG) : 0;
        }
        public static void setCurrentVis(ItemStack stack, int value) {
            if (stack.hasTag()) {
                CompoundNBT nbt = stack.getTag();
                nbt.putInt(CURRENT_VIS_TAG, Math.max(0, Math.min(nbt.getInt(VIS_CAP_TAG), value)));
            }
        }

        public static int drainVis(ItemStack stack, int amount, boolean force) {
            int currentVis = getCurrentVis(stack);

            if (amount > currentVis) {
                if (!force) return 0;
                amount = currentVis;
            }

            setCurrentVis(stack, currentVis - amount);
            return amount;
        }

        public static int chargeVis(ItemStack stack, int amount) {
            int currentVis = getCurrentVis(stack);
            int visCap = getVisCap(stack);

            if (currentVis + amount > visCap) {
                setCurrentVis(stack, visCap);
                return currentVis + amount - visCap;
            }

            setCurrentVis(stack, currentVis + amount);
            return 0;
        }
    }
}
