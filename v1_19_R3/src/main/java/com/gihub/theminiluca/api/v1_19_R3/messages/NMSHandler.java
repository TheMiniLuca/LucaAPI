package com.gihub.theminiluca.api.v1_19_R3.messages;

import com.gihub.theminiluca.api.messages.ComponentText;
import com.gihub.theminiluca.api.utils.NMS;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.ItemTag;
import net.md_5.bungee.api.chat.hover.content.Item;
import net.minecraft.nbt.NBTTagCompound;
import org.bukkit.craftbukkit.v1_19_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class NMSHandler implements NMS {


    @Override
    public ComponentText hoverItem(ComponentText text, ItemStack stack) {
        net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(stack);
        if (!nmsItem.s()) return text;
        NBTTagCompound compound = nmsItem.v();
        if (compound == null) return text;
        text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new Item("minecraft:" + nmsItem.c().toString(),
                1, ItemTag.ofNbt(compound.toString()))));
        return text;
    }
}
