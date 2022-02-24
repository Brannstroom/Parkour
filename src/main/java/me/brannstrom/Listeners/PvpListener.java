package me.brannstrom.Listeners;

import java.util.UUID;

import me.brannstrom.Handlers.MemoryHandler;
import me.brannstrom.ParkourPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PvpListener implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
		if (!e.isCancelled()) {
			if (e.getEntity() instanceof Player) {
				Player defender = (Player) e.getEntity();

				// Finn angriper
				Player attacker = null;
				if (e.getDamager() instanceof Player) {
					attacker = (Player) e.getDamager();
				} else if (e.getDamager() instanceof Projectile) {
					Projectile projectile = (Projectile) e.getDamager();
					if (projectile.getShooter() instanceof Player) {
						attacker = (Player) projectile.getShooter();
					}
				} else if (e.getDamager() instanceof Wolf) {
					Wolf wolf = (Wolf) e.getDamager();
					if (wolf.isTamed() && wolf.getOwner() instanceof Player) {
						attacker = Bukkit.getPlayer(wolf.getOwner().getUniqueId());
					}
				}

				// Gjør noe med det
				if (attacker != null) {

					// Deaktiver PvP i overworld
					if (!defender.getWorld().getName().endsWith("_nether")
							&& !defender.getWorld().getName().endsWith("_the_end")) {
						e.setCancelled(true);
						return;
					}

					// Jobb-modus
					if (defender.hasPermission("spillere.jobb") || attacker.hasPermission("spillere.jobb")) {
						e.setCancelled(true);
						return;
					}

					// PvP timer (combat logger)
					if (attacker.getUniqueId().equals(defender.getUniqueId())) {
						return;
					}
					if (!MemoryHandler.combatTimer.containsKey(defender.getUniqueId())
							|| MemoryHandler.combatTimer.get(defender.getUniqueId()) < System.currentTimeMillis()) {
						// Slå av PvP timer
						final UUID defenderUUID = defender.getUniqueId();
						pvpTimer(defenderUUID);

					}
					if (!MemoryHandler.combatTimer.containsKey(attacker.getUniqueId())
							|| MemoryHandler.combatTimer.get(attacker.getUniqueId()) < System.currentTimeMillis()) {

						// Slå av PvP timer
						final UUID attackerUUID = attacker.getUniqueId();
						pvpTimer(attackerUUID);

					}

					// Oppdater PvP timer
					MemoryHandler.combatTimer.put(defender.getUniqueId(), System.currentTimeMillis() + 30000);
					MemoryHandler.combatTimer.put(attacker.getUniqueId(), System.currentTimeMillis() + 30000);
				}
			}

		}
	}

	private void pvpTimer(UUID uuid) {
		Bukkit.getScheduler().runTaskLaterAsynchronously(ParkourPlugin.instance, () -> {

			if (!MemoryHandler.combatTimer.containsKey(uuid)
					|| MemoryHandler.combatTimer.get(uuid) < System.currentTimeMillis()) {
				MemoryHandler.combatTimer.remove(uuid);
			} else {
				pvpTimer(uuid);
			}

		}, 20);
	}
}
