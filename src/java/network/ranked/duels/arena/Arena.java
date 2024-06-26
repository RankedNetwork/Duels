package java.network.ranked.duels.arena;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.network.ranked.duels.Core;
import java.network.ranked.duels.file.PlayerFile;
import java.network.ranked.duels.utils.ChatUtil;
import java.util.*;

public class Arena {

    // Cba to rewrite the whole class, but you get the idea.

    private int maxPlayers;
    private int matchTime;

    private String name;
    private String winner;

    private Location spawnLoc;
    private Location playerOneLoc;
    private Location playerTwoLoc;

    private GameState state;

    private List<UUID> players;

    private Timer timer;

    private Map<UUID, Location> playerLoc;
    private Map<UUID, ItemStack[]> playerInv;
    private Map<UUID, ItemStack[]> playerArmor;
    private Map<UUID, String> playerKit;

    public Arena( String name, Location spawnLoc, Location playerOne, Location playerTwo, int matchTime) {
        this.name = name;
        this.spawnLoc = spawnLoc;
        this.playerOneLoc = playerOne;
        this.playerTwoLoc = playerTwo;
        this.winner = null;

        maxPlayers = 2;
        this.matchTime = matchTime;

        state = GameState.IDLE;

        players =  new ArrayList<>();
        playerLoc = new HashMap<>();
        playerInv = new HashMap<>();
        playerKit = new HashMap<>();
        playerArmor = new HashMap<>();

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
        if (Core.getInstance().arenaManager.getPlayersArena(p.getUniqueId()) != null) {
            p.sendMessage(Core.getInstance().msgFile.getAlreadyInGameMsg());
            return;
        }

        PlayerFile playerFile = new PlayerFile(p);

        if (!playerFile.isExists()) {
            playerFile.createNewFile();
            playerFile.save();
        }

        if (!Core.getInstance().storageFile.isPlayerExists(p)) {
            Core.getInstance().storageFile.addPlayerUUID(p);
            Core.getInstance().storageFile.save();
        }

        if (!players.contains(p.getUniqueId())) {
            if (spawnLoc != null || playerOneLoc != null || playerTwoLoc != null) {
                if (players.size() < maxPlayers) {

                    players.add(p.getUniqueId());

                    playerInv.put(p.getUniqueId(), p.getInventory().getContents());
                    playerLoc.put(p.getUniqueId(), p.getLocation());
                    playerArmor.put(p.getUniqueId(), p.getInventory().getArmorContents());

                    broadcastMessage(Core.getInstance().msgFile.getJoinMsg(p.getName()));

                    p.getInventory().setArmorContents(null);
                    p.getInventory().clear();
                    p.teleport(spawnLoc);
                    p.setHealth(p.getMaxHealth());
                    p.setFoodLevel(20);

                    // Add lobby items
                    p.getInventory().addItem(Core.getInstance().createItem(Material.BOW, ChatUtil.format("&bKit Selector"), Arrays.asList("Right-click to select your kit.")));
                    p.getInventory().addItem(Core.getInstance().createItem(Material.BED, ChatUtil.format("&bLeave duel"), Arrays.asList("Right-click leave the duel.")));

                    if (players.size() >= maxPlayers) {
                        broadcastMessage(Core.getInstance().msgFile.getGameStartingMsg());
                        setState(GameState.LOBBY);
                    }
                }
                else {
                    p.sendMessage(Core.getInstance().msgFile.getArenaFullMsg());
                }
            }
            else {
                p.sendMessage(Core.getInstance().msgFile.getArenaNotSetupMsg());
            }
        }
        else {
            p.sendMessage(Core.getInstance().msgFile.getAlreadyInGameMsg());
        }
    }

