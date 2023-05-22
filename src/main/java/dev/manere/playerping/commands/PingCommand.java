package dev.manere.playerping.commands;

import dev.manere.cmdapi.commands.CommandExecutor;
import dev.manere.cmdapi.utils.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;

public class PingCommand implements CommandExecutor {
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            // No player specified, assume the sender wants their own ping
            if (sender instanceof Player) {
                Player player = (Player) sender;
                int ping = getPing(player);
                String pingColor = getPingColor(ping);
                sender.sendMessage(ColorUtils.translate("#00ff00&l⇄ &fPing&7: <ping>&7ms").replaceAll("<ping>", pingColor + ping));
            } else {
                sender.sendMessage(ColorUtils.translate("#ff0000Only players can execute this command."));
            }
        } else {
            // Player name specified, get ping for that player
            Player player = Bukkit.getPlayer(args[0]);
            if (player != null) {
                int ping = getPing(player);
                String pingColor = getPingColor(ping);
                sender.sendMessage(ColorUtils.translate("#00ff00&l⇄ #0077ff<player>'s &fPing&7: <ping>&7ms")
                        .replaceAll("<player>", player.getName())
                        .replaceAll("<ping>", pingColor + ping));
            } else {
                sender.sendMessage(ColorUtils.translate("#ff0000Player not found."));
            }
        }
    }

    public static int getPing(Player player) {
        try {
            Method handle = player.getClass().getMethod("getHandle");
            Object nmsHandle = handle.invoke(player);
            Field pingField = nmsHandle.getClass().getField("ping");
            return pingField.getInt(nmsHandle);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
                 | SecurityException | NoSuchFieldException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Exception while trying to get player ping.", e);
        }

        return -1;
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
