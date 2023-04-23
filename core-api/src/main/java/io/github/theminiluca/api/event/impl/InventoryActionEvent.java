package io.github.theminiluca.api.event.impl;

import io.github.theminiluca.api.inventory.CustomTitle;
import io.github.theminiluca.api.utils.ItemExtension;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;


public class InventoryActionEvent extends InventoryClickEvent {
    private static final HandlerList handlers = new HandlerList();
    public InventoryActionEvent(@NotNull InventoryView view, @NotNull InventoryType.SlotType type, int slot, @NotNull ClickType click, @NotNull InventoryAction action) {
        super(view, type, slot, click, action);
    }

    public InventoryActionEvent(@NotNull InventoryView view, @NotNull InventoryType.SlotType type, int slot, @NotNull ClickType click, @NotNull InventoryAction action, int key) {
        super(view, type, slot, click, action, key);
    }

    public ItemExtension currentItem() {
        return ItemExtension.item(getCurrentItem());
    }

    public Player player() {
        return (Player) getView().getPlayer();
    }

    public UUID uniqueId() {
        return player().getUniqueId();
    }

    public CustomTitle title() {
        return CustomTitle.valueOf(getView().getTitle());
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
