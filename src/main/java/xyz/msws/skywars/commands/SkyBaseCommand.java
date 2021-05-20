package xyz.msws.skywars.commands;

import xyz.msws.skywars.GamePlugin;

public class SkyBaseCommand extends BaseCommand {

    public SkyBaseCommand(String name, GamePlugin plugin) {
        super(name, plugin);

        commands.put("parse", new ParseCommand("parse", plugin));
    }
}
