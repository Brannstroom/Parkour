package no.brannstrom.Parkour.model;

import java.util.Date;
import java.util.UUID;

import org.bukkit.Location;

public class SParkour {
	
	private UUID id;

	private String name;
	
	Location joinLoc;
	
	Location startLoc;
	
	Location finishLoc;
	
	private Date updatedAt;
	
	private Date createdAt;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

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
