package xyz.msws.skywars.data;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import xyz.msws.skywars.GamePlugin;
import xyz.msws.skywars.utils.Callback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class MapData implements ConfigurationSerializable {
    protected GamePlugin plugin;
    protected final xyz.msws.skywars.data.Map map;
    protected Map<GamePoint.Type, List<GamePoint>> points;
    protected final Map<BlockQuery, GamePoint.Type> targets = new HashMap<>();

    public MapData(GamePlugin plugin, xyz.msws.skywars.data.Map map) {
        this.plugin = plugin;
        this.map = map;
    }

    public void load(Callback<MapData> call) {
        load(call, false);
    }

    public abstract void load(Callback<MapData> data, boolean refresh);

    public abstract void saveData();

    public void addTarget(BlockQuery data, GamePoint.Type point) {
        targets.put(data, point);
    }

    @Override
    public Map<String, Object> serialize() {
        return null;
    }
}
