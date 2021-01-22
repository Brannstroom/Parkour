package no.brannstrom.Parkour.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import no.brannstrom.Parkour.dao.ParkourStatsRepo;
import no.brannstrom.Parkour.model.Parkour;
import no.brannstrom.Parkour.model.ParkourPlayer;
import no.brannstrom.Parkour.model.ParkourStats;

public class ParkourHandler {

	public static void joinParkour(Player p, Parkour parkour) {
		p.sendMessage(InfoKeeper.parkourJoined.replaceAll("<parkour>", parkour.getName()));
		p.teleport(parkour.getJoinTeleport());
	}

	public static void startParkour(Player p, ParkourPlayer parkourPlayer) {
		MemoryHandler.parkourPlayers.put(p.getUniqueId().toString(), parkourPlayer);
		MainHandler.sendActionBar(p, InfoKeeper.parkourStartedHotbar);
	}

	public static void finishParkour(Player p, ParkourPlayer parkourPlayer) {
		if(MemoryHandler.parkourPlayers.containsKey(p.getUniqueId().toString())) {
			MemoryHandler.parkourPlayers.remove(p.getUniqueId().toString());
		}

		long time = System.currentTimeMillis()-parkourPlayer.getStartTime();
		
		Parkour parkour = parkourPlayer.getParkour();
		Bukkit.broadcastMessage("1");
		ParkourStats pStats = null;
		if(MemoryHandler.parkourStats.containsKey(p.getUniqueId().toString())) {
			Bukkit.broadcastMessage("2");
			pStats = MemoryHandler.parkourStats.get(p.getUniqueId().toString());
			if(hasFinishedParkourBefore(p, parkour)) {
				Bukkit.broadcastMessage("3");
				long previousTime = pStats.getTimes().get(pStats.getNames().indexOf(parkour.getName()));
				Bukkit.broadcastMessage("4");
				if(time < previousTime) {
					Bukkit.broadcastMessage("5");
					sendFinishMessageImproved(p, parkour, previousTime, time);
					Bukkit.broadcastMessage("6");
					pStats.getTimes().set(pStats.getNames().indexOf(parkour.getName()), time);
					Bukkit.broadcastMessage("7");
				} else {
					Bukkit.broadcastMessage("8");
					sendFinishMessageUnimproved(p, parkour, time);
					Bukkit.broadcastMessage("9");	
				}
			} else {
				Bukkit.broadcastMessage("10");
				sendFinishMessageUnimproved(p, parkour, time);
				Bukkit.broadcastMessage("11");
			}
		} else {
			Bukkit.broadcastMessage("12");
			pStats = new ParkourStats();
			List<String> names = new ArrayList<>();
			List<Long> times = new ArrayList<>();
			names.add(parkour.getName());
			times.add(time);
			pStats.setNames(names);
			pStats.setTimes(times);
			Bukkit.broadcastMessage("13");
			MemoryHandler.parkourStats.put(p.getUniqueId().toString(), pStats);
			Bukkit.broadcastMessage("14");
			sendFinishMessageUnimproved(p, parkour, time);
			Bukkit.broadcastMessage("15");
		}

		MainHandler.sendActionBar(p, InfoKeeper.parkourFinishedHotbar);
	}

	public static void stopParkour() {

	}

	public static void showStats(Player p, Parkour parkour) {
		ParkourStats pStats = MemoryHandler.parkourStats.get(p.getUniqueId().toString());
		if(pStats != null) {
			p.sendMessage(parkour.getName() + " with the time " + MainHandler.formatTime(pStats.getTimes().get(pStats.getNames().indexOf(parkour.getName()))));
		} else {
			p.sendMessage("You havent finished the " + parkour.getName() + " parkour.");
		}
	}

	public static boolean isParkour(String name) {
		boolean isParkour = false;
		for(Parkour parkour : MemoryHandler.parkours) {
			if(parkour.getName().equalsIgnoreCase(name)) {
				isParkour = true;
			}
		}
		return isParkour;
	}

	public static Parkour getParkour(String name) {
		for(Parkour parkour : MemoryHandler.parkours) {
			if(parkour.getName().equalsIgnoreCase(name)) {
				return parkour;
			}
		}
		return null;
	}

	public static Parkour getParkourByStart(Location bLoc) {
		for(Parkour parkour : MemoryHandler.parkours) {
			Location pLoc = parkour.getStartPoint();

			if(bLoc.getBlockX() == pLoc.getBlockX() && bLoc.getBlockY() == pLoc.getBlockY() && bLoc.getBlockZ() == pLoc.getBlockZ()) {
				return parkour;
			}
		}
		return null;
	}

