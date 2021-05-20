package xyz.msws.skywars.commands;

import org.bukkit.command.defaults.BukkitCommand;
import xyz.msws.skywars.GamePlugin;

public abstract class GameCommand extends BukkitCommand {

    protected GamePlugin plugin;

    protected GameCommand(String name, GamePlugin plugin) {
        super(name);
        this.plugin = plugin;
    }
}
