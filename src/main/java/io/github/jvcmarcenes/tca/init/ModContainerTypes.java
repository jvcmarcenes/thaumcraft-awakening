package io.github.jvcmarcenes.tca.init;

import io.github.jvcmarcenes.tca.TCA;
import io.github.jvcmarcenes.tca.blocks.ArcaneWorkbench.ArcaneWorkbenchContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = TCA.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModContainerTypes {

  public static final DeferredRegister<ContainerType<?>> CONTAINER_TYPES = DeferredRegister.create(ForgeRegistries.CONTAINERS, TCA.MOD_ID);

  public static final RegistryObject<ContainerType<ArcaneWorkbenchContainer>> ARCANE_WORKBENCH = CONTAINER_TYPES.register(
    "arcane_workbench", () -> IForgeContainerType.create(ArcaneWorkbenchContainer::new));

}
