package xyz.mukri.duels.arena;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ArenaManager {

    private static ArenaManager arenaManager;

    private List<Arena> arenas = new ArrayList<Arena>();

    public void loadAllArena() {

    }

    public Arena getArenaByName(String name) {
        for (Arena a : this.arenas) {
            if (a.getArenaName().equals(name)) {
                return a;
            }
        }

        return null;
    }

    public Arena getArenaById(int i) {
        for (Arena a : this.arenas) {
            if (a.getArenaId() == i) {
                return a;
            }
        }

        return null;
    }

    public Arena getPlayersArena(UUID uuid) {
        for (Arena a : this.arenas) {
            if(a.getPlayers().contains(uuid)) {
                return a;
            }
        }

        return null;
    }


}
