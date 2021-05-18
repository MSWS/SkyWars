package xyz.msws.skywars;

import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.msws.skywars.game.Game;

public class SkyWars extends JavaPlugin {
    @Override
    public void onEnable() {

    }

    public World getGameWorld() {
        return null;
    }

    public World getLobbyWorld() {
        return null;
    }

    public Game getGame() {
        return null;
    }
}