    public void userLeave(Player p) {
        if (players.contains(p.getUniqueId())) {
            if (getState() == GameState.LOBBY) {
                broadcastMessage(Core.getInstance().msgFile.getLeftMsg(p.getName()));
                setState(GameState.IDLE);
                timer.reset();
            }

            if (getState() == GameState.START) {
                p.damage(999);
            }

            if (getState() == GameState.IDLE) {
                broadcastMessage(Core.getInstance().msgFile.getLeftMsg(p.getName()));
            }

            if (playerLoc.containsKey(p.getUniqueId())) {
                p.teleport(playerLoc.get(p.getUniqueId()));
            }

            if (playerInv.containsKey(p.getUniqueId())) {
                p.getInventory().clear();
                p.getInventory().setContents(playerInv.get(p.getUniqueId()));
            }

            if (playerArmor.containsKey(p.getUniqueId())) {
                p.getInventory().setArmorContents(null);
                p.getInventory().setArmorContents(playerArmor.get(p.getUniqueId()));
            }

            playerArmor.remove(p.getUniqueId());
            playerInv.remove(p.getUniqueId());
            playerLoc.remove(p.getUniqueId());
            players.remove(p.getUniqueId());

        }
    }

    public void teleportToArena() {
        Bukkit.getPlayer(players.get(0)).teleport(playerOneLoc);
        Bukkit.getPlayer(players.get(1)).teleport(playerTwoLoc);
    }

    public void reset() {
        for (int i = 0; i < players.size(); i++) {
            Player p = Bukkit.getPlayer(players.get(i));

            if (playerLoc.containsKey(p.getUniqueId())) {
                p.teleport(playerLoc.get(p.getUniqueId()));
            }

            if (playerInv.containsKey(p.getUniqueId())) {
                p.getInventory().clear();
                p.getInventory().setContents(playerInv.get(p.getUniqueId()));
            }

            if (playerArmor.containsKey(p.getUniqueId())) {
                p.getInventory().setArmorContents(null);
                p.getInventory().setArmorContents(playerArmor.get(p.getUniqueId()));
            }
        }

        players.clear();
        playerLoc.clear();
        playerInv.clear();
        playerArmor.clear();
        playerKit.clear();
        timer.reset();
    }

    public void givePlayerKits() {
        for (int i = 0; i < players.size(); i++) {
            Player p = Bukkit.getPlayer(getPlayers().get(i));

            p.closeInventory();
            p.getInventory().clear();
            p.getInventory().setArmorContents(null);

            p.setFoodLevel(100);
            p.setHealth(p.getMaxHealth());

            if (playerKit.containsKey(p.getUniqueId())) {
                Core.getInstance().kitFile.givePlayerKit(p, playerKit.get(p.getUniqueId()));
            }
            else {
                Core.getInstance().kitFile.givePlayerKit(p, Core.getInstance().kitFile.getDefaultKits());
            }
        }
    }

    public void addPlayerStats() {
        for (int i = 0; i < players.size(); i++) {
            Player p = Bukkit.getPlayer(players.get(i));

            if (p.getName() == getWinner()) {
                PlayerFile playerFile = new PlayerFile(p);

                playerFile.addWin(1);
                playerFile.addGamesPlayed(1);
                playerFile.save();

                if (Core.getInstance().storageFile.checkTop3(p, playerFile.getWin())) {
                    Core.getInstance().storageFile.updateLeaderboards(p, playerFile.getWin());
                }
            }
            else {
                PlayerFile playerFile = new PlayerFile(p);

                playerFile.addGamesPlayed(1);
                playerFile.save();
            }
        }
    }

    // Getters
    public String getArenaName() {
        return this.name;
    }

    public List<UUID> getPlayers() {
        return this.players;
    }

    public Map<UUID, String> getPlayerKits() {
        return this.playerKit;
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

    public String getWinner() {
        return  this.winner;
    }

    public int getMatchTime() {
        return this.matchTime;
    }

    public int getMaxPlayers() {
        return this.maxPlayers;
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

    public void setWinner(String winner) {
        this.winner = winner;
    }

}
