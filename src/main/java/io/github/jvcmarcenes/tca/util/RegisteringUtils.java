package io.github.jvcmarcenes.tca.util;

import java.util.Map;
import java.util.function.BiFunction;

import io.github.jvcmarcenes.tca.TCA;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;

public class RegisteringUtils {

  public static <B extends Block> void registerBakedModel(Map<ResourceLocation, IBakedModel> modelRegistry, B block, Class<? extends IBakedModel> bakedModel$class) {
    for (BlockState state : block.getStateContainer().getValidStates()) {
      ModelResourceLocation variant = BlockModelShapes.getModelLocation(state);
      IBakedModel existingModel = modelRegistry.get(variant);

      if (existingModel == null)
        TCA.LOGGER.warn("Did not find the expected vanilla baked model(s) for " + block.getRegistryName() + " in registry");
      else if (bakedModel$class.isInstance(existingModel))
        TCA.LOGGER.warn("Tried to replace " + block.getRegistryName() + " Baked Model twice");
      else
        try {
          modelRegistry.put(variant, (IBakedModel)bakedModel$class.getConstructor(IBakedModel.class).newInstance(existingModel));
        } catch (Exception e) {
          TCA.LOGGER.warn("Could not find constructor which matches the expected signature for " + block.getRegistryName() + " Baked Model");
        }
    }
  }

  public static void registerItemProperty(Item item, String property, TriFunction<ItemStack, ClientWorld, LivingEntity, Float> callback) {
    ItemModelsProperties.registerProperty(item, new ResourceLocation(TCA.MOD_ID, property), new IItemPropertyGetter(){
      @Override
      public float call(ItemStack stack, ClientWorld world, LivingEntity entity) {
        return callback.apply(stack, world, entity);
      }
    });
  }

  public static void registerItemColor(ItemColors colors, BiFunction<ItemStack, Integer, Integer> callback, Item... items) {
    colors.register(new IItemColor() {
      @Override
      public int getColor(ItemStack stack, int tintIndex) {
        return callback.apply(stack, tintIndex);
      }
    }, items);
  }

  public static void registerBlockColor(BlockColors colors, QuadFunction<BlockState, IBlockDisplayReader, BlockPos, Integer, Integer> callback, Block... blocks) {
    colors.register(new IBlockColor() {
      @Override
      public int getColor(BlockState state, IBlockDisplayReader world, BlockPos pos, int tintIndex) {
        return callback.apply(state, world, pos, tintIndex);
      }
    }, blocks);
  }

}
