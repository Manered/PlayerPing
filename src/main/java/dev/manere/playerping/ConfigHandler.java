package dev.manere.playerping;

import dev.manere.utils.library.Utils;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class ConfigHandler {

    /**
     * Returns the FileConfiguration of YAML file config.yml found in this project.
     *
     * @return The FileConfiguration of YAML file config.yml found in this project.
     */
    public static FileConfiguration config() {
        return Utils.plugin().getConfig();
    }

    /**
     * Sets the default values of some keys, adds comments, configures the file.
     */
    public static void init() {
        config().addDefault("sender_is_not_player", "<red>Only players can execute this command.");
        config().addDefault("usage", "<red>Correct Usage: /<command> [player]");
        config().addDefault("player_not_found", "<red>Player '<target>' not found!");
        config().addDefault("ping_message", "<white><target>'s ping: <ping> ms");
        config().addDefault("ping_colors.below_or_60", "<#00ff00>");
        config().addDefault("ping_colors.below_or_120", "<#fff000>");
        config().addDefault("ping_colors.below_or_250", "<#ff5000>");
        config().addDefault("ping_colors.higher_than_250", "<#ff0000>");

        config().options()
                .copyDefaults(true)
                .setHeader(List.of("This plugin uses the MiniMessage format: https://webui.advntr.dev/"))
                .parseComments(true);

        config().setComments(
                "sender_is_not_player",
                List.of(
                        "This message is showed to the",
                        "console whenever they try to",
                        "execute the ping command."
                )
        );

        config().setComments(
                "usage",
                List.of(
                        "This message is showed to the",
                        "player whenever they try to",
                        "execute the ping command IMPROPERLY.",
                        "You can use <command> variable in this message."
                )
        );

        config().setComments(
                "player_not_found",
                List.of(
                        "This message is showed to the",
                        "player whenever they try to",
                        "run the command with an argument",
                        "of value 'PLAYER' with a player",
                        "that isn't online.",
                        "You can use <target> variable in this message."
                )
        );

        config().setComments(
                "ping_message",
                List.of(
                        "This message is showed to the",
                        "player whenever they successfully",
                        "run the command.",
                        "You can use <target>, <target_smallfont>, <ping>, <raw_ping> variables in this message."
                )
        );
    }
}
