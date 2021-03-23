package io.github.jvcmarcenes.tca.client;

import io.github.jvcmarcenes.tca.TCA;
import io.github.jvcmarcenes.tca.alchemy.Aspects;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = TCA.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientEventHandlerForge {
  
  @SubscribeEvent
  public static void onItemTooltip(final ItemTooltipEvent event) {
    ItemStack stack = event.getItemStack();
    if (Screen.hasShiftDown())
      event.getToolTip().add(
        new StringTextComponent(Aspects.get(stack).toString()).setStyle(Style.EMPTY.setColor(Color.fromInt(0xbf80ff)))
      );
  }

}
