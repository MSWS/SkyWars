package xyz.msws.skywars.game;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import xyz.msws.skywars.GamePlugin;
import xyz.msws.skywars.data.BlockTarget;
import xyz.msws.skywars.data.GamePlayer;
import xyz.msws.skywars.utils.Callback;

import java.util.List;

public abstract class Game implements Listener {
    protected List<GamePlayer> players;
    protected GameStatus status;
    protected Countdown cd;
    protected GamePlugin plugin;

    public Game(GamePlugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public Countdown getCountdown() {
        return cd;
    }

    public GameStatus getStatus() {
        return status;
    }

    public abstract void load(Callback<Game> call);

    public abstract void unload();

    public abstract void start();

    public abstract void stop();

    public abstract boolean addPlayer(Player player);

    public abstract boolean removePlayer(Player player);

    public GamePlayer getPlayer(Player player) {
        for (GamePlayer p : players)
            if (p.getUUID().equals(player.getUniqueId()))
                return p;
        return null;
    }

    public abstract List<ItemStack> getBuildItems();

    public abstract List<BlockTarget> getTargets();

}
