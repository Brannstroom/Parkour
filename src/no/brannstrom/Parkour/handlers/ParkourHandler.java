package no.brannstrom.Parkour.handlers;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import no.brannstrom.Parkour.model.Parkour;
import no.brannstrom.Parkour.model.ParkourPlayer;

public class ParkourHandler {
	
	public static void joinParkour(Player p, Parkour parkour) {
		p.sendMessage(InfoKeeper.parkourJoined.replaceAll("<parkour>", parkour.getName()));
		p.teleport(parkour.getJoinTeleport());
	}
	
	public static void startParkour(Player p, ParkourPlayer parkourPlayer) {
		MemoryHandler.parkourPlayers.put(p.getUniqueId(), parkourPlayer);
	}
	
	public static void finishParkour(Player p, ParkourPlayer parkourPlayer) {
		if(MemoryHandler.parkourPlayers.containsKey(p.getUniqueId())) {
			MemoryHandler.parkourPlayers.remove(p.getUniqueId());
		}
		
		long time = System.currentTimeMillis()-parkourPlayer.getStartTime();
		String name = parkourPlayer.getParkour().getName();
		p.sendMessage(InfoKeeper.finishedParkour.replaceAll("<parkour>", name).replaceAll("<time>", String.valueOf(time)));
	}
	
	public static void stopParkour() {
		
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
	
	public static Parkour getParkourByStart(Location loc) {
		for(Parkour parkour : MemoryHandler.parkours) {
			if(parkour.getStartPoint().equals(loc)) {
				return parkour;
			}
		}
		return null;
	}
	
	public static Parkour getParkourByFinish(Location loc) {
		for(Parkour parkour : MemoryHandler.parkours) {
			if(parkour.getFinishPoint().equals(loc)) {
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
		
		p.sendMessage(InfoKeeper.parkourCreated.replaceAll("<parkour>", name));
	}
	
	public static void setJoinLocation(Player p, Parkour parkour) {
		parkour.setJoinTeleport(p.getLocation());
		p.sendMessage(InfoKeeper.setParkourJoinLocation.replaceAll("<parkour>", parkour.getName()));
	}
	
	public static void setStartLocation(Player p, Parkour parkour) {
		parkour.setStartPoint(p.getLocation());
		p.sendMessage(InfoKeeper.setParkourStartLocation.replaceAll("<parkour>", parkour.getName()));
	}
	
	public static void setFinishLocation(Player p, Parkour parkour) {
		parkour.setFinishPoint(p.getLocation());
		p.sendMessage(InfoKeeper.setParkourFinishLocation.replaceAll("<parkour>", parkour.getName()));
	}
	
	public static boolean isStartPressurePlate(Block b) {
		boolean isStartPressurePlate = false;
		for(Parkour parkour : MemoryHandler.parkours) {
			if(b.getLocation().equals(parkour.getStartPoint())) {
				isStartPressurePlate = true;
			}
		}
		return isStartPressurePlate;
	}
	
	public static boolean isFinishPressurePlate(Block b) {
		boolean isFinishPressurePlate = false;
		for(Parkour parkour : MemoryHandler.parkours) {
			if(b.getLocation().equals(parkour.getFinishPoint())) {
				isFinishPressurePlate = true;
			}
		}
		return isFinishPressurePlate;
	}

}
