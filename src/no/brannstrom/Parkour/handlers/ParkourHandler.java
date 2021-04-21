package no.brannstrom.Parkour.handlers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import no.brannstrom.Parkour.ParkourPlugin;
import no.brannstrom.Parkour.model.SParkour;
import no.brannstrom.Parkour.model.Parkour;
import no.brannstrom.Parkour.model.ParkourPlayer;
import no.brannstrom.Parkour.model.ParkourStats;
import no.brannstrom.Parkour.model.User;
import no.brannstrom.Parkour.service.ParkourService;
import no.brannstrom.Parkour.service.ParkourStatsService;
import no.brannstrom.Parkour.service.UserService;

public class ParkourHandler {

	public static void joinParkour(Player p, SParkour parkour) {
		if(p.hasPermission("spillere.admin")) {
			p.sendMessage(InfoKeeper.parkourJoined.replaceAll("<parkour>", parkour.getName()));
			Location loc = new Location(Bukkit.getWorlds().get(0),parkour.getJointeleportx(),parkour.getJointeleporty(),parkour.getJointeleportz(),parkour.getJointeleportyaw(),parkour.getJointeleportpitch());
			p.teleport(loc);
		}
		else {
			p.sendMessage(InfoKeeper.permission);
		}
	}

	public static void startParkour(Player p, ParkourPlayer parkourPlayer) {
		MemoryHandler.parkourPlayers.put(p.getUniqueId().toString(), parkourPlayer);
		MainHandler.sendActionBar(p, InfoKeeper.parkourStartedHotbar);


		Bukkit.getScheduler().runTaskLater(ParkourPlugin.instance, () -> {
			if(MemoryHandler.parkourPlayers.containsKey(p.getUniqueId().toString()));
			MainHandler.sendActionBar(p, ChatColor.GREEN + "Tid: " + ChatColor.DARK_GREEN + MainHandler.formatTime(System.currentTimeMillis()+MemoryHandler.parkourPlayers.get(p.getUniqueId().toString()).getStartTime()));
		}, 2);
	}

	public static void finishParkour(Player p, ParkourPlayer parkourPlayer) {
		if(MemoryHandler.parkourPlayers.containsKey(p.getUniqueId().toString())) {
			MemoryHandler.parkourPlayers.remove(p.getUniqueId().toString());
		}

		long time = System.currentTimeMillis()-parkourPlayer.getStartTime();

		ParkourStats parkourRecord = ParkourStatsService.getParkourRecord(parkourPlayer.getParkour());
		if(time > parkourRecord.getTime()) {

		}

		SParkour parkour = parkourPlayer.getParkour();
		ParkourStats parkourStats = new ParkourStats();
		parkourStats.setUuid(parkourPlayer.getUuid());
		parkourStats.setParkourName(parkour.getName());
		parkourStats.setTime(time);
		ParkourStatsService.update(parkourStats);

		ParkourStats previousBest = ParkourStatsService.getBestTimeOnParkour(parkourPlayer.getUuid(), parkour.getName());
		if(parkourStats.getTime() < previousBest.getTime()) {
			sendFinishMessageImproved(p, parkour, previousBest.getTime(), time);
		} else {
			sendFinishMessageUnimproved(p,parkour,time);
		}

		MainHandler.sendActionBar(p, InfoKeeper.parkourFinishedHotbar);
	}

	public static void stopParkour() {

	}

	public static SParkour getParkourByStart(Location bLoc) {
		for(SParkour parkour : ParkourService.getParkours()) {
			if(bLoc.getBlockX() == parkour.getStartpointx() && bLoc.getBlockY() == parkour.getStartpointy() && bLoc.getBlockZ() == parkour.getStartpointz()) {
				return parkour;
			}
		}
		return null;
	}

	public static SParkour getParkourByFinish(Location bLoc) {
		for(SParkour parkour : ParkourService.getParkours()) {
			if(bLoc.getBlockX() == parkour.getFinishpointx() && bLoc.getBlockY() == parkour.getFinishpointy() && bLoc.getBlockZ() == parkour.getFinishpointz()) {
				return parkour;
			}
		}
		return null;
	}

