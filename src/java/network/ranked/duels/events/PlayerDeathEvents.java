package java.network.ranked.duels.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.network.ranked.duels.Core;
import java.network.ranked.duels.arena.Arena;
import java.network.ranked.duels.arena.GameState;

public class PlayerDeathEvents implements Listener {

    public Core plugin;

    // Pain...

    public PlayerDeathEvents(Core plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player p = event.getEntity();
        Arena arena = plugin.arenaManager.getPlayersArena(p.getUniqueId());
        if (arena == null) return;
        if (!arena.getPlayers().contains(p.getUniqueId())) return;
        event.setDeathMessage(null);
        if (arena.getState() == GameState.START) {
            event.getDrops().clear();
            p.setHealth(p.getMaxHealth());
            p.getInventory().clear();
            p.getInventory().setArmorContents(null);

            if (arena.getPlayers().get(0) == p.getUniqueId()) {
                Player winner = Bukkit.getPlayer(arena.getPlayers().get(1));

                arena.broadcastMessage(plugin.msgFile.getDeathMsg(p.getName(), winner.getName()));

                winner.setHealth(p.getMaxHealth());
                winner.getInventory().clear();
                winner.getInventory().setArmorContents(null);

                arena.setWinner(winner.getName());
                arena.setState(GameState.END);
                return;
            }
            Player winner = Bukkit.getPlayer(arena.getPlayers().get(0));

            arena.broadcastMessage(plugin.msgFile.getDeathMsg(p.getName(), winner.getName()));

            winner.setHealth(p.getMaxHealth());
            winner.getInventory().clear();
            winner.getInventory().setArmorContents(null);

            arena.setWinner(winner.getName());
            arena.setState(GameState.END);
            return;
        }

    }


}
