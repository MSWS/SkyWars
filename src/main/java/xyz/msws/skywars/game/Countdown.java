package xyz.msws.skywars.game;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import xyz.msws.skywars.GamePlugin;
import xyz.msws.skywars.utils.EmptyCallback;

import java.lang.reflect.Method;
import java.util.List;

public abstract class Countdown extends BukkitRunnable {
    protected long startTime, endTime, updated;
    protected EmptyCallback run;
    protected List<Method> methods;
    protected BukkitTask task;
    protected GamePlugin plugin;

    public Countdown(GamePlugin plugin, long time, EmptyCallback run) {
        this.plugin = plugin;
        this.startTime = System.currentTimeMillis();
        this.endTime = startTime + time;
        this.run = run;
    }

    public void start() {
        this.runTaskTimer(plugin, 0, 20);
    }

    public long getTimeLeft() {
        return System.currentTimeMillis() - endTime;
    }

    public int getSecondsLeft() {
        return (int) Math.ceil(getTimeLeft() / 1000.0);
    }

    public EmptyCallback getCall() {
        return run;
    }

    public BukkitTask getTask() {
        return task;
    }
}
