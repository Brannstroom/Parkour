package no.brannstrom.Parkour.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import no.brannstrom.Parkour.handlers.InfoKeeper;
import no.brannstrom.Parkour.handlers.MessageHandler;
import no.brannstrom.Parkour.handlers.ParkourHandler;

public class ParkourCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		Player p = (Player) sender;
		
		if(args.length == 1) {
			if(args[0].equalsIgnoreCase("list") || args[0].equalsIgnoreCase("liste")) {
				MessageHandler.sendList(p);
			}
		}
		else if(args.length == 2) {
			if(args[0].equalsIgnoreCase("stats") || args[0].equalsIgnoreCase("statistikk")) {
				if(ParkourHandler.isParkour(args[1])) {
					ParkourHandler.showStats(p, ParkourHandler.getParkour(args[1]));
				}
				else {
					p.sendMessage(InfoKeeper.parkourDontExist.replaceAll("<parkour>", args[1]));
				}
			}
			else {
				if(p.hasPermission("spillere.admin")) {
					if(args[0].equalsIgnoreCase("join")) {
						if(ParkourHandler.isParkour(args[1])) {
							ParkourHandler.joinParkour(p, ParkourHandler.getParkour(args[1]));
						}
						else {
							p.sendMessage(InfoKeeper.parkourDontExist.replaceAll("<parkour>", args[1]));
						}
					}
					else if(args[0].equalsIgnoreCase("create")) {
						if(!ParkourHandler.isParkour(args[1])) {
							ParkourHandler.createParkour(p, args[1]);
						}
						else {
							p.sendMessage(InfoKeeper.parkourAlreadyExist.replaceAll("<parkour>", args[1]));
						}
					}
					else if(args[0].equalsIgnoreCase("remove")) {
						if(ParkourHandler.isParkour(args[1])) {
							ParkourHandler.removeParkour(p, ParkourHandler.getParkour(args[1]));
						}
						else {
							p.sendMessage(InfoKeeper.parkourDontExist.replaceAll("<parkour>", args[1]));
						}
					}
					else if(args[0].equalsIgnoreCase("setjoin")) {
						if(ParkourHandler.isParkour(args[1])) {
							ParkourHandler.setJoinLocation(p, ParkourHandler.getParkour(args[1]));
						}
						else {
							p.sendMessage(InfoKeeper.parkourDontExist.replaceAll("<parkour>", args[1]));
						}
					}
					else if(args[0].equalsIgnoreCase("setstart")) {
						if(ParkourHandler.isParkour(args[1])) {
							ParkourHandler.setStartLocation(p, ParkourHandler.getParkour(args[1]));
						}
						else {
							p.sendMessage(InfoKeeper.parkourDontExist.replaceAll("<parkour>", args[1]));
						}
					}
					else if(args[0].equalsIgnoreCase("setfinish")) {
						if(ParkourHandler.isParkour(args[1])) {
							ParkourHandler.setFinishLocation(p, ParkourHandler.getParkour(args[1]));
						}
						else {
							p.sendMessage(InfoKeeper.parkourDontExist.replaceAll("<parkour>", args[1]));
						}
					}
					else if(args[0].equalsIgnoreCase("createholo")) {
						if(ParkourHandler.isParkour(args[1])) {
							ParkourHandler.createHologram(p, ParkourHandler.getParkour(args[1]));
						}
						else {
							p.sendMessage(InfoKeeper.parkourDontExist.replaceAll("<parkour>", args[1]));
						}
					}
					else if(args[0].equalsIgnoreCase("removeholo")) {
						if(ParkourHandler.isParkour(args[1])) {
							ParkourHandler.removeHologram(p, ParkourHandler.getParkour(args[1]));
						}
						else {
							p.sendMessage(InfoKeeper.parkourDontExist.replaceAll("<parkour>", args[1]));
						}
					}
					else {
						MessageHandler.sendPlayerInfoMessage(p);
					}
				}
				else {
					MessageHandler.sendPlayerInfoMessage(p);
				}
			}
		}
		else {
			MessageHandler.sendPlayerInfoMessage(p);
		}
		
		return true;
	}
}
