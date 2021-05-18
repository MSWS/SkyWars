package xyz.msws.skywars.game;

import xyz.msws.skywars.utils.EmptyCallback;

import java.lang.reflect.Method;
import java.util.List;

public abstract class Countdown {
    protected long startTime, endTime, updated;
    protected EmptyCallback run;
    protected List<Method> methods;

    public void start(long time, EmptyCallback run) {
        this.startTime = System.currentTimeMillis();
        this.endTime = startTime + time;
        this.run = run;
        start();
    }

    public abstract void start();

    public abstract void end();
}
