package io.github.jvcmarcenes.tca.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import io.github.jvcmarcenes.tca.TCA;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;

@SuppressWarnings("deprecation")
public class ThaumonomiconScreen extends Screen {

  private static final ResourceLocation TEX = new ResourceLocation(TCA.MOD_ID, "gui/thaumonomicon/research_browser");
  private static final ResourceLocation BG0 = new ResourceLocation(TCA.MOD_ID, "gui/thaumonomicon/research_bg_over");
  private static final ResourceLocation BG1 = new ResourceLocation(TCA.MOD_ID, "gui/thaumonomicon/research_bg_1");

  private PlayerEntity player;

  private double scrollX;
  private double scrollY;

  public ThaumonomiconScreen(PlayerEntity player) {
    super(new StringTextComponent("Thaumonomicon"));

    this.player = player;
  }

  @Override
  public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
    RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);

    renderBackground(matrixStack);

    renderSpace(matrixStack);
  }

  private void renderSpace(MatrixStack matrixStack) {
    minecraft.getTextureManager().bindTexture(BG1);

    this.blit(matrixStack, 0, 0, 0, 0, 256, 256);

    // RenderSystem.disableAlphaTest();
    // RenderSystem.defaultBlendFunc();

    // minecraft.textureManager.bindTexture(BG0);
    // this.blit(matrixStack, 0, 0, 0, 0, 256, 256);

    // RenderSystem.enableAlphaTest();
  }

  @Override
  public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
    return super.keyPressed(keyCode, scanCode, modifiers);
  }

}

