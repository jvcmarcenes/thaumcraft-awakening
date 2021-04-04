package io.github.jvcmarcenes.tca.client.models;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import io.github.jvcmarcenes.tca.blocks.EssentiaJar.EssentiaJarTE;
import io.github.jvcmarcenes.tca.util.BakedModelHelper;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelProperty;

public class JarBakedModel implements IBakedModel {
  
  public JarBakedModel(IBakedModel baseModel) {
    this.baseModel = baseModel;
  }

  public static ModelProperty<Integer> LEVEL = new ModelProperty<>();

  @Override
  public List<BakedQuad> getQuads(BlockState state, Direction side, Random rand, IModelData data) {

    List<BakedQuad> quads = new LinkedList<>();
    quads.addAll(baseModel.getQuads(state, side, rand, data));

    if (!data.hasProperty(LEVEL) || data.getData(LEVEL) == 0) return quads;

    float waterLevel = (data.getData(LEVEL)/(float)EssentiaJarTE.MAX_STORAGE) * 10 + 1;

    quads.addAll(BakedModelHelper.bakeCuboid(new Vector3f(4, 1, 4), new Vector3f(12, waterLevel, 12), 
      new ResourceLocation("tca", "block/animatedglow"), new float[][] {{4, 4, 12, 12}, {4, 3, 12, waterLevel + 3}}, 
      new int[] {1, 1, 0, 0, 1, 1}, 23));

    return quads;
  }

  private IBakedModel baseModel;

  @Override
  public List<BakedQuad> getQuads(BlockState state, Direction side, Random rand) {
    return null; // never should get called
  }

  @Override
  public boolean isAmbientOcclusion() { return baseModel.isAmbientOcclusion(); }

  @Override
  public boolean isGui3d() { return baseModel.isGui3d(); }

  @Override
  public boolean isSideLit() { return baseModel.isSideLit(); }

  @Override
  public boolean isBuiltInRenderer() { return baseModel.isBuiltInRenderer(); }

  @Override
  public TextureAtlasSprite getParticleTexture() { return baseModel.getParticleTexture(); }

  @Override
  public ItemOverrideList getOverrides() { return baseModel.getOverrides(); }

}
