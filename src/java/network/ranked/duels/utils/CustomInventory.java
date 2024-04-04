package java.network.ranked.duels.utils;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.network.ranked.duels.Core;
import java.network.ranked.duels.arena.Arena;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomInventory {

    public Core plugin;

    public CustomInventory(Core plugin) {
        this.plugin = plugin;
    }

    public static void openArenaSettings(Player p, Arena arena) {
        Inventory inv = Bukkit.createInventory(null, 27, "Arena: " + arena.getArenaName());

        if (arena.getPlayerOneSpawn() != null) {
            String locationStr = Core.getInstance().locationToString(arena.getPlayerOneSpawn());
            inv.setItem(12, Core.getInstance().createItem(Material.ACACIA_DOOR_ITEM, "§a§lPlayer One Spawn", Arrays.asList("§fYou already set it to:", "§2" + locationStr, " ", "§fRight-Click to teleport", "§fLeft-Click to set to current location.")));
        }
        else {
            inv.setItem(12, Core.getInstance().createItem(Material.ACACIA_DOOR_ITEM, "§c§lPlayer One Spawn", Arrays.asList("§fYou already set it to:", "§cNOT SET", " ", "§fLeft-Click to set to current location.")));
        }

        if (arena.getPlayerTwoSpawn() != null) {
            String locationStr = Core.getInstance().locationToString(arena.getPlayerTwoSpawn());
            inv.setItem(13, Core.getInstance().createItem(Material.BIRCH_DOOR_ITEM, "§a§lPlayer Two Spawn", Arrays.asList("§fYou already set it to:", "§2" + locationStr, " ", "§fRight-Click to teleport", "§fLeft-Click to set to current location.")));
        }
        else {
            inv.setItem(13, Core.getInstance().createItem(Material.BIRCH_DOOR_ITEM, "§c§lPlayer Two Spawn", Arrays.asList("§fYou already set it to:", "§cNOT SET", " ", "§fLeft-Click to set to current location.")));
        }

        if (arena.getSpawn() != null) {
            String locationStr = Core.getInstance().locationToString(arena.getSpawn());
            inv.setItem(14, Core.getInstance().createItem(Material.IRON_DOOR, "§a§lMain Spawn", Arrays.asList("§fYou already set it to:", "§2" + locationStr, " ", "§fRight-Click to teleport", "§fLeft-Click to set to current location.")));
        }
        else {
            inv.setItem(14, Core.getInstance().createItem(Material.IRON_DOOR, "§c§lMain Spawn", Arrays.asList("§fYou already set it to:", "§cNOT SET", " ", "§fLeft-Click to set to current location.")));
        }

        p.openInventory(inv);
    }

    public static void getKitGUI(Player p) {
        Inventory inv = Bukkit.createInventory(null, 45, "§7Kit Selector");


        for (String kit : Core.getInstance().kitFile.getConfig().getConfigurationSection("kits").getKeys(false)) {
            String[] items = Core.getInstance().kitFile.getConfig().getString("kits." + kit + ".display").split(":");

            ItemStack item = new ItemStack(Material.valueOf(items[0]), Integer.valueOf(items[1]), Byte.valueOf(items[2]));
            ItemMeta meta = item.getItemMeta();
            List<String> lore = new ArrayList<>();

            meta.setDisplayName(ChatUtil.format("&7Kit: &a" + kit));

            if (items.length > 3) {
                for (int i = 3; i < items.length; i++) {
                    lore.add(ChatUtil.format(items[i]));
                }
            }

            if (!lore.isEmpty()) {
                meta.setLore(lore);
            }

            item.setItemMeta(meta);

            inv.addItem(item);

        }

        p.openInventory(inv);
    }

    public static void getArenaListGUI(Player p) {
        Inventory inv = Bukkit.createInventory(null, 45, ChatUtil.format("&a&lDuels"));

        for (Arena a : Core.getInstance().arenaManager.getAllArena()) {
            String arenaName = a.getArenaName();
            String arenaState = a.getState().toString();
            int playersJoin = a.getPlayers().size();
            int maxPlayers = a.getMaxPlayers();

            inv.addItem(Core.getInstance().createItem(Material.DIAMOND_CHESTPLATE, ChatUtil.format("&7Arena: " + arenaName), Arrays.asList("", ChatUtil.format("&7State: &a" + arenaState),
                    ChatUtil.format("&7Players: &a" + playersJoin + "/" + maxPlayers), " ", ChatUtil.format("&7Right-Click to join."))));
        }

        p.openInventory(inv);
    }

}
