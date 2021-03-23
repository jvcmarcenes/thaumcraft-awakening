package io.github.jvcmarcenes.tca.client;

import io.github.jvcmarcenes.tca.TCA;
import io.github.jvcmarcenes.tca.client.gui.ArcaneWorkbenchScreen;
import io.github.jvcmarcenes.tca.init.ModContainerTypes;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = TCA.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEventHandler {

    @SubscribeEvent
    public static void clientSetup(final FMLClientSetupEvent event) {
        TCA.LOGGER.info("doing client setup");

        // Container Screen Registration
        event.enqueueWork(() -> {
            ScreenManager.registerFactory(ModContainerTypes.ARCANE_WORKBENCH.get(), ArcaneWorkbenchScreen::new);
        });
    }
}
