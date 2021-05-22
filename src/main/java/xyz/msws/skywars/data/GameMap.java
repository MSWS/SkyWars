package xyz.msws.skywars.data;

import org.bukkit.World;
import xyz.msws.skywars.GamePlugin;

public class GameMap {
    private World world;
    private MapData data;

    public GameMap(GamePlugin plugin, World world) {
        this.world = world;
        this.data = new MapFileData(plugin, this);
    }

    public World getWorld() {
        return world;
    }

    public MapData getData() {
        return data;
    }

}
