package no.brannstrom.Parkour.model;

import java.util.UUID;

public class ParkourPlayer {

	UUID uuid;
	
	Long startTime;

	SParkour parkour;
	
	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public SParkour getParkour() {
		return parkour;
	}

	public void setParkour(SParkour parkour) {
		this.parkour = parkour;
	}
}
