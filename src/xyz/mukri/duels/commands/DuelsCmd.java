package xyz.mukri.duels.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.mukri.duels.Core;
import xyz.mukri.duels.arena.Arena;
import xyz.mukri.duels.file.ArenaFile;
import xyz.mukri.duels.file.KitFile;
import xyz.mukri.duels.file.MsgFile;
import xyz.mukri.duels.utils.CustomInventory;

public class DuelsCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) {

            sender.sendMessage("You must be a player to perform this commands.");

            return false;
        }

        Player p = (Player) sender;

        if (cmd.getName().equalsIgnoreCase("duels")) {

            if (args.length == 0) {
                // TODO: Send help messages etc
                CustomInventory.getArenaListGUI(p);
                return false;
            }

            // EG: /duels test
            else if (args.length == 1) {

                if (args[0].equalsIgnoreCase("leave")) {
                    Arena arena = Core.getInstance().arenaManager.getPlayersArena(p.getUniqueId());

                    if (arena != null) {
                        if (arena.getPlayers().contains(p.getUniqueId())) {
                            arena.userLeave(p);
                        }
                        else {
                            p.sendMessage(Core.getInstance().msgFile.getYouareNotIngameMsg());
                        }
                    }
                }

                else if (args[0].equalsIgnoreCase("reload")) {
                    // TODO: Update arena

                    Core.getInstance().kitFile = new KitFile();
                    Core.getInstance().msgFile = new MsgFile();
                    Core.getInstance().arenaFile = new ArenaFile();

                    p.sendMessage(Core.getInstance().msgFile.getReloadedMsg());
                }

            }

            // EG: /duels addarena arenaname
            else if (args.length == 2) {

                if (args[0].equalsIgnoreCase("addarena")) {
                    String arenaName = args[1];

                    Arena arena = Core.getInstance().arenaManager.getArenaByName(arenaName);

                    if (arena == null) {

                        Arena newArena = new Arena(arenaName, null, null, null, 120);

                        Core.getInstance().arenaManager.addArena(newArena);
                        Core.getInstance().arenaFile.addNewArena(arenaName);

                        p.sendMessage(Core.getInstance().msgFile.getArenaCreatedMsg(arenaName));
                    }
                    else {
                        p.sendMessage(Core.getInstance().msgFile.getArenaAlreadyExistsMsg(arenaName));
                    }
                }

                else if (args[0].equalsIgnoreCase("setspawn")) {
                    String arenaName = args[1];

                    Arena arena = Core.getInstance().arenaManager.getArenaByName(arenaName);

                    if (arena != null) {
                        arena.setSpawn(p.getLocation());

                        Core.getInstance().arenaFile.setArenaSpawn(p.getLocation(), arena);
                        Core.getInstance().arenaFile.save();
                        p.sendMessage(Core.getInstance().msgFile.getSetSpawnMsg("Main Spawn"));
                    }
                    else {
                        p.sendMessage(Core.getInstance().msgFile.getArenaAlreadyExistsMsg(arenaName));
                    }

                }

                else if (args[0].equalsIgnoreCase("setspawn1")) {
                    String arenaName = args[1];

                    Arena arena = Core.getInstance().arenaManager.getArenaByName(arenaName);

                    if (arena != null) {
                        arena.setPlayerOneSpawn(p.getLocation());

                        Core.getInstance().arenaFile.setPlayerSpawn(p.getLocation(), arena, 1);
                        Core.getInstance().arenaFile.save();
                        p.sendMessage(Core.getInstance().msgFile.getSetSpawnMsg("Player One"));
                    }
                    else {
                        p.sendMessage(Core.getInstance().msgFile.getArenaAlreadyExistsMsg(arenaName));
                    }

                }

                else if (args[0].equalsIgnoreCase("setspawn2")) {
                    String arenaName = args[1];

                    Arena arena = Core.getInstance().arenaManager.getArenaByName(arenaName);

                    if (arena != null) {
                        arena.setPlayerTwoSpawn(p.getLocation());

                        Core.getInstance().arenaFile.setPlayerSpawn(p.getLocation(), arena, 2);
                        Core.getInstance().arenaFile.save();
                        p.sendMessage(Core.getInstance().msgFile.getSetSpawnMsg("Player Two"));
                    }
                    else {
                        p.sendMessage(Core.getInstance().msgFile.getArenaAlreadyExistsMsg(arenaName));
                    }

                }

                else if (args[0].equals("join")) {
                    String arenaName = args[1];

                    Arena arena = Core.getInstance().arenaManager.getArenaByName(arenaName);

                    if (arena != null) {
                        arena.userJoin(p);
                    }
                    else {
                        p.sendMessage(Core.getInstance().msgFile.getArenaDoesNotExists(arenaName));
                    }
                }

                else if (args[0].equalsIgnoreCase("settings")) {
                    String arenaName = args[1];

                    Arena arena = Core.getInstance().arenaManager.getArenaByName(arenaName);

                    if (arena != null) {
                        Core.getInstance().playerSettings.put(p, arena);
                        CustomInventory.openArenaSettings(p, arena);
                    }
                    else {
                        p.sendMessage(Core.getInstance().msgFile.getArenaDoesNotExists(arenaName));
                    }
                }

            }

            else {
                p.sendMessage("§aUnkown command! /Duels help");
            }

        }

        return false;
    }
}
