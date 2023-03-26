package com.gihub.theminiluca.api.messages;

import com.gihub.theminiluca.api.user.IUser;
import com.gihub.theminiluca.api.utils.Colour;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;

public class TranslatableComponent implements Component {

    private String key;

    private String hoverKey;
    private Object[] args;
    public String colour = Colour.WHITE;
    private Object[] hoverArgs;
    private ClickEvent clickEvent;

    public ClickEvent getClickEvent() {
        return clickEvent;
    }

    private TranslatableComponent(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }


    public static TranslatableComponent key(String key) {
        return new TranslatableComponent(key);
    }
    @Override
    public BaseComponent build() {
        //        if (hoverEvent != null)
//            component.setHoverEvent(hoverEvent);
//        if (clickEvent != null)
//            component.setClickEvent(clickEvent);
        return new net.md_5.bungee.api.chat.TextComponent(key);
    }

    private void setClickEvent(ClickEvent clickEvent) {
        this.clickEvent = clickEvent;
    }

    public TranslatableComponent execute(String command) {
        setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + command));
        return this;
    }

    public TranslatableComponent colour(String colour) {
        this.colour = colour;
        return this;
    }

    public TranslatableComponent clickEvent(ClickEvent.Action action, String text) {
        setClickEvent(new ClickEvent(action, "/" + text));
        return this;
    }

    public TranslatableComponent suggest(String command) {
        setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command));
        return this;
    }

    public TranslatableComponent clipboard(String command) {
        setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, command));
        return this;
    }

    public TranslatableComponent openURL(String command) {
        setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, command));
        return this;
    }

    public Object[] getArgs() {
        return args;
    }

    public String getHoverKey() {
        return hoverKey;
    }

    public boolean hasHoverKey() {
        return hoverKey != null;
    }
    public boolean hasClickEvent() {
        return clickEvent != null;
    }
    public TranslatableComponent hoverKey(String key) {
        this.hoverKey = key;
        return this;
    }


    public Object[] getHoverArgs() {
        return hoverArgs;
    }
    public TranslatableComponent hoverFormat(Object... args) {
        this.hoverArgs = args;
        return this;
    }

    public TranslatableComponent formatted(Object... args) {
        this.args = args;
        return this;
    }

    public String build(IUser user) {
        return LanguageManager.formatted(user.translatable(key()), getArgs());
    }

    @Override
    public String key() {
        return key;
    }
}
