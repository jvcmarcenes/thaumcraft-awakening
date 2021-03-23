package io.github.jvcmarcenes.tca.util;

import com.google.gson.JsonObject;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class JsonHelper {

  public static Item deserializeItem(JsonObject obj, String key) {
    String itemName = JSONUtils.getString(obj, key);
    Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName));

    return item;
  }

  public static ItemStack deserializeItemStack(JsonObject obj) {
    Item item = deserializeItem(obj, "item");
    int count = JSONUtils.getInt(obj, "count", 1);

    return new ItemStack(item, count);
  }
}
