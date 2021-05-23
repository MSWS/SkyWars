package xyz.msws.skywars.game;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.msws.skywars.GamePlugin;
import xyz.msws.skywars.data.BlockTarget;
import xyz.msws.skywars.data.GameMap;
import xyz.msws.skywars.data.GamePlayer;
import xyz.msws.skywars.data.GamePoint;
import xyz.msws.skywars.utils.Callback;
import xyz.msws.skywars.utils.CustomSkull;
import xyz.msws.skywars.utils.MSG;

import java.util.ArrayList;
import java.util.List;

public class SkyGame extends Game {

    private World lobby;
    private GameMap game;

    public SkyGame(GamePlugin plugin, World lobby, World game) {
        super(plugin);
        this.lobby = lobby;
        this.game = new GameMap(plugin, game);
    }

    public static final String SPAWN_SKIN = "3ed9d96c393e38a36b61aa3c859ede5eb744ef1e846d4f7d0ecbd6588a021";

    @Override
    public void load(Callback<Game> call) {
        for (BlockTarget bt : getTargets())
            game.getData().addTarget(bt);
        game.getData().load(result -> call.execute(SkyGame.this), true);
    }

    @Override
    public void unload() {

    }

    @Override
    public void start() {
        this.status = GameStatus.INGAME;

        List<GamePoint> spawns = game.getData().getGamePoints(GamePoint.Type.SPAWN);
        if (spawns.isEmpty()) {
            MSG.tell(this, "&4&l[ERROR] No spawns were loaded, stopping the game.");
            stop();
        }
        int index = 0;
        for (GamePlayer player : players) {
            Player p = player.getPlayer();
            if (p == null)
                continue;
            GamePoint spawn = spawns.get(index % spawns.size());
            p.teleport(spawn.getLocation());
            index++;
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

    @Override
    public List<ItemStack> getBuildItems() {


        return null;
    }

    @Override
    public List<BlockTarget> getTargets() {
        List<BlockTarget> targets = new ArrayList<>();
        targets.add(state -> {
            if (state.getType() != Material.PLAYER_HEAD)
                return GamePoint.Type.NONE;
            CustomSkull skull = CustomSkull.fromBlock(state);
            MSG.log("Skull string: %s", skull.getURLId());
            switch (skull.getURLId()) {
                case SPAWN_SKIN:
                    return GamePoint.Type.SPAWN;
                default:
                    return GamePoint.Type.NONE;
            }
        });
        return targets;
    }
}
