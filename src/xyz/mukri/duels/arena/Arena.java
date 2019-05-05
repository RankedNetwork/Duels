package xyz.mukri.duels.arena;

import org.bukkit.Location;

import java.util.UUID;
import java.util.List;

public class Arena {

    private int id;
    private String name;
    private Location spawnLoc;
    private GameState state;
    private List<UUID> players;

    public Arena(int id, String name, Location spawnLoc) {
        this.id = id;
        this.name = name;
        this.spawnLoc = spawnLoc;
    }

    // Getters
    public int getArenaId() {
        return this.id;
    }

    public String getArenaName() {
        return this.name;
    }

    public List<UUID> getPlayers() {
        return this.players;
    }


}