	public static void createParkour(Player p, String name) {
		Bukkit.broadcastMessage("5");
		if(p.hasPermission("spillere.admin")) {
			Bukkit.broadcastMessage("6");
			SParkour sparkour = new SParkour();
			Bukkit.broadcastMessage("7");
			sparkour.setName(name);
			Bukkit.broadcastMessage("8");
			Location loc = p.getLocation();
			Bukkit.broadcastMessage("9");
			
			sparkour.setJoinLoc(loc);
			sparkour.setStartLoc(loc);
			sparkour.setFinishLoc(loc);
			
			ObjectMapper mapper = new ObjectMapper();
			String joinLoc = mapper.writeValueAsString(loc);
			String startLoc = mapper.writeValueAsString(loc);
			String finishLoc = mapper.writeValueAsString(loc);
			
			Parkour parkour = new Parkour(); 
			parkour.setName(sparkour.getName());
			parkour.setCoordinatesJoin(joinLoc);
			parkour.setCoordinatesStart(startLoc);
			parkour.setCoordinatesFinish(finishLoc);
			
			
			
			
			
			ParkourService.update(parkour);
			Bukkit.broadcastMessage("13");

			p.sendMessage(InfoKeeper.parkourCreated.replaceAll("<parkour>", name));
			Bukkit.broadcastMessage("14");
		}
		else {
			p.sendMessage(InfoKeeper.permission);
		}
	}

	public static void removeParkour(Player p, SParkour parkour) {
		if(p.hasPermission("spillere.admin")) {
			ParkourStatsService.deleteParkourStatsOnParkour(parkour);
			ParkourService.deleteParkour(parkour);

			p.sendMessage(InfoKeeper.parkourRemoved.replaceAll("<parkour>", parkour.getName()));
		}
		else {
			p.sendMessage(InfoKeeper.permission);
		}
	}

	public static void setJoinLocation(Player p, SParkour parkour) {
		if(p.hasPermission("spillere.admin")) {
			Location loc = p.getLocation();
			parkour.setJointeleportx(loc.getBlockX());
			parkour.setJointeleporty(loc.getBlockY());
			parkour.setJointeleportz(loc.getBlockZ());
			parkour.setJointeleportyaw(loc.getYaw());
			parkour.setJointeleportpitch(loc.getPitch());
			ParkourService.update(parkour);
			p.sendMessage(InfoKeeper.setParkourJoinLocation.replaceAll("<parkour>", parkour.getName()));
		}
		else {
			p.sendMessage(InfoKeeper.permission);
		}
	}

	public static void setStartLocation(Player p, SParkour parkour) {
		if(p.hasPermission("spillere.admin")) {
			Location loc = p.getLocation();
			parkour.setStartpointx(loc.getBlockX());
			parkour.setStartpointy(loc.getBlockY());
			parkour.setStartpointz(loc.getBlockZ());
			parkour.setStartpointyaw(loc.getYaw());
			parkour.setStartpointpitch(loc.getPitch());
			ParkourService.update(parkour);
			p.sendMessage(InfoKeeper.setParkourStartLocation.replaceAll("<parkour>", parkour.getName()));
		}
		else {
			p.sendMessage(InfoKeeper.permission);
		}
	}

	public static void setFinishLocation(Player p, SParkour parkour) {
		if(p.hasPermission("spillere.admin")) {
			Location loc = p.getLocation();
			parkour.setFinishpointx(loc.getBlockX());
			parkour.setFinishpointy(loc.getBlockY());
			parkour.setFinishpointz(loc.getBlockZ());
			parkour.setFinishpointyaw(loc.getYaw());
			parkour.setFinishpointpitch(loc.getPitch());
			ParkourService.update(parkour);
			p.sendMessage(InfoKeeper.setParkourFinishLocation.replaceAll("<parkour>", parkour.getName()));
		}
		else {
			p.sendMessage(InfoKeeper.permission);
		}
	}

