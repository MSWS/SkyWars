package xyz.msws.skywars.data;

import org.bukkit.ChunkSnapshot;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockVector;
import xyz.msws.skywars.GamePlugin;
import xyz.msws.skywars.utils.Callback;
import xyz.msws.skywars.utils.MSG;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class MapFileData extends MapData {
    private File data;
    private FileConfiguration config;

    public MapFileData(GamePlugin plugin, Map map) {
        super(plugin, map);
        data = new File(plugin.getDataFolder(), map.getWorld().getName() + "._data.yml");
    }

    @Override
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


    @Override
    public void saveData() {
        config = new YamlConfiguration();
        if (points == null) {
            save();
            return;
        }

        for (java.util.Map.Entry<GamePoint.Type, List<GamePoint>> ps : points.entrySet()) {
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

    private void parseFile() {
        points = new HashMap<>();
        java.util.Map<GamePoint.Type, List<BlockVector>> blocks = new HashMap<>();
        if (!data.exists())
            return;
        config = YamlConfiguration.loadConfiguration(data);
        for (java.util.Map.Entry<String, Object> entry : config.getValues(false).entrySet()) {
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
        generateGamepoints(blocks);
    }

    @Override
    protected void parseWorld() {
        World world = map.getWorld();
//        world.getWorldBorder().setSize(5);
        MSG.log("Begin parsing of world %s", world.getName());
        MSG.log("Loading chunks...");
        loadAllChunks(world, result -> {
            MSG.log("Loaded %d chunks, parsing...", result.size());
            java.util.Map<GamePoint.Type, List<BlockVector>> points = new HashMap<>();
            new BukkitRunnable() {
                @Override
                public void run() {
                    parseNext(result, 0, points);
                }
            }.runTaskAsynchronously(plugin);
            generateGamepoints(points);
        });

    }

    private void parseNext(List<ChunkSnapshot> snapshots, int index, java.util.Map<GamePoint.Type, List<BlockVector>> data) {
        if (index == snapshots.size()) {
            MSG.log("Finished parsing chunks, generating game points...");
            generateGamepoints(data);
            return;
        }
        MSG.log("Parsing chunk %d/%d (%.2f%%)", index, snapshots.size(), ((double) index / snapshots.size()) * 100.0);
        parseChunk(snapshots.get(index), data);
        parseNext(snapshots, index + 1, data);
    }

    protected void loadAllChunks(World world, Callback<List<ChunkSnapshot>> call) {
        WorldBorder border = world.getWorldBorder();
        border.setSize(2000);
        int chunks = (int) Math.ceil(border.getSize() / 16.0);
        Location center = border.getCenter();
        List<ChunkSnapshot> snapshots = new ArrayList<>();
        long start = System.currentTimeMillis();

        int threads = (int) Math.ceil(chunks / 16.0);
        int blockAmo = chunks / threads;

        new BukkitRunnable() {
            int x = center.getBlockX() - chunks * 8;
            int z = center.getBlockZ() - chunks * 8;

            @Override
            public void run() {
                if (x >= center.getBlockX() + chunks * 8) {
                    x = center.getBlockX() - chunks * 8;
                    z += blockAmo * 16;
                }
                snapshots.addAll(loadChunks(world, x, z, x + blockAmo * 16, z + blockAmo * 16));
                x += blockAmo * 16;
                z += blockAmo * 16;
                if (z > center.getBlockZ() + chunks * 8) {
                    this.cancel();
                    call.execute(snapshots);
                }
            }
        }.runTaskTimer(plugin, 0, 20);
    }

    private List<ChunkSnapshot> loadChunks(World world, int minX, int minZ, int maxX, int maxZ) {
        List<ChunkSnapshot> snapshots = new ArrayList<>();
        for (int x = minX; x < maxX; x += 16) {
            for (int z = minZ; z < maxZ; z += 16) {
                snapshots.add(world.getChunkAt(x, z).getChunkSnapshot());
            }
        }
        return snapshots;
    }

    private void generateGamepoints(java.util.Map<GamePoint.Type, List<BlockVector>> types) {
        points = new HashMap<>();
        for (java.util.Map.Entry<GamePoint.Type, List<BlockVector>> entry : types.entrySet()) {
            List<GamePoint> pointList = points.getOrDefault(entry.getKey(), new ArrayList<>());
            for (BlockVector vec : entry.getValue()) {
                pointList.add(entry.getKey().generate(map.getWorld(), vec));
            }
            points.put(entry.getKey(), pointList);
        }
    }

    private void parseChunk(ChunkSnapshot cs, java.util.Map<GamePoint.Type, List<BlockVector>> result) {
        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 255; y++) {
                for (int z = 0; z < 16; z++) {
                    parseBlock(cs, x, y, z, result);
                }
            }
        }
    }

    private void parseBlock(ChunkSnapshot cs, int x, int y, int z, java.util.Map<GamePoint.Type, List<BlockVector>> result) {
        BlockData block = cs.getBlockData(x, y, z);

        for (java.util.Map.Entry<BlockQuery, GamePoint.Type> entry : targets.entrySet()) {
            if (!entry.getKey().matches(block))
                continue;
            List<BlockVector> vs = result.getOrDefault(entry.getValue(), new ArrayList<>());
            vs.add(new BlockVector(x, y, z));
            result.put(entry.getValue(), vs);
        }
    }
}
