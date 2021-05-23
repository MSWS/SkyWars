package xyz.msws.skywars.commands;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.msws.skywars.SkyWars;
import xyz.msws.skywars.game.SkyGame;
import xyz.msws.skywars.utils.MSG;

import java.lang.reflect.Field;

public class ParseCommand extends SubCommand {
    private SkyWars plugin;

    public ParseCommand(String name, SkyWars plugin) {
        super(name, plugin);
        this.plugin = plugin;
    }

    private static Field profileField;

    @Override
    protected boolean exec(CommandSender sender, String label, String[] args) {
        World world = null;
        if (sender instanceof Player)
            world = ((Player) sender).getWorld();
        if (args.length >= 1)
            world = Bukkit.getWorld(String.join(" ", args));

        if (world == null) {
            MSG.tell(sender, "Unknown world");
            return true;
        }

        SkyGame game = plugin.getGame();
        MSG.tell(sender, "Loading world...");
        game.load(result -> MSG.tell(sender, "Loaded"));
        return true;
    }
}
