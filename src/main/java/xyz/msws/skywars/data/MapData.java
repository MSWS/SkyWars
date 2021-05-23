package xyz.msws.skywars.data;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import xyz.msws.skywars.GamePlugin;
import xyz.msws.skywars.utils.Callback;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class MapData implements ConfigurationSerializable {
    protected GamePlugin plugin;
    protected final GameMap gameMap;
    protected Map<GamePoint.Type, List<GamePoint>> points;
    protected final List<BlockTarget> targets = new ArrayList<>();

    public MapData(GamePlugin plugin, GameMap gameMap) {
        this.plugin = plugin;
        this.gameMap = gameMap;
    }

    public void load(Callback<MapData> call) {
        load(call, false);
    }

    public abstract void load(Callback<MapData> data, boolean refresh);

    public abstract void saveData();

    public void addTarget(BlockTarget data) {
        targets.add(data);
    }

    public List<GamePoint> getGamePoints(GamePoint.Type type) {
        return points.getOrDefault(type, new ArrayList<>());
    }

    public Map<GamePoint.Type, List<GamePoint>> getGamePoints() {
        return points;
    }

    @Override
    public Map<String, Object> serialize() {
        return null;
    }
}
