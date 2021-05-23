package xyz.msws.skywars.commands;

import org.bukkit.command.CommandSender;
import xyz.msws.skywars.GamePlugin;
import xyz.msws.skywars.utils.MSG;

public class StartCommand extends SubCommand {

    protected StartCommand(String name, GamePlugin plugin) {
        super(name, plugin);
    }

    @Override
    protected boolean exec(CommandSender sender, String label, String[] args) {
        plugin.getGame().getCountdown().start();
        MSG.tell(sender, "Started game countdown");
        return true;
    }
}
