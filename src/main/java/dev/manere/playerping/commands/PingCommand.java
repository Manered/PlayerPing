package dev.manere.playerping.commands;

import dev.manere.cmdapi.argument.Argument;
import dev.manere.cmdapi.argument.ArgumentBuilder;
import dev.manere.cmdapi.argument.TabCompletionBuilder;
import dev.manere.cmdapi.util.ColorUtils;
import dev.manere.playerping.PlayerPingPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PingCommand implements CommandExecutor {


    private final String permission;

    private final PlayerPingPlugin plugin;

    public PingCommand(PlayerPingPlugin plugin) {
        String commandName = "ping";
        this.permission = "ping.cmd";
        this.plugin = plugin;

        Argument<Player> playerArgument = new ArgumentBuilder<Player>("player")
                .setDescription("Retrieves the ping / latency of a player.")
                .setValidationRule(playerName -> playerName != null && !playerName.isEmpty())
                .setPermissionCheck(sender -> sender.hasPermission(permission))
                .build();

        Argument<String> reloadArgument = new ArgumentBuilder<String>("reload")
                .setDescription("Reloads the configuration files.")
                .setPermissionCheck(sender -> sender.hasPermission("ping.reload"))
                .build();
    }

    @Override
    public boolean onCommand(CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        String noPermissionMsg = ColorUtils.translate(Objects.requireNonNull(plugin.getMessages().getString("no_permission.message")));
        String usageMsg = ColorUtils.translate(Objects.requireNonNull(plugin.getMessages().getString("usage.message")));
        String playerNotFoundMsg = ColorUtils.translate(Objects.requireNonNull(plugin.getMessages().getString("player_not_found.message")));
        String pingSenderMsg = ColorUtils.translate(Objects.requireNonNull(plugin.getMessages().getString("ping_sender.message")));
        String pingOtherMsg = ColorUtils.translate(Objects.requireNonNull(plugin.getMessages().getString("ping_other.message")));
        String reloadMsg = ColorUtils.translate(Objects.requireNonNull(plugin.getMessages().getString("reload.message")));

        if (!sender.hasPermission(permission)) {
            sender.sendMessage(noPermissionMsg);
            return true;
        }

        if (args.length > 1) {
            sender.sendMessage(usageMsg);
            return true;
        }

        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("This command can only be executed by players.");
                return true;
            }

            Player player = (Player) sender;
            int ping = player.getPing();
            String pingColor = getPingColor(ping);

            player.sendMessage(pingSenderMsg
                    .replaceAll("<ping>", pingColor + ping)
                    .replaceAll("<player>", player.getName())
            );
        } else if (args[0].equalsIgnoreCase("reload") && sender.hasPermission("ping.reload")) {
            plugin.reloadConfigFiles();
            sender.sendMessage(reloadMsg);
        } else {
            String playerName = args[0];
            Player player = Bukkit.getPlayer(playerName);

            if (player == null) {
                sender.sendMessage(playerNotFoundMsg
                        .replaceAll("<player>", playerName)
                );
                return true;
            }

            int ping = player.getPing();
            String pingColor = getPingColor(ping);
            sender.sendMessage(pingOtherMsg
                    .replaceAll("<ping>", pingColor + ping)
                    .replaceAll("<player>", player.getName())
            );
        }

        return true;
    }


    public static String getPingColor(int ping) {
        if (ping <= 60) {
            return ColorUtils.translate("#00ff00"); // Green color
        } else if (ping <= 120) {
            return ColorUtils.translate("#fff000"); // Yellow color
        } else if (ping <= 250) {
            return ColorUtils.translate("#ff5000"); // Orange color
        } else {
            return ColorUtils.translate("#ff0000"); // Red color
        }
    }
}
