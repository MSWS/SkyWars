package xyz.msws.skywars.data;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

public class SpawnPoint extends GamePoint {
    public SpawnPoint(Location loc) {
        super(loc);
    }

    @Override
    public BlockData getRepresentation() {
        return Bukkit.createBlockData(Material.GREEN_WOOL);
    }
}
