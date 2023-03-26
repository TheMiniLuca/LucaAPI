package com.gihub.theminiluca.api.messages;

import net.md_5.bungee.api.chat.BaseComponent;

public interface Component {

    public BaseComponent build();
    public String key();
}
