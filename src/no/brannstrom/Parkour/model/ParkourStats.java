package no.brannstrom.Parkour.model;

import java.util.Date;
import java.util.UUID;

public class ParkourStats {
	
	UUID id;
	
	UUID uuid;
	
	String parkourName;
	
	long time;
	
	private Date updatedAt;

	private Date createdAt;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public String getParkourName() {
		return parkourName;
	}

	public void setParkourName(String parkourName) {
		this.parkourName = parkourName;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

}