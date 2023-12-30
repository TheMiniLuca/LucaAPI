package io.github.theminiluca.api.gui;

import io.github.theminiluca.api.LucaAPI;
import io.github.theminiluca.api.event.impl.InventoryActionEvent;
import io.github.theminiluca.api.utils.Colour;
import io.github.theminiluca.api.utils.ItemExtension;
import io.github.theminiluca.api.utils.ItemGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;

public abstract class CustomGUI implements Cloneable {

    public static <T extends CustomGUI> void registerGUI(T customTitle) {
        titles.put(customTitle.getClass().getName(), customTitle);
    }

    public static final Map<String, CustomGUI> titles = new HashMap<>();
    private String title;

    public final Map<UUID, Integer> page = new HashMap<>();
    public final Map<UUID, Object> data = new HashMap<>();
    public final Map<UUID, Map<String, Object>> hashMapData = new HashMap<>();



    public void interact(InventoryActionEvent event) {

    }

    public void close(InventoryCloseEvent event) {

    }

    public final void view(UUID uniqueId, String... args) {
        Bukkit.getScheduler().runTask(LucaAPI.instance, () -> {
            _view(uniqueId, args);
        });
    }

    protected abstract void _view(UUID uniqueId, String... args);

    public static boolean clickEvent(InventoryActionEvent event, Function<String, String> map) {
        final CustomGUI title = valueOf(event.getView().getTitle(), map);
        if (title.otherCondition(event.player().getUniqueId())) {
            title.interact(event);
            return true;
        }
        return false;
    }

    public static void closeEvent(InventoryCloseEvent event, Function<String, String> map) {
        final CustomGUI title = valueOf(event.getView().getTitle(), map);
        if (title.otherCondition(event.getPlayer().getUniqueId()))
            title.close(event);
    }

    public CustomGUI(String title) {
        this.title = title;
    }


    public int getPage(UUID uniqueId, int defaults) {
        if (!page.containsKey(uniqueId)) {
            page.put(uniqueId, defaults);
        }
        return page.get(uniqueId);
    }

    public Object getData(UUID uniqueId, Object defaults) {
        if (!data.containsKey(uniqueId)) {
            data.put(uniqueId, defaults);
        }
        return data.get(uniqueId);
    }

    public Map<UUID, Map<String, Object>> getHashMapData() {
        return hashMapData;
    }

    public CustomGUI translatable(String message) {
        this.title = message;
        return this;
    }

    public String title() {
        return title;
    }

    private final boolean contains(CustomGUI title) {
        return this.title().contains(title.title());
    }

    public final CustomGUI append(String name, boolean forward) {
        if (!forward)
            this.title = this.title + " " + name;
        else
            this.title = name + " " + this.title;
        return this;
    }

    public final CustomGUI append(String name) {
        return append(name, false);
    }


    public final String extract(InventoryActionEvent event) {
        String temp = event.getView().getTitle();
        temp = temp.replace(this.title(), "");

        return temp;
    }

    public final String extract_1(InventoryActionEvent event) {
        String temp = event.getView().getTitle();
        temp = temp.replace(" " + this.title(), "");

        return temp;
    }

    public boolean otherCondition(UUID uniqueId) {
        return true;
    }

    ;


    public static CustomGUI valueOf(String title) {
        return valueOf(title, Function.identity());
    }

    public static CustomGUI valueOf(String title, Function<String, String> fun) {
        for (CustomGUI customTitle : titles.values()) {
            if (title.contains(fun.apply(customTitle.title)))
                return customTitle;
        }
        throw new NullPointerException("존재 하지 않는 커스텀 타이틀");
    }

    public static CustomGUI valueOf(Class<? extends CustomGUI> clazz) {
        if (titles.containsKey(clazz.getName())) {
            return titles.get(clazz.getName());
        } else
            throw new NullPointerException("존재 하지 않는 커스텀 타이틀");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomGUI that = (CustomGUI) o;
        return Objects.equals(title, that.title);
    }

    @Override
    public final int hashCode() {
        return title != null ? title.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "CustomTitle{" +
                "title='" + title + '\'' +
                '}';
    }

    public enum InventoryFrame {
        EXIT_ON_CLOSE,
        PAGE,
        SORTED


    }

    public GUI createGUI(int line) {
        return new CraftGUI(this, line);
    }

    @Deprecated
    public GUI createInventory(int line) {
        return new CraftGUI(this, line);
    }


    @Override
    public CustomGUI clone() {
        try {
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return (CustomGUI) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }





}
