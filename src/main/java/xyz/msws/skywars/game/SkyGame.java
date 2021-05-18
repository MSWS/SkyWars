package xyz.msws.skywars.game;

import org.bukkit.World;
import org.bukkit.entity.Player;

public class SkyGame extends Game {

    private World lobby, game;

    public SkyGame(World lobby, World game) {
        this.lobby = lobby;
        this.game = game;
    }

    @Override
    public void onLoad() {

    }

    @Override
    public void unLoad() {

    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public boolean addPlayer(Player player) {
        return false;
    }

    @Override
    public boolean removePlayer(Player player) {
        return false;
    }
}
