package io.github.theminiluca.api.inventory;

import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class TitleRunnable {

    public void interact(InventoryInteractEvent event) {

    }


    public abstract void view(String uniqueId, String... args);


}
