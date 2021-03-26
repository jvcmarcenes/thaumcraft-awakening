package io.github.jvcmarcenes.tca.client.models;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import io.github.jvcmarcenes.tca.blocks.Crucible.Crucible;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.BlockFaceUV;
import net.minecraft.client.renderer.model.BlockPartFace;
import net.minecraft.client.renderer.model.FaceBakery;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.SimpleModelTransform;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelProperty;

public class CrucibleBakedModel implements IBakedModel {

  public CrucibleBakedModel(IBakedModel baseModel) {
    this.baseModel = baseModel;
  }

  public static ModelProperty<Integer> LEVEL = new ModelProperty<>();

  // @Override
  // public IModelData getModelData(IBlockDisplayReader world, BlockPos pos, BlockState state, IModelData tileData) {
  //   return (new ModelDataMap.Builder()).withInitial(LEVEL, state.get(Crucible.LEVEL)).build();
  // }

  @Override
  public List<BakedQuad> getQuads(BlockState state, Direction side, Random rand, IModelData data) {
    
    List<BakedQuad> quads = new LinkedList<>();
    quads.addAll(baseModel.getQuads(state, side, rand, data));

    if (state.get(Crucible.LEVEL) == 0) return quads;

    float waterLevel = ((float)state.get(Crucible.LEVEL)/(float)Crucible.MAX_LEVEL) * 11 + 4;
    
    BlockFaceUV blockFaceUV = new BlockFaceUV(new float[]{2, 2, 14, 14}, 0);
    BlockPartFace blockPartFace = new BlockPartFace(null, 23, "", blockFaceUV);

    TextureAtlasSprite tex = ModelLoader.instance().getSpriteMap().getAtlasTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE)
      .getSprite(new ResourceLocation("minecraft", "block/water_still"));

    BakedQuad waterQuad = faceBakery.bakeQuad(
      new Vector3f(2, waterLevel, 2), new Vector3f(14, waterLevel, 14), 
      blockPartFace, tex, Direction.UP, SimpleModelTransform.IDENTITY, 
      null, true, new ResourceLocation("dummy"));

    quads.add(waterQuad);

    return quads;
  }


  private FaceBakery faceBakery = new FaceBakery();
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
