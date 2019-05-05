package xyz.mukri.duels.arena;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.UUID;
import java.util.List;

public class Arena {

    private int maxPlayers;

    private String name;

    private Location spawnLoc;

    private GameState state;

    private List<UUID> players;

    private Timer timer;

    public Arena( String name, Location spawnLoc) {
        this.name = name;
        this.spawnLoc = spawnLoc;

        maxPlayers = 2;

        state = GameState.LOBBY;

        timer = new Timer(this);
    }

    // Functions
    public void broadcastMessage(String msg) {
        for (int i = 0; i < players.size(); i++) {
            Bukkit.getPlayer(players.get(i)).sendMessage(msg);
        }
    }

    // Getters
    public String getArenaName() {
        return this.name;
    }

    public List<UUID> getPlayers() {
        return this.players;
    }

    // Setters
    public void setState(GameState state) {
        this.state = state;
    }

}
