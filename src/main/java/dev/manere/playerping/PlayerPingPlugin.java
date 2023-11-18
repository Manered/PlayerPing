package dev.manere.playerping;

import dev.manere.playerping.command.PingCommand;
import dev.manere.utils.command.CommandType;
import dev.manere.utils.command.builder.CommandBuilder;
import dev.manere.utils.command.builder.permission.CommandPermission;
import dev.manere.utils.config.Config;
import dev.manere.utils.library.Utils;
import org.bukkit.plugin.java.JavaPlugin;

public final class PlayerPingPlugin extends JavaPlugin {

    /**
     * {@inheritDoc}
     */
    @Override
    public void onEnable() {
        Utils.init(this);
        Config.init();
        Config.reload();

        ConfigHandler.init();

        registerCommands();
    }

    /**
     * Registers the /ping [player] command.
     */
    public void registerCommands() {
        CommandBuilder.command("ping", CommandType.COMMAND_MAP)
                .description("Lets you view a different player's ping, or yours.")
                .permission()
                    .type(CommandPermission.CUSTOM)
                    // The permission required should be 'ping.command'.
                    .custom("ping", "command")
                    .build()
                .executes(new PingCommand())
                .completes(new PingCommand())
                .build()
                .register();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDisable() {
        Config.save();
    }
}
