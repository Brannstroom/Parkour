package no.brannstrom.Parkour.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import no.brannstrom.Parkour.handlers.InfoKeeper;
import no.brannstrom.Parkour.handlers.MainHandler;
import no.brannstrom.Parkour.handlers.MemoryHandler;
import no.brannstrom.Parkour.handlers.MessageHandler;
import no.brannstrom.Parkour.handlers.ParkourHandler;
import no.brannstrom.Parkour.model.Parkour;
import no.brannstrom.Parkour.service.ParkourService;

public class ParkourCommand implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		Player player = (Player) sender;

		if(args.length == 1) {
			if(args[0].equalsIgnoreCase("list") || args[0].equalsIgnoreCase("liste")) {
				MessageHandler.sendList(player);
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
							player.sendMessage(ChatColor.GREEN + "Du oppdaterte hologram for " + ChatColor.DARK_GREEN + ChatColor.BOLD + parkour.getName() + ChatColor.RESET + ChatColor.GREEN + " parkouren.");
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
		if (args.length == 2) {
			for (Parkour parkour : ParkourService.getParkours()) {
				String homeName = parkour.getName();
				if (homeName.toLowerCase().startsWith(args[0].toLowerCase())) {
					tabList.add(homeName);
				}
			}
		}
		return tabList; 
	}
}
