package io.github.jvcmarcenes.tca.util;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.BlockFaceUV;
import net.minecraft.client.renderer.model.BlockPartFace;
import net.minecraft.client.renderer.model.FaceBakery;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.SimpleModelTransform;

@SuppressWarnings("deprecation")
public class BakedModelHelper {

  private static final FaceBakery faceBakery = new FaceBakery();

  public static List<BakedQuad> bakeCuboid(Vector3f start, Vector3f end, ResourceLocation spriteRL, float[][] uvMap, int tintIndex) {
    return bakeCuboid(start, end, spriteRL, uvMap, new int[]{0, 1, 2, 3, 4, 5}, tintIndex);
  }

  public static List<BakedQuad> bakeCuboid(Vector3f start, Vector3f end, ResourceLocation spriteRL, float[][] uvMap, int[] uvMapMap, int tintIndex) {
    List<BakedQuad> quads = new LinkedList<>();

    TextureAtlasSprite sprite = ModelLoader.instance().getSpriteMap().getAtlasTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE).getSprite(spriteRL);

    quads.add(faceBakery.bakeQuad(
      new Vector3f(start.getX(), start.getY(), start.getZ()), new Vector3f(start.getX(), end.getY(), end.getZ()),
      new BlockPartFace(null, tintIndex, "", new BlockFaceUV(uvMap[uvMapMap[0]], 0)), 
      sprite, Direction.WEST, SimpleModelTransform.IDENTITY, 
      null, true, new ResourceLocation("dummy")));

    quads.add(faceBakery.bakeQuad(
      new Vector3f(end.getX(), start.getY(), start.getZ()), new Vector3f(end.getX(), end.getY(), end.getZ()),
      new BlockPartFace(null, tintIndex, "", new BlockFaceUV(uvMap[uvMapMap[1]], 0)), 
      sprite, Direction.EAST, SimpleModelTransform.IDENTITY, 
      null, true, new ResourceLocation("dummy")));

    quads.add(faceBakery.bakeQuad(
      new Vector3f(start.getX(), start.getY(), start.getZ()), new Vector3f(end.getX(), start.getY(), end.getZ()),
      new BlockPartFace(null, tintIndex, "", new BlockFaceUV(uvMap[uvMapMap[2]], 0)), 
      sprite, Direction.DOWN, SimpleModelTransform.IDENTITY, 
      null, true, new ResourceLocation("dummy")));

    quads.add(faceBakery.bakeQuad(
      new Vector3f(start.getX(), end.getY(), start.getZ()), new Vector3f(end.getX(), end.getY(), end.getZ()),
      new BlockPartFace(null, tintIndex, "", new BlockFaceUV(uvMap[uvMapMap[3]], 0)), 
      sprite, Direction.UP, SimpleModelTransform.IDENTITY, 
      null, true, new ResourceLocation("dummy")));

    quads.add(faceBakery.bakeQuad(
      new Vector3f(start.getX(), start.getY(), start.getZ()), new Vector3f(end.getX(), end.getY(), start.getZ()),
      new BlockPartFace(null, tintIndex, "", new BlockFaceUV(uvMap[uvMapMap[4]], 0)), 
      sprite, Direction.NORTH, SimpleModelTransform.IDENTITY, 
      null, true, new ResourceLocation("dummy")));

    quads.add(faceBakery.bakeQuad(
      new Vector3f(start.getX(), start.getY(), end.getZ()), new Vector3f(end.getX(), end.getY(), end.getZ()),
      new BlockPartFace(null, tintIndex, "", new BlockFaceUV(uvMap[uvMapMap[5]], 0)), 
      sprite, Direction.SOUTH, SimpleModelTransform.IDENTITY, 
      null, true, new ResourceLocation("dummy")));

    return quads;
  }

  
}
