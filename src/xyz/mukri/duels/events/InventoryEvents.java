package xyz.mukri.duels.events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import xyz.mukri.duels.Core;
import xyz.mukri.duels.arena.Arena;
import xyz.mukri.duels.arena.GameState;

public class InventoryEvents implements Listener {

    public Core plugin;

    public InventoryEvents(Core plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerClickSettings(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        if (plugin.playerSettings.containsKey(p)) {
            int slot = e.getSlot();
            Arena arena = plugin.playerSettings.get(p);

            e.setCancelled(true);

            if (e.getClick() == ClickType.LEFT) {
                if (slot == 12) {
                    if (arena.getPlayerOneSpawn() == null) {
                        p.sendMessage("You have not set the location yet.");
                    }
                    else {
                        p.teleport(arena.getPlayerOneSpawn());
                    }

                    p.closeInventory();
                }
                else if (slot == 13) {
                    if (arena.getPlayerTwoSpawn() == null) {
                        p.sendMessage("You have not set the location yet.");
                    }
                    else {
                        p.teleport(arena.getPlayerTwoSpawn());
                    }

                    p.closeInventory();
                }
                else if (slot == 14) {
                    if (arena.getSpawn() == null) {
                        p.sendMessage("You have not set the location yet.");
                    }
                    else {
                        p.teleport(arena.getSpawn());
                    }

                    p.closeInventory();
                }
            }

            if (e.getClick() == ClickType.RIGHT) {
                if (slot == 12) {
                    arena.setPlayerOneSpawn(p.getLocation());

                    Core.getInstance().arenaFile.setPlayerSpawn(p.getLocation(), arena, 1);
                    Core.getInstance().arenaFile.save();

                    p.closeInventory();
                    p.sendMessage("Updated the spawn for player 1.");
                }
                else if (slot == 13) {
                    arena.setPlayerTwoSpawn(p.getLocation());

                    Core.getInstance().arenaFile.setPlayerSpawn(p.getLocation(), arena, 2);
                    Core.getInstance().arenaFile.save();

                    p.closeInventory();
                    p.sendMessage("Updated the spawn for player 2.");
                }
                else if (slot == 14) {
                    arena.setSpawn(p.getLocation());

                    Core.getInstance().arenaFile.setArenaSpawn(p.getLocation(), arena);
                    Core.getInstance().arenaFile.save();

                    p.closeInventory();
                    p.sendMessage("Updated spawn location to your current location.");
                }
            }

        }
    }

    @EventHandler
    public void onPlayerSelectKit(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        Inventory inv = e.getClickedInventory();
        Arena arena = plugin.arenaManager.getPlayersArena(p.getUniqueId());

        if (arena != null) {
            if (arena.getPlayers().contains(p.getUniqueId())) {

                if (arena.getState() == GameState.LOBBY || arena.getState() == GameState.IDLE) {
                    e.setCancelled(true);

                    if (inv.getName().equals("§7Kit Selector"))  {
                        ItemStack item = e.getCurrentItem();

                        if (item != null) {
                            if (item.getType() != Material.AIR || item.getType() != null) {
                                String[] kitsName = item.getItemMeta().getDisplayName().split("§7Kit: §a");

                                p.sendMessage(kitsName[1]);

                                if (plugin.kitFile.getConfig().getConfigurationSection("kits." + kitsName[1]) != null) {
                                    if (arena.getPlayerKits().containsKey(p.getUniqueId())) {
                                        arena.getPlayerKits().remove(p.getUniqueId());
                                        arena.getPlayerKits().put(p, kitsName[1]);
                                    }
                                    else {
                                        arena.getPlayerKits().put(p, kitsName[1]);
                                    }

                                    p.closeInventory();
                                }

                            }
                        }
                    }

                }

                if (arena.getState() == GameState.END) {
                    e.setCancelled(true);
                }

            }
        }
    }

    @EventHandler
    public void onCloseInventory(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();

        if (plugin.playerSettings.containsKey(p)) {
            plugin.playerSettings.remove(p);
        }
    }

}