package io.github.theminiluca.api.utils;

import io.github.theminiluca.api.messages.ComponentText;
import org.bukkit.inventory.ItemStack;

public interface NMS {

    ComponentText hoverItem(ComponentText text, ItemStack itemStack);
}
