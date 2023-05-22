package dev.manere.playerping;

import dev.manere.cmdapi.builders.CommandBuilder;
import dev.manere.cmdapi.commands.CommandManager;
import dev.manere.playerping.commands.PingCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class PlayerPingPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        CommandManager commandManager = new CommandManager();

        // Register the /ping command
        CommandBuilder pingCommandBuilder = new CommandBuilder("ping", commandManager)
                .executor(new PingCommand());
        pingCommandBuilder.register();
    }
}
