package io.github.theminiluca.api.inventory;

import io.github.theminiluca.api.event.impl.InventoryActionEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

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
    public static final Set<CustomTitle> titles = new HashSet<>();
    private String title;
    public final Map<String, Integer> page = new HashMap<>();
    public final Map<String, Object> data = new HashMap<>();
    public final Map<String, Map<String, Object>> hashMapData = new HashMap<>();

    public final Map<ItemStack, String> itemMap = new HashMap<>();


    public void interact(InventoryActionEvent event) {

    }

    public void close(InventoryCloseEvent event) {

    }

    public abstract void view(String uniqueId, String... args);

    public static void clickEvent(InventoryActionEvent event) {
        final CustomTitle title = valueOf(event.getView().getTitle());
        title.interact(event);
    }

    public static void closeEvent(InventoryCloseEvent event) {
        final CustomTitle title = valueOf(event.getView().getTitle());
        title.close(event);
    }

    private final boolean flag;

    public CustomTitle(String title, boolean flag) {
        this.title = title;
        this.flag = flag;
        if (!flag)
            titles.add(this);
    }


    public int getPage(String uniqueId, int defaults) {
        if (!page.containsKey(uniqueId)) {
            page.put(uniqueId, defaults);
        }
        return page.get(uniqueId);
    }

    public Object getData(String uniqueId, Object defaults) {
        if (!data.containsKey(uniqueId)) {
            data.put(uniqueId, defaults);
        }
        return data.get(uniqueId);
    }

    public Map<String, Map<String, Object>> getHashMapData() {
        return hashMapData;
    }

    public CustomTitle translatable(String message) {
        this.title = message;
        return this;
    }

    public boolean flag() {
        return flag;
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

    public static CustomTitle valueOf(String title) {
        for (CustomTitle customTitle : titles) {
            if (customTitle.title.contains(title))
                return customTitle;
        }
        throw new NullPointerException("존재 하지 않는 커스텀 타이틀");
    }

    public static CustomTitle valueOf(Class<? extends CustomTitle> clazz) {
        for (CustomTitle customTitle : titles) {
            if (customTitle.getClass().equals(clazz))
                return customTitle;
        }
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
