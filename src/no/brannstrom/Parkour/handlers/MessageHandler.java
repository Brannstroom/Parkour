package no.brannstrom.Parkour.handlers;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import no.brannstrom.Parkour.model.Parkour;
import no.brannstrom.Parkour.model.SParkour;
import no.brannstrom.Parkour.service.ParkourService;

public class MessageHandler {

	public static void sendPlayerInfoMessage(Player p) {
		p.sendMessage(ChatColor.BLUE + " ---- " + ChatColor.DARK_BLUE + "Parkour" +  ChatColor.BLUE + " ---- ");
		p.sendMessage(ChatColor.BLUE + "Bruk: '" + ChatColor.DARK_BLUE + "/parkour list" + ChatColor.BLUE + "' for å se en liste over alle parkours");
		p.sendMessage(ChatColor.BLUE + "Bruk: '" + ChatColor.DARK_BLUE + "/parkour stats <navn>" + ChatColor.BLUE + "' for å se stats på en parkour");
		if(p.hasPermission("spillere.admin")) {
			p.sendMessage(ChatColor.BLUE + "Bruk: '" + ChatColor.DARK_BLUE + "/parkour <navn>" + ChatColor.BLUE + "' for å se info om en parkour");
			p.sendMessage(ChatColor.BLUE + "Bruk: '" + ChatColor.DARK_BLUE + "/parkour join <navn>" + ChatColor.BLUE + "' for å joine en parkour");
			p.sendMessage(ChatColor.BLUE + "Bruk: '" + ChatColor.DARK_BLUE + "/parkour create <navn>" + ChatColor.BLUE + "' for å lage en parkour");
			p.sendMessage(ChatColor.BLUE + "Bruk: '" + ChatColor.DARK_BLUE + "/parkour remove <navn>" + ChatColor.BLUE + "' for å fjerne en parkour");
			p.sendMessage(ChatColor.BLUE + "Bruk: '" + ChatColor.DARK_BLUE + "/parkour setjoin <navn>" + ChatColor.BLUE + "' for å sette tp lokasjon på en parkour");
			p.sendMessage(ChatColor.BLUE + "Bruk: '" + ChatColor.DARK_BLUE + "/parkour setstart <navn>" + ChatColor.BLUE + "' for å sette start lokasjon på en parkour");
			p.sendMessage(ChatColor.BLUE + "Bruk: '" + ChatColor.DARK_BLUE + "/parkour setfinish <navn>" + ChatColor.BLUE + "' for å sette slutt lokasjon på en parkour");
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

	public static void showParkourInfo(Player p, SParkour parkour) {
		if(p.hasPermission("spillere.admin")) {
			p.sendMessage("Name: " + parkour.getName());
			p.sendMessage("Join Position: X:" + parkour.getJoinLoc().getBlockX() + " Y:" + parkour.getJoinLoc().getBlockY() + " Z:" + parkour.getJoinLoc().getBlockZ());
			p.sendMessage("Start Position: X:" + parkour.getStartLoc().getBlockX() + " Y:" + parkour.getStartLoc().getBlockY() + " Z:" + parkour.getStartLoc().getBlockZ());
			p.sendMessage("Finish Position: X:" + parkour.getFinishLoc().getBlockX() + " Y:" + parkour.getFinishLoc().getBlockY() + " Z:" + parkour.getFinishLoc().getBlockZ());
		} else {
			p.sendMessage(InfoKeeper.permission);
		}
	}
}
