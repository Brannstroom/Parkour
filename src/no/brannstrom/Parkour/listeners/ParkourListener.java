package no.brannstrom.Parkour.listeners;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRiptideEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
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
			else if(player.isRiptiding()) {
				ParkourHandler.disqualify(player, parkour, "Du ble diskvalifisert fra parkouren for å fly.");
			}
			else if(player.getInventory().getBoots() != null) {
				if(player.getInventory().getBoots().containsEnchantment(Enchantment.SOUL_SPEED)) {
					ParkourHandler.disqualify(player, parkour, "Du ble diskvalifisert for å bruke soulspeed.");
				}
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
			ParkourHandler.disqualifyTeleport(player, parkour, "Du ble diskvalifisert for å teleportere.");
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerEffect(EntityPotionEffectEvent event) {
		Entity entity = event.getEntity();
		if(entity instanceof Player) {
			Player player = (Player) entity;
			if(MemoryHandler.parkourPlayers.containsKey(player.getUniqueId().toString())) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		if(MemoryHandler.parkourPlayers.containsKey(player.getUniqueId().toString())) {
			Parkour parkour = MemoryHandler.parkourPlayers.get(player.getUniqueId().toString()).getParkour();
			ParkourHandler.disqualify(player, parkour, "Du ble diskvalifert for å plassere blokker.");
			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		if(MemoryHandler.parkourPlayers.containsKey(player.getUniqueId().toString())) {
			Parkour parkour = MemoryHandler.parkourPlayers.get(player.getUniqueId().toString()).getParkour();
			ParkourHandler.disqualify(player, parkour, "Du ble diskvalifert for å ødelegge blokker.");
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void playerInteractEvent(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if(MemoryHandler.parkourPlayers.containsKey(player.getUniqueId().toString())) {
			Block block = event.getClickedBlock();
			if(block != null) {
				if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
					if (player.getInventory().getItemInMainHand() != null) {
						Material[] material = { Material.LAVA, Material.LAVA_BUCKET, Material.LAVA_BUCKET, Material.WATER,
								Material.WATER_BUCKET, Material.BONE_MEAL, Material.ARMOR_STAND, Material.TRIPWIRE_HOOK,
								Material.ACACIA_BOAT, Material.BIRCH_BOAT, Material.DARK_OAK_BOAT, Material.JUNGLE_BOAT,
								Material.OAK_BOAT, Material.SPRUCE_BOAT};
						for (Material m : material) {
							if (m == (player.getInventory().getItemInMainHand().getType())
									|| m == player.getInventory().getItemInOffHand().getType()) {
								Parkour parkour = MemoryHandler.parkourPlayers.get(player.getUniqueId().toString()).getParkour();
								ParkourHandler.disqualify(player, parkour, "Du ble diskvalifisert for å plassere ting i parkouren.");
								event.setCancelled(true);
							}
						}
					}
				}
				else if(isSpawnEgg(player.getInventory().getItemInOffHand())) {
					if(isSpawnEgg(player.getInventory().getItemInOffHand())) {
						event.setCancelled(true);
					}
				}
			}

			if(event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
				if (player.getInventory().getItemInMainHand() != null) {
					if ((player.getInventory().getItemInMainHand().getType().equals(Material.TRIDENT))
							|| (player.getInventory().getItemInOffHand().getType().equals(Material.TRIDENT))) {
						Parkour parkour = MemoryHandler.parkourPlayers.get(player.getUniqueId().toString()).getParkour();
						ParkourHandler.disqualify(player, parkour, "Du ble diskvalifisert for å bruke trident.");
					}
				}
			}
		}
	}


	@EventHandler
	public void onPlayerUse(PlayerInteractEntityEvent event) {
		Player player = event.getPlayer();
		if(MemoryHandler.parkourPlayers.containsKey(player.getUniqueId().toString())) {
			if(isSpawnEgg(player.getInventory().getItemInOffHand())) {
				if(isSpawnEgg(player.getInventory().getItemInOffHand())) {
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		if(MemoryHandler.parkourPlayers.containsKey(player.getUniqueId().toString())) {
			Parkour parkour = MemoryHandler.parkourPlayers.get(player.getUniqueId().toString()).getParkour();
			ParkourHandler.disqualify(player, parkour, "Du ble fjernet fra parkouren for å dø.");
		}
	}

	@EventHandler
	public void onPlayerRiptide(PlayerRiptideEvent event) {
		Player player = event.getPlayer();
		if(MemoryHandler.parkourPlayers.containsKey(player.getUniqueId().toString())) {
			Parkour parkour = MemoryHandler.parkourPlayers.get(player.getUniqueId().toString()).getParkour();
			ParkourHandler.disqualify(player, parkour, "Du ble fjernet fra parkouren for å bruke trident.");
		}
	}

	public static String eggName = ChatColor.ITALIC + "" + ChatColor.UNDERLINE + "Safari Net";
	public static String singleUseEggLore = ChatColor.GREEN + "Engangsbruk";
	public static String reusableEggLore = ChatColor.GREEN + "Flergangsbruk";

	public boolean isSpawnEgg(ItemStack item) {
		boolean isSpawnEgg = false;
		if(item.getType().toString().endsWith("_SPAWN_EGG")) {
			isSpawnEgg = true;
		}
		return isSpawnEgg;
	}	
}
