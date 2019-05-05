package xyz.mukri.duels.arena;

import org.bukkit.Location;
import xyz.mukri.duels.Core;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ArenaManager {

    private static ArenaManager arenaManager;

    private List<Arena> arenas = new ArrayList<Arena>();

    public Core plugin;

    public ArenaManager(Core plugin) {
        this.plugin = plugin;
    }

    public Arena getArenaByName(String name) {
        for (Arena a : this.arenas) {
            if (a.getArenaName().equals(name)) {
                return a;
            }
        }

        return null;
    }

    public Arena getPlayersArena(UUID uuid) {
        for (Arena a : this.arenas) {
            if(a.getPlayers().contains(uuid)) {
                return a;
            }
        }

        return null;
    }

    public void loadAllArena() {

        if (plugin.arenaFile.getConfig().getConfigurationSection("arena") == null) {
            plugin.getLogger().info("No arena found!");
            return;
        }

        for (String a : plugin.arenaFile.getConfig().getConfigurationSection("arena").getKeys(false)) {

            String arenaName = plugin.arenaFile.getConfig().getString("arena." + a + ".name");
            String locStr = plugin.arenaFile.getConfig().getString("arena." + a + ".spawn");
            Location loc;

            if (locStr.equals("NONE")) {
                loc = null;
            }
            else {
                loc = Core.getInstance().stringToLocation(locStr);
            }

            Arena arena = new Arena(arenaName, loc);
            arenas.add(arena);

        }

        Core.getInstance().getLogger().info("Loaded all arenas.");
        Core.getInstance().getLogger().info("Total arena of: " + arenas.size());
    }

}
