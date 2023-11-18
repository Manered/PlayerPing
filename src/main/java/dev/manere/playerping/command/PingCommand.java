package dev.manere.playerping.command;

import dev.manere.utils.command.builder.dispatcher.CommandContext;
import dev.manere.utils.command.builder.dispatcher.CommandDispatcher;
import dev.manere.utils.command.builder.dispatcher.SuggestionDispatcher;
import dev.manere.utils.config.Config;
import dev.manere.utils.player.PlayerUtils;
import dev.manere.utils.server.ServerUtils;
import dev.manere.utils.text.color.TextStyle;
import dev.manere.utils.text.font.SmallFont;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PingCommand implements CommandDispatcher, SuggestionDispatcher {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean run(@NotNull CommandContext ctx) {
        CommandSender sender = ctx.sender();

        // Returns true if the sender is also a player
        // Returns false if the sender isn't a player, so in this case they are a console sender.
        if (!ctx.senderInstanceOfPlayer()) {
            sender.sendMessage(Config.key("sender_is_not_player").val().asText().component());
            return true;
        }

        // If there is more than 1 argument set (so for example /command <arg_1> [HERE -> <arg_2>]
        if (ctx.args().size() > 1) {
            Component toSend = TextStyle.component(Config.key("usage")
                    .val()
                    .asText()
                    .string()
                        // ctx.commandRan() returns the alias of the command,
                        // so what the player ran should be /ping.
                        .replaceAll("<command>", ctx.commandRan()));

            sender.sendMessage(toSend);
            return true;
        }

        // Finally casts the sender to a player
        // since we already made sure that
        // only players can execute this.
        Player player = ctx.player();

        // Checks if there was no arg set, so just /ping.
        if (ctx.args().isEmpty()) {
            return ping(player);
        }

        // Checks if there was an arg set, so /ping <player>
        if (ctx.args().size() == 1) {
            // ctx.argAt(0) returns a string,
            // we then wrap it in PlayerUtils.player(String name)
            // to retrieve a player by that name
            Player target = PlayerUtils.player(ctx.argAt(0));

            // If the player doesn't exist we send a player not found message.
            if (target == null) {
                Component toSend = TextStyle.component(Config.key("player_not_found")
                        .val()
                        .asText()
                        .string()
                            .replaceAll("<target>", ctx.argAt(0)));

                player.sendMessage(toSend);
                return true;
            }

            // If everything works, the message is sent.
            return ping(player, target);
        }

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @Nullable List<String> suggest(@NotNull CommandContext ctx) {
        // This should return true if the player provided for example:
        // /ping Manere_
        if (ctx.args().size() == 1) {
            List<String> players = new ArrayList<>();

            for (Player player : ServerUtils.online()) {
                players.add(player.getName());
            }

            // We remove the player that's receiving suggestions
            // from the list.
            // So basically we have Alex, Steve
            // If your name is Steve it will only have Alex.
            players.remove(ctx.sender().getName());

            // We return every player on the server,
            // so you can understand what the argument does.
            return players;
        }

        return null;
    }

    /**
     * Sends a Player the ping message.
     *
     * @param player The player to send the message to.
     * @return {@code true}, for stopping the command execution after sending the message.
     */
    public boolean ping(@NotNull Player player) {
        return ping(player, player);
    }

    /**
     * Sends a Player the ping message.
     *
     * @param player The player to send the message to.
     * @param target The ping to use for the message.
     * @return {@code true}, for stopping the command execution after sending the message.
     */
    public boolean ping(@NotNull Player player, @NotNull Player target) {
        Component toSend = TextStyle.component(Config.key("ping_message").val()
                .asText()
                .string()
                    .replaceAll("<target>", target.getName())
                    // <target_smallfont> returns a small caps/font
                    // representation of the target's name.
                    .replaceAll("<target_smallfont>", SmallFont.convert(target.getName()))
                    .replaceAll("<ping>", pingColor(target.getPing()) + target.getPing())
                    .replaceAll("<raw_ping>", String.valueOf(target.getPing())));

        player.sendMessage(toSend);
        return true;
    }

    /**
     * Returns the appropriate color for a player's ping.
     *
     * @param ping A player's ping.
     * @return The appropriate color for a player's ping.
     */
    public @NotNull String pingColor(int ping) {
        if (ping <= 60) {
            return Config.key("ping_colors.below_or_60").val().asText().string();
        } else if (ping <= 120) {
            return Config.key("ping_colors.below_or_120").val().asText().string();
        } else if (ping <= 250) {
            return Config.key("ping_colors.below_or_250").val().asText().string();
        } else {
            return Config.key("ping_colors.higher_than_250").val().asText().string();
        }
    }
}
