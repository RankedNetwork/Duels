package xyz.mukri.duels;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.mukri.duels.arena.Arena;
import xyz.mukri.duels.arena.ArenaManager;
import xyz.mukri.duels.commands.DuelsCmd;
import xyz.mukri.duels.events.JoinAndQuitEvents;
import xyz.mukri.duels.file.ArenaFile;

public class Core extends JavaPlugin {

    private static Core instance;
    public ArenaFile arenaFile;
    public ArenaManager arenaManager;

    @Override
    public void onEnable() {
        instance = this;

        arenaFile = new ArenaFile();

        if (!arenaFile.isFileExists()) {
            arenaFile.createNewFile();
        }

        arenaManager = new ArenaManager(this);
        arenaManager.loadAllArena();

        registerCommands();
        registerListeners();

        getLogger().info("Duels is now enabled.");
    }

    @Override
    public void onDisable() {

        for (Player p : Bukkit.getOnlinePlayers()) {
            Arena arena = arenaManager.getPlayersArena(p.getUniqueId());

            if (arena != null) {
                if (arena.getPlayers().contains(p.getUniqueId())) {
                    arena.userLeave(p);
                }
            }
        }

        getLogger().info("Dules is now disabled.");
    }

    public void registerCommands() {
        getCommand("duels").setExecutor(new DuelsCmd());
    }

    public void registerListeners() {
        getServer().getPluginManager().registerEvents(new JoinAndQuitEvents(), this);
    }

    public static Core getInstance() {
        return instance;
    }

    public String replaceColor(String text) {
        return text.replaceAll("&", "§");
    }

    public String locationToString(Location loc) {
        return loc.getWorld().getName() + ":" + loc.getBlockX() + ":" + loc.getBlockY() + ":" + loc.getBlockZ();
    }

    public Location stringToLocation(String s) {
        String[] loc = s.split(":");
        return new Location(Bukkit.getWorld(loc[0]), Integer.parseInt(loc[1]), Integer.parseInt(loc[2]), Integer.parseInt(loc[3]));
    }

}
