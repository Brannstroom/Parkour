package me.brannstrom.Handlers;

import java.util.HashMap;
import java.util.UUID;

import me.brannstrom.Model.ParkourPlayer;

public class MemoryHandler {
	
	// UUID = Players uuid, Long = parkour starttime
	public static HashMap<String, ParkourPlayer> parkourPlayers = new HashMap<>();
	
	public static HashMap<UUID, Long> combatTimer = new HashMap<UUID, Long>();
}
