package xyz.msws.skywars.data;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.BlockVector;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public abstract class GamePoint {
    protected Location loc;

    public GamePoint(Location loc) {
        this.loc = loc;
    }

    public GamePoint(World world, BlockVector vector) {
        this(vector.toLocation(world));
    }

    public Location getLocation() {
        return loc;
    }

    public abstract void applyTo(Block block);

    public enum Type {
        SPAWN(SpawnPoint.class), CHEST(null), ENEMY(null), TEAM(null), BARRIER(null), CENTER(null), BORDER(null), MISC(null), GAME_SPECIFIC(null), NONE(null);

        private final Class<? extends GamePoint> clazz;

        Type(Class<? extends GamePoint> clazz) {
            this.clazz = clazz;
        }

        public Class<? extends GamePoint> getClazz() {
            return clazz;
        }

        public <T extends GamePoint> T generate(Location loc) {
            return this.generate((Object) loc);
        }

        public <T extends GamePoint> T generate(World world, BlockVector vector) {
            return generate(world, (Object) vector);
        }


        <T extends GamePoint> T generate(Object... args) {
            Class<?>[] classes = new Class[args.length];
            for (int i = 0; i < args.length; i++)
                classes[i] = args[i].getClass();
            Constructor<T> con = null;
            try {
                Constructor<?> constructor = clazz.getConstructor(classes);
                con = (Constructor<T>) clazz.getConstructor(classes);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                return null;
            }
            con.setAccessible(true);
            try {
                return con.newInstance(args);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

}
