package me.brannstrom.Commands;

import me.brannstrom.Handlers.*;
import me.brannstrom.Model.Parkour;
import me.brannstrom.Service.ParkourService;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ParkourCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Player player = (Player) sender;

        if(args.length == 1) {
            if(args[0].equalsIgnoreCase("list") || args[0].equalsIgnoreCase("liste")) {
                MessageHandler.sendList(player);
            }
            else if(args[0].equalsIgnoreCase("stats") || args[0].equalsIgnoreCase("statistikk")) {
                player.sendMessage(ChatColor.RED + "Du må spesifisere parkour. Bruk '/parkour liste' for å se en liste over alle parkours.");
            }
            else if(args[0].equalsIgnoreCase("tp") || args[0].equalsIgnoreCase("teleport")) {
                player.sendMessage(ChatColor.RED + "Du må spesifisere parkour. Bruk '/parkour liste' for å se en liste over alle parkours.");
            }
            else {
                MessageHandler.sendPlayerInfoMessage(player);
            }
        }
        else if(args.length == 2) {
            if(args[0].equalsIgnoreCase("stats") || args[0].equalsIgnoreCase("statistikk")) {
                if(ParkourHandler.isParkour(args[1])) {
                    ParkourHandler.showStats(player, ParkourHandler.getParkour(args[1]));
                }
                else {
                    player.sendMessage(InfoKeeper.parkourDontExist.replaceAll("<parkour>", args[1]));
                }

            }
            else if(args[0].equalsIgnoreCase("tp") || args[0].equalsIgnoreCase("teleport")) {
                if(!MemoryHandler.combatTimer.containsKey(player.getUniqueId()) || MemoryHandler.combatTimer.get(player.getUniqueId()) < System.currentTimeMillis()) {
                    if(ParkourHandler.isParkour(args[1])) {
                        ParkourHandler.joinParkour(player, ParkourHandler.getParkour(args[1]));
                    }
                    else {
                        player.sendMessage(InfoKeeper.parkourDontExist.replaceAll("<parkour>", args[1]));
                    }
                }
                else {
                    player.sendMessage(MainHandler.getCombatTimer(player));
                }
            }
            else {
                if(player.hasPermission("spillere.mod")) {
                    if(args[0].equalsIgnoreCase("info") || args[0].equalsIgnoreCase("informasjon")) {
                        if(ParkourHandler.isParkour(args[1])) {
                            MessageHandler.showParkourInfo(player, ParkourHandler.getParkour(args[1]));
                        }
                        else {
                            player.sendMessage(InfoKeeper.parkourDontExist.replaceAll("<parkour>", args[1]));
                        }
                    }
                    else if(args[0].equalsIgnoreCase("create")) {
                        if(!ParkourHandler.isParkour(args[1])) {
                            ParkourHandler.createParkour(player, args[1]);
                        }
                        else {
                            player.sendMessage(InfoKeeper.parkourAlreadyExist.replaceAll("<parkour>", args[1]));
                        }
                    }
                    else if(args[0].equalsIgnoreCase("remove")) {
                        if(ParkourHandler.isParkour(args[1])) {
                            ParkourHandler.removeParkour(player, ParkourHandler.getParkour(args[1]));
                        }
                        else {
                            player.sendMessage(InfoKeeper.parkourDontExist.replaceAll("<parkour>", args[1]));
                        }
                    }
                    else if(args[0].equalsIgnoreCase("setjoin")) {
                        if(ParkourHandler.isParkour(args[1])) {
                            ParkourHandler.setJoinLocation(player, ParkourHandler.getParkour(args[1]));
                        }
                        else {
                            player.sendMessage(InfoKeeper.parkourDontExist.replaceAll("<parkour>", args[1]));
                        }
                    }
                    else if(args[0].equalsIgnoreCase("setstart")) {
                        if(ParkourHandler.isParkour(args[1])) {
                            ParkourHandler.setStartLocation(player, ParkourHandler.getParkour(args[1]));
                        }
                        else {
                            player.sendMessage(InfoKeeper.parkourDontExist.replaceAll("<parkour>", args[1]));
                        }
                    }
                    else if(args[0].equalsIgnoreCase("setfinish")) {
                        if(ParkourHandler.isParkour(args[1])) {
                            ParkourHandler.setFinishLocation(player, ParkourHandler.getParkour(args[1]));
                        }
                        else {
                            player.sendMessage(InfoKeeper.parkourDontExist.replaceAll("<parkour>", args[1]));
                        }
                    }
                    else if(args[0].equalsIgnoreCase("createholo")) {
                        if(ParkourHandler.isParkour(args[1])) {
                            ParkourHandler.createHologram(player, ParkourHandler.getParkour(args[1]));
                        }
                        else {
                            player.sendMessage(InfoKeeper.parkourDontExist.replaceAll("<parkour>", args[1]));
                        }
                    }
                    else if(args[0].equalsIgnoreCase("removeholo")) {
                        if(ParkourHandler.isParkour(args[1])) {
                            ParkourHandler.removeHologram(player, ParkourHandler.getParkour(args[1]));
                        }
                        else {
                            player.sendMessage(InfoKeeper.parkourDontExist.replaceAll("<parkour>", args[1]));
                        }
                    }
                    else if(args[0].equalsIgnoreCase("updateholo")) {
                        if(ParkourHandler.isParkour(args[1])) {
                            Parkour parkour = ParkourHandler.getParkour(args[1]);
                            ParkourHandler.updateHologram(parkour);
                            player.sendMessage(ChatColor.GREEN + "Du oppdaterte hologram for " + ChatColor.DARK_GREEN + ChatColor.BOLD + parkour.getParkourName() + ChatColor.RESET + ChatColor.GREEN + " parkouren.");
                        }
                        else {
                            player.sendMessage(InfoKeeper.parkourDontExist.replaceAll("<parkour>", args[1]));
                        }
                    }
                    else {
                        MessageHandler.sendPlayerInfoMessage(player);
                    }
                }
                else {
                    MessageHandler.sendPlayerInfoMessage(player);
                }
            }
        }
        else {
            MessageHandler.sendPlayerInfoMessage(player);
        }

        return true;
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args){
        List<String> tabList = new ArrayList<String>();
        if (args.length == 1) {
            Player player = (Player) sender;
            List<String> arguments = new ArrayList<>();
            arguments.add("liste");arguments.add("tp");arguments.add("stats");arguments.add("info");arguments.add("create");arguments.add("remove");arguments.add("setJoin");
            arguments.add("setStart");arguments.add("setFinish");arguments.add("createHolo");arguments.add("removeHolo");arguments.add("updateHolo");
            if(player.hasPermission("spillere.admin")) {
                for(String s : arguments) {
                    if(s.toLowerCase().startsWith(args[0].toLowerCase())) {
                        tabList.add(s);
                    }
                }
            }
            else {
                tabList.add("liste");tabList.add("tp");tabList.add("stats");
            }
        }
        if (args.length == 2) {
            for (Parkour parkour : ParkourService.getParkours()) {
                String parkourname = parkour.getParkourName();
                if (parkourname.toLowerCase().startsWith(args[1].toLowerCase())) {
                    tabList.add(parkourname);
                }
            }
        }
        return tabList;
    }
}
