package xyz.msws.skywars.data;

import org.bukkit.ChatColor;

public class Team {
    private final ChatColor color;
    private final String name;

    public Team(ChatColor color, String name) {
        this.color = color;
        this.name = name;
    }

    public ChatColor getColor() {
        return color;
    }

    public String getName() {
        return name;
    }
}
