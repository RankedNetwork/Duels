package java.network.ranked.duels.arena;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.network.ranked.duels.Core;
import java.network.ranked.duels.utils.ChatUtil;
import java.util.List;
import java.util.UUID;

public class Timer extends BukkitRunnable {

    private Arena arena;
    private int start;
    private int lobby;
    private int end;

    public Timer(Arena arena) {
        this.arena = arena;
        this.start = arena.getMatchTime();
        this.lobby = 10;
        this.end = 10;
    }

    public void start() {
        this.runTaskTimer(Core.getInstance(), 0, 20);
    }

    public void reset() {
        this.start = arena.getMatchTime();
        this.lobby = 10;
        this.end = 10;
    }

    @Override
    public void run() {

        if (arena.getState() == GameState.LOBBY) {
            lobby--;

            if (lobby <= 5) {
                arena.broadcastMessage(ChatUtil.format(Core.getInstance().msgFile.getStartCountdownMsg(lobby)));
                for (UUID uuid : arena.getPlayers()) {
                    Player bukkitPlayer = Core.getInstance().getServer().getPlayer(uuid);
                    if (bukkitPlayer != null) {
                        bukkitPlayer.playSound(bukkitPlayer.getLocation(), Sound.NOTE_PLING, 1, 1);
                    }
                }

            }

            if (lobby == 0) {
                arena.setState(GameState.START);
                arena.teleportToArena();
                arena.givePlayerKits();
            }
        }

        if (arena.getState() == GameState.START) {
            start--;

            if (start == 10) {
                arena.broadcastMessage(ChatUtil.format(Core.getInstance().msgFile.getEndingMsg(start)));
            }

            if (start <=5) {
                arena.broadcastMessage(ChatUtil.format(Core.getInstance().msgFile.getEndingMsg(start)));
            }

            if (start == 0) {
                arena.setState(GameState.END);
                arena.broadcastMessage(Core.getInstance().msgFile.getEndingTiedMsg());
                arena.setWinner("No one!");
            }
        }

        if (arena.getState() == GameState.END) {
            end--;

            if (end == 5) {

                arena.addPlayerStats();

                List<String> msg = Core.getInstance().msgFile.getGameWinMsg();

                for (String a : msg) {
                    arena.broadcastMessage(a.replaceAll("&", "§").replaceAll("%winner%", arena.getWinner()));
                }


            }

            if (end == 0) {
                arena.reset();
                arena.setState(GameState.IDLE);
            }
        }

    }
}
