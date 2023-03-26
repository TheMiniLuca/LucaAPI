package io.github.theminiluca.api.messages;

import io.github.theminiluca.api.LucaAPI;
import io.github.theminiluca.api.utils.Colour;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class ComponentText implements Component {
    public String text;
    private ClickEvent clickEvent;


    private HoverEvent hoverEvent;

    public ClickEvent getClickEvent() {
        return clickEvent;
    }

    public void setClickEvent(ClickEvent clickEvent) {
        this.clickEvent = clickEvent;
    }

    public HoverEvent getHoverEvent() {
        return hoverEvent;
    }

    public void setHoverEvent(HoverEvent hoverEvent) {
        this.hoverEvent = hoverEvent;
    }

    private ComponentText(String text) {
        this.text = text;
    }


    public ComponentText execute(String command) {
        setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + command));
        return this;
    }

    public ComponentText clickEvent(ClickEvent.Action action, String text) {
        setClickEvent(new ClickEvent(action, "/" + text));
        return this;
    }


    public ComponentText suggest(String command) {
        setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command));
        return this;
    }

    public ComponentText clipboard(String command) {
        setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, command));
        return this;
    }

    public ComponentText openURL(String command) {
        setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, command));
        return this;
    }

    public ComponentText hoverText(String... args) {
        Text[] texts = new Text[args.length];
        int i = 0;
        for (String value : args) {
            texts[i] = new Text(value + (args.length == i + 1 ? "" : "\n"));
            i++;
        }
        setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, texts));
        return this;
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
        BaseComponent[] extra = TextComponent.fromLegacyText(text);
        TextComponent component = new TextComponent("");
        component.setExtra(Arrays.asList(extra));
        if (hoverEvent != null)
            component.setHoverEvent(hoverEvent);
        if (clickEvent != null)
            component.setClickEvent(clickEvent);
        return component;
    }


}
