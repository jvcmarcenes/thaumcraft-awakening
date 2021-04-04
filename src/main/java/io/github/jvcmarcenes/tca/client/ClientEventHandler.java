package io.github.jvcmarcenes.tca.client;

import java.util.Map;

import io.github.jvcmarcenes.tca.TCA;
import io.github.jvcmarcenes.tca.alchemy.Aspects;
import io.github.jvcmarcenes.tca.blocks.EssentiaJar.EssentiaJarTE;
import io.github.jvcmarcenes.tca.client.gui.ArcaneWorkbenchScreen;
import io.github.jvcmarcenes.tca.client.models.CrucibleBakedModel;
import io.github.jvcmarcenes.tca.client.models.JarBakedModel;
import io.github.jvcmarcenes.tca.init.ModBlocks;
import io.github.jvcmarcenes.tca.init.ModContainerTypes;
import io.github.jvcmarcenes.tca.init.ModItems;
import io.github.jvcmarcenes.tca.items.EssentiaStorage.IAspectStorage;
import io.github.jvcmarcenes.tca.util.RegisteringUtils;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
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
    //

    // Setting render layers
    RenderTypeLookup.setRenderLayer(ModBlocks.CRUCIBLE.get(), RenderType.getCutout());
    RenderTypeLookup.setRenderLayer(ModBlocks.RESEARCH_TABLE.get(), RenderType.getCutout());
    RenderTypeLookup.setRenderLayer(ModBlocks.ARCANE_WORKBENCH.get(), RenderType.getCutout());
    RenderTypeLookup.setRenderLayer(ModBlocks.TABLE.get(), RenderType.getCutout());
    RenderTypeLookup.setRenderLayer(ModBlocks.STONE_TABLE.get(), RenderType.getCutout());
    RenderTypeLookup.setRenderLayer(ModBlocks.JAR.get(), RenderType.getTranslucent());
    //

    // Item overrides
    RegisteringUtils.registerItemProperty(ModItems.ASPECT_PHIAL.get(), "empty", (stack, world, entity) -> IAspectStorage.getStoredAspect(stack).equals(Aspects.NONE) ? 1f : 0f);
    //
  }

  @SubscribeEvent
  public static void onModelBake(ModelBakeEvent event) {
    Map<ResourceLocation, IBakedModel> modelRegistry = event.getModelRegistry();

    RegisteringUtils.registerBakedModel(modelRegistry, ModBlocks.CRUCIBLE.get(), CrucibleBakedModel.class);
    RegisteringUtils.registerBakedModel(modelRegistry, ModBlocks.JAR.get(), JarBakedModel.class);
  }

  @SubscribeEvent
  public static void itemColorHandler(ColorHandlerEvent.Item event) {
    RegisteringUtils.registerItemColor(
      event.getItemColors(), 
      (stack, index) -> index == 1 ? Aspects.ASPECT_COLORS.getOrDefault(IAspectStorage.getStoredAspect(stack), 0xffffff) : 0xffffff, 
      ModItems.ASPECT_PHIAL.get()
    );

    RegisteringUtils.registerItemColor(
      event.getItemColors(), 
      (stack, index) -> index == 23 ? Aspects.ASPECT_COLORS.getOrDefault(IAspectStorage.getStoredAspect(stack), 0xffffff) : 0xffffff, 
      ModItems.ESSENTIA_JAR.get()
    );
  }

  @SubscribeEvent
  public static void blockColorHandler(ColorHandlerEvent.Block event) {
    RegisteringUtils.registerBlockColor(
      event.getBlockColors(), 
      (state, world, pos, index) -> index == 23 ? BiomeColors.getWaterColor(world, pos) | 0xff000000 : 0xffffff, 
      ModBlocks.CRUCIBLE.get()
    );

    RegisteringUtils.registerBlockColor(
      event.getBlockColors(), 
      (state, world, pos, index) -> {
        if (index != 23) return 0xffffff;

        TileEntity te = world.getTileEntity(pos);
        String aspect = ((EssentiaJarTE)te).getAspect();
        return aspect.equals(Aspects.NONE) ? 0xffffff : Aspects.ASPECT_COLORS.get(aspect);
      }, 
      ModBlocks.JAR.get()
    );
  }
}
