package io.github.theminiluca.api.utils;

import io.github.theminiluca.api.LucaAPI;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public abstract class ItemGUI {

    public abstract @NotNull ItemStack item(UUID uniqueId, Object... args);
    public abstract @NotNull String unique();

    public final @NotNull ItemStack getItem(UUID uniqueId, Object... args) {
        return LucaAPI.localizedItem(unique(), item(uniqueId, args));
    }
}
