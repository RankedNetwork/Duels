package java.network.ranked.duels.file;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.network.ranked.duels.Core;
import java.network.ranked.duels.arena.Arena;
import java.util.ArrayList;

public class ArenaFile {

    public File file;
    public FileConfiguration config;

    public ArenaFile() {
        file = new File(Core.getInstance().getDataFolder(), "arena.yml");
        config = YamlConfiguration.loadConfiguration(file);
    }

    public boolean isFileExists() {
        return file.exists();
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public void createNewFile() {
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        try {
            file.createNewFile();

            config.set("arena", new ArrayList<>());

        } catch (IOException e) {
            e.printStackTrace();
        }

        save();
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addNewArena(String name) {
        config.set("arena." + name + ".name", name);
        config.set("arena." + name + ".match-time", 120);
        config.set("arena." + name + ".spawn", "NONE");
        config.set("arena." + name + ".spawn-one", "NONE");
        config.set("arena." + name + ".spawn-two", "NONE");

        save();
    }

    public void setArenaSpawn(Location loc, Arena arena) {
        String location = Core.getInstance().locationToString(loc);

        config.set("arena." + arena.getArenaName() + ".spawn", location);
    }

    public void setPlayerSpawn(Location loc, Arena arena, int playerSpawn) {
        String location = Core.getInstance().locationToString(loc);

        if (playerSpawn == 1) {
            config.set("arena." + arena.getArenaName() + ".spawn-one", location);
        }
        else {
            config.set("arena." + arena.getArenaName() + ".spawn-two", location);
        }
    }

}