	public static void showStats(Player p, SParkour parkour) {
		int i = 0;
		for (ParkourStats stats : ParkourStatsService.getParkourRecordTop10(parkour)){
			i++;
			UUID uuid = stats.getUuid();
			User user = UserService.getUser(uuid);
			String name = "anonym";
			if (user != null) name = MainHandler.getPrefixName(user);
			p.sendMessage(ChatColor.YELLOW + "" + i + ". " + ChatColor.WHITE + name + ChatColor.GRAY + " » " + ChatColor.DARK_GREEN + MainHandler.formatTime(stats.getTime()) + ChatColor.GREEN + ".");
		}
		Integer placement = ParkourStatsService.getParkourPlacement(p.getUniqueId(), parkour.getName());
		if(placement > 10) {
			ParkourStats stats = ParkourStatsService.getBestTimeOnParkour(p.getUniqueId(), parkour.getName());
			p.sendMessage(ChatColor.YELLOW + "" + i + ". " + ChatColor.WHITE + MainHandler.getPrefixName(UserService.getUser(p.getUniqueId())) + ChatColor.GRAY + " » " + ChatColor.DARK_GREEN + MainHandler.formatTime(stats.getTime()) + ChatColor.GREEN + ".");
		}
	}

	public static boolean isStartPressurePlate(Block b) {
		boolean isStartPressurePlate = false;
		for(SParkour parkour : ParkourService.getParkours()) {
			Location bLoc = b.getLocation();			

			if(bLoc.getBlockX() == parkour.getStartpointx() && bLoc.getBlockY() == parkour.getStartpointy() && bLoc.getBlockZ() == parkour.getStartpointz()) {
				isStartPressurePlate = true;
			}
		}
		return isStartPressurePlate;
	}

	public static boolean isFinishPressurePlate(Block b) {
		boolean isFinishPressurePlate = false;
		for(SParkour parkour : ParkourService.getParkours()) {
			Location bLoc = b.getLocation();

			if(bLoc.getBlockX() == parkour.getFinishpointx() && bLoc.getBlockY() == parkour.getFinishpointy() && bLoc.getBlockZ() == parkour.getFinishpointz()) {
				isFinishPressurePlate = true;
			}
		}
		return isFinishPressurePlate;
	}


	public static void sendFinishMessageUnimproved(Player p, SParkour parkour, long time) {
		p.sendMessage(InfoKeeper.finishedParkour.replaceAll("<parkour>", parkour.getName()).replaceAll("<time>", new SimpleDateFormat("mm:ss:SSS").format(new Date(time))));
	}

	public static void sendFinishMessageImproved(Player p, SParkour parkour, long previousTime, long newTime) {
		p.sendMessage(InfoKeeper.improvedTime.replaceAll("<parkour>", parkour.getName()).replaceAll("<time>", new SimpleDateFormat("mm:ss:SSS").format(new Date(newTime))).replaceAll("<improvement>", new SimpleDateFormat("mm:ss:SSS").format(new Date(previousTime-newTime))));
	}

	public static void sendNewParkourRecord(Player p, SParkour parkour, long previousTime, long newTime) {
		String parkourRecord = InfoKeeper.improvedTime.replaceAll("<player>", MainHandler.getPrefixName(UserService.getUser(p.getUniqueId()))).replaceAll("<parkour>", parkour.getName()).replaceAll("<time>", new SimpleDateFormat("mm:ss:SSS").format(new Date(newTime))).replaceAll("<improvement>", new SimpleDateFormat("mm:ss:SSS").format(new Date(previousTime-newTime)));
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("broadcast");
		out.writeUTF(parkourRecord);
		out.writeUTF(":computer:");
		p.sendPluginMessage(ParkourPlugin.instance, "spillere:bungee", out.toByteArray());
	}
}
