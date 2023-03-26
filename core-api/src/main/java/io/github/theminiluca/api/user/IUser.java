package io.github.theminiluca.api.user;

import io.github.theminiluca.api.inventory.CustomTitle;
import io.github.theminiluca.api.messages.Component;
import io.github.theminiluca.api.utils.BukkitSound;
import io.github.theminiluca.api.utils.Duration;
import io.github.theminiluca.api.utils.Title;
import io.github.theminiluca.sql.SQLObject;

import net.minecraft.network.protocol.Packet;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public interface IUser extends SQLObject {

    UUID getUniqueId();

    String getLocale();

    void setLocale(String locale);

    long getFirstJoin();

    long getLastJoin();

    void setLastJoin(long lastJoin);

    long getLastQuit();

    void setLastQuit(long lastQuit);

    Location getLocation();

    boolean isOnline();

    boolean isPlayer();

    void sendTexts(Component[][] components);

    void sendText(Component... components);

    void playSound(BukkitSound sound);

    Player getPlayer();

    void showTitle(Title title);

    String getName();

    void sendPacket(Packet<?> packet);

    default boolean notTran() {
        return false;
    }

    boolean isCooltime(String key);

    void setCooltime(String key, boolean cooltime);

    void startCoolTime(String key, Duration delay);

    boolean cooltime(String key, Duration delay);

    boolean cooltime(String waringMessage, String key, Duration delay);

    void startCoolTime(String key);

    CustomTitle titleTranslatable(CustomTitle customTitle);

    String translatable(String key);

    String translatable(String locale, String key);

    String translatable(String[] keys);

    String getClientLanguage();

    String getMyLanguage();


}
