package xyz.msws.skywars.commands;

import xyz.msws.skywars.GamePlugin;

public class TestBaseCommand extends BaseCommand {
    public TestBaseCommand(String name, GamePlugin plugin) {
        super(name, plugin);
        commands.put("world", new WorldCommand("world", plugin));
        commands.put("gethead", new GetHeadCommand("gethead", plugin));
    }
}
