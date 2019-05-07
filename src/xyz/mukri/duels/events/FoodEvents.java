package xyz.mukri.duels.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import xyz.mukri.duels.Core;
import xyz.mukri.duels.arena.Arena;

public class FoodEvents implements Listener {

    @EventHandler
    public void onFoodLevelDown(FoodLevelChangeEvent e) {
        Player p = (Player) e.getEntity();
        Arena arena = Core.getInstance().arenaManager.getPlayersArena(p.getUniqueId());

        if (arena != null) {
            if (arena.getPlayers().contains(p.getUniqueId())) {
                e.setFoodLevel(20);
            }
        }
    }

}
