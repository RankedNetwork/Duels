package xyz.mukri.duels.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.mukri.duels.Core;
import xyz.mukri.duels.arena.Arena;

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

                return false;
            }

            // EG: /duels test
            else if (args.length == 1) {

            }

            // EG: /duels addarena arenaname
            else if (args.length == 2) {

                if (args[0].equalsIgnoreCase("addarena")) {
                    String arenaName = args[1];

                    Arena arena = Core.getInstance().arenaManager.getArenaByName(arenaName);

                    if (arena == null) {
                        // TODO: Add arena to config and to the arena manager
                        Core.getInstance().arenaFile.addNewArena(arenaName);
                    }
                    else {
                        p.sendMessage("Arena with the name of '" + arenaName + "' already existed.");
                    }
                }

                else if (args[0].equalsIgnoreCase("setspawn")) {
                    String arenaName = args[1];

                    Arena arena = Core.getInstance().arenaManager.getArenaByName(arenaName);

                    if (arena != null) {
                        // TODO: Update arena location
                        Core.getInstance().arenaFile.setArenaSpawn(p.getLocation(), arena);
                        Core.getInstance().arenaFile.save();
                        p.sendMessage("Updated spawn location to your current location.");
                    }
                    else {
                        p.sendMessage("Arena '" + arenaName + "' does not exists.");
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
