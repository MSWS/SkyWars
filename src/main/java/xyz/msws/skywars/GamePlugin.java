package xyz.msws.skywars;

import org.bukkit.plugin.Plugin;
import xyz.msws.skywars.game.Game;

public interface GamePlugin extends Plugin {
    public Game getGame();
}
