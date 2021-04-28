package no.brannstrom.Parkour.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import no.brannstrom.Parkour.handlers.MemoryHandler;
import no.brannstrom.Parkour.handlers.ParkourHandler;
import no.brannstrom.Parkour.model.Parkour;
import no.brannstrom.Parkour.model.ParkourPlayer;

public class ParkourListener implements Listener {

	@EventHandler(priority = EventPriority.HIGH)
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

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if(MemoryHandler.parkourPlayers.containsKey(player.getUniqueId().toString())) {
			Parkour parkour = MemoryHandler.parkourPlayers.get(player.getUniqueId().toString()).getParkour();
			if(player.isFlying()) {
				ParkourHandler.disqualify(player, parkour, "Du ble diskvalifisert fra parkouren for å fly.");
			}
			else if(player.isGliding()) {
				ParkourHandler.disqualify(player, parkour, "Du ble diskvalifisert fra parkouren for å fly.");
			}
			else if(player.getActivePotionEffects() != null) {
				for(PotionEffect e : player.getActivePotionEffects()) {
					if(e.getType().equals(PotionEffectType.SPEED) || e.getType().equals(PotionEffectType.JUMP) || e.getType().equals(PotionEffectType.SLOW_FALLING) || e.getType().equals(PotionEffectType.LEVITATION)) {
						player.removePotionEffect(e.getType());
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerCommandPreprocess(final PlayerCommandPreprocessEvent event) {
		Player player = event.getPlayer();
		if(MemoryHandler.parkourPlayers.containsKey(player.getUniqueId().toString())) {
			Parkour parkour = MemoryHandler.parkourPlayers.get(player.getUniqueId().toString()).getParkour();
			String cmd = event.getMessage().toLowerCase();
			String[] arrCmd = {"/m ", "/msg ", "/pm ", "/tell ", "/whisper ", "/r ", "/reply ", "/svar ", "/regler ", "/rules ", "/stem ", "/vote ", "/h ", "/handel ", "/bank ", "/b ", "/parkour "};
			boolean cheater = true;
			for(String s : arrCmd) {
				if(cmd.startsWith(s)){
					cheater = false;
				}
			}
			if(cheater) {
				ParkourHandler.disqualify(player, parkour, "Du ble diskvalifisert for å bruke kommandoer.");
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerTeleport(final PlayerTeleportEvent event) {
		Player player = event.getPlayer();
		if(MemoryHandler.parkourPlayers.containsKey(player.getUniqueId().toString())) {
			Parkour parkour = MemoryHandler.parkourPlayers.get(player.getUniqueId().toString()).getParkour();
			ParkourHandler.disqualify(player, parkour, "Du ble diskvalifisert for å teleportere.");
		}
	}
}
