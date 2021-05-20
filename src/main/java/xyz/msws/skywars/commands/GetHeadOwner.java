package xyz.msws.skywars.commands;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import xyz.msws.skywars.GamePlugin;
import xyz.msws.skywars.utils.MSG;

public class GetHeadOwner extends SubCommand {
    public GetHeadOwner(String name, GamePlugin plugin) {
        super(name, plugin);
    }

    @Override
    protected boolean exec(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) {
            MSG.tell(sender, "You must be a player to run this command.");
            return true;
        }
        Player player = (Player) sender;
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType() != Material.PLAYER_HEAD)
            item = player.getInventory().getItemInOffHand();
        if (item.getType() != Material.PLAYER_HEAD) {
            MSG.tell(sender, "Please hold a head in your hand!");
            return true;
        }

        SkullMeta meta = (SkullMeta) item.getItemMeta();
        MSG.tell(sender, "The owner is %s (%s)", meta.getOwningPlayer().getUniqueId(), meta.getOwningPlayer().getName());
        return true;
    }
}
