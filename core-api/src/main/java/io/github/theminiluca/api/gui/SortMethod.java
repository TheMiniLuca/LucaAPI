package io.github.theminiluca.api.gui;

public enum SortMethod {
    LATEST("최신"),
    OLDEST("최고");

    public final String display;

    SortMethod(String display) {
        this.display = display;
    }
}
