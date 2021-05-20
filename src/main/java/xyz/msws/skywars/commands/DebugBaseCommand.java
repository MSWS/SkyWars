package xyz.msws.skywars.commands;

import xyz.msws.skywars.GamePlugin;

public class DebugBaseCommand extends BaseCommand {
    public DebugBaseCommand(String name, GamePlugin plugin) {
        super(name, plugin);
        commands.put("world", new WorldCommand("world", plugin));
        commands.put("gethead", new GetHeadOwner("gethead", plugin));
    }
}
