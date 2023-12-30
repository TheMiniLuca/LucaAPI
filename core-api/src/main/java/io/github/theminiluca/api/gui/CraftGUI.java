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

public class CraftGUI implements GUI {
    public final CustomGUI customTitle;

    public final int line;
    public String prefix = null;

    public InventoryFrame[] frame = new InventoryFrame[]{};
    public String translatable = null;


    public CraftGUI(CustomGUI customTitle, int line) {
        this.customTitle = customTitle;
        this.line = line;
    }


    public Inventory create(UUID uniqueId, SortMethod method) {
        Inventory inventory;

        if (prefix == null) {
            inventory = Bukkit.createInventory(null, Math.min(line, 6) * 9,
                    (translatable != null ? translatable : customTitle.title()));
        } else {
            inventory = Bukkit.createInventory(null, Math.min(line, 6) * 9, prefix + " " +
                    (translatable != null ? translatable : customTitle.title()));
        }
        Set<InventoryFrame> frames = new HashSet<>(List.of(frame));
        int standard = (line - 1) * 9;
        if (frames.contains(InventoryFrame.EXIT_ON_CLOSE)) {
            inventory.setItem(standard + 4, exit().getItem(uniqueId));
        }
        if (frames.contains(InventoryFrame.PAGE)) {
            inventory.setItem(standard, back().getItem(uniqueId));
            inventory.setItem(standard + 8, forward().getItem(uniqueId));
        }
        if (frames.contains(InventoryFrame.SORTED)) {
            inventory.setItem(standard, sortItem(method).getItem(uniqueId));
        }
        return inventory;
    }


    @Override
    public void frame(InventoryFrame... frame) {
        this.frame = frame;
    }

    public void setFrame(UUID uniqueId, InventoryFrame... frame) {

    }

    private ItemGUI back() {
        return new ItemGUI("back-item") {
            public @NotNull ItemStack item(UUID uuid, Object... args) {
                assert args[0] instanceof Integer : "0번은 Integer 값이여야합니다.";
                return ItemExtension.item(Material.ARROW).setDisplayName(Colour.GREEN + "뒤로 가기").addLore(Colour.WHITE + "현재 페이지 : " + args[0]);
            }
        };
    }

    private ItemGUI exit() {
        return new ItemGUI("gui-exit") {
            @Override
            public @NotNull ItemStack item(UUID uuid, Object... objects) {
                return ItemExtension.item(Material.BARRIER).setDisplayName(Colour.RED + "나가기");
            }
        };
    }

    private ItemGUI forward() {
        return new ItemGUI("forward-item") {
            @Override
            public @NotNull ItemStack item(UUID uuid, Object... args) {
                assert args[0] instanceof Integer : "0번은 Integer 값이여야합니다.";
                return ItemExtension.item(Material.ARROW).setDisplayName(Colour.GREEN + "앞으로 가기")
                        .addLore(Colour.WHITE + "현재 페이지 : " + args[0]);
            }
        };
    }

    private ItemGUI sortItem(SortMethod method) {
        return new ItemGUI("gui-sorted") {
            @Override
            public @NotNull ItemStack item(UUID uuid, Object... objects) {
                return ItemExtension.item(Material.OAK_SIGN).setDisplayName(Colour.WHITE + "정렬 순서 ▼").setLore(Colour.GRAY + method.display);
            }
        };
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