	public static Parkour getParkourByFinish(Location bLoc) {
		for(Parkour parkour : MemoryHandler.parkours) {
			Location pLoc = parkour.getFinishPoint();

			if(bLoc.getBlockX() == pLoc.getBlockX() && bLoc.getBlockY() == pLoc.getBlockY() && bLoc.getBlockZ() == pLoc.getBlockZ()) {
				return parkour;
			}
		}
		return null;
	}

	public static void createParkour(Player p, String name) {
		Parkour parkour = new Parkour();
		parkour.setName(name);
		Location loc = p.getLocation();
		parkour.setJoinTeleport(loc);
		parkour.setStartPoint(loc);
		parkour.setFinishPoint(loc);

		MemoryHandler.parkours.add(parkour);

		p.sendMessage(InfoKeeper.parkourCreated.replaceAll("{0}", name));
	}
	
	public static void removeParkour(Player p, Parkour parkour) {
		List<Parkour> parkours = MemoryHandler.parkours;
		parkours.remove(parkour);
		
		for(Entry<String, ParkourStats> entry : MemoryHandler.parkourStats.entrySet()) {
			String uuid = entry.getKey();
			ParkourStats pStats = entry.getValue();
			if(hasFinishedParkourBefore(uuid, parkour)) {
				pStats.getTimes().remove(pStats.getNames().indexOf(parkour.getName()));
				pStats.getNames().remove(parkour.getName());
			}
		}
		p.sendMessage(InfoKeeper.parkourRemoved.replaceAll("{0}", parkour.getName()));
	}

	public static void setJoinLocation(Player p, Parkour parkour) {
		parkour.setJoinTeleport(p.getLocation());
		p.sendMessage(InfoKeeper.setParkourJoinLocation.replaceAll("{0}", parkour.getName()));
	}

	public static void setStartLocation(Player p, Parkour parkour) {
		parkour.setStartPoint(p.getLocation());
		p.sendMessage(InfoKeeper.setParkourStartLocation.replaceAll("{0}", parkour.getName()));
	}

	public static void setFinishLocation(Player p, Parkour parkour) {
		parkour.setFinishPoint(p.getLocation());
		p.sendMessage(InfoKeeper.setParkourFinishLocation.replaceAll("{0}", parkour.getName()));
	}

	public static boolean isStartPressurePlate(Block b) {
		boolean isStartPressurePlate = false;
		for(Parkour parkour : MemoryHandler.parkours) {
			Location bLoc = b.getLocation();
			Location pLoc = parkour.getStartPoint();

			if(bLoc.getBlockX() == pLoc.getBlockX() && bLoc.getBlockY() == pLoc.getBlockY() && bLoc.getBlockZ() == pLoc.getBlockZ()) {
				isStartPressurePlate = true;
			}
		}
		return isStartPressurePlate;
	}

	public static boolean isFinishPressurePlate(Block b) {
		boolean isFinishPressurePlate = false;
		for(Parkour parkour : MemoryHandler.parkours) {
			Location bLoc = b.getLocation();
			Location pLoc = parkour.getFinishPoint();

			if(bLoc.getBlockX() == pLoc.getBlockX() && bLoc.getBlockY() == pLoc.getBlockY() && bLoc.getBlockZ() == pLoc.getBlockZ()) {
				isFinishPressurePlate = true;
			}
		}
		return isFinishPressurePlate;
	}
	
	public static boolean hasFinishedParkourBefore(Player p, Parkour parkour) {
		ParkourStats pStats = ParkourStatsRepo.getParkourStats(p);
		if(pStats.getNames() != null) {
			if(pStats.getNames().contains(parkour.getName())) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean hasFinishedParkourBefore(String uuid, Parkour parkour) {
		ParkourStats pStats = ParkourStatsRepo.getParkourStats(uuid);
		if(pStats.getNames() != null) {
			if(pStats.getNames().contains(parkour.getName())) {
				return true;
			}
		}
		return false;
	}
	
	public static void sendFinishMessageUnimproved(Player p, Parkour parkour, long time) {
		p.sendMessage(InfoKeeper.finishedParkour.replaceAll("{0}", parkour.getName()).replaceAll("{1}", MainHandler.formatTime(time)));
	}
	
	public static void sendFinishMessageImproved(Player p, Parkour parkour, long previousTime, long newTime) {
		p.sendMessage(InfoKeeper.improvedTime.replaceAll("{0}", parkour.getName()).replaceAll("{1}", MainHandler.formatTime(newTime)).replaceAll("{2}", MainHandler.formatTime(previousTime-newTime)));
	}
}
