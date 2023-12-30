package io.github.theminiluca.api.gui;

import org.bukkit.inventory.Inventory;

import java.util.UUID;

public interface GUI {

    Inventory create(UUID uniqueId, SortMethod method);

    void translatable(String translatable);
    void prefix(String prefix);

    void frame(InventoryFrame... frame);
}
