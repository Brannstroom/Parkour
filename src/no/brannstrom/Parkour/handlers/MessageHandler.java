package no.brannstrom.Parkour.handlers;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import no.brannstrom.Parkour.model.Parkour;
import no.brannstrom.Parkour.model.Serialize;
import no.brannstrom.Parkour.service.ParkourService;

public class MessageHandler {

	public static void sendPlayerInfoMessage(Player p) {
		p.sendMessage(ChatColor.DARK_GRAY + "--------------" + ChatColor.GOLD + "{ Parkour }" + ChatColor.DARK_GRAY + "-------------");
		p.sendMessage(ChatColor.GRAY + "Bruk: " + ChatColor.YELLOW + "/parkour list" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + " for å se en liste over alle parkours");
		p.sendMessage(ChatColor.GRAY + "Bruk: " + ChatColor.YELLOW + "/parkour stats <navn>" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + " for å se stats på en parkour");
		if(p.hasPermission("spillere.admin")) {
			p.sendMessage(ChatColor.GRAY + "Bruk: " + ChatColor.YELLOW + "/parkour tp <navn>" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + " for å tp til en parkour");
			p.sendMessage(ChatColor.GRAY + "Bruk: " + ChatColor.YELLOW + "/parkour info <navn>" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + " for å se info om en parkour");
			p.sendMessage(ChatColor.GRAY + "Bruk: " + ChatColor.YELLOW + "/parkour create <navn>" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + " for å lage en parkour");
			p.sendMessage(ChatColor.GRAY + "Bruk: " + ChatColor.YELLOW + "/parkour remove <navn>" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + " for å fjerne en parkour");
			p.sendMessage(ChatColor.GRAY + "Bruk: " + ChatColor.YELLOW + "/parkour setjoin <navn>" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + " for å sette tp lokasjon på en parkour");
			p.sendMessage(ChatColor.GRAY + "Bruk: " + ChatColor.YELLOW + "/parkour setstart <navn>" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + " for å sette start lokasjon på en parkour");
			p.sendMessage(ChatColor.GRAY + "Bruk: " + ChatColor.YELLOW + "/parkour setfinish <navn>" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + " for å sette slutt lokasjon på en parkour");
			p.sendMessage(ChatColor.GRAY + "Bruk: " + ChatColor.YELLOW + "/parkour createholo <navn>" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + " for å lage hologram for top 10");
			p.sendMessage(ChatColor.GRAY + "Bruk: " + ChatColor.YELLOW + "/parkour removeholo <navn>" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + " for å fjerne hologram");
			p.sendMessage(ChatColor.GRAY + "Bruk: " + ChatColor.YELLOW + "/parkour updateholo <navn>" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + " oppdaterer holo om den sliter");
		}
		p.sendMessage(ChatColor.DARK_GRAY + "-------------------------------------");
	}

	public static void sendList(Player p) {
		if(!ParkourService.getParkours().isEmpty()) {
			String parkourList = "";
			List<Parkour> parkours = ParkourService.getParkours();
			for(Parkour parkour : parkours) {
				parkourList += parkour.getName() + ", ";
			}
			parkourList = parkourList.substring(0, parkourList.length() - 2);

			p.sendMessage(ChatColor.DARK_GRAY + "--------------" + ChatColor.GOLD + "{ Parkours (" + parkours.size() + ") }" + ChatColor.DARK_GRAY + "-------------");
			p.sendMessage(parkourList);

		}
		else {
			p.sendMessage(ChatColor.DARK_GRAY + "--------------" + ChatColor.GOLD + "{ Parkours }" + ChatColor.DARK_GRAY + "-------------");
			p.sendMessage(ChatColor.RED + "Det er enda ikke laget noen parkours.");
		}
	}

	public static void showParkourInfo(Player p, Parkour parkour) {
		p.sendMessage(ChatColor.GREEN + "Parkour navn: " + ChatColor.RESET + parkour.getName());
		Serialize serialize = new Serialize();
		Location joinLocation = serialize.deserialize(parkour.getJoinLocation());
		Location startLocation = serialize.deserialize(parkour.getStartLocation());
		Location finishLocation = serialize.deserialize(parkour.getFinishLocation());

		p.sendMessage(ChatColor.GREEN + "Join Location: X:" + ChatColor.RESET + joinLocation.getBlockX() + ChatColor.GREEN + " Y:" + ChatColor.RESET + joinLocation.getBlockY() + ChatColor.GREEN +  " Z:" + ChatColor.RESET + joinLocation.getBlockZ());
		p.sendMessage(ChatColor.GREEN + "Start Location: X:" + ChatColor.RESET + startLocation.getBlockX() + ChatColor.GREEN + " Y:" + ChatColor.RESET + startLocation.getBlockY() + ChatColor.GREEN +  " Z:" + ChatColor.RESET + startLocation.getBlockZ());
		p.sendMessage(ChatColor.GREEN + "Finish Location: X:" + ChatColor.RESET + finishLocation.getBlockX() + ChatColor.GREEN + " Y:" + ChatColor.RESET + finishLocation.getBlockY() + ChatColor.GREEN +  " Z:" + ChatColor.RESET + finishLocation.getBlockZ());
	}

}
