package io.github.theminiluca.api.inventory;

import org.bukkit.inventory.Inventory;

public interface GUI {

    Inventory create();

    void translatable(String translatable);
    void prefix(String prefix);
}
