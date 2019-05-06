package xyz.mukri.duels.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import xyz.mukri.duels.Core;
import xyz.mukri.duels.arena.Arena;
import xyz.mukri.duels.arena.GameState;

public class PlayerDeathEvents implements Listener {

    public Core plugin;

    public PlayerDeathEvents(Core plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDied(EntityDeathEvent e) {
        if (e.getEntity() instanceof  Player) {
            Player p = (Player) e.getEntity();
            Arena arena = plugin.arenaManager.getPlayersArena(p.getUniqueId());

            if (arena != null) {
                if (arena.getPlayers().contains(p.getUniqueId())) {
                    if (arena.getState() == GameState.START) {

                        p.setHealth(p.getMaxHealth());

                        if (arena.getPlayers().get(0) == p.getUniqueId()) {
                            Player winner = Bukkit.getPlayer(arena.getPlayers().get(1));

                            arena.setWinner(winner.getName());
                            arena.setState(GameState.END);
                        }
                        else {
                            Player winner = Bukkit.getPlayer(arena.getPlayers().get(0));

                            arena.setWinner(winner.getName());
                            arena.setState(GameState.END);
                        }

                    }
                }
            }
        }
    }

}
