package io.github.theminiluca.api.gui;

public enum BaseSort {
    LATEST("latest.name"),
    OLDEST("oldest.name");

    public final String display;

    BaseSort(String display) {
        this.display = display;
    }

    public String propertiesKey() {
        return display;
    }
}
