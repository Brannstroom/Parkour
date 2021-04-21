package no.brannstrom.Parkour.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import no.brannstrom.Parkour.model.Parkour;
import no.brannstrom.Parkour.model.ParkourPlayer;
import no.brannstrom.Parkour.model.ParkourStats;

public class MemoryHandler {
	
	// Sting = UUID, ParkourStats = Object with all the players finished parkours with time
	public static HashMap<String, ParkourStats> parkourStats = new HashMap<>();
	
	// List of all parkour maps
	public static List<Parkour> parkours = new ArrayList<>();
	
	// UUID = Players uuid, Long = parkour starttime
	public static HashMap<String, ParkourPlayer> parkourPlayers = new HashMap<>();

}
