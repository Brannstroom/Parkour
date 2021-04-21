package no.brannstrom.Parkour.model;

import org.bukkit.Location;

public class Parkour {

	private String name;
	
	private Location joinTeleport;

	private Location startPoint;
	
	private Location finishPoint;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Location getJoinTeleport() {
		return joinTeleport;
	}

	public void setJoinTeleport(Location joinTeleport) {
		this.joinTeleport = joinTeleport;
	}

	public Location getStartPoint() {
		return startPoint;
	}

	public void setStartPoint(Location startPoint) {
		this.startPoint = startPoint;
	}

	public Location getFinishPoint() {
		return finishPoint;
	}

	public void setFinishPoint(Location finishPoint) {
		this.finishPoint = finishPoint;
	}


}
