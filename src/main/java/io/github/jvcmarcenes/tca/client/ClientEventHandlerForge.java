package io.github.jvcmarcenes.tca.client;

import com.mojang.blaze3d.matrix.MatrixStack;

import io.github.jvcmarcenes.tca.TCA;
import io.github.jvcmarcenes.tca.alchemy.Aspects;
import io.github.jvcmarcenes.tca.blocks.Crucible.CrucibleTE;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
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

  @SubscribeEvent
  public static void onRenderWorld(final RenderWorldLastEvent event) {
    Minecraft mc = Minecraft.getInstance();

    Vector3d hit = mc.objectMouseOver.getHitVec();
    Vector3d look = mc.player.getLookVec();
    Vector3d in = new Vector3d(hit.x + look.x * .1, hit.y + look.y * .1, hit.z + look.z * .1);

    BlockPos blockPos = new BlockPos(in);
    TileEntity te = mc.world.getTileEntity(blockPos);

    //TODO fix weird jiggling
    if (te instanceof CrucibleTE) {
      CrucibleTE crucibleTe = (CrucibleTE)te;

      String display = crucibleTe.aspects.toString();

      MatrixStack ms = event.getMatrixStack();

      ms.push();
      
      ms.translate(blockPos.getX() + .5 - mc.player.getPosX(), blockPos.getY() + 1.4 - mc.player.getPosYEye(), blockPos.getZ() + .5 - mc.player.getPosZ());
      ms.rotate(mc.getRenderManager().getCameraOrientation());
      ms.scale(-0.02f, -0.02f, 0.02f);
      
      int w = mc.fontRenderer.getStringWidth(display);
      mc.fontRenderer.drawString(ms, display, -w/2, 0, 0xbf80ff);

      ms.pop();
    }
  }

}
