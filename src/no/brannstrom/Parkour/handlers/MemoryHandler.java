package no.brannstrom.Parkour.handlers;

import java.util.HashMap;
import java.util.UUID;

import no.brannstrom.Parkour.model.ParkourPlayer;

public class MemoryHandler {
	
	// UUID = Players uuid, Long = parkour starttime
	public static HashMap<String, ParkourPlayer> parkourPlayers = new HashMap<>();
	
	public static HashMap<UUID, Long> combatTimer = new HashMap<UUID, Long>();
}
