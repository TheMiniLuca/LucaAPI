package io.github.theminiluca.api.messages;

import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Content;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.checkerframework.checker.units.qual.A;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class HoverText implements Serializable {

    private final String[] text;

    public HoverText(String[] text) {
        this.text = text;
    }

    public HoverText(Text[] text) {
        this.text = (String[]) Arrays.stream(text)
                .map(Text::getValue).filter(value -> value instanceof String).toArray();
    }

    public HoverEvent build() {
        return new HoverEvent(HoverEvent.Action.SHOW_TEXT, ComponentText.stringToText(text));

    }

    public String[] getText() {
        return text;
    }
}
