package io.github.theminiluca.api.utils;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public abstract class InteractItem {

    private static final Map<String, InteractItem> interactItems = new HashMap<>();

    public static InteractItem getInteract(String unique) {
        return interactItems.get(unique);
    }
    public static InteractItem getInteract(Class<? extends InteractItem> clazz) {
        try {
            return clazz.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }



    public static void interactEvent(PlayerInteractEvent event) {
        ItemStack is = event.getItem();
        if (is == null) return;
        if (is.getType().isAir()) return;
        interactItems.get(localized(is)).interact(event);
    }

    public static <T extends InteractItem> void registerInteract(T interactItem) {
        interactItems.put(interactItem.unique(), interactItem);
    }

    public static String localized(ItemStack is) {
        return is == null || is.getType().isAir() || is.getItemMeta() == null ? null : is.getItemMeta().getLocalizedName();
    }

    protected abstract @NotNull ItemStack item(UUID uniqueId, Object... args);

    public final @NotNull ItemStack getItem(UUID uniqueId, Object... args) {
        ItemStack itemStack = item(uniqueId, args);
        if (itemStack.getType().isAir()) throw new IllegalArgumentException("아이템은 AIR 타입이 될 수 없습니다.");
        ItemMeta im = itemStack.getItemMeta();
        assert im != null;
        im.setLocalizedName(unique());
        itemStack.setItemMeta(im);
        return itemStack;
    }

    public abstract @NotNull String unique();

    public abstract void interact(PlayerInteractEvent event);

    public boolean otherCondition(Player player) {
        return true;
    }
}
