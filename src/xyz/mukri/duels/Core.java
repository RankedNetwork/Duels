package xyz.mukri.duels;

import org.bukkit.plugin.java.JavaPlugin;
import xyz.mukri.duels.arena.Arena;
import xyz.mukri.duels.file.ArenaFile;

public class Core extends JavaPlugin {

    private static Core instance;
    public ArenaFile arenaFile;

    @Override
    public void onEnable() {
        instance = this;

        arenaFile = new ArenaFile();
        if (!arenaFile.isFileExists()) {
            arenaFile.createNewFile();
        }

        getLogger().info("Duels is now enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("Dules is now disabled.");
    }

    public static Core getInstance() {
        return instance;
    }

    public String replaceColor(String text) {
        return text.replaceAll("&", "§");
    }
}
