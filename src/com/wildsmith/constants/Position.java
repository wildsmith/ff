package com.wildsmith.constants;

public enum Position {
    QUARTER_BACK("Quarterbacks"),
    RUNNING_BACK("Running Backs"),
    WIDE_RECEIVER("Wide Receivers"),
    TIGHT_END("Tight Ends"),
    KICKER("Kickers"),
    DEFENSIVE_SPECIAL_TEAM("Defensive Special Teams");

    Position(String name) {
        this.name = name;
    }

    private String name;

    public String getName() {
        return name;
    }

    public static Position fromString(String name) {
        for (Position position : Position.values()) {
            if (position.name.equalsIgnoreCase(name)) {
                return position;
            }
        }

        return null;
    }
}