package no.brannstrom.Parkour.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import no.brannstrom.Parkour.handlers.MemoryHandler;
import no.brannstrom.Parkour.handlers.ParkourHandler;
import no.brannstrom.Parkour.model.Parkour;
import no.brannstrom.Parkour.model.ParkourPlayer;

public class ParkourListener implements Listener {

	public void onPlayerInteract(PlayerInteractEvent event) {
		if(event.getAction().equals(Action.PHYSICAL)) {
			Block block = event.getClickedBlock();
			if(block != null && block.getType().equals(Material.STONE_PRESSURE_PLATE)) {
				Player p = event.getPlayer();
				if(ParkourHandler.isStartPressurePlate(block)) {
					if(MemoryHandler.parkourPlayers.containsKey(p.getUniqueId())) {
						MemoryHandler.parkourPlayers.remove(p.getUniqueId());
					}
					
					ParkourPlayer parkourPlayer = new ParkourPlayer();
					Parkour parkour = ParkourHandler.getParkourByStart(block.getLocation());
					
					parkourPlayer.setUuid(p.getUniqueId());
					parkourPlayer.setParkour(parkour);
					parkourPlayer.setStartTime(System.currentTimeMillis());
					
					ParkourHandler.startParkour(p, parkourPlayer);
					
				} else if(ParkourHandler.isFinishPressurePlate(block)) {
					if(MemoryHandler.parkourPlayers.containsKey(p.getUniqueId())) {
						if(ParkourHandler.getParkourByFinish(block.getLocation()).getName().equalsIgnoreCase(MemoryHandler.parkourPlayers.get(p.getUniqueId()).getParkour().getName())) {
							ParkourHandler.finishParkour(p, MemoryHandler.parkourPlayers.get(p.getUniqueId()));
						}
					}
				}
			}
		}
	}
}
