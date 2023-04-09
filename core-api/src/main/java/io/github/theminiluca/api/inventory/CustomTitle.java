package io.github.theminiluca.api.inventory;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class CustomTitle {
    public static final Set<CustomTitle> titles = new HashSet<>();
    private String title;
    public final HashMap<String, Integer> page = new HashMap<>();
    public final HashMap<String, Object> data = new HashMap<>();
    public final HashMap<String, HashMap<String, Object>> hashMapData = new HashMap<>();
    public InputRunnable runnables;


    private final boolean flag;

    public CustomTitle(String title, boolean flag) {
        this.title = title;
        this.flag = flag;
        titles.add(this);
    }

    public CustomTitle(String title) {
        this.title = title;
        this.flag = false;
        titles.add(this);
    }

    public void run(String uniqueId, String... args) {
        runnables.running(uniqueId, args);
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
    public HashMap<String, HashMap<String, Object>> getHashMapData() {
        return hashMapData;
    }

    public CustomTitle translatable(String message) {
        return new CustomTitle(message);
    }
    public boolean flag() {
        return flag;
    }

    public String title() {
        return title;
    }

    public final boolean contains(CustomTitle title) {
        return this.title().contains(title.title());
    }

    public final CustomTitle append(String name, boolean forward) {
        if (!forward)
            return new CustomTitle(this.title + " " + name);
        else
            return new CustomTitle((name + " " + this.title));
    }

    public final CustomTitle append(String name) {
        return append(name, false);
    }


    public final String extract(CustomTitle title) {
        String temp = this.title();
        temp = temp.replace(title.title(), "");

        return temp;
    }

    public final String extract_1(CustomTitle title) {
        String temp = this.title();
        temp = temp.replace(" " + title.title(), "");

        return temp;
    }

    public static CustomTitle customtitle(String name) {
        return new CustomTitle(name);
    }

    @Override
    public final boolean equals(Object o) {
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

    public static Inventory createInventory(CustomTitle title, int line) {
        return Bukkit.createInventory(null, Math.min(line, 6) * 9, title.title());
    }

}
