package no.brannstrom.Parkour.handlers;

import org.bukkit.entity.Player;

import no.brannstrom.Parkour.model.Parkour;
import no.brannstrom.Parkour.model.ParkourStats;

public class MessageHandler {

	public static void sendPlayerInfoMessage(Player p) {
		p.sendMessage("Parkour");
		p.sendMessage("Use: '/parkour list' to list all parkours");
		p.sendMessage("Use: '/parkour join <name>' to join a parkour");
		p.sendMessage("Use: '/parkour stats <name>' to see stats");
		if(p.hasPermission(InfoKeeper.adminPermission)) {
			p.sendMessage("Use: '/parkour <name>' to get info about a parkour");
			p.sendMessage("Use: '/parkour create <name>' to create a parkour");
			p.sendMessage("Use: '/parkour remove <name>' to remove a parkour");
			p.sendMessage("Use: '/parkour setjoin <name>' to set teleport location for parkour");
			p.sendMessage("Use: '/parkour setstart <name>' to set a parkours start point");
			p.sendMessage("Use: '/parkour setfinish <name>' to set a parkours finish point");
		}
	}
	
	public static void sendList(Player p) {
		if(!MemoryHandler.parkours.isEmpty()) {
		String parkourList = "";
		for(Parkour parkour : MemoryHandler.parkours) {
			parkourList += parkour.getName() + ", ";
		}		
		parkourList = parkourList.substring(0, parkourList.length() - 2);
		
		p.sendMessage("Parkour List");
		p.sendMessage(parkourList);
		} else {
			p.sendMessage(InfoKeeper.noParkours);
		}
	}

	public static void showParkourInfo(Player p, Parkour parkour) {
		if(p.hasPermission(InfoKeeper.adminPermission)) {
			p.sendMessage(parkourTime(p, parkour));
			p.sendMessage("Name: " + parkour.getName());
			p.sendMessage("Join Position: X:" + parkour.getJoinTeleport().getX() + " Y:" + parkour.getJoinTeleport().getY() + " Z:" + parkour.getJoinTeleport().getZ());
			p.sendMessage("Start Position: X:" + parkour.getStartPoint().getX() + " Y:" + parkour.getStartPoint().getY() + " Z:" + parkour.getStartPoint().getZ());
			p.sendMessage("Finish Position: X:" + parkour.getFinishPoint().getX() + " Y:" + parkour.getFinishPoint().getY() + " Z:" + parkour.getFinishPoint().getZ());
		} else {
			p.sendMessage(parkourTime(p, parkour));
		}
	}
	
	public static String parkourTime(Player p, Parkour parkour) {
		ParkourStats parkourStats = null;
		String returning = "";
		if(MemoryHandler.parkourStats.containsKey(p.getUniqueId().toString())) {
			parkourStats = MemoryHandler.parkourStats.get(p.getUniqueId().toString());
			
			if(parkourStats.getNames().contains(parkour.getName())) {
				returning = parkour.getName() + " : " + parkourStats.getTimes().get(parkourStats.getNames().indexOf(parkour.getName()));
			} else {
				returning = "You have not finished this parkour";
			}
		} else {
			returning = "You have not finished any parkour";
		}
		
		return returning;
	}
	
}
