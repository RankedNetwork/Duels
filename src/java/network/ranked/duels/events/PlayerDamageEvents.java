package java.network.ranked.duels.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.network.ranked.duels.Core;
import java.network.ranked.duels.arena.Arena;
import java.network.ranked.duels.arena.GameState;

public class PlayerDamageEvents implements Listener {

    public Core plugin;

    public PlayerDamageEvents(Core plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) return;

        Player p = (Player) e.getEntity();
        Arena arena = plugin.arenaManager.getPlayersArena(p.getUniqueId());
        if (arena != null) {
            if (arena.getState() == GameState.IDLE || arena.getState() == GameState.END || arena.getState() == GameState.LOBBY) {
                e.setCancelled(true);
            }
        }
    }

}
