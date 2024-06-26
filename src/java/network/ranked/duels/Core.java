package java.network.ranked.duels;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import java.network.ranked.duels.arena.Arena;
import java.network.ranked.duels.arena.ArenaManager;
import java.network.ranked.duels.events.*;
import java.network.ranked.duels.file.KitFile;
import java.network.ranked.duels.file.MsgFile;
import java.network.ranked.duels.commands.DuelsCmd;
import java.network.ranked.duels.file.ArenaFile;
import java.network.ranked.duels.file.StorageFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Core extends JavaPlugin {

    private static Core instance;

    public ArenaFile arenaFile;
    public MsgFile msgFile;
    public KitFile kitFile;
    public StorageFile storageFile;
    public ArenaManager arenaManager;

    public Map<Player, Arena> playerSettings = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;

        arenaFile = new ArenaFile();
        msgFile = new MsgFile();
        kitFile = new KitFile();
        storageFile = new StorageFile();

        if (!arenaFile.isFileExists()) {
            arenaFile.createNewFile();
        }

        if (!storageFile.isExists()) {
            storageFile.createNewFile();
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
        getServer().getPluginManager().registerEvents(new InventoryEvents(this), this);
        getServer().getPluginManager().registerEvents(new JoinAndQuitEvents(), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathEvents(this), this);
        getServer().getPluginManager().registerEvents(new PlayerDamageEvents(this), this);
        getServer().getPluginManager().registerEvents(new ItemsInteractEvents(), this);
        getServer().getPluginManager().registerEvents(new ItemDropsEvents(), this);
        getServer().getPluginManager().registerEvents(new FoodEvents(), this);
        getServer().getPluginManager().registerEvents(new CommandProccessEvents(), this);
    }

    public static Core getInstance() {
        return instance;
    }

    public String locationToString(Location loc) {
        return loc.getWorld().getName() + ":" + loc.getBlockX() + ":" + loc.getBlockY() + ":" + loc.getBlockZ();
    }

    public Location stringToLocation(String s) {
        String[] loc = s.split(":");
        return new Location(Bukkit.getWorld(loc[0]), Integer.parseInt(loc[1]), Integer.parseInt(loc[2]), Integer.parseInt(loc[3]));
    }

    public ItemStack createItem(Material material, String name, List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(name);
        meta.setLore(lore);

        item.setItemMeta(meta);

        return item;
    }

}
