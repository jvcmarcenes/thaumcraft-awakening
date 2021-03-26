package io.github.jvcmarcenes.tca.client;

import io.github.jvcmarcenes.tca.TCA;
import io.github.jvcmarcenes.tca.client.gui.ArcaneWorkbenchScreen;
import io.github.jvcmarcenes.tca.client.models.CrucibleBakedModel;
import io.github.jvcmarcenes.tca.init.ModBlocks;
import io.github.jvcmarcenes.tca.init.ModContainerTypes;
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraft.world.biome.BiomeColors;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = TCA.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEventHandler {

  @SubscribeEvent
  public static void clientSetup(final FMLClientSetupEvent event) {
    // Container Screen Registration
    event.enqueueWork(() -> {
      ScreenManager.registerFactory(ModContainerTypes.ARCANE_WORKBENCH.get(), ArcaneWorkbenchScreen::new);
    });

    // Setting render layers
    RenderTypeLookup.setRenderLayer(ModBlocks.CRUCIBLE.get(), RenderType.getCutout());
  }

  @SubscribeEvent
  public static void onModelBake(ModelBakeEvent event) {
    for (BlockState state : ModBlocks.CRUCIBLE.get().getStateContainer().getValidStates()) {
      ModelResourceLocation variant = BlockModelShapes.getModelLocation(state);
      IBakedModel existingModel = event.getModelRegistry().get(variant);

      if (existingModel == null)
        TCA.LOGGER.warn("Did not find the expected vanilla baked model(s) for CRUCIBLE in registry");
      else if (existingModel instanceof CrucibleBakedModel)
        TCA.LOGGER.warn("Tried to replace CrucibleBakedModel twice");
      else
        event.getModelRegistry().put(variant, new CrucibleBakedModel(existingModel));
    }
  }

  @SubscribeEvent
  public static void blockColorHandler(ColorHandlerEvent.Block event) {
    event.getBlockColors().register(new IBlockColor() {
      @Override
      public int getColor(BlockState state, IBlockDisplayReader world, BlockPos pos,int tintIndex) {
        return tintIndex == 23 ? BiomeColors.getWaterColor(world, pos) | 0xff000000 : 0xffffff;
      }
    }, ModBlocks.CRUCIBLE.get());
  }
}
