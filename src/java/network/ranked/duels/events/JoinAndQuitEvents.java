package java.network.ranked.duels.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.network.ranked.duels.Core;
import java.network.ranked.duels.arena.Arena;

public class JoinAndQuitEvents implements Listener {

    @EventHandler
    public void onPlayerQuitWhileInGame(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        Arena arena = Core.getInstance().arenaManager.getPlayersArena(p.getUniqueId());

        if (arena != null) {
            if (arena.getPlayers().contains(p.getUniqueId())) {
                arena.userLeave(p);
            }
        }

        if (Core.getInstance().playerSettings.containsKey(p)) {
            Core.getInstance().playerSettings.remove(p);
        }
    }

}
