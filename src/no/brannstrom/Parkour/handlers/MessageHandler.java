package no.brannstrom.Parkour.handlers;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import no.brannstrom.Parkour.model.Parkour;
import no.brannstrom.Parkour.service.ParkourService;

public class MessageHandler {

	public static void sendPlayerInfoMessage(Player p) {
		p.sendMessage(ChatColor.BLUE + " ---- " + ChatColor.DARK_BLUE + "Parkour" +  ChatColor.BLUE + " ---- ");
		p.sendMessage(ChatColor.BLUE + "Bruk: '" + ChatColor.DARK_BLUE + "/parkour list" + ChatColor.BLUE + "' for å se en liste over alle parkours");
		p.sendMessage(ChatColor.BLUE + "Bruk: '" + ChatColor.DARK_BLUE + "/parkour stats <name>" + ChatColor.BLUE + "' for å se stats på en parkour");
		if(p.hasPermission("spillere.admin")) {
			p.sendMessage(ChatColor.BLUE + "Bruk: '" + ChatColor.DARK_BLUE + "/parkour <name>" + ChatColor.BLUE + "' for å se info om en parkour");
			p.sendMessage(ChatColor.BLUE + "Bruk: '" + ChatColor.DARK_BLUE + "/parkour create <name>" + ChatColor.BLUE + "' for å lage en parkour");
			p.sendMessage(ChatColor.BLUE + "Bruk: '" + ChatColor.DARK_BLUE + "/parkour remove <name>" + ChatColor.BLUE + "' for å fjerne en parkour");
			p.sendMessage(ChatColor.BLUE + "Bruk: '" + ChatColor.DARK_BLUE + "/parkour setjoin <name>" + ChatColor.BLUE + "' for å sette tp lokasjon på en parkour");
			p.sendMessage(ChatColor.BLUE + "Bruk: '" + ChatColor.DARK_BLUE + "/parkour setstart <name>" + ChatColor.BLUE + "' for å sette start lokasjon på en parkour");
			p.sendMessage(ChatColor.BLUE + "Bruk: '" + ChatColor.DARK_BLUE + "/parkour setfinish <name>" + ChatColor.BLUE + "' for å sette slutt lokasjon på en parkour");
		}
	}
	
	public static void sendList(Player p) {
		if(!ParkourService.getParkours().isEmpty()) {
		String parkourList = "";
		for(Parkour parkour : ParkourService.getParkours()) {
			parkourList += parkour.getName() + ", ";
		}		
		parkourList = parkourList.substring(0, parkourList.length() - 2);
		
		p.sendMessage(ChatColor.BLUE + " ---- " + ChatColor.DARK_BLUE + "Parkours " + ChatColor.BLUE + "(" + ChatColor.DARK_BLUE + ParkourService.getParkours().size() + ChatColor.BLUE + ") ---- ");
		p.sendMessage(parkourList);
		} else {
			p.sendMessage(InfoKeeper.noParkours);
		}
	}

	public static void showParkourInfo(Player p, Parkour parkour) {
		if(p.hasPermission("spillere.admin")) {
			p.sendMessage("Name: " + parkour.getName());
			p.sendMessage("Join Position: X:" + parkour.getJoinTeleport().getX() + " Y:" + parkour.getJoinTeleport().getY() + " Z:" + parkour.getJoinTeleport().getZ());
			p.sendMessage("Start Position: X:" + parkour.getStartPoint().getX() + " Y:" + parkour.getStartPoint().getY() + " Z:" + parkour.getStartPoint().getZ());
			p.sendMessage("Finish Position: X:" + parkour.getFinishPoint().getX() + " Y:" + parkour.getFinishPoint().getY() + " Z:" + parkour.getFinishPoint().getZ());
		} else {
			p.sendMessage(InfoKeeper.permission);
		}
	}
}
