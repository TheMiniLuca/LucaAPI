package io.github.theminiluca.api.messages;

import net.md_5.bungee.api.chat.ClickEvent;

import java.io.Serializable;

public class ClickText implements Serializable {

    private final Action action;
    private final String value;

    public ClickText(Action action, String value) {
        this.action = action;
        this.value = value;
    }

    public Action getAction() {
        return action;
    }

    public String getValue() {
        return value;
    }

    public ClickEvent build() {
        return new ClickEvent(ClickEvent.Action.valueOf(action.name()), value);
    }

    public enum Action implements Serializable {
        OPEN_URL,
        RUN_COMMAND,
        SUGGEST_COMMAND,
        CHANGE_PAGE,
        COPY_TO_CLIPBOARD;

        private Action() {
        }
    }
}