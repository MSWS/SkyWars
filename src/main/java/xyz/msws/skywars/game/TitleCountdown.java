package xyz.msws.skywars.game;

import org.bukkit.entity.Player;
import xyz.msws.skywars.utils.EmptyCallback;

public class TitleCountdown extends Countdown {

    private Game game;

    public TitleCountdown(Game game, long time, EmptyCallback run) {
        super(game.getPlugin(), time, run);
        this.game = game;
    }

    @Override
    public void run() {
        for (Player p : game.getValidPlayers())
            p.sendTitle(getSecondsLeft() + "", "", 0, 20, 0);
        if (System.currentTimeMillis() > endTime) {
            this.cancel();
            run.execute();
        }
    }
}
