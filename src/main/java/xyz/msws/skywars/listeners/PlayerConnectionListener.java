package xyz.msws.skywars.listeners;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import xyz.msws.skywars.GamePlugin;
import xyz.msws.skywars.SkyWars;

public class PlayerConnectionListener implements Listener {
    private SkyWars sw;

    public PlayerConnectionListener(GamePlugin plugin) {
        if (!(plugin instanceof SkyWars))
            return;
        Bukkit.getPluginManager().registerEvents(this, plugin);
        this.sw = (SkyWars) plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        World world = player.getWorld();
        if (world.equals(sw.getGameWorld()))
            player.teleport(sw.getLobbyWorld().getSpawnLocation());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        World world = player.getWorld();
        if (!world.equals(sw.getGameWorld()))
            return;
        switch (sw.getGame().getStatus()) {
            case LOBBY:
                break;
            case INGAME:
                break;
            case FINISHED:
                break;
            case COUNTDOWN:
                break;
        }
        sw.getGame().removePlayer(player);
    }
}
