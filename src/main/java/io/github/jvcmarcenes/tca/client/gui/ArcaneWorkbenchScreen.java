package io.github.jvcmarcenes.tca.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import io.github.jvcmarcenes.tca.blocks.ArcaneWorkbench.ArcaneWorkbenchContainer;
import io.github.jvcmarcenes.tca.blocks.ArcaneWorkbench.ArcaneWorkbenchTE;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class ArcaneWorkbenchScreen extends ContainerScreen<ArcaneWorkbenchContainer> {

  private static final ResourceLocation BG_TEX = new ResourceLocation("minecraft", "textures/gui/container/crafting_table.png");

  public ArcaneWorkbenchScreen(ArcaneWorkbenchContainer container, PlayerInventory inv, ITextComponent title) {
    super(container, inv, title);
  }

  @Override
  public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(matrixStack);
    super.render(matrixStack, mouseX, mouseY, partialTicks);

    this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
    RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
    getMinecraft().getTextureManager().bindTexture(BG_TEX);

    this.blit(matrixStack, this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
  }

  @Override
  protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {
    super.drawGuiContainerForegroundLayer(matrixStack, x, y);

    if (!container.tileEntity.inventory.getStackInSlot(ArcaneWorkbenchTE.CRYSTAL_SLOT).isEmpty()) {
      this.font.drawString(matrixStack, Integer.toUnsignedString(container.tileEntity.getVisStorageCharge()), 8, 20, 0x300060);
    }
    this.font.drawString(matrixStack, Integer.toUnsignedString(container.tileEntity.visCost), 8, 34, 0x300060);
  }
}
