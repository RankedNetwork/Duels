package xyz.mukri.duels.file;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Pig;
import xyz.mukri.duels.Core;

import java.io.File;
import java.io.IOException;

public class ArenaFile {

    private File file;
    private FileConfiguration config;

    public ArenaFile() {
        file = new File(Core.getInstance().getDataFolder(), "arena.yml");
        config = YamlConfiguration.loadConfiguration(file);
    }

    public boolean isFileExists() {
        if (file.exists())
            return true;

        return false;
    }

    /*
     * Arena config:
     * total-arena - Total arena
     * id - Arena id
     * id.name - Arena name
     * id.spawn - Spawn location
     *
     */
    public void createNewFile() {
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        config.set("total-arena", 0);

        save();
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addNewArena(String name, String location) {
        // TODO: add 1 to total arena and save everything.
    }

}

