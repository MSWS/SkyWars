package xyz.msws.skywars;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.msws.skywars.commands.SkyBaseCommand;
import xyz.msws.skywars.game.Game;
import xyz.msws.skywars.game.SkyGame;

public class SkyWars extends JavaPlugin implements GamePlugin {

    private World lobby, gameWorld;

    private Game game;

    @Override
    public void onEnable() {
        lobby = Bukkit.getWorld("world");
        gameWorld = Bukkit.createWorld(new WorldCreator("game").type(WorldType.FLAT).generateStructures(false));
        game = new SkyGame(this, lobby, gameWorld);


        getCommand("skywars").setExecutor(new SkyBaseCommand("skywars", this));
    }

    public World getGameWorld() {
        return gameWorld;
    }

    public World getLobbyWorld() {
        return lobby;
    }

    public Game getGame() {
        return game;
    }
}