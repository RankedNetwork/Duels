package java.network.ranked.duels.file;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.network.ranked.duels.Core;
import java.network.ranked.duels.utils.ChatUtil;
import java.util.ArrayList;
import java.util.List;

public class MsgFile {

    File file;
    FileConfiguration config;

    public MsgFile() {
        file = new File(Core.getInstance().getDataFolder(), "resources/message.yml");

        if (!isExists()) {
            Core.getInstance().saveResource("resources/message.yml", false);
        }

        config = YamlConfiguration.loadConfiguration(file);
    }

    public boolean isExists() {
        return file.exists();
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Players
    public String getJoinMsg(String playerName) {
        return config.getString("player.join")
                .replaceAll("%player%", playerName);
    }

    public String getLeftMsg(String playerName) {
        return config.getString("player.left")
                .replaceAll("%player%", playerName);
    }

    public String getAlreadyInGameMsg() {
        return config.getString("player.already-ingame");
    }

    public String getYouareNotIngameMsg() {
        return config.getString("player.not-ingame");
    }

    public String getDeathMsg(String playerName, String killerName) {
        return config.getString("player.death")
                .replaceAll("%player%", playerName)
                .replaceAll("%killer%", killerName);
    }

    public String getKitMsg(String kitName) {
        return config.getString("player.kit-selected")
                .replaceAll("%kit%", kitName);
    }

    public String getNoPermissionsMsg() {
        return ChatUtil.format(config.getString("player.no-permissions"));
    }

    public List<String> getTop3Msg() {
        return config.getStringList("player.top-3");
    }

    // Arena
    public String getArenaFullMsg() {
        return ChatUtil.format(config.getString("arena.full"));
    }

    public String getArenaNotSetupMsg() {
        return ChatUtil.format(config.getString("arena.not-setup"));
    }

    // Game
    public String getGameStartingMsg() {
        return ChatUtil.format(config.getString("game.starting"));
    }

    public String getStartCountdownMsg(int time) {
        return ChatUtil.format(config.getString("game.start-countdown")
                .replaceAll("%time%", time + ""));
    }

    public String getEndingMsg(int time) {
        return ChatUtil.format(config.getString("game.ending")
                .replaceAll("%time%", time + ""));
    }

    public List<String> getGameWinMsg() {
        List<String> msg = new ArrayList<>();
        for (String m : config.getStringList("game.win")) {
            msg.add(ChatUtil.format(m));
        }
        return msg;
    }

    public String getEndingTiedMsg() {
        return ChatUtil.format(config.getString("game.ending-no-winner"));
    }

    // Admin
    public String getReloadedMsg() {
        return ChatUtil.format(config.getString("admin.reloaded"));
    }

    public String getArenaCreatedMsg(String arenaName) {
        return ChatUtil.format(config.getString("admin.arena-created").replaceAll("%arenaname%", arenaName));
    }

    public String getArenaAlreadyExistsMsg(String arenaName) {
        return ChatUtil.format(config.getString("admin.arena-already-exists").replaceAll("%arenaname%", arenaName));
    }

    public String getSetSpawnMsg(String spawn) {
        return ChatUtil.format(config.getString("admin.set-spawn").replaceAll("%spawn%", spawn));
    }

    public String getArenaDoesNotExists(String arenaName) {
        return ChatUtil.format(config.getString("admin.arena-does-not-exists").replaceAll("%arenaname%", arenaName));
    }

    public String getArenaRemoved(String arenaName) {
        return ChatUtil.format(config.getString("admin.arena-removed").replaceAll("%arenaname%", arenaName));
    }

    public String getLocationNotSet() {
        return ChatUtil.format(config.getString("admin.location-not-set"));
    }


}
