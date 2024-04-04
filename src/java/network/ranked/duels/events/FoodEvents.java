package java.network.ranked.duels.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import java.network.ranked.duels.Core;
import java.network.ranked.duels.arena.Arena;

public class FoodEvents implements Listener {

    @EventHandler
    public void onFoodLevelDown(FoodLevelChangeEvent e) {
        Player p = (Player) e.getEntity();
        Arena arena = Core.getInstance().arenaManager.getPlayersArena(p.getUniqueId());

        if (arena != null) {
            if (arena.getPlayers().contains(p.getUniqueId())) {
                e.setFoodLevel(20);
                e.setCancelled(true);
            }
        }
    }

}
