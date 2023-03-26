package com.gihub.theminiluca.api.utils;

import com.gihub.theminiluca.api.messages.ComponentText;
import org.bukkit.inventory.ItemStack;

public interface NMS {

    ComponentText hoverItem(ComponentText text, ItemStack itemStack);
}
