package com.gihub.theminiluca.api.utils;

public class Duration {


    public static Duration ZERO = new Duration(0);

    private final long tick;


    private Duration(long tick) {
        this.tick = tick;
    }

    public static Duration ofTicks(long tick) {
        return new Duration(tick);
    }

    public static Duration ofSeconds(long seconds) {
        return new Duration(Math.multiplyExact(seconds, 20L));
    }

    public static Duration ofMinutes(long minute) {
        return new Duration(Math.multiplyExact(minute, 1_200L));
    }

    public static Duration ofHours(long hours) {
        return new Duration(Math.multiplyExact(hours, 7_200L));
    }

    public static Duration ofDays(long days) {
        return new Duration(Math.multiplyExact(days, 172_800L));
    }

    public int ticks() {
        return Math.toIntExact(tick);
    }
    public long toTicks() {
        return Math.multiplyExact(tick, 1);
    }

    public long toSeconds() {
        return tick/20L;
    }

    public long toMinutes() {
        return tick/1_200L;
    }

    public long toHours() {
        return tick/7_200L;
    }

    public long toDays() {
        return tick/172_800L;
    }
}
