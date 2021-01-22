package no.brannstrom.Parkour.dao;

import org.bukkit.entity.Player;

import no.brannstrom.Parkour.handlers.MemoryHandler;
import no.brannstrom.Parkour.model.ParkourStats;

public class ParkourStatsRepo {

	public static ParkourStats getParkourStats(Player p) {
		if(MemoryHandler.parkourStats.containsKey(p.getUniqueId().toString())) {
			return MemoryHandler.parkourStats.get(p.getUniqueId().toString());
		} else {
			return new ParkourStats();
		}
	}
	
	public static ParkourStats getParkourStats(String uuid) {
		if(MemoryHandler.parkourStats.containsKey(uuid)) {
			return MemoryHandler.parkourStats.get(uuid);
		} else {
			return new ParkourStats();
		}
	}
	
}
