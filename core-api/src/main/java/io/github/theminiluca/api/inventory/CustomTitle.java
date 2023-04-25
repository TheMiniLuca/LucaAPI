package io.github.theminiluca.api.inventory;

import io.github.theminiluca.api.event.impl.InventoryActionEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;
import java.util.*;

public abstract class CustomTitle implements Cloneable {

    public enum SortMethod {
        LATEST("최신"),
        OLDEST("최고");

        public final String display;

        SortMethod(String display) {
            this.display = display;
        }
    }

    public static <T extends CustomTitle> void registerGUI(T customTitle) {
        titles.put(customTitle.getClass().getName(), customTitle);
    }

    public static final Map<String, CustomTitle> titles = new HashMap<>();
    private String title;
    public final Map<UUID, Integer> page = new HashMap<>();
    public final Map<UUID, Object> data = new HashMap<>();
    public final Map<UUID, Map<String, Object>> hashMapData = new HashMap<>();

    public final Map<ItemStack, String> itemMap = new HashMap<>();


    public void interact(InventoryActionEvent event) {

    }

    public void close(InventoryCloseEvent event) {

    }

    public abstract void view(UUID uniqueId, String... args);

    public static void clickEvent(InventoryActionEvent event) {
        final CustomTitle title = valueOf(event.getView().getTitle());
        title.interact(event);
    }

    public static void closeEvent(InventoryCloseEvent event) {
        final CustomTitle title = valueOf(event.getView().getTitle());
        title.close(event);
    }

    public CustomTitle(String title) {
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

    public CustomTitle translatable(String message) {
        this.title = message;
        return this;
    }

    public String title() {
        return title;
    }

    private final boolean contains(CustomTitle title) {
        return this.title().contains(title.title());
    }

    public final CustomTitle append(String name, boolean forward) {
        if (!forward)
            this.title = this.title + " " + name;
        else
            this.title = name + " " + this.title;
        return this;
    }

    public final CustomTitle append(String name) {
        return append(name, false);
    }


    public final String extract() {
        String temp = this.title();
        temp = temp.replace(this.title(), "");

        return temp;
    }

    public final String extract_1() {
        String temp = this.title();
        temp = temp.replace(" " + this.title(), "");

        return temp;
    }

    public boolean otherCondition(Player player) {
        return true;
    };



    public static CustomTitle valueOf(String title) {
        for (CustomTitle customTitle : titles.values()) {
            if (customTitle.title.contains(title))
                return customTitle;
        }
        throw new NullPointerException("존재 하지 않는 커스텀 타이틀");
    }

    public static CustomTitle valueOf(Class<? extends CustomTitle> clazz) {
        if (titles.containsKey(clazz.getName())) {
            return titles.get(clazz.getName());
        } else
            throw new NullPointerException("존재 하지 않는 커스텀 타이틀");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomTitle that = (CustomTitle) o;
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

    public GUI createGUI(int line) {
        return new CraftGUI(this, line);
    }

    @Deprecated
    public GUI createInventory(int line) {
        return new CraftGUI(this, line);
    }


    @Override
    public CustomTitle clone() {
        try {
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return (CustomTitle) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
