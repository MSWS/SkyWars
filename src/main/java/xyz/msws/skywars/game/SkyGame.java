package xyz.msws.skywars.game;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import xyz.msws.skywars.GamePlugin;
import xyz.msws.skywars.data.GamePlayer;
import xyz.msws.skywars.data.GamePoint;
import xyz.msws.skywars.data.Map;
import xyz.msws.skywars.data.MapData;

public class SkyGame extends Game {

    private World lobby, game;

    public SkyGame(GamePlugin plugin, World lobby, World game) {
        super(plugin);
        this.lobby = lobby;
        this.game = game;
    }

    @Override
    public void onLoad() {
        Map gameMap = new Map(plugin, game);
        MapData data = gameMap.getData();

        data.addTarget(d -> d.getMaterial() == Material.GREEN_WOOL, GamePoint.Type.SPAWN);

        gameMap.getData().load(null);
    }

    @Override
    public void unLoad() {

    }

    @Override
    public void start() {
        for (GamePlayer players : players) {

        }

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
