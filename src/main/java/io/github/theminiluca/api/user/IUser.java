package io.github.theminiluca.api.user;

import io.github.theminiluca.api.inventory.CustomTitle;
import io.github.theminiluca.api.messages.Component;
import io.github.theminiluca.api.messages.ComponentText;
import io.github.theminiluca.api.messages.LanguageManager;
import io.github.theminiluca.api.messages.TranslatableComponent;
import io.github.theminiluca.api.utils.BukkitSound;
import io.github.theminiluca.api.utils.Colour;
import io.github.theminiluca.api.utils.Title;
import io.github.theminiluca.sql.SQL;
import io.github.theminiluca.sql.SQLManager;
import io.github.theminiluca.sql.SQLObject;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class IUser implements SQLObject {

    @SQL(primary = true)
    protected final UUID uniqueId;
    protected final String name;

    protected String locale = null;

    public boolean notTran = false;

    public IUser(UUID uniqueId, String name, long firstJoin) {
        this.uniqueId = uniqueId;
        this.name = name;
        this.firstJoin = firstJoin;
    }

    protected final long firstJoin;
    protected long lastJoin = 0;
    protected long lastQuit = 0;

    public UUID getUniqueId() {
        return uniqueId;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public long getFirstJoin() {
        return firstJoin;
    }

    public long getLastJoin() {
        return lastJoin;
    }

    public void setLastJoin(long lastJoin) {
        this.lastJoin = lastJoin;
    }

    public long getLastQuit() {
        return lastQuit;
    }

    public void setLastQuit(long lastQuit) {
        this.lastQuit = lastQuit;
    }

    public Location getLocation() {
        return getPlayer().getLocation();
    }

    public boolean isOnline() {
        return Bukkit.getOfflinePlayer(getUniqueId()).isOnline();
    }


    public boolean isPlayer() {
        if (uniqueId == null) return false;
        Player player = Bukkit.getPlayer(uniqueId);
        return player != null && player.isOnline();
    }

    public CustomTitle titleTranslatable(CustomTitle customTitle) {
        return customTitle.translatable(this.translatable(customTitle.title()));
    }

    public String translatable(String key) {
        return translatable(getMyLanguage(), key);
    }

    public String translatable(String locale, String key) {
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

    public String translatable(String[] keys) {
        StringBuilder sb = new StringBuilder();
        for (String key : keys) {
            sb.append(translatable(key));
        }
        return sb.toString();
    }

    public String getClientLanguage() {
        return getPlayer().getLocale();
    }

    public String getMyLanguage() {
        if (locale == null) return getClientLanguage();
        return locale;
    }



    public void sendText(Component... components) {
        CraftPlayer craftPlayer = ((CraftPlayer) getPlayer());
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

    public void playSound(BukkitSound sound) {
        if (isPlayer())
            getPlayer().playSound(getLocation(), sound.sound(), sound.volume(), sound.pitch());
    }

    public Player getPlayer() throws NullPointerException {
        if (isPlayer()) return Bukkit.getPlayer(uniqueId);
        throw new NullPointerException("플레이어가 null 값으로 플레이어 정보를 불러올 수 없습니다.");
    }

    public void showTitle(Title title) {
        getPlayer().sendTitle(title.title(), title.subtitle(), title.fadeOut().ticks(),
                title.stay().ticks(), title.fadeOut().ticks());
    }

    public String getName() {
        if (isPlayer())
            return getPlayer().getName();
        else return Bukkit.getOfflinePlayer(uniqueId).getName();
    }

    @Override
    public void saveSQL() {
        SQLManager.saveToJson(this, SQLManager.getDriver(this));
    }
}
