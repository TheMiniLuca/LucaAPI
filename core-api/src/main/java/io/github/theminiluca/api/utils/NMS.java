package io.github.theminiluca.api.utils;

import io.github.theminiluca.api.messages.ComponentText;
import net.minecraft.nbt.NBTTagCompound;
import org.bukkit.inventory.ItemStack;

public interface NMS {

    ComponentText hoverItem(ComponentText text, ItemStack itemStack);
    NBTTagCompound getNBTTag(ItemStack itemStack);
}
