package xyz.msws.skywars.data;

import org.bukkit.Chunk;
import org.bukkit.block.BlockState;
import org.bukkit.util.BlockVector;

import java.util.*;

public class ChunkStateList implements Map<BlockVector, BlockState>, Iterable<BlockState> {
    private final Map<BlockVector, BlockState> states = new HashMap<>();

    public ChunkStateList(Collection<ChunkState> states) {
        for (ChunkState state : states) {
            this.states.putAll(state.getStates());
        }
    }

    public ChunkStateList() {
    }

    public void add(ChunkState state) {
        states.putAll(state.getStates());
    }

    public void add(Chunk chunk) {
        add(new ChunkState(chunk));
    }

    public BlockState getBlockState(int x, int y, int z) {
        return getBlockState(new BlockVector(x, y, z));
    }

    public BlockState getBlockState(BlockVector state) {
        return states.getOrDefault(state, null);
    }

    @Override
    public int size() {
        return states.size();
    }

    @Override
    public boolean isEmpty() {
        return states.isEmpty();
    }

    @Override
    public Iterator<BlockState> iterator() {
        return states.values().iterator();
    }

    @Override
    public boolean containsKey(Object key) {
        return states.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return states.containsValue(value);
    }

    @Override
    public BlockState get(Object key) {
        return states.get(key);
    }

    @Override
    public BlockState put(BlockVector key, BlockState value) {
        return states.put(key, value);
    }

    @Override
    public BlockState remove(Object key) {
        return states.remove(key);
    }

    @Override
    public void putAll(Map<? extends BlockVector, ? extends BlockState> m) {
        states.putAll(m);
    }

    @Override
    public void clear() {
        states.clear();
    }

    @Override
    public Set<BlockVector> keySet() {
        return states.keySet();
    }

    @Override
    public Collection<BlockState> values() {
        return states.values();
    }

    @Override
    public Set<Entry<BlockVector, BlockState>> entrySet() {
        return states.entrySet();
    }

}
