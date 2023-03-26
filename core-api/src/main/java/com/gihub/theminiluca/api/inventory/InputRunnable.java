package com.gihub.theminiluca.api.inventory;

import org.bukkit.scheduler.BukkitRunnable;

public abstract class InputRunnable implements RunnableInput {

    public abstract void start(String uniqueId);


    public void running(String uniqueId) {
        new BukkitRunnable() {
            @Override
            public void run() {
                start(uniqueId);
            }
        }.run();
    }

}
