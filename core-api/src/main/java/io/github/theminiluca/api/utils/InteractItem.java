package io.github.theminiluca.api.utils;

import io.github.theminiluca.api.LucaAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
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

    public static InteractItem getInteract(Class<? extends InteractItem> clazz) {
        try {
            return clazz.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            return null;
        }
    }


    public static void interactEvent(PlayerInteractEvent event) {
        ItemStack is = event.getPlayer().getInventory().getItemInMainHand();
        if (is.getType().isAir()) return;
        InteractItem interactItem = getInteract(interactItems.getOrDefault(localized(is), null));
        if (interactItem != null && interactItem.otherCondition(event.getPlayer().getUniqueId()))
            interactItem.interact(event);
    }

    public static <T extends InteractItem> void registerInteract(T interactItem) {
        interactItems.put(interactItem.unique(), interactItem.getClass());
    }

    protected abstract @NotNull ItemStack item(UUID uniqueId, Object... args);

    public final @NotNull ItemStack getItem(UUID uniqueId, Object... args) {
        return LucaAPI.localizedItem(unique(), item(uniqueId, args));
    }

    public abstract @NotNull String unique();

    public abstract void interact(PlayerInteractEvent event);

    public boolean otherCondition(UUID uniqueId) {
        return true;
    }
}
