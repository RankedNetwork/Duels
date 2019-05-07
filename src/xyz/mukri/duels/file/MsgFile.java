package xyz.mukri.duels.file;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import xyz.mukri.duels.Core;

import java.io.File;
import java.io.IOException;

public class MsgFile {

    File file;
    FileConfiguration config;

    public MsgFile() {
        file = new File(Core.getInstance().getDataFolder(), "message.yml");

        if (!isExists()) {
            Core.getInstance().saveResource("message.yml", false);
        }

        config = YamlConfiguration.loadConfiguration(file);
    }

    public boolean isExists() {
        return file.exists();
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getJoinMsg() {
        return config.getString("join-game-msg");
    }

}
