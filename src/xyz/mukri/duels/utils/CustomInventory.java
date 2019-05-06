package xyz.mukri.duels.utils;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import xyz.mukri.duels.Core;
import xyz.mukri.duels.arena.Arena;

import java.util.Arrays;

public class CustomInventory {

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
}
