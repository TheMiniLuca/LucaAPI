package io.github.theminiluca.api.gui;

import io.github.theminiluca.api.utils.Colour;
import io.github.theminiluca.api.utils.ItemExtension;
import io.github.theminiluca.api.utils.ItemGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static io.github.theminiluca.api.gui.CustomGUI.*;

public class CraftGUI implements GUI {
    public final CustomGUI customTitle;

    public final int line;
    public String prefix = null;

    public String translatable = null;


    public CraftGUI(CustomGUI customTitle, int line) {
        this.customTitle = customTitle;
        this.line = line;
    }


    public Inventory create() {
        Inventory inventory;

        if (prefix == null) {
            inventory = Bukkit.createInventory(null, Math.min(line, 6) * 9,
                    (translatable != null ? translatable : customTitle.title()));
        } else {
            inventory = Bukkit.createInventory(null, Math.min(line, 6) * 9, prefix + " " +
                    (translatable != null ? translatable : customTitle.title()));
        }
        return inventory;
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
