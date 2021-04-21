package no.brannstrom.Parkour.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import no.brannstrom.Parkour.handlers.MemoryHandler;
import no.brannstrom.Parkour.handlers.ParkourHandler;
import no.brannstrom.Parkour.model.Parkour;
import no.brannstrom.Parkour.model.ParkourPlayer;

public class ParkourListener implements Listener {

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if(event.getAction().equals(Action.PHYSICAL)) {
			Block block = event.getClickedBlock();
			if(block != null && block.getType().equals(Material.STONE_PRESSURE_PLATE)) {
				Player p = event.getPlayer();
				if(ParkourHandler.isStartPressurePlate(block)) {
					if(MemoryHandler.parkourPlayers.containsKey(p.getUniqueId().toString())) {
						MemoryHandler.parkourPlayers.remove(p.getUniqueId().toString());
					}
					
					ParkourPlayer parkourPlayer = new ParkourPlayer();
					Parkour parkour = ParkourHandler.getParkourByStart(block.getLocation());
					
					parkourPlayer.setUuid(p.getUniqueId());
					parkourPlayer.setParkour(parkour);
					parkourPlayer.setStartTime(System.currentTimeMillis());
					
					ParkourHandler.startParkour(p, parkourPlayer);
					
				} else if(ParkourHandler.isFinishPressurePlate(block)) {
					if(MemoryHandler.parkourPlayers.containsKey(p.getUniqueId().toString())) {
						ParkourPlayer parkourPlayer = MemoryHandler.parkourPlayers.get(p.getUniqueId().toString());
						Parkour activeParkour = parkourPlayer.getParkour();
						Parkour finishedParkour = ParkourHandler.getParkourByFinish(block.getLocation());
						
						if(activeParkour.getName().equalsIgnoreCase(finishedParkour.getName())) {
							ParkourHandler.finishParkour(p, parkourPlayer);
						}
					}
				}
			}
		}
	}
}
