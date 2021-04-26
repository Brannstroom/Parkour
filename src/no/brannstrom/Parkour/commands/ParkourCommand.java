package no.brannstrom.Parkour.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import no.brannstrom.Parkour.handlers.MessageHandler;
import no.brannstrom.Parkour.handlers.ParkourHandler;

public class ParkourCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		Player p = (Player) sender;
		
		if(args.length == 1 && args[0].equalsIgnoreCase("list")) {
			MessageHandler.sendList(p);
		} else if(args.length == 1 && ParkourHandler.getParkour(args[0]) != null) {
			MessageHandler.showParkourInfo(p, ParkourHandler.getParkour(args[0]));
		} else if(args.length == 2) {
			if(args[0].equalsIgnoreCase("join") && ParkourHandler.isParkour(args[1])) {
				ParkourHandler.joinParkour(p, ParkourHandler.getParkour(args[1]));
			} else if(args[0].equalsIgnoreCase("create") && ParkourHandler.getParkour(args[1]) == null) {
				Bukkit.broadcastMessage("1");
				ParkourHandler.createParkour(p, args[1]);
				Bukkit.broadcastMessage("2");
			} else if(args[0].equalsIgnoreCase("remove") && ParkourHandler.getParkour(args[1]) != null) {
				ParkourHandler.removeParkour(p, ParkourHandler.getParkour(args[1]));
			} else if(args[0].equalsIgnoreCase("setjoin") && ParkourHandler.getParkour(args[1]) != null) {
				ParkourHandler.setJoinLocation(p, ParkourHandler.getParkour(args[1]));
			} else if(args[0].equalsIgnoreCase("setstart") && ParkourHandler.getParkour(args[1]) != null) {
				ParkourHandler.setStartLocation(p, ParkourHandler.getParkour(args[1]));
			} else if(args[0].equalsIgnoreCase("setfinish") && ParkourHandler.getParkour(args[1]) != null) {
				ParkourHandler.setFinishLocation(p, ParkourHandler.getParkour(args[1]));
			} else if(args[0].equalsIgnoreCase("stats") && ParkourHandler.getParkour(args[1]) != null) {
				ParkourHandler.showStats(p, ParkourHandler.getParkour(args[1]));
			} else {
				MessageHandler.sendPlayerInfoMessage(p);
			}
		} else {
			MessageHandler.sendPlayerInfoMessage(p);
		}
		
		return true;
	}
}
