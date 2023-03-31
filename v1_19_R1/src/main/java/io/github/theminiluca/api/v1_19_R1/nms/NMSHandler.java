package io.github.theminiluca.api.v1_19_R1.nms;

import io.github.theminiluca.api.messages.ComponentText;
import io.github.theminiluca.api.utils.NMS;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.ItemTag;
import net.md_5.bungee.api.chat.hover.content.Item;
import net.minecraft.nbt.NBTTagCompound;
import org.bukkit.craftbukkit.v1_19_R1.inventory.CraftItemStack;
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

    @Override
    public NBTTagCompound getNBTTag(ItemStack itemStack) {
        return CraftItemStack.asNMSCopy(itemStack).v();
    }
}
