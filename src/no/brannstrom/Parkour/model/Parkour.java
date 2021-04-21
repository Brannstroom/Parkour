package no.brannstrom.Parkour.model;

import org.bukkit.Location;

public class Parkour {

	private String name;
	
	private Location joinLoc;

	private Location startLoc;
	
	private Location finishLoc;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Location getJoinLoc() {
		return joinLoc;
	}

	public void setJoinLoc(Location joinLoc) {
		this.joinLoc = joinLoc;
	}

	public Location getStartLoc() {
		return startLoc;
	}

	public void setStartLoc(Location startLoc) {
		this.startLoc = startLoc;
	}

	public Location getFinishLoc() {
		return finishLoc;
	}

	public void setFinishLoc(Location finishLoc) {
		this.finishLoc = finishLoc;
	}

	
}
