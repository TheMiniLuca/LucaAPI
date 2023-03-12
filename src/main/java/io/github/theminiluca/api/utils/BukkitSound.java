package io.github.theminiluca.utils;

import org.bukkit.Sound;

public record BukkitSound(org.bukkit.Sound sound, float volume, float pitch){

    public static final BukkitSound SUCCESS_CLICK_GUI = new BukkitSound(Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 2.0F);
}


