package xyz.msws.skywars.commands;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.msws.skywars.GamePlugin;
import xyz.msws.skywars.data.Map;
import xyz.msws.skywars.utils.MSG;

public class ParseCommand extends SubCommand {
    protected ParseCommand(String name, GamePlugin plugin) {
        super(name, plugin);
    }

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

        MSG.tell(sender, "Loading world...");
        Map map = new Map(plugin, world);
        map.getData().load(true);
        MSG.tell(sender, "Successfully loaded!");
        return true;
    }
}
