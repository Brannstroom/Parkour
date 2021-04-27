package no.brannstrom.Parkour.handlers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import no.brannstrom.Parkour.model.Parkour;
import no.brannstrom.Parkour.model.ParkourPlayer;
import no.brannstrom.Parkour.model.ParkourStats;
import no.brannstrom.Parkour.model.Serialize;
import no.brannstrom.Parkour.model.User;
import no.brannstrom.Parkour.service.ParkourService;
import no.brannstrom.Parkour.service.ParkourStatsService;
import no.brannstrom.Parkour.service.UserService;

public class ParkourHandler {

	public static void joinParkour(Player p, Parkour parkour) {
		p.sendMessage(InfoKeeper.parkourJoined.replaceAll("<parkour>", parkour.getName()));
		Serialize serialize = new Serialize();
		p.teleport(serialize.deserialize(parkour.getJoinLocation()));
	}

	public static void startParkour(Player p, ParkourPlayer parkourPlayer) {
		MemoryHandler.parkourPlayers.put(p.getUniqueId().toString(), parkourPlayer);
		MainHandler.sendActionBar(p, InfoKeeper.parkourStartedHotbar);
	}

	public static void finishParkour(Player p, ParkourPlayer parkourPlayer) {
		if(MemoryHandler.parkourPlayers.containsKey(p.getUniqueId().toString())) {
			MemoryHandler.parkourPlayers.remove(p.getUniqueId().toString());
		}

		Parkour parkour = parkourPlayer.getParkour();
		long time = System.currentTimeMillis()-parkourPlayer.getStartTime();

		Bukkit.broadcastMessage("Parkour finished: " + parkour.getName());
		ParkourStats parkourRecord = ParkourStatsService.getParkourRecord(parkour);
		Bukkit.broadcastMessage("1");
		if(parkourRecord != null) {
			Bukkit.broadcastMessage("PakrourRecord: " + parkourRecord.getParkourName() + " | Time: "+ parkourRecord.getParkourTime());
			Bukkit.broadcastMessage("2");
			if(time < parkourRecord.getParkourTime()) {
				Bukkit.broadcastMessage("3");
				User user = UserService.getUser(p.getUniqueId());
				//				MainHandler.broadcast(InfoKeeper.newParkourRecord.replaceAll("<player>", MainHandler.getPrefixName(user)).replaceAll("<parkour>", parkour.getName()).replaceAll("<time>", new SimpleDateFormat("mm:ss:SSS").format(new Date(time))).replaceAll("<improvement>", new SimpleDateFormat("mm:ss:SSS").format(new Date(parkourRecord.getParkourTime()-time))));
				MainHandler.broadcast("Hei 123");
			}
		}
		else {
			User user = UserService.getUser(p.getUniqueId());
			//			MainHandler.broadcast(InfoKeeper.firstRecord.replaceAll("<player>", MainHandler.getPrefixName(user)).replaceAll("<parkour>", parkour.getName()).replaceAll("<time>", new SimpleDateFormat("mm:ss:SSS").format(new Date(time))));
			MainHandler.broadcast("Hei 1");
		}

		ParkourStats parkourStats = new ParkourStats();
		parkourStats.setUuid(parkourPlayer.getUuid());
		parkourStats.setParkourName(parkour.getName());
		parkourStats.setParkourTime(time);

		Bukkit.broadcastMessage("2.1");
		ParkourStats personalBest = ParkourStatsService.getPersonalBest(parkourPlayer.getUuid(), parkour.getName());
		Bukkit.broadcastMessage("New time: " + time);
		Bukkit.broadcastMessage("2.2");
		if(personalBest != null) {
			Bukkit.broadcastMessage("PB: " + personalBest.getParkourName() + " | Time: " + personalBest.getParkourTime());
			Bukkit.broadcastMessage("2.3");
			if(time < personalBest.getParkourTime()) {
				Bukkit.broadcastMessage("2.4");
				sendFinishMessageImproved(p, parkour, personalBest.getParkourTime(), time);
			} else {
				Bukkit.broadcastMessage("2.5");
				sendFinishMessageUnimproved(p,parkour,time);
			}
		}
		else {
			Bukkit.broadcastMessage("2.6");
			sendFirstTimeFinish(p,parkour,time);
		}

		MainHandler.sendActionBar(p, InfoKeeper.parkourFinishedHotbar);
		ParkourStatsService.update(parkourStats);
	}

