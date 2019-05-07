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
                Core.getInstance().kitFile.giveKit(p);
                Core.getInstance().kitFile.giveArmor(p);
                Core.getInstance().kitFile.createNewKit(p, "test");
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
                            p.sendMessage("You are not in a game");
                        }
                    }
                }

                else if (args[0].equalsIgnoreCase("reload")) {
                    // TODO: Update arena

                    Core.getInstance().kitFile = new KitFile();
                    Core.getInstance().msgFile = new MsgFile();
                    Core.getInstance().arenaFile = new ArenaFile();

                    p.sendMessage("Reloaded.");
                }

            }

            // EG: /duels addarena arenaname
            else if (args.length == 2) {

                if (args[0].equalsIgnoreCase("addarena")) {
                    String arenaName = args[1];

                    Arena arena = Core.getInstance().arenaManager.getArenaByName(arenaName);

                    if (arena == null) {
                        // TODO: Add arena to config and to the arena manager

                        Arena newArena = new Arena(arenaName, null, null, null);

                        Core.getInstance().arenaManager.addArena(newArena);
                        Core.getInstance().arenaFile.addNewArena(arenaName);

                        p.sendMessage("Added a new arena called '" + arenaName + "'.");
                    }
                    else {
                        p.sendMessage("Arena with the name of '" + arenaName + "' already existed.");
                    }
                }

                else if (args[0].equalsIgnoreCase("setspawn")) {
                    String arenaName = args[1];

                    Arena arena = Core.getInstance().arenaManager.getArenaByName(arenaName);

                    if (arena != null) {
                        arena.setSpawn(p.getLocation());

                        Core.getInstance().arenaFile.setArenaSpawn(p.getLocation(), arena);
                        Core.getInstance().arenaFile.save();
                        p.sendMessage("Updated spawn location to your current location.");
                    }
                    else {
                        p.sendMessage("Arena '" + arenaName + "' does not exists.");
                    }

                }

                else if (args[0].equalsIgnoreCase("setspawn1")) {
                    String arenaName = args[1];

                    Arena arena = Core.getInstance().arenaManager.getArenaByName(arenaName);

                    if (arena != null) {
                        arena.setPlayerOneSpawn(p.getLocation());

                        Core.getInstance().arenaFile.setPlayerSpawn(p.getLocation(), arena, 1);
                        Core.getInstance().arenaFile.save();
                        p.sendMessage("Updated the spawn for player 1.");
                    }
                    else {
                        p.sendMessage("Arena '" + arenaName + "' does not exists.");
                    }

                }

                else if (args[0].equalsIgnoreCase("setspawn2")) {
                    String arenaName = args[1];

                    Arena arena = Core.getInstance().arenaManager.getArenaByName(arenaName);

                    if (arena != null) {
                        arena.setPlayerTwoSpawn(p.getLocation());

                        Core.getInstance().arenaFile.setPlayerSpawn(p.getLocation(), arena, 2);
                        Core.getInstance().arenaFile.save();
                        p.sendMessage("Updated the spawn for player 2.");
                    }
                    else {
                        p.sendMessage("Arena '" + arenaName + "' does not exists.");
                    }

                }

                else if (args[0].equals("join")) {
                    String arenaName = args[1];

                    Arena arena = Core.getInstance().arenaManager.getArenaByName(arenaName);

                    if (arena != null) {
                        arena.userJoin(p);
                    }
                    else {
                        p.sendMessage("Arena does not exists!");
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
                        p.sendMessage("Arena does not exists.");
                    }
                }

            }

            else {
                p.sendMessage("Unkown command! /Duels for help.");
            }

        }

        return false;
    }
}
