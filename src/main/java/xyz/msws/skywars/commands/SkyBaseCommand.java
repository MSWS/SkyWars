package xyz.msws.skywars.commands;

import xyz.msws.skywars.GamePlugin;
import xyz.msws.skywars.SkyWars;

public class SkyBaseCommand extends BaseCommand {

    public SkyBaseCommand(String name, GamePlugin plugin) {
        super(name, plugin);

        if (plugin instanceof SkyWars)
            commands.put("parse", new ParseCommand("parse", (SkyWars) plugin));
    }
}
