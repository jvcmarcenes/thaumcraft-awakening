package io.github.jvcmarcenes.tca.items.EssentiaStorage;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;

import io.github.jvcmarcenes.tca.TCA;
import io.github.jvcmarcenes.tca.client.models.JarBakedModel;
import io.github.jvcmarcenes.tca.init.ModBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.data.ModelDataMap;

public class JarItemISTER extends ItemStackTileEntityRenderer {
  
  @Override
  public void func_239207_a_(ItemStack stack, TransformType type, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {

    IAspectStorage aspectStorage = (JarBlockItem)stack.getItem();

    ModelDataMap modelData = new ModelDataMap.Builder().withInitial(JarBakedModel.LEVEL, aspectStorage.getStoredAmount(stack)).build();

    TCA.LOGGER.info("JarItemISTER: LEVEL = " + aspectStorage.getStoredAmount(stack));

    Minecraft.getInstance().getBlockRendererDispatcher().renderBlock(
      ModBlocks.JAR.get().getDefaultState(), matrixStack, buffer, combinedLight, combinedOverlay, 
      modelData
    );

    //super.func_239207_a_(stack, type, matrixStack, buffer, combinedLight, combinedOverlay);
  }

}
