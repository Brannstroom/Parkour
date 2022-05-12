package me.brannstrom.Handlers;

import java.util.Collection;
import java.util.UUID;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import me.brannstrom.Model.*;
import me.brannstrom.ParkourPlugin;
import me.brannstrom.Service.ParkourService;
import me.brannstrom.Service.ParkourStatsService;
import me.brannstrom.Service.UserService;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class ParkourHandler {

	public static void joinParkour(Player p, Parkour parkour) {
		Serialize serialize = new Serialize();
		Location loc = serialize.deserialize(parkour.getJoinLocation());
		if(p.getLocation().getWorld().equals(loc.getWorld())) {
			p.sendMessage(InfoKeeper.parkourJoined.replaceAll("<parkour>", parkour.getParkourName()));
			p.teleport(loc);
		}
		else {
			p.sendMessage(ChatColor.RED + "Denne parkouren er i en annen verden eller på en annen server.");
		}
	}

	public static void startParkour(Player p, ParkourPlayer parkourPlayer) {

		if(MemoryHandler.parkourPlayers.containsKey(p.getUniqueId().toString())) {
			MemoryHandler.parkourPlayers.remove(p.getUniqueId().toString());
		}

		if (p.getInventory().getItemInMainHand() != null) {
			if ((p.getInventory().getItemInMainHand().getType().equals(Material.TRIDENT))
					|| (p.getInventory().getItemInOffHand().getType().equals(Material.TRIDENT))) {
				p.sendMessage(ChatColor.RED + "Du kan ikke starte parkouren mens du holder en trident.");
				return;
			}
		}
		else if(p.getInventory().getBoots() != null) {
			if(p.getInventory().getBoots().containsEnchantment(Enchantment.SOUL_SPEED)) {
				p.sendMessage(ChatColor.RED + "Du kan ikke starte parkouren med soulspeed enchantment.");
			}
		}

		MainHandler.sendActionBar(p, InfoKeeper.parkourStartedHotbar);
		if(p.getActivePotionEffects() != null) {
			for(PotionEffect e : p.getActivePotionEffects()) {
				p.removePotionEffect(e.getType());
			}
		}

		long startTime = parkourPlayer.getStartTime();
		BukkitTask task = new BukkitRunnable() {
			public void run() {
				if(MemoryHandler.parkourPlayers.containsKey(p.getUniqueId().toString())) {
					p.sendActionBar(ChatColor.GREEN + "Tid: " + ChatColor.RESET + MainHandler.formatTime(System.currentTimeMillis()-startTime));
				}
				else {
					cancel();
				}
			}
		}.runTaskTimer(ParkourPlugin.instance, 20L, 5L);

		parkourPlayer.setTaskId(task.getTaskId());
		MemoryHandler.parkourPlayers.put(p.getUniqueId().toString(), parkourPlayer);
	}

	public static void finishParkour(Player p, ParkourPlayer parkourPlayer) {
		if(MemoryHandler.parkourPlayers.containsKey(p.getUniqueId().toString())) {
			ParkourPlayer pp = MemoryHandler.parkourPlayers.get(p.getUniqueId().toString());
			Bukkit.getScheduler().cancelTask(pp.getTaskId());
			MemoryHandler.parkourPlayers.remove(p.getUniqueId().toString());
		}

		Parkour parkour = parkourPlayer.getParkour();
		long time = System.currentTimeMillis()-parkourPlayer.getStartTime();

		boolean messageSent = false;

		ParkourStats parkourRecord = ParkourStatsService.getParkourRecord(parkour);
		if(parkourRecord != null) {
			if(time < parkourRecord.getParkourTime()) {
				User user = UserService.getUser(p.getUniqueId());
				MainHandler.broadcastMessage(parkour, InfoKeeper.newParkourRecord.replaceAll("<player>", MainHandler.getPrefixName(user)).replaceAll("<parkour>", parkour.getParkourName()).replaceAll("<time>", MainHandler.formatTime(time)).replaceAll("<improvement>", MainHandler.formatTime(parkourRecord.getParkourTime()-time)));
				messageSent = true;
			}
		}
		else {
			User user = UserService.getUser(p.getUniqueId());
			MainHandler.broadcastMessage(parkour, InfoKeeper.firstRecord.replaceAll("<player>", MainHandler.getPrefixName(user)).replaceAll("<parkour>", parkour.getParkourName()).replaceAll("<time>", MainHandler.formatTime(time)));
			messageSent = true;
		}

		ParkourStats personalBest = ParkourStatsService.getPersonalBest(parkourPlayer.getUuid(), parkour.getParkourName());

		if(personalBest != null) {
			if(time < personalBest.getParkourTime()) {
				if(!messageSent) {
					sendFinishMessageImproved(p, parkour, personalBest.getParkourTime(), time);
				}
				personalBest.setParkourTime(time);
				ParkourStatsService.update(personalBest);
			} else {
				if(!messageSent) {
					sendFinishMessageUnimproved(p,parkour,time);
				}
			}
		}
		else {
			if(!messageSent) {
				sendFirstTimeFinish(p,parkour,time);
			}
			ParkourStats parkourStats = new ParkourStats();
			parkourStats.setUuid(parkourPlayer.getUuid());
			parkourStats.setParkourName(parkour.getParkourName());
			parkourStats.setParkourTime(time);
			ParkourStatsService.update(parkourStats);
		}

		MainHandler.sendActionBar(p, InfoKeeper.parkourFinishedHotbar);

		Integer placement = ParkourStatsService.getParkourPlacement(p.getUniqueId(), parkour.getParkourName());
		if(placement != null) {
			if(placement <= 10) {
				if(hologramExists(parkour)) {
					updateHologram(parkour);
				}
			}
		}
	}

	public static void disqualify(Player player, Parkour parkour, String reason) {
		if(MemoryHandler.parkourPlayers.containsKey(player.getUniqueId().toString())) {
			ParkourPlayer pp = MemoryHandler.parkourPlayers.get(player.getUniqueId().toString());
			Bukkit.getScheduler().cancelTask(pp.getTaskId());
			MemoryHandler.parkourPlayers.remove(player.getUniqueId().toString());
		}

		player.sendMessage(ChatColor.RED + reason);
		Serialize serialize = new Serialize();
		player.teleport(serialize.deserialize(parkour.getJoinLocation()));
	}

	public static void disqualifyTeleport(Player player, Parkour parkour, String reason) {
		if(MemoryHandler.parkourPlayers.containsKey(player.getUniqueId().toString())) {
			ParkourPlayer pp = MemoryHandler.parkourPlayers.get(player.getUniqueId().toString());
			Bukkit.getScheduler().cancelTask(pp.getTaskId());
			MemoryHandler.parkourPlayers.remove(player.getUniqueId().toString());
		}

		player.sendMessage(ChatColor.RED + reason);
	}

	public static void removePlayer(UUID uuid, Parkour parkour) {
		if(MemoryHandler.parkourPlayers.containsKey(uuid.toString())) {
			ParkourPlayer pp = MemoryHandler.parkourPlayers.get(uuid.toString());
			Bukkit.getScheduler().cancelTask(pp.getTaskId());
			MemoryHandler.parkourPlayers.remove(uuid.toString());
		}
	}

	public static void showStats(Player p, Parkour parkour) {
		int i = 0;
		p.sendMessage(ChatColor.DARK_GRAY + "------" + ChatColor.BOLD + ChatColor.GOLD + "{ " + parkour.getParkourName() + " Top }" + ChatColor.RESET + ChatColor.DARK_GRAY + "------");
		if(!ParkourStatsService.getTop10(parkour).isEmpty()) {
			for (ParkourStats stats : ParkourStatsService.getTop10(parkour)){
				i++;
				UUID uuid = stats.getUuid();
				User user = UserService.getUser(uuid);
				String name = "anonym";
				if (user != null) name = MainHandler.getPrefixName(user);
				p.sendMessage(ChatColor.YELLOW + "" + i + ". " + ChatColor.WHITE + name + ChatColor.GRAY + " » " + ChatColor.DARK_GREEN + MainHandler.formatTime(stats.getParkourTime()) + ChatColor.GREEN + ".");
			}
			Integer placement = ParkourStatsService.getParkourPlacement(p.getUniqueId(), parkour.getParkourName());
			if(placement != null) {
				if(placement > 10) {
					ParkourStats stats = ParkourStatsService.getPersonalBest(p.getUniqueId(), parkour.getParkourName());
					p.sendMessage(ChatColor.YELLOW + "" + placement + ". " + ChatColor.WHITE + MainHandler.getPrefixName(UserService.getUser(p.getUniqueId())) + ChatColor.GRAY + " » " + ChatColor.DARK_GREEN + MainHandler.formatTime(stats.getParkourTime()));
				}
			}
		}
		else {
			p.sendMessage(ChatColor.RED + "Ingen har fullført " + ChatColor.DARK_RED + parkour.getParkourName() + ChatColor.RED + " parkouren. Bli den første!");
		}
	}

	public static void createHologram(Player player, Parkour parkour) {
		if(!hologramExists(parkour)) {
			Hologram hologram = HologramsAPI.createHologram(ParkourPlugin.instance, player.getLocation());
			int i = 0;
			hologram.appendTextLine(ChatColor.DARK_GRAY + "------" + ChatColor.BOLD + ChatColor.GOLD + "{ " + parkour.getParkourName() + " Top }" + ChatColor.RESET + ChatColor.DARK_GRAY + "------");
			if(!ParkourStatsService.getTop10(parkour).isEmpty()) {
				for (ParkourStats stats : ParkourStatsService.getTop10(parkour)){
					i++;
					UUID uuid = stats.getUuid();
					User user = UserService.getUser(uuid);
					String name = "anonym";
					if (user != null) name = MainHandler.getPrefixName(user);
					hologram.appendTextLine(ChatColor.YELLOW + "" + i + ". " + ChatColor.WHITE + name + ChatColor.GRAY + " » " + ChatColor.DARK_GREEN + MainHandler.formatTime(stats.getParkourTime()));
				}
			}
			hologram.appendTextLine(ChatColor.DARK_GRAY + "/parkour stats " + parkour.getParkourName());

			Serialize serialize = new Serialize();
			parkour.setHoloLocation(serialize.serialize(player.getLocation()));
			ParkourService.update(parkour);

			player.sendMessage(ChatColor.GREEN + "Du satt opp hologram for " + ChatColor.DARK_GREEN + ChatColor.BOLD + parkour.getParkourName() + ChatColor.RESET + ChatColor.GREEN + " parkouren.");
		}
		else {
			player.sendMessage(ChatColor.RED + "Det eksisterer allerede et hologram for " + ChatColor.DARK_RED + ChatColor.BOLD + parkour.getParkourName() + ChatColor.RESET + ChatColor.RED + " parkouren. Fjern den før du setter ny.");
		}
	}

	public static void createHologram(Parkour parkour) {
		if(parkour.getHoloLocation() != null) {
			if(!hologramExists(parkour)) {
				Serialize serialize = new Serialize();
				Hologram hologram = HologramsAPI.createHologram(ParkourPlugin.instance, serialize.deserialize(parkour.getHoloLocation()));
				int i = 0;
				hologram.appendTextLine(ChatColor.DARK_GRAY + "------" + ChatColor.BOLD + ChatColor.GOLD + "{ " + parkour.getParkourName() + " Top }" + ChatColor.RESET + ChatColor.DARK_GRAY + "------");
				if(!ParkourStatsService.getTop10(parkour).isEmpty()) {
					for (ParkourStats stats : ParkourStatsService.getTop10(parkour)){
						i++;
						UUID uuid = stats.getUuid();
						User user = UserService.getUser(uuid);
						String name = "anonym";
						if (user != null) name = MainHandler.getPrefixName(user);
						hologram.appendTextLine(ChatColor.YELLOW + "" + i + ". " + ChatColor.WHITE + name + ChatColor.GRAY + " » " + ChatColor.DARK_GREEN + MainHandler.formatTime(stats.getParkourTime()));
					}
				}
				hologram.appendTextLine(ChatColor.DARK_GRAY + "/parkour stats " + parkour.getParkourName());
			}
		}
	}

	public static void removeHologram(Player player, Parkour parkour) {
		Collection<Hologram> holograms = HologramsAPI.getHolograms(ParkourPlugin.instance);
		boolean removed = false;
		for(Hologram hologram : holograms) {
			Location aLoc = hologram.getLocation();
			Serialize serialize = new Serialize();
			Location bLoc = serialize.deserialize(parkour.getHoloLocation());
			if(aLoc.getBlockX() == bLoc.getBlockX() && aLoc.getBlockY() == bLoc.getBlockY() && aLoc.getBlockZ() == bLoc.getBlockZ()) {
				hologram.delete();
				removed = true;
			}
		}

		if(removed) {
			player.sendMessage(ChatColor.GREEN + "Du fjernet hologram for " + ChatColor.DARK_GREEN + ChatColor.BOLD + parkour.getParkourName() + ChatColor.RESET + ChatColor.GREEN + " parkouren.");
		}
		else {
			player.sendMessage("" + ChatColor.DARK_RED + ChatColor.BOLD + parkour.getParkourName() + ChatColor.RESET + ChatColor.RED + " har ikke Hologram, eller var ikke mulig å fjerne.");
		}
	}

	public static void updateHologram(Parkour parkour) {
		Collection<Hologram> holograms = HologramsAPI.getHolograms(ParkourPlugin.instance);
		for(Hologram hologram : holograms) {
			Location aLoc = hologram.getLocation();
			Serialize serialize = new Serialize();
			Location bLoc = serialize.deserialize(parkour.getHoloLocation());
			if(aLoc.getBlockX() == bLoc.getBlockX() && aLoc.getBlockY() == bLoc.getBlockY() && aLoc.getBlockZ() == bLoc.getBlockZ()) {
				hologram.clearLines();
				int i = 0;
				hologram.appendTextLine(ChatColor.DARK_GRAY + "------" + ChatColor.BOLD + ChatColor.GOLD + "{ " + parkour.getParkourName() + " Top }" + ChatColor.RESET + ChatColor.DARK_GRAY + "------");
				if(!ParkourStatsService.getTop10(parkour).isEmpty()) {
					for (ParkourStats stats : ParkourStatsService.getTop10(parkour)){
						i++;
						UUID uuid = stats.getUuid();
						User user = UserService.getUser(uuid);
						String name = "anonym";
						if (user != null) name = MainHandler.getPrefixName(user);
						hologram.appendTextLine(ChatColor.YELLOW + "" + i + ". " + ChatColor.WHITE + name + ChatColor.GRAY + " » " + ChatColor.DARK_GREEN + MainHandler.formatTime(stats.getParkourTime()));
					}
				}
				hologram.appendTextLine(ChatColor.DARK_GRAY + "/parkour stats " + parkour.getParkourName());
			}
		}
	}

	public static boolean hologramExists(Parkour parkour) {
		boolean exists = false;
		Collection<Hologram> holograms = HologramsAPI.getHolograms(ParkourPlugin.instance);
		if(! holograms.isEmpty()) {
			for(Hologram hologram : holograms) {
				Location aLoc = hologram.getLocation();
				Serialize serialize = new Serialize();
				if(parkour.getHoloLocation() != null) {
					Location bLoc = serialize.deserialize(parkour.getHoloLocation());
					if(aLoc.getBlockX() == bLoc.getBlockX() && aLoc.getBlockY() == bLoc.getBlockY() && aLoc.getBlockZ() == bLoc.getBlockZ()) {
						exists = true;
					}
				}
			}
		}
		return exists;
	}

	public static boolean isParkour(String name) {
		boolean isParkour = false;
		if(ParkourService.getParkours() != null) {
			if(!ParkourService.getParkours().isEmpty()) {
				for(Parkour parkour : ParkourService.getParkours()) {
					if(parkour.getParkourName().equalsIgnoreCase(name)) {
						isParkour = true;
					}
				}
			}
		}
		return isParkour;
	}

	public static Parkour getParkour(String name) {
		if(!ParkourService.getParkours().isEmpty()) {
			for(Parkour parkour : ParkourService.getParkours()) {
				if(parkour.getParkourName().equalsIgnoreCase(name)) {
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
		parkour.setParkourName(name);
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
		ParkourStatsService.deleteByParkour(parkour.getParkourName());
		if(hologramExists(parkour)) {
			removeHologram(p, parkour);
		}
		ParkourService.deleteParkour(parkour);

		p.sendMessage(InfoKeeper.parkourRemoved.replaceAll("<parkour>", parkour.getParkourName()));
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
		Serialize serialize = new Serialize();
		parkour.setJoinLocation(serialize.serialize(p.getLocation()));
		ParkourService.update(parkour);
		p.sendMessage(InfoKeeper.setParkourJoinLocation.replaceAll("<parkour>", parkour.getParkourName()));

	}

	public static void setStartLocation(Player p, Parkour parkour) {
		Serialize serialize = new Serialize();
		parkour.setStartLocation(serialize.serialize(p.getLocation()));
		ParkourService.update(parkour);
		p.sendMessage(InfoKeeper.setParkourStartLocation.replaceAll("<parkour>", parkour.getParkourName()));

	}

	public static void setFinishLocation(Player p, Parkour parkour) {
		Serialize serialize = new Serialize();
		parkour.setFinishLocation(serialize.serialize(p.getLocation()));
		ParkourService.update(parkour);
		p.sendMessage(InfoKeeper.setParkourFinishLocation.replaceAll("<parkour>", parkour.getParkourName()));
	}

	public static void sendFinishMessageUnimproved(Player p, Parkour parkour, long time) {
		p.sendMessage(InfoKeeper.finishedParkour.replaceAll("<parkour>", parkour.getParkourName()).replaceAll("<time>", MainHandler.formatTime(time)));
	}

	public static void sendFinishMessageImproved(Player p, Parkour parkour, long previousTime, long newTime) {
		p.sendMessage(InfoKeeper.improvedTime.replaceAll("<parkour>", parkour.getParkourName()).replaceAll("<time>", MainHandler.formatTime(newTime)).replaceAll("<improvement>", MainHandler.formatTime(previousTime-newTime)));
	}

	public static void sendFirstTimeFinish(Player p, Parkour parkour, long time) {
		p.sendMessage(InfoKeeper.firstTimeFinishingParkour.replaceAll("<parkour>", parkour.getParkourName()).replaceAll("<time>", MainHandler.formatTime(time)));
	}
}
