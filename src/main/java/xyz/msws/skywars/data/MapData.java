package xyz.msws.skywars.data;

import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import xyz.msws.skywars.GamePlugin;

import java.io.File;
import java.util.List;
import java.util.Map;

public class MapData implements ConfigurationSerializable {
    private GamePlugin plugin;
    private xyz.msws.skywars.data.Map map;
    private Map<GamePoint.Type, List<GamePoint>> points;
    private File data;

    public MapData(GamePlugin plugin, xyz.msws.skywars.data.Map map) {
        this.map = map;
        data = new File(plugin.getDataFolder(), map.getWorld().getName() + "._data.yml");
    }

    public void load() {
        load(false);
    }

    public void load(boolean refresh) {
        if (points != null)
            return;
        if (!refresh && data.exists()) {
            parseFile();
            return;
        }
        parseWorld();
        saveData();
    }

    private void parseWorld() {
        World world = map.getWorld();

    }

    private void parseFile() {
        
    }

    private void saveData() {

    }

    @Override
    public Map<String, Object> serialize() {
        return null;
    }
}
