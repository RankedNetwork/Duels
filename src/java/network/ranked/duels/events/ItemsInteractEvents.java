package java.network.ranked.duels.events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.network.ranked.duels.Core;
import java.network.ranked.duels.arena.Arena;
import java.network.ranked.duels.arena.GameState;
import java.network.ranked.duels.utils.CustomInventory;

public class ItemsInteractEvents implements Listener {

    @EventHandler
    public void onPlayerClickKitSelector(PlayerInteractEvent e) {


        if (e.getAction() == null) return;
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            ItemStack item = e.getItem();
            Player p = e.getPlayer();
            Arena arena = Core.getInstance().arenaManager.getPlayersArena(p.getUniqueId());
            if (arena == null) return;
            if (!arena.getPlayers().contains(p.getUniqueId())) return;
            if (arena.getState() == GameState.IDLE || arena.getState() == GameState.LOBBY) {
                if (item != null && item.getType() != Material.AIR) {
                    if (item.getType() == Material.BOW) {
                        CustomInventory.getKitGUI(p);
                    }
                    if (item.getType() == Material.BED) {
                        p.performCommand("duels leave");
                    }
                }
            }
        }
    }
}
