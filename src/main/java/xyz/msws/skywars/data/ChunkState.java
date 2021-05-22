package xyz.msws.skywars.data;

import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.util.BlockVector;

import java.util.HashMap;
import java.util.Map;

public class ChunkState {
    private Chunk chunk;
    private Map<BlockVector, BlockState> states = new HashMap<>();

    public ChunkState(Chunk chunk) {
        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 255; y++) {
                for (int z = 0; z < 15; z++) {
                    Block block = chunk.getBlock(x, y, z);
                    if (block.getType().isAir())
                        continue;
                    BlockState state = block.getState();
                    BlockVector vector = new BlockVector(x, y, z);
                    states.put(vector, state);
                }
            }
        }
    }

    public BlockState getBlockState(int x, int y, int z) {
        return getBlockState(new BlockVector(x, y, z));
    }

    public BlockState getBlockState(BlockVector state) {
        return states.getOrDefault(state, null);
    }

    public Map<BlockVector, BlockState> getStates() {
        return states;
    }
}
