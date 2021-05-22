package xyz.msws.skywars.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.msws.skywars.GamePlugin;
import xyz.msws.skywars.utils.CustomSkull;
import xyz.msws.skywars.utils.MSG;

public class GetHeadCommand extends SubCommand {

    public GetHeadCommand(String name, GamePlugin plugin) {
        super(name, plugin);
    }

    @Override
    protected boolean exec(CommandSender sender, String label, String[] args) {
        String url = null;
        Player player = null;
        MSG.tell(sender, String.join(", ", args));
        if (args.length == 2) {
            player = Bukkit.getPlayer(args[0]);
            url = args[1];
        } else if (args.length == 1) {
            url = args[0];
            if (sender instanceof Player) {
                player = (Player) sender;
            } else {
                MSG.tell(sender, "You must specify a player.");
                return true;
            }
        }
        if (player == null) {
            MSG.tell(sender, "Invalid player.");
            return true;
        }
        if (url == null) {
            MSG.tell(sender, "Invalid URL");
            return true;
        }

        CustomSkull skull = CustomSkull.fromId(url);
        player.getInventory().setItemInMainHand(skull.getItemStack());
        MSG.tell(sender, "Gave you a custom skull");
        return true;
    }
}
