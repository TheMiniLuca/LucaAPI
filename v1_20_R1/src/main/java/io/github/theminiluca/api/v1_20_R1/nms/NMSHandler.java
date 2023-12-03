package io.github.theminiluca.api.v1_20_R1.nms;

import io.github.theminiluca.api.messages.ComponentText;
import io.github.theminiluca.api.utils.NMS;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.ItemTag;
import net.md_5.bungee.api.chat.hover.content.Item;
import net.minecraft.core.BlockPosition;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.PacketPlayOutBlockChange;
import net.minecraft.network.protocol.game.PacketPlayOutOpenSignEditor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.TileEntitySign;
import net.minecraft.world.level.block.state.IBlockData;
import org.bukkit.craftbukkit.v1_20_R1.block.CraftSign;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.CompletableFuture;

import static org.bukkit.Bukkit.getPlayer;

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

    public void {
        Player player = getPlayer();

        final BlockPosition blockPosition = new BlockPosition(player.getLocation().getBlockX(), 1, player.getLocation().getBlockZ());
        IBlockData signData = Blocks.cE.n();
        PacketPlayOutBlockChange packet = new PacketPlayOutBlockChange(blockPosition, signData);
        sendPacket(packet);
        IChatBaseComponent[] ComponentTexts = CraftSign.sanitizeLines(withLines);
        TileEntitySign sign = new TileEntitySign(blockPosition, signData);
        for (var i = 0; i < ComponentTexts.length; i++)
            sign.a(ComponentTexts[i]);
        sendPacket(sign.h());
        callback = new CompletableFuture<>();
        PacketPlayOutOpenSignEditor outOpenSignEditor = new PacketPlayOutOpenSignEditor(blockPosition, true);
        sendPacket(outOpenSignEditor);
        return callback;
    }


    @Override
    public NBTTagCompound getNBTTag(ItemStack itemStack) {
        return CraftItemStack.asNMSCopy(itemStack).v();
    }
}
