package xyz.msws.skywars.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Skull;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.msws.skywars.GamePlugin;
import xyz.msws.skywars.data.GameMap;
import xyz.msws.skywars.data.GamePoint;
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
        GameMap gameMap = new GameMap(plugin, world);

        gameMap.getData().addTarget(state -> {
            if (state.getType() != Material.PLAYER_HEAD)
                return GamePoint.Type.NONE;
            Skull skull = (Skull) state;
            MSG.log("Skull casted");
            return GamePoint.Type.NONE;
        });

        gameMap.getData().load(result -> MSG.tell(sender, "Successfully loaded!"), true);
        return true;
    }
}
