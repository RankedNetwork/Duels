package xyz.mukri.duels.arena;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.mukri.duels.Core;

public class Timer extends BukkitRunnable {

    private Arena arena;
    private int start;
    private int lobby;
    private int end;

    public Timer(Arena arena) {
        this.arena = arena;
        this.start = 60;
        this.lobby = 10;
        this.end = 10;
    }

    public void start() {
        this.runTaskTimer(Core.getInstance(), 0, 20);
    }

    public void reset() {
        this.start = 60;
        this.lobby = 10;
        this.end = 10;
    }

    @Override
    public void run() {

        if (arena.getState() == GameState.LOBBY) {
            lobby--;

            if (lobby <= 5) {
                arena.broadcastMessage("Game is starting in " + lobby + "s!");
            }

            if (lobby == 0) {
                arena.setState(GameState.START);
                arena.teleportToArena();
            }
        }

        if (arena.getState() == GameState.START) {
            start--;

            if (start <= 60) {
                arena.broadcastMessage("Time: " + start + "s");
            }

            if (start == 0) {
                arena.setState(GameState.END);
                arena.broadcastMessage("Ended!");
            }
        }

        if (arena.getState() == GameState.END) {
            end--;

            if (end == 5) {
                arena.broadcastMessage("Winner is: " + arena.getWinner());
            }

            if (end == 0) {
                arena.reset();
                arena.setState(GameState.IDLE);
            }
        }

    }
}
