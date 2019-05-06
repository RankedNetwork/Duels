package xyz.mukri.duels.arena;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

public class Arena {

    private int maxPlayers;

    private String name;

    private Location spawnLoc;
    private Location playerOneLoc;
    private Location playerTwoLoc;

    private GameState state;

    private List<UUID> players;

    private Timer timer;

    private Map<UUID, Location> playerLoc;

    public Arena( String name, Location spawnLoc, Location playerOne, Location playerTwo) {
        this.name = name;
        this.spawnLoc = spawnLoc;
        this.playerOneLoc = playerOne;
        this.playerTwoLoc = playerTwo;

        maxPlayers = 2;

        state = GameState.IDLE;

        players =  new ArrayList<>();
        playerLoc = new HashMap<>();

        timer = new Timer(this);
        timer.start();
    }

    // Functions
    public void broadcastMessage(String msg) {
        for (int i = 0; i < players.size(); i++) {
            Bukkit.getPlayer(players.get(i)).sendMessage(msg);
        }
    }

    public void userJoin(Player p) {
        if (!players.contains(p.getUniqueId())) {
            if (spawnLoc != null || playerOneLoc != null || playerTwoLoc != null) {
                if (players.size() < maxPlayers) {
                    playerLoc.put(p.getUniqueId(), p.getLocation());
                    p.teleport(spawnLoc);
                    players.add(p.getUniqueId());

                    if (players.size() >= maxPlayers) {
                        broadcastMessage("Game is starting...");
                        setState(GameState.LOBBY);
                    }
                }
                else {
                    p.sendMessage("Arena already full!");
                }
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

    public void teleportToArena() {
        Bukkit.getPlayer(players.get(0)).teleport(playerOneLoc);
        Bukkit.getPlayer(players.get(1)).teleport(playerTwoLoc);
    }

    public void reset() {
        for (int i = 0; i < players.size(); i++) {
            Player p  = Bukkit.getPlayer(players.get(i));

            if (playerLoc.containsKey(p.getUniqueId())) {
                p.teleport(playerLoc.get(p.getUniqueId()));
                playerLoc.remove(p.getUniqueId());
            }
        }

        players.clear();
        setState(GameState.IDLE);
        timer.reset();
    }


    // Getters
    public String getArenaName() {
        return this.name;
    }

    public List<UUID> getPlayers() {
        return this.players;
    }

    public Location getSpawn() {
        return this.spawnLoc;
    }

    public GameState getState() {
        return this.state;
    }

    public Location getPlayerOneSpawn() {
        return playerOneLoc;
    }

    public Location getPlayerTwoSpawn() {
        return playerTwoLoc;
    }

    // Setters
    public void setState(GameState state) {
        this.state = state;
    }

    public void setSpawn(Location loc) {
        this.spawnLoc = loc;
    }

    public void setPlayerOneSpawn(Location loc) {
        this.playerOneLoc = loc;
    }

    public void setPlayerTwoSpawn(Location loc) {
        this.playerTwoLoc = loc;
    }

}
