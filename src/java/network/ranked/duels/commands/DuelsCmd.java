package java.network.ranked.duels.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.network.ranked.duels.Core;
import java.network.ranked.duels.arena.Arena;
import java.network.ranked.duels.file.ArenaFile;
import java.network.ranked.duels.file.KitFile;
import java.network.ranked.duels.file.MsgFile;
import java.network.ranked.duels.file.StorageFile;
import java.network.ranked.duels.utils.ChatUtil;
import java.network.ranked.duels.utils.CustomInventory;
import java.util.List;

public class DuelsCmd implements CommandExecutor {

    // This was so bad :(

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) {

            sender.sendMessage(ChatUtil.format("&cYou must be a player to perform this commands."));

            return true;
        }

        Player p = (Player) sender;

        // cmd.getName not needed you idiot

        if (args.length == 0 && p.hasPermission("duels.players")) {
            CustomInventory.getArenaListGUI(p);
            return true;
        }

        if (args.length < 1) {
            p.sendMessage(ChatUtil.format("&cUnknown command! /duels help"));
            return true;
        }

        if (args[0].equalsIgnoreCase("leave")) {
            if (!p.hasPermission("duels.players")) {
                p.sendMessage(ChatUtil.format("&cYou do not have permission to perform this command."));
                return true;
            }
            Arena arena = Core.getInstance().arenaManager.getPlayersArena(p.getUniqueId());
            if (arena == null) {
                p.sendMessage(ChatUtil.format(Core.getInstance().msgFile.getYouareNotIngameMsg()));
                return true;
            }

            if (!arena.getPlayers().contains(p.getUniqueId())) {
                p.sendMessage(ChatUtil.format(Core.getInstance().msgFile.getYouareNotIngameMsg()));
                return true;
            }

            arena.userLeave(p);
        }

        if (args[0].equalsIgnoreCase("top")) {
            if (!p.hasPermission("duels.players")) {
                p.sendMessage(ChatUtil.format("&cYou do not have permission to perform this command."));
                return true;
            }

            List<String> top3 = Core.getInstance().storageFile.getTop3();

            for (int i = 0; i < top3.size(); i++) {
                String[] s = top3.get(i).split(":");

                top3.set(i, s[0] + ": W: " + s[1]);
            }

            String p1 = top3.get(0);
            String p2 = top3.get(1);
            String p3 = top3.get(2);

            List<String> msgfromFile = Core.getInstance().msgFile.getTop3Msg();

            for (String msg : msgfromFile) {
                p.sendMessage(ChatUtil.format(msg.replaceAll("%top1%", p1).replaceAll("%top2%", p2)
                        .replaceAll("%top3%", p3)));
            }
        }

        if (args[0].equalsIgnoreCase("reload") && p.hasPermission("duels.admin")) {
            Core.getInstance().kitFile = new KitFile();
            Core.getInstance().msgFile = new MsgFile();
            Core.getInstance().arenaFile = new ArenaFile();
            Core.getInstance().storageFile = new StorageFile();

            p.sendMessage(ChatUtil.format(Core.getInstance().msgFile.getReloadedMsg()));

        } else {
            p.sendMessage(ChatUtil.format("&cUnknown command! /duels help"));
        }

        if (args[0].equalsIgnoreCase("help")) {
            if (p.hasPermission("duels.admin")) {
                p.sendMessage(" ");
                p.sendMessage("/duels - Open Arena GUI");
                p.sendMessage("/duels leave - Leave an arena");
                p.sendMessage("/duels join <arena> - Join an arena");
                p.sendMessage("/duels reload - Reload all .yml");
                p.sendMessage("/duels addarena <arena> - Create an arena");
                p.sendMessage("/duels delarena <arena> - Delete an arena");
                p.sendMessage("/duels settings <arena> - Set an arena");
                p.sendMessage("/duels setspawn1 <arena> - Set playerone spawn");
                p.sendMessage("/duels setspawn2 <arena> - Set playertwo spawn");
                p.sendMessage("/duels setspawn <arena> - Set main spawn");
                p.sendMessage(" ");
            } else {
                p.sendMessage(" ");
                p.sendMessage("/duels - Open Arena GUI");
                p.sendMessage("/duels leave - Leave an arena");
                p.sendMessage("/duels join <arena> - Join an arena");
                p.sendMessage(" ");
            }
            return true;
        }


        if (args.length > 1) {

            if (args[0].equalsIgnoreCase("join")) {
                if (!p.hasPermission("duels.players")) {
                    p.sendMessage(ChatUtil.format(Core.getInstance().msgFile.getNoPermissionsMsg()));
                    return true;
                }

                String arenaName = args[1];

                Arena arena = Core.getInstance().arenaManager.getArenaByName(arenaName);

                if (arena != null) {
                    arena.userJoin(p);
                } else {
                    p.sendMessage(ChatUtil.format(Core.getInstance().msgFile.getArenaDoesNotExists(arenaName)));
                }
                return true;
            }


            if (!p.hasPermission("duels.admin")) {
                p.sendMessage(ChatUtil.format(Core.getInstance().msgFile.getNoPermissionsMsg()));
                return true;
            }

            if (args[0].equalsIgnoreCase("settings")) {
                String arenaName = args[1];

                Arena arena = Core.getInstance().arenaManager.getArenaByName(arenaName);

                if (arena != null) {
                    Core.getInstance().playerSettings.put(p, arena);
                    CustomInventory.openArenaSettings(p, arena);
                } else {
                    p.sendMessage(Core.getInstance().msgFile.getArenaDoesNotExists(arenaName));
                }
                return true;
            }


            if (args[0].equalsIgnoreCase("addarena")) {
                String arenaName = args[1];

                Arena arena = Core.getInstance().arenaManager.getArenaByName(arenaName);

                if (arena == null) {

                    Arena newArena = new Arena(arenaName, null, null, null, 120);

                    Core.getInstance().arenaManager.addArena(newArena);
                    Core.getInstance().arenaFile.addNewArena(arenaName);

                    p.sendMessage(ChatUtil.format(Core.getInstance().msgFile.getArenaCreatedMsg(arenaName)));
                } else {
                    p.sendMessage(ChatUtil.format(Core.getInstance().msgFile.getArenaAlreadyExistsMsg(arenaName)));
                }
                return true;
            }

            if (args[0].equalsIgnoreCase("delarena")) {
                String arenaName = args[1];

                Arena arena = Core.getInstance().arenaManager.getArenaByName(arenaName);

                if (arena != null) {
                    Core.getInstance().arenaManager.removeArena(arena);
                    Core.getInstance().arenaFile.getConfig().set("arena." + arena.getArenaName(), null);
                    p.sendMessage(ChatUtil.format(Core.getInstance().msgFile.getArenaRemoved(arenaName)));
                } else {
                    p.sendMessage(ChatUtil.format(Core.getInstance().msgFile.getArenaDoesNotExists(arenaName)));
                }
                return true;
            }

            if (args[0].equalsIgnoreCase("setspawn")) {
                String arenaName = args[1];

                Arena arena = Core.getInstance().arenaManager.getArenaByName(arenaName);

                if (arena != null) {
                    arena.setSpawn(p.getLocation());

                    Core.getInstance().arenaFile.setArenaSpawn(p.getLocation(), arena);
                    Core.getInstance().arenaFile.save();
                    p.sendMessage(ChatUtil.format(Core.getInstance().msgFile.getSetSpawnMsg("Main Spawn")));
                } else {
                    p.sendMessage(ChatUtil.format(Core.getInstance().msgFile.getArenaAlreadyExistsMsg(arenaName)));
                }
                return true;
            }

            if (args[0].equalsIgnoreCase("setspawn1")) {
                String arenaName = args[1];

                Arena arena = Core.getInstance().arenaManager.getArenaByName(arenaName);

                if (arena != null) {
                    arena.setPlayerOneSpawn(p.getLocation());

                    Core.getInstance().arenaFile.setPlayerSpawn(p.getLocation(), arena, 1);
                    Core.getInstance().arenaFile.save();
                    p.sendMessage(ChatUtil.format(Core.getInstance().msgFile.getSetSpawnMsg("Player One")));
                } else {
                    p.sendMessage(ChatUtil.format(Core.getInstance().msgFile.getArenaAlreadyExistsMsg(arenaName)));
                }
                return true;
            }

            if (args[0].equalsIgnoreCase("setspawn2")) {
                String arenaName = args[1];

                Arena arena = Core.getInstance().arenaManager.getArenaByName(arenaName);

                if (arena != null) {
                    arena.setPlayerTwoSpawn(p.getLocation());

                    Core.getInstance().arenaFile.setPlayerSpawn(p.getLocation(), arena, 2);
                    Core.getInstance().arenaFile.save();
                    p.sendMessage(ChatUtil.format(Core.getInstance().msgFile.getSetSpawnMsg("Player Two")));
                } else {
                    p.sendMessage(ChatUtil.format(Core.getInstance().msgFile.getArenaAlreadyExistsMsg(arenaName)));
                }
                return true;
            }

            p.performCommand("duels help");
            // Lol
        }

        // EG: /duels addarena arenaname
        return true;
    }

}
