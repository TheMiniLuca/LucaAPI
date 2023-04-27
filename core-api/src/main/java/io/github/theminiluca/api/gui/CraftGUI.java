package io.github.theminiluca.api.gui;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class CraftGUI implements GUI {
    public final CustomGUI customTitle;

    public final int line;
    public String prefix = null;
    public InventoryHolder owner = null;
    public String translatable = null;

    public CraftGUI(CustomGUI customTitle, int line) {
        this.customTitle = customTitle;
        this.line = line;
    }


    public Inventory create() {
        if (prefix == null) {
            return Bukkit.createInventory(null, Math.min(line, 6) * 9,
                    (translatable != null ? translatable : customTitle.title()));
        } else {
            return Bukkit.createInventory(null, Math.min(line, 6) * 9, prefix + " " +
                    (translatable != null ? translatable : customTitle.title()));
        }
    }

    @Override
    public void translatable(String translatable) {
        this.translatable = translatable;
    }

    @Override
    public void prefix(String prefix) {
        this.prefix = prefix;
    }
}
