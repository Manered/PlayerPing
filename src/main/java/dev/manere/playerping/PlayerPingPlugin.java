package dev.manere.playerping;

import dev.manere.playerping.commands.PingCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public final class PlayerPingPlugin extends JavaPlugin {
    private FileConfiguration messagesConfig;

    @Override
    public void onEnable() {
        reloadConfigFiles();
        this.getCommand("ping").setExecutor(new PingCommand(this));
    }

    public FileConfiguration getMessages() {
        return messagesConfig;
    }

    public void reloadConfigFiles() {
        // Reload messages.yml
        File messagesFile = new File(getDataFolder(), "messages.yml");
        if (!messagesFile.exists()) {
            saveResource("messages.yml", false);
        }

        messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);

        try {
            InputStream defaultMessagesStream = getResource("messages.yml");
            Path defaultMessagesPath = Files.createTempFile(getName() + "_default_messages", ".yml");
            Files.copy(defaultMessagesStream, defaultMessagesPath, StandardCopyOption.REPLACE_EXISTING);
            FileConfiguration defaultMessagesConfig = YamlConfiguration.loadConfiguration(defaultMessagesPath.toFile());

            messagesConfig.setDefaults(defaultMessagesConfig);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
