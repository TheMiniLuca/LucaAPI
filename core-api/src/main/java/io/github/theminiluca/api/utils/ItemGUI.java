package io.github.theminiluca.api.utils;

import io.github.theminiluca.api.LucaAPI;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public abstract class ItemGUI {

    public final String unique;

    public ItemGUI(String unique) {
        this.unique = unique;
    }

    public abstract @NotNull ItemStack item(UUID uniqueId, Object... args);

    public String getUnique() {
        return unique;
    }

    public final @NotNull ItemStack getItem(UUID uniqueId, Object... args) {
        return LucaAPI.localizedItem(unique, item(uniqueId, args));
    }
}
