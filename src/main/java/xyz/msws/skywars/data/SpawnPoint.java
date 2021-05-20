package xyz.msws.skywars.data;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Skull;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Rotatable;
import org.bukkit.util.BlockVector;

public class SpawnPoint extends GamePoint {
    private BlockFace dir;

    public SpawnPoint(Location loc) {
        super(loc);

        Block block = loc.getBlock();
        BlockData data = block.getBlockData();
        if (!(data instanceof Rotatable)) {
            dir = BlockFace.NORTH;
            return;
        }

        Rotatable rot = (Rotatable) data;
        dir = rot.getRotation();
    }

    public SpawnPoint(World world, BlockVector vector) {
        super(world, vector);
    }

    @Override
    public void applyTo(Block block) {
        block.setType(Material.PLAYER_HEAD);

        ((Rotatable) block).setRotation(dir);
        Skull skull = (Skull) block.getBlockData();
        skull.setOwningPlayer(Bukkit.getOfflinePlayer("SPAWNPOINT"));
    }

}
