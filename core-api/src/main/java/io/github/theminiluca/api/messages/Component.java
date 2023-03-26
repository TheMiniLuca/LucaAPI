package io.github.theminiluca.api.messages;

import net.md_5.bungee.api.chat.BaseComponent;

public interface Component {

    public BaseComponent build();
    public String key();
}
