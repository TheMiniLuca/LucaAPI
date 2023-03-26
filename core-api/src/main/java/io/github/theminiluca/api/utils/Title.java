package io.github.theminiluca.api.utils;

public record Title(String title, String subtitle,
                    Duration fadeIn,
                    Duration stay,
                    Duration fadeOut) {

    public static Title title(String title, String subtitle, Duration fadeIn, Duration stay, Duration fadeOut) {
        return new Title(title, subtitle, fadeIn, stay, fadeOut);
    }

    public static Title titleNoFade(String title, String subtitle, Duration stay) {
        return Title.title(title, subtitle, Duration.ZERO, stay, Duration.ZERO);
    }

    public static Title title(String title, String subtitle) {
        return new Title(title, subtitle, Duration.ofTicks(10), Duration.ofTicks(70), Duration.ofTicks(20));
    }
}
