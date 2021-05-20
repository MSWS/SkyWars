package xyz.msws.skywars.commands;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.msws.skywars.GamePlugin;
import xyz.msws.skywars.utils.MSG;

public class WorldCommand extends SubCommand {

    protected WorldCommand(String name, GamePlugin plugin) {
        super(name, plugin);
    }

    @Override
    protected boolean exec(CommandSender sender, String label, String[] args) {
        if (args.length == 0) {
            MSG.tell(sender, "Please specify a world.");
            return true;
        }

        Player player;
        World world;

        MSG.log("Args: %s, len: %d", String.join(", ", args), args.length);

        if (args.length == 2) {
            player = Bukkit.getPlayer(args[0]);
            world = Bukkit.getWorld(args[1]);
        } else if (sender instanceof Player) {
            player = (Player) sender;
            world = Bukkit.getWorld(args[0]);
        } else {
            MSG.tell(sender, "You must specify a player.");
            return true;
        }

        if (player == null) {
            MSG.tell(sender, "Invalid player.");
            return true;
        }

        if (world == null) {
            MSG.tell(sender, "Invalid world.");
            return true;
        }

        player.teleport(world.getSpawnLocation());
        MSG.tell(sender, "Teleported.");
        return true;
    }
}