	public static void stopParkour() {

	}

	public static void showStats(Player p, Parkour parkour) {
		int i = 0;
		if(ParkourStatsService.getTop10(parkour).isEmpty()) {
			for (ParkourStats stats : ParkourStatsService.getTop10(parkour)){
				i++;
				UUID uuid = stats.getUuid();
				User user = UserService.getUser(uuid);
				String name = "anonym";
				if (user != null) name = MainHandler.getPrefixName(user);
				p.sendMessage(ChatColor.YELLOW + "" + i + ". " + ChatColor.WHITE + name + ChatColor.GRAY + " » " + ChatColor.DARK_GREEN + MainHandler.formatTime(stats.getParkourTime()) + ChatColor.GREEN + ".");
			}
			Integer placement = ParkourStatsService.getParkourPlacement(p.getUniqueId(), parkour.getName());
			if(placement > 10) {
				ParkourStats stats = ParkourStatsService.getPersonalBest(p.getUniqueId(), parkour.getName());
				p.sendMessage(ChatColor.YELLOW + "" + i + ". " + ChatColor.WHITE + MainHandler.getPrefixName(UserService.getUser(p.getUniqueId())) + ChatColor.GRAY + " » " + ChatColor.DARK_GREEN + MainHandler.formatTime(stats.getParkourTime()) + ChatColor.GREEN + ".");
			}
		}
		else {
			p.sendMessage(ChatColor.RED + "Ingen har fullført " + ChatColor.DARK_RED + parkour.getName() + ChatColor.RED + " parkouren. Bli den første!");
		}
	}

	public static boolean isParkour(String name) {
		boolean isParkour = false;
		for(Parkour parkour : ParkourService.getParkours()) {
			if(parkour.getName().equalsIgnoreCase(name)) {
				isParkour = true;
			}
		}
		return isParkour;
	}

	public static Parkour getParkour(String name) {
		if(!ParkourService.getParkours().isEmpty()) {
			for(Parkour parkour : ParkourService.getParkours()) {
				if(parkour.getName().equalsIgnoreCase(name)) {
					return parkour;
				}
			}
		}
		return null;
	}

	public static Parkour getParkourByStart(Location bLoc) {
		for(Parkour parkour : ParkourService.getParkours()) {
			Serialize serialize = new Serialize();
			Location pLoc = serialize.deserialize(parkour.getStartLocation());

			if(bLoc.getBlockX() == pLoc.getBlockX() && bLoc.getBlockY() == pLoc.getBlockY() && bLoc.getBlockZ() == pLoc.getBlockZ()) {
				return parkour;
			}
		}
		return null;
	}

	public static Parkour getParkourByFinish(Location bLoc) {
		if(!ParkourService.getParkours().isEmpty()) {
			for(Parkour parkour : ParkourService.getParkours()) {
				Serialize serialize = new Serialize();
				Location pLoc = serialize.deserialize(parkour.getFinishLocation());

				if(bLoc.getBlockX() == pLoc.getBlockX() && bLoc.getBlockY() == pLoc.getBlockY() && bLoc.getBlockZ() == pLoc.getBlockZ()) {
					return parkour;
				}
			}
		}
		return null;
	}

	public static void createParkour(Player p, String name) {
		Parkour parkour = new Parkour();
		parkour.setName(name);
		Location loc = p.getLocation();

		Serialize serialize = new Serialize();
		String location = serialize.serialize(loc);

		parkour.setJoinLocation(location);
		parkour.setStartLocation(location);
		parkour.setFinishLocation(location);

		ParkourService.update(parkour);

		p.sendMessage(InfoKeeper.parkourCreated.replaceAll("<parkour>", name));
	}

