package io.github.theminiluca.api.v1_19_R3.user;

import io.github.theminiluca.api.LucaAPI;
import io.github.theminiluca.api.inventory.CustomTitle;
import io.github.theminiluca.api.messages.Component;
import io.github.theminiluca.api.messages.ComponentText;
import io.github.theminiluca.api.messages.LanguageManager;
import io.github.theminiluca.api.messages.TranslatableComponent;
import io.github.theminiluca.api.user.IUser;
import io.github.theminiluca.api.utils.BukkitSound;
import io.github.theminiluca.api.utils.Colour;
import io.github.theminiluca.api.utils.Duration;
import io.github.theminiluca.api.utils.Title;
import io.github.theminiluca.sql.SQLObject;
import net.md_5.bungee.api.chat.BaseComponent;
import net.minecraft.network.protocol.Packet;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class CraftUser implements IUser, SQLObject {

    protected final UUID uniqueId;
    protected String name;

    protected String locale = null;

    public boolean notTran = false;

    protected transient CompletableFuture<String[]> callback;

    public transient HashMap<String, Boolean> cooltimes = new HashMap<>();

    public CraftUser(UUID uniqueId, String name, long firstJoin) {
        this.uniqueId = uniqueId;
        this.name = name;
        this.firstJoin = firstJoin;
    }

    public void setName(String name) {
        this.name = name;
    }


    public final boolean isCooltime(String key) {
        return cooltimes.getOrDefault(key.toUpperCase(), false);
    }

    public final void setCooltime(String key, boolean cooltime) {
        this.cooltimes.put(key.toUpperCase(), cooltime);
    }


    public final void startCoolTime(String key, Duration delay) {
        setCooltime(key.toUpperCase(), true);
        Bukkit.getServer().getScheduler()
                .scheduleSyncDelayedTask(LucaAPI.getInstance(), () -> setCooltime(key.toUpperCase(), false));
    }

    public final boolean cooltime(@Nullable String waringMessage, String key, Duration delay) {
        if (isCooltime(key)) {
            if (waringMessage != null)
                sendText(ComponentText.waring(waringMessage));
            return true;
        }
        startCoolTime(key, delay);
        return false;
    }

    public final boolean cooltime(String key, Duration delay) {
        return cooltime(null, key, delay);
    }

    public final void startCoolTime(String key) {
        startCoolTime(key, Duration.ofSeconds(1));
    }

    protected final long firstJoin;
    protected long lastJoin = 0;
    protected long lastQuit = 0;

    public final UUID getUniqueId() {
        return uniqueId;
    }

    public final String getLocale() {
        return locale;
    }

    public final void setLocale(String locale) {
        this.locale = locale;
    }

    public final long getFirstJoin() {
        return firstJoin;
    }

    public final long getLastJoin() {
        return lastJoin;
    }

    public final void setLastJoin(long lastJoin) {
        this.lastJoin = lastJoin;
    }

    public final long getLastQuit() {
        return lastQuit;
    }

    public final void setLastQuit(long lastQuit) {
        this.lastQuit = lastQuit;
    }

    public final Location getLocation() {
        return getPlayer().getLocation();
    }

    public final boolean isOnline() {
        return Bukkit.getOfflinePlayer(getUniqueId()).isOnline();
    }


    public final boolean isPlayer() {
        if (uniqueId == null) return false;
        Player player = Bukkit.getPlayer(uniqueId);
        return player != null && player.isOnline();
    }

    public final CustomTitle titleTranslatable(CustomTitle customTitle) {
        return customTitle.translatable(this.translatable(customTitle.title()));
    }

    public final String translatable(String key) {
        return translatable(getMyLanguage(), key);
    }

    public final String translatable(String locale, String key) {
        StringBuilder sb = new StringBuilder();
        if (key == null)
            sb.append("N/A");
        else if (key.contains("explain"))
            sb.append(Colour.EXPLAIN);
        String tra = LanguageManager.getLanguage(locale, key);

        if (tra != null && !notTran)
            sb.append(Colour.format(tra));
        else
            sb.append(key);
        return sb.toString();
    }

    public final String translatable(String[] keys) {
        StringBuilder sb = new StringBuilder();
        for (String key : keys) {
            sb.append(translatable(key));
        }
        return sb.toString();
    }

    public final String getClientLanguage() {
        return getPlayer().getLocale();
    }

    public final String getMyLanguage() {
        if (locale == null) return getClientLanguage();
        return locale;
    }


    public void sendTexts(Component[][] components) {
        for (Component[] component : components) {
            sendText(component);
        }
    }

    public void sendText(Component... components) {
        if (components == null) return;
        if (components.length == 0) return;
        if (isPlayer()) {
            BaseComponent[] baseComponent = new BaseComponent[components.length];
            for (int i = 0; i < components.length; i++) {
                if (components[i] instanceof TranslatableComponent component) {
                    String tra = translatable(component.getKey());
                    try {
                        ComponentText com = ComponentText
                                .text(LanguageManager.formatted(tra, component.getArgs()));
                        if (component.hasHoverKey())
                            com.hoverText(LanguageManager.formatted(translatable(component.getHoverKey()), component.getArgs()));
                        if (component.hasClickEvent())
                            com.setClickEvent(component.getClickEvent());
                        baseComponent[i] = com.build();
                    } catch (Exception e) {
                        ComponentText com = ComponentText
                                .text(tra);
                        if (component.hasHoverKey())
                            com.hoverText(translatable(component.getHoverKey()));
                        if (component.hasClickEvent())
                            com.setClickEvent(component.getClickEvent());
                        baseComponent[i] = com.build();
                    }
                } else {
                    baseComponent[i] = components[i].build();
                }
            }
            getPlayer().spigot().sendMessage(baseComponent);
        }
    }

    public final void playSound(BukkitSound sound) {
        if (isPlayer())
            getPlayer().playSound(getLocation(), sound.sound(), sound.volume(), sound.pitch());
    }

    public final Player getPlayer() throws NullPointerException {
        if (isPlayer()) return Bukkit.getPlayer(uniqueId);
        throw new NullPointerException("플레이어가 null 값으로 플레이어 정보를 불러올 수 없습니다.");
    }

    public final void showTitle(Title title) {
        getPlayer().sendTitle(title.title(), title.subtitle(), title.fadeOut().ticks(),
                title.stay().ticks(), title.fadeOut().ticks());
    }

    public final String getName() {
        if (isPlayer())
            return getPlayer().getName();
        else return Bukkit.getOfflinePlayer(uniqueId).getName();
    }


    public void sendPacket(Packet<?> packet) {
        if (!isPlayer()) return;

        ((CraftPlayer) getPlayer()).getHandle().b.a(packet);

    }
}
