package xyz.msws.skywars.data;

import org.bukkit.Location;
import org.bukkit.block.data.BlockData;

public abstract class GamePoint {
    protected Location loc;

    public GamePoint(Location loc) {
        this.loc = loc;
    }

    public Location getLocation() {
        return loc;
    }

    public abstract BlockData getRepresentation();

    public enum Type {
        SPAWN, CHEST, ENEMY, TEAM, BARRIER, CENTER, BORDER, MISC, GAME_SPECIFICs;
    }

}
