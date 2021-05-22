package xyz.msws.skywars.data;

import org.bukkit.block.BlockState;

public interface BlockTarget {
    public GamePoint.Type getType(BlockState state);
}
