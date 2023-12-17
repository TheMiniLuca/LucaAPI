package io.github.theminiluca.api.v1_20_R2.nms;

import io.github.theminiluca.api.messages.ComponentText;
import io.github.theminiluca.api.utils.NMS;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.ItemTag;
import net.md_5.bungee.api.chat.hover.content.Item;
import net.minecraft.core.BlockPosition;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayOutBlockChange;
import net.minecraft.network.protocol.game.PacketPlayOutOpenSignEditor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.TileEntitySign;
import net.minecraft.world.level.block.state.IBlockData;
import org.bukkit.craftbukkit.v1_20_R2.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_20_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_20_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.CompletableFuture;

public class NMSHandler implements NMS {

    @Override
    public ComponentText hoverItem(ComponentText text, ItemStack stack) {
        net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(stack);
        if (!nmsItem.t()) return text;
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
