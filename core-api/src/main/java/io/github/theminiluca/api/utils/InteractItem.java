package io.github.theminiluca.api.utils;

import io.github.theminiluca.api.LucaAPI;
import io.github.theminiluca.api.event.impl.InventoryActionEvent;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import static io.github.theminiluca.api.LucaAPI.localized;

public abstract class InteractItem {

    private static final Map<String, Class<? extends InteractItem>> interactItems = new HashMap<>();

    public static Class<?> getInteract(String unique) {
        return interactItems.get(unique);
    }

    public static InteractItem getInteract(@NotNull Class<? extends InteractItem> clazz) {
        try {
            return clazz.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            return null;
        }
    }


    public static boolean interactEvent(@NotNull PlayerInteractEvent event) {
        ItemStack is = event.getPlayer().getInventory().getItemInMainHand();
        if (is.getType().isAir()) return false;
        Class<? extends InteractItem> interact = interactItems.getOrDefault(localized(is), null);
        if (interact == null) return false;
        InteractItem interactItem = getInteract(interact);
        if (interactItem != null && interactItem.otherCondition(event.getPlayer().getUniqueId())) {
            interactItem.interact(event);
            return true;
        }
        return false;
    }

    public static boolean interactActionEvent(@NotNull InventoryActionEvent event) {
        ItemStack is = event.currentItem();
        if (is.getType().isAir()) return false;
        Class<? extends InteractItem> interact = interactItems.getOrDefault(localized(is), null);
        if (interact == null) return false;
        InteractItem interactItem = getInteract(interact);
        if (interactItem != null && interactItem.otherCondition(event.player().getUniqueId())) {
            interactItem.interactInventory(event);
            return true;
        }
        return false;
    }

    public static boolean itemDropEvent(@NotNull PlayerDropItemEvent event) {
        ItemStack is = event.getItemDrop().getItemStack();
        if (is.getType().isAir()) return false;
        Class<? extends InteractItem> interact = interactItems.getOrDefault(localized(is), null);
        if (interact == null) return false;
        InteractItem interactItem = getInteract(interact);
        if (interactItem != null && interactItem.otherCondition(event.getPlayer().getUniqueId())) {
            interactItem.itemDrop(event);
            return true;
        }
        return false;
    }

    public static boolean itemPickupEvent(@NotNull EntityPickupItemEvent event) {
        ItemStack is = event.getItem().getItemStack();
        if (is.getType().isAir()) return false;
        Class<? extends InteractItem> interact = interactItems.getOrDefault(localized(is), null);
        if (interact == null) return false;
        InteractItem interactItem = getInteract(interact);
        if (interactItem != null && interactItem.otherCondition(event.getEntity().getUniqueId())) {
            interactItem.itemPickup(event);
            return true;
        }
        return false;
    }

    public static boolean interactEntityEvent(@NotNull PlayerInteractEntityEvent event) {
        ItemStack is = event.getPlayer().getInventory().getItemInMainHand();
        if (is.getType().isAir()) return false;
        Class<? extends InteractItem> interact = interactItems.getOrDefault(localized(is), null);
        if (interact == null) return false;
        InteractItem interactItem = getInteract(interact);
        if (interactItem != null && interactItem.otherCondition(event.getPlayer().getUniqueId())) {
            interactItem.interactEntity(event);
            return true;
        }
        return false;
    }

    public static <T extends InteractItem> void registerInteract(@NotNull T interactItem) {
        interactItems.put(interactItem.unique(), interactItem.getClass());
    }

    protected abstract @NotNull ItemStack item(UUID uniqueId, Object... args);

    public final @NotNull ItemStack getItem(UUID uniqueId, Object... args) {
        return LucaAPI.localizedItem(unique(), item(uniqueId, args));
    }

    public abstract @NotNull String unique();

    public abstract void interact(PlayerInteractEvent event);

    public void interactInventory(InventoryActionEvent event) {

    }

    public void itemDrop(PlayerDropItemEvent event) {

    }
    public void itemPickup(EntityPickupItemEvent event) {

    }
    public void interactEntity(PlayerInteractEntityEvent event) {

    }

    ;

    public boolean otherCondition(UUID uniqueId) {
        return true;
    }
}
