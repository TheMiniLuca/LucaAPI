package io.github.theminiluca.api.inventory;

import org.bukkit.scheduler.BukkitRunnable;

public abstract class InputRunnable implements RunnableInput {

    public abstract void start(String uniqueId, String... args);


    public void running(String uniqueId, String... args) {
        new BukkitRunnable() {
            @Override
            public void run() {
                start(uniqueId, args);
            }
        }.run();
    }

}
