package xyz.msws.skywars.data;

import org.bukkit.block.data.BlockData;

public interface BlockQuery {
    public boolean matches(BlockData data);
}
