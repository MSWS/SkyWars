package xyz.msws.skywars.data;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.metadata.Metadatable;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;
import xyz.msws.skywars.GamePlugin;
import xyz.msws.skywars.utils.Callback;
import xyz.msws.skywars.utils.MSG;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class MapFileData extends MapData {
    private File data;
    private FileConfiguration config;
    private Map<BlockVector, BlockState> states = new HashMap<>();

    public MapFileData(GamePlugin plugin, GameMap gameMap) {
        super(plugin, gameMap);
        data = new File(plugin.getDataFolder(), gameMap.getWorld().getName() + "_data.yml");
    }

    @Override
    public void load(Callback<MapData> call, boolean refresh) {
        if (points != null)
            return;
        if (!refresh && data.exists()) {
            parseFile(call);
            return;
        }
        parseWorld(call);
        saveData();
    }

    @Override
    public void saveData() {
        config = new YamlConfiguration();
        if (points == null) {
            save();
            return;
        }

        for (Map.Entry<GamePoint.Type, List<GamePoint>> ps : points.entrySet()) {
            List<String> locations = new ArrayList<>();
            for (GamePoint gp : ps.getValue()) {
                BlockVector bv = new BlockVector(gp.getLocation().toVector());
                locations.add(String.format("%d,%d,%d", bv.getBlockX(), bv.getBlockY(), bv.getBlockZ()));
            }
            config.set(ps.getKey().toString(), locations);
        }
    }

    private void save() {
        try {
            config.save(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseFile(Callback<MapData> call) {
        points = new HashMap<>();
        Map<GamePoint.Type, List<BlockVector>> blocks = new HashMap<>();
        if (!data.exists()) {
            call.execute(this);
            return;
        }
        config = YamlConfiguration.loadConfiguration(data);
        for (Map.Entry<String, Object> entry : config.getValues(false).entrySet()) {
            GamePoint.Type type;
            try {
                type = GamePoint.Type.valueOf(entry.getKey());
            } catch (IllegalArgumentException e) {
                MSG.log("Invalid GamePoint Type: %s", entry.getKey());
                continue;
            }

            if (!(entry.getValue() instanceof Collection<?>)) {
                MSG.log("GamePoint data for %s is not a Collection/List", entry.getKey());
                continue;
            }

            Collection<?> pointCollection = (Collection<?>) entry.getValue();
            List<String> toParse = new ArrayList<>();
            for (Object point : pointCollection) {
                if (!(point instanceof String)) {
                    MSG.log("GamePoint data for %s contains invalid type (%s)", entry.getKey(), entry.getValue().getClass());
                    continue;
                }
                toParse.add((String) point);
            }

            List<BlockVector> vectors = new ArrayList<>();

            for (String point : toParse) {
                String[] digits = point.split(",");
                if (digits.length != 3) {
                    MSG.log("Malformed GamePoint data for %s (%s)", entry.getKey(), point);
                    continue;
                }
                int x, y, z;
                try {
                    x = Integer.parseInt(digits[0]);
                    y = Integer.parseInt(digits[1]);
                    z = Integer.parseInt(digits[2]);
                    BlockVector bv = new BlockVector(x, y, z);
                    vectors.add(bv);
                } catch (NumberFormatException e) {
                    MSG.log("Malformed GamePoint data for %s (%s)", entry.getKey(), point);
                }
            }
            blocks.put(type, vectors);
        }
        generateGamepoints(blocks, call);
    }

    protected void parseWorld(Callback<MapData> call) {
        World world = gameMap.getWorld();
        MSG.log("Begin parsing of world %s", world.getName());
        MSG.log("Loading chunks...");
        loadAllChunks(world, states -> {
            MSG.log("Loaded %d chunks, parsing...", states.size());
            Map<GamePoint.Type, List<BlockVector>> points = new HashMap<>();
            new BukkitRunnable() {
                @Override
                public void run() {
                    parseNext(states.iterator(), 0, points, call);
                }
            }.runTaskAsynchronously(plugin);
        });
    }

    private void parseNext(Iterator<BlockState> chunks, int index, Map<GamePoint.Type, List<BlockVector>> data, Callback<MapData> call) {
        if (!chunks.hasNext()) {
            MSG.log("Finished parsing chunks, generating game points...");
            generateGamepoints(data, call);
            return;
        }
        parseBlock(chunks.next(), data);
        parseNext(chunks, index + 1, data, call);
    }

    protected void loadAllChunks(World world, Callback<ChunkStateList> call) {
        WorldBorder border = world.getWorldBorder();
        int chunks = (int) Math.pow(Math.ceil(border.getSize() / 16.0), 2);
        int size = (int) (border.getSize() / 2);
        Location center = border.getCenter();
        ChunkStateList states = new ChunkStateList();

        int threads = (int) Math.ceil(chunks / 8.0) + 1;
        int chunkAmo = chunks / threads;
        MSG.log("Loading %d chunks (size: %d)", chunks, size);
        MSG.log("Using %d threads, each thread covering %d chunks", threads, chunkAmo);

        new BukkitRunnable() {
            int x = center.getBlockX() - size;
            int z = center.getBlockZ() - size;

            @Override
            public void run() {
                MSG.log("Mass chunk loading %d %d", x, z);
                Map<BlockVector, BlockState> result = loadChunks(world, x, z, x + chunkAmo * 16, z + chunkAmo * 16);
                states.putAll(result);
                x += chunkAmo * 16;
                if (x >= center.getBlockX() + size) {
                    x = center.getBlockX() - size;
                    z += chunkAmo * 16;
                    if (z > center.getBlockZ() + size) {
                        MSG.log("Finished loading all chunks (%d/%d)", states.size(), chunks);
                        call.execute(states);
                        this.cancel();
                    }
                }
            }
        }.runTaskTimer(plugin, 0, 15);
    }

    private ChunkStateList loadChunks(World world, int minX, int minZ, int maxX, int maxZ) {
        ChunkStateList states = new ChunkStateList();
        MSG.log("Loading %d chunks", ((maxX - minX) / 16 * (maxZ - minZ) / 16));
        Set<Chunk> loaded = new HashSet<>();
        for (int x = minX; x < maxX; x += 16) {
            for (int z = minZ; z < maxZ; z += 16) {
                for (int bx = 0; bx < 16; bx++) {
                    Chunk chunk = world.getChunkAt(new Location(world, x, 0, z));
                    chunk.setForceLoaded(true);
                    states.add(chunk);
                    chunk.setForceLoaded(false);
                    if (loaded.contains(chunk))
                        MSG.log("DUPLICATE CHUNK LOAD %d, %d", x, z);
                    loaded.add(chunk);
                }
            }
        }
        return states;
    }

    private void generateGamepoints(Map<GamePoint.Type, List<BlockVector>> types, Callback<MapData> call) {
        points = new HashMap<>();
        for (Map.Entry<GamePoint.Type, List<BlockVector>> entry : types.entrySet()) {
            List<GamePoint> pointList = points.getOrDefault(entry.getKey(), new ArrayList<>());
            for (BlockVector vec : entry.getValue()) {
                pointList.add(entry.getKey().generate(vec.toLocation(gameMap.getWorld())));
            }
            points.put(entry.getKey(), pointList);
        }
        call.execute(this);
        MSG.log("Successfully generated %d gamepoints", points.size());
    }

    private void parseBlock(BlockState state, Map<GamePoint.Type, List<BlockVector>> result) {
        for (BlockTarget entry : targets) {
            GamePoint.Type type = entry.getType(state);
            if (type == GamePoint.Type.NONE)
                continue;
            List<BlockVector> vs = result.getOrDefault(type, new ArrayList<>());
            vs.add(new BlockVector(state.getLocation().toVector()));
            result.put(type, vs);
        }
    }
}
