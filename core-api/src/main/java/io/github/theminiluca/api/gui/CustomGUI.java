package io.github.theminiluca.api.gui;

import io.github.theminiluca.api.LucaAPI;
import io.github.theminiluca.api.event.impl.InventoryActionEvent;
import io.github.theminiluca.api.messages.BaseUser;
import io.github.theminiluca.api.messages.LanguageManager;
import io.github.theminiluca.api.utils.BukkitSound;
import io.github.theminiluca.api.utils.Colour;
import io.github.theminiluca.api.utils.ItemExtension;
import io.github.theminiluca.api.utils.ItemGUI;
import net.minecraft.world.level.levelgen.HeightMap;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;

import static io.github.theminiluca.api.messages.LanguageManager.formatted;

public abstract class CustomGUI {


    public static final InventoryFrame[] FRAMES = new InventoryFrame[]{InventoryFrame.PAGE, InventoryFrame.SORTED, InventoryFrame.EXIT_ON_CLOSE};

    public static <T extends CustomGUI> void registerGUI(T customTitle) {
        titles.put(customTitle.getClass().getName(), customTitle);
    }

    public static final Map<String, CustomGUI> titles = new HashMap<>();
    private String title;
    public int line = 0;

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

    public BaseSort getBaseSort(UUID uniqueId, Object defaults) {
        if (!data.containsKey(uniqueId)) {
            data.put(uniqueId, defaults);
        }
        return (BaseSort) data.get(uniqueId);
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

    public void setFrame(UUID uniqueId, Inventory inv, InventoryFrame... frame) {
        Set<InventoryFrame> frames = new HashSet<>(List.of(frame));
        int standard = (line - 1) * 9;
        int page = this.page.getOrDefault(uniqueId, 0);
        if (frames.contains(InventoryFrame.EXIT_ON_CLOSE)) {
            inv.setItem(standard + 4, exit().getItem(uniqueId));
        }
        if (frames.contains(InventoryFrame.PAGE)) {
            if (page > 0)
                inv.setItem(standard, back().getItem(uniqueId, page));
            inv.setItem(standard + 8, forward().getItem(uniqueId, page));
        }
        if (frames.contains(InventoryFrame.SORTED)) {
            inv.setItem(standard + 5, sortItem(getBaseSort(uniqueId, BaseSort.LATEST)).getItem(uniqueId));
        }
        if (frames.contains(InventoryFrame.SEARCH)) {
            inv.setItem(standard + 6, search().getItem(uniqueId));
        }
    }

    public boolean frameEvents(UUID uniqueId, String localized) {
        Player player = Bukkit.getPlayer(uniqueId);
        if (player == null) throw new NullPointerException();
        int page = getPage(uniqueId, 0);
        switch (localized) {
            case "gui-exit" -> {
                player.closeInventory();
                return true;
            }
            case "gui-forward" -> {
                this.page.put(uniqueId, page + 1);
                this.view(uniqueId);
                return true;
            }
            case "gui-back" -> {
                this.page.put(uniqueId, Math.max(page - 1, 0));
                this.view(uniqueId);
                return true;
            }
            case "gui-sorted" -> {
                if (getBaseSort(uniqueId, BaseSort.LATEST).equals(BaseSort.LATEST)) {
                    this.data.put(uniqueId, BaseSort.OLDEST);
                } else {
                    this.data.put(uniqueId, BaseSort.LATEST);
                }
                player.playSound(player.getLocation(), Sound.BLOCK_LEVER_CLICK, 1.0F, 2.0F);
                this.view(uniqueId);
                return true;
            }
        }
        return false;
    }

    public GUI createGUI(int line) {
        this.line = line;
        return new CraftGUI(this, line);
    }


    protected static ItemGUI exit() {
        return new ItemGUI("gui-exit") {
            @Override
            public @NotNull ItemStack item(UUID uuid, Object... args) {
                BaseUser user = LucaAPI.lucaAPI().getBaseUser(uuid);
                return ItemExtension.item(Material.BARRIER).setDisplayName(user.translatable("gui.exit.name"));
            }
        };
    }

    protected static ItemGUI sortItem(BaseSort method) {
        return new ItemGUI("gui-sorted") {
            @Override
            public @NotNull ItemStack item(UUID uuid, Object... args) {
                BaseUser user = LucaAPI.lucaAPI().getBaseUser(uuid);
                return ItemExtension.item(Material.OAK_SIGN).setDisplayName(formatted(user.translatable("gui.sorted.name"),
                        user.translatable(method.propertiesKey())));
            }
        };
    }

    protected static ItemGUI forward() {
        return new ItemGUI("gui-forward") {
            @Override
            public @NotNull ItemStack item(UUID uuid, Object... args) {
                assert args[0] instanceof Integer : "0번은 Integer 값이여야합니다.";
                BaseUser user = LucaAPI.lucaAPI().getBaseUser(uuid);
                return ItemExtension.item(Material.ARROW).setDisplayName(user.translatable("gui.forward.name"))
                        .addLore(Colour.WHITE + formatted(user.translatable("gui.now.page.name")));
            }
        };
    }

    protected static ItemGUI back() {
        return new ItemGUI("gui-back") {
            public @NotNull ItemStack item(UUID uuid, Object... args) {
                assert args[0] instanceof Integer : "0번은 Integer 값이여야합니다.";
                BaseUser user = LucaAPI.lucaAPI().getBaseUser(uuid);
                return ItemExtension.item(Material.ARROW).setDisplayName(user.translatable("gui.back.name"))
                        .addLore(Colour.WHITE + formatted(user.translatable("gui.now.page.name")));
            }
        };
    }

    protected static ItemGUI search() {
        return new ItemGUI("gui-search") {
            public @NotNull ItemStack item(UUID uuid, Object... args) {
                assert args[0] instanceof String : "0번은 String 값이여야합니다.";
                BaseUser user = LucaAPI.lucaAPI().getBaseUser(uuid);
                return ItemExtension.item(Material.ACACIA_SIGN).setDisplayName(user.translatable("gui.search.name"));
            }
        };
    }


}
