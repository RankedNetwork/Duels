package xyz.mukri.duels.arena;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

public class Arena {

    private int maxPlayers;

    private String name;

    private Location spawnLoc;

    private GameState state;

    private List<UUID> players;

    private Timer timer;

    private Map<UUID, Location> playerLoc;

    public Arena( String name, Location spawnLoc) {
        this.name = name;
        this.spawnLoc = spawnLoc;

        maxPlayers = 2;

        state = GameState.LOBBY;

        players =  new ArrayList<>();
        playerLoc = new HashMap<>();

        timer = new Timer(this);
    }

    // Functions
    public void broadcastMessage(String msg) {
        for (int i = 0; i < players.size(); i++) {
            Bukkit.getPlayer(players.get(i)).sendMessage(msg);
        }
    }

    public void userJoin(Player p) {
        if (!players.contains(p.getUniqueId())) {
            if (spawnLoc != null) {
                playerLoc.put(p.getUniqueId(), p.getLocation());
                p.teleport(spawnLoc);
                players.add(p.getUniqueId());
            }
            else {
                p.sendMessage("Arena spawn has not been set.");
            }
        }
        else {
            p.sendMessage("You already ingame");
        }
    }

    public void userLeave(Player p) {
        if (players.contains(p.getUniqueId())) {
            if (playerLoc.containsKey(p.getUniqueId())) {
                p.teleport(playerLoc.get(p.getUniqueId()));
            }

            players.remove(p.getUniqueId());
            p.sendMessage("You left the game.");
        }
    }

    // Getters
    public String getArenaName() {
        return this.name;
    }

    public List<UUID> getPlayers() {
        return this.players;
    }

    public Location getSpawn() { return this.spawnLoc; }

    // Setters
    public void setState(GameState state) {
        this.state = state;
    }

    public void setSpawn(Location loc) { this.spawnLoc = loc; }

}