	public static void removeParkour(Player p, Parkour parkour) {

		ParkourService.deleteParkour(parkour);

		p.sendMessage(InfoKeeper.parkourRemoved.replaceAll("<parkour>", parkour.getName()));
	}

	public static boolean isStartPressurePlate(Block b) {
		boolean isStartPressurePlate = false;
		if(!ParkourService.getParkours().isEmpty()) {
			for(Parkour parkour : ParkourService.getParkours()) {
				Location bLoc = b.getLocation();

				Serialize serialize = new Serialize();
				Location pLoc = serialize.deserialize(parkour.getStartLocation());

				if(bLoc.getBlockX() == pLoc.getBlockX() && bLoc.getBlockY() == pLoc.getBlockY() && bLoc.getBlockZ() == pLoc.getBlockZ()) {
					isStartPressurePlate = true;
				}
			}
		}
		return isStartPressurePlate;
	}

	public static boolean isFinishPressurePlate(Block b) {
		boolean isFinishPressurePlate = false;
		if(!ParkourService.getParkours().isEmpty()) {
			for(Parkour parkour : ParkourService.getParkours()) {
				Location bLoc = b.getLocation();

				Serialize serialize = new Serialize();
				Location pLoc = serialize.deserialize(parkour.getFinishLocation());

				if(bLoc.getBlockX() == pLoc.getBlockX() && bLoc.getBlockY() == pLoc.getBlockY() && bLoc.getBlockZ() == pLoc.getBlockZ()) {
					isFinishPressurePlate = true;
				}
			}
		}
		return isFinishPressurePlate;
	}

	public static void setJoinLocation(Player p, Parkour parkour) {
		if(p.hasPermission("spillere.admin")) {
			Serialize serialize = new Serialize();
			parkour.setJoinLocation(serialize.serialize(p.getLocation()));
			ParkourService.update(parkour);
			p.sendMessage(InfoKeeper.setParkourJoinLocation.replaceAll("<parkour>", parkour.getName()));
		}
		else {
			p.sendMessage(InfoKeeper.permission);
		}
	}

	public static void setStartLocation(Player p, Parkour parkour) {
		if(p.hasPermission("spillere.admin")) {
			Serialize serialize = new Serialize();
			parkour.setStartLocation(serialize.serialize(p.getLocation()));
			ParkourService.update(parkour);
			p.sendMessage(InfoKeeper.setParkourStartLocation.replaceAll("<parkour>", parkour.getName()));
		}
		else {
			p.sendMessage(InfoKeeper.permission);
		}
	}

	public static void setFinishLocation(Player p, Parkour parkour) {
		if(p.hasPermission("spillere.admin")) {
			Serialize serialize = new Serialize();
			parkour.setFinishLocation(serialize.serialize(p.getLocation()));
			ParkourService.update(parkour);
			p.sendMessage(InfoKeeper.setParkourFinishLocation.replaceAll("<parkour>", parkour.getName()));
		}
		else {
			p.sendMessage(InfoKeeper.permission);
		}
	}

	public static void sendFinishMessageUnimproved(Player p, Parkour parkour, long time) {
		p.sendMessage(InfoKeeper.finishedParkour.replaceAll("<parkour>", parkour.getName()).replaceAll("<time>", new SimpleDateFormat("mm:ss:SSS").format(new Date(time))));
	}

	public static void sendFinishMessageImproved(Player p, Parkour parkour, long previousTime, long newTime) {
		p.sendMessage(InfoKeeper.improvedTime.replaceAll("<parkour>", parkour.getName()).replaceAll("<time>", new SimpleDateFormat("mm:ss:SSS").format(new Date(newTime))).replaceAll("<improvement>", new SimpleDateFormat("mm:ss:SSS").format(new Date(previousTime-newTime))));
	}

	public static void sendFirstTimeFinish(Player p, Parkour parkour, long time) {
		p.sendMessage(InfoKeeper.firstTimeFinishingParkour.replaceAll("<parkour>", parkour.getName()).replaceAll("<time>", new SimpleDateFormat("mm:ss:SSS").format(new Date(time))));
	}
}
