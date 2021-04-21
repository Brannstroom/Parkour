package no.brannstrom.Parkour.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import no.brannstrom.Parkour.handlers.MessageHandler;
import no.brannstrom.Parkour.handlers.ParkourHandler;
import no.brannstrom.Parkour.service.ParkourService;

public class ParkourCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		Player p = (Player) sender;
		
		if(args.length == 1 && args[0].equalsIgnoreCase("list")) {
			MessageHandler.sendList(p);
		} else if(args.length == 1 && ParkourService.getParkour(args[0]) != null) {
			MessageHandler.showParkourInfo(p, ParkourService.getParkour(args[0]));
		} else if(args.length == 2) {
			if(args[0].equalsIgnoreCase("join") && ParkourService.getParkour(args[1]) != null) {
				ParkourHandler.joinParkour(p, ParkourService.getParkour(args[1]));
			} else if(args[0].equalsIgnoreCase("create") && ParkourService.getParkour(args[1]) == null) {
				ParkourHandler.createParkour(p, args[1]);
			} else if(args[0].equalsIgnoreCase("remove") && ParkourService.getParkour(args[1]) != null) {
				ParkourHandler.removeParkour(p, ParkourService.getParkour(args[1]));
			} else if(args[0].equalsIgnoreCase("setjoin") && ParkourService.getParkour(args[1]) != null) {
				ParkourHandler.setJoinLocation(p, ParkourService.getParkour(args[1]));
			} else if(args[0].equalsIgnoreCase("setstart") && ParkourService.getParkour(args[1]) != null) {
				ParkourHandler.setStartLocation(p, ParkourService.getParkour(args[1]));
			} else if(args[0].equalsIgnoreCase("setfinish") && ParkourService.getParkour(args[1]) != null) {
				ParkourHandler.setFinishLocation(p, ParkourService.getParkour(args[1]));
			} else if(args[0].equalsIgnoreCase("stats") && ParkourService.getParkour(args[1]) != null) {
				ParkourHandler.showStats(p, ParkourService.getParkour(args[1]));
			} else {
				MessageHandler.sendPlayerInfoMessage(p);
			}
		} else {
			MessageHandler.sendPlayerInfoMessage(p);
		}
		
		return true;
	}
}
