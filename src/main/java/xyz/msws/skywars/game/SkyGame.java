package xyz.msws.skywars.game;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.msws.skywars.GamePlugin;
import xyz.msws.skywars.data.GameMap;
import xyz.msws.skywars.data.GamePoint;
import xyz.msws.skywars.data.MapData;

import java.util.List;

public class SkyGame extends Game {

    private World lobby, game;

    public SkyGame(GamePlugin plugin, World lobby, World game) {
        super(plugin);
        this.lobby = lobby;
        this.game = game;
    }

    public static final String SPAWN_SKIN = "http://textures.minecraft.net/texture/8b8a99cbe98f09c87417abd4ec12f779a52d83314fc474be507d420573366ba7";

    @Override
    public void onLoad() {
        GameMap gameMap = new GameMap(plugin, game);
        MapData data = gameMap.getData();

        data.addTarget(d -> d.getType() == Material.GREEN_WOOL ? GamePoint.Type.SPAWN : GamePoint.Type.NONE);
        gameMap.getData().load(null);
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

    @Override
    public List<ItemStack> getBuildItems() {


        return null;
    }
}
