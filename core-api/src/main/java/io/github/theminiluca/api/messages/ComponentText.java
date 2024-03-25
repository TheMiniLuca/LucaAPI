package io.github.theminiluca.api.messages;

import io.github.theminiluca.api.LucaAPI;
import io.github.theminiluca.api.utils.Colour;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.hover.content.Content;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.inventory.ItemStack;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class ComponentText implements Component, Serializable {
    public String text;
    private ClickText clickEvent;

    private transient HoverEvent hoverEvent;
    private HoverText hoverText;




    public ClickText getClickEvent() {
        return clickEvent;
    }

    public void setClickEvent(ClickText clickEvent) {
        this.clickEvent = clickEvent;
    }

    public void setHover(HoverEvent hoverEvent) {
        this.hoverEvent = hoverEvent;
        if (this.hoverEvent == null) {
            this.hoverText = null;
            return;
        }
        if (this.hoverEvent.getAction().equals(HoverEvent.Action.SHOW_TEXT)) {
            this.hoverText = new HoverText((Text[]) hoverEvent.getContents().stream().map(map -> (Text) map).toArray());
        }
    }


    public void setHoverEvent(HoverEvent hoverEvent) {
        this.hoverEvent = hoverEvent;
    }

    private ComponentText(String text) {
        this.text = text;
    }


    public ComponentText execute(String command) {
        setClickEvent(new ClickText(ClickText.Action.RUN_COMMAND, "/" + command));
        return this;
    }

    public ComponentText clickEvent(ClickText.Action action, String text) {
        setClickEvent(new ClickText(action, "/" + text));
        return this;
    }


    public ComponentText suggest(String command) {
        setClickEvent(new ClickText(ClickText.Action.SUGGEST_COMMAND, command));
        return this;
    }

    public ComponentText clipboard(String command) {
        setClickEvent(new ClickText(ClickText.Action.COPY_TO_CLIPBOARD, command));
        return this;
    }

    public ComponentText openURL(String command) {
        setClickEvent(new ClickText(ClickText.Action.OPEN_URL, command));
        return this;
    }

    public ComponentText hoverText(String... args) {
        setHover(new HoverEvent(HoverEvent.Action.SHOW_TEXT, stringToText(args)));
        return this;
    }

    public static Text[] stringToText(String[] args) {
        Text[] texts = new Text[args.length];
        int i = 0;
        for (String value : args) {
            texts[i] = new Text(value + (args.length == i + 1 ? "" : "\n"));
            i++;
        }
        return texts;
    }


    public ComponentText hoverItem(ItemStack stack) {
        return LucaAPI.nmsHandler.hoverItem(this, stack);
    }

    public static ComponentText text(String text) {
        return new ComponentText(text);
    }

    public ComponentText formatted(String s) {
        text = s.replace(REPLACE_SYMBOL, text);
        return this;
    }

    public static String REPLACE_SYMBOL = "{original}";

    public static ComponentText waring(String waring) {
        return new ComponentText(Colour.RED + waring);
    }

    public static ComponentText success(String success) {
        return new ComponentText(Colour.GREEN + success);
    }

    public static ComponentText explain(String explain) {
        return new ComponentText(Colour.EXPLAIN + explain);
    }

    @Override
    public String key() {
        return text;
    }

    public BaseComponent build() {
        TextComponent component = new TextComponent();
        component.setText(text);
        if (hoverEvent != null)
            component.setHoverEvent(hoverEvent);
        else if (hoverText != null) {
            component.setHoverEvent(hoverText.build());
        }
        if (clickEvent != null)
            component.setClickEvent(clickEvent.build());
        return component;
    }

    @Override
    public String toString() {
        return "ComponentText{" +
                "text='" + text + '\'' +
                ", clickEvent=" + clickEvent +
                ", hoverEvent=" + hoverEvent +
                '}';
    }
}
