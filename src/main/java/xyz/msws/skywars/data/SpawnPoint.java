package xyz.msws.skywars.data;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.util.BlockVector;

public class SpawnPoint extends GamePoint {
    public SpawnPoint(Location loc) {
        super(loc);
    }

    public SpawnPoint(World world, BlockVector vector) {
        super(world, vector);
    }

    @Override
    public BlockData getRepresentation() {
        return Bukkit.createBlockData(Material.GREEN_WOOL);
    }
}
