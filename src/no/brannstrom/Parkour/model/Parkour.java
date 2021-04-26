package no.brannstrom.Parkour.model;

import java.util.Date;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Parkour {
	
	@JsonIgnore 
	private UUID uuid;

	@JsonIgnore 
	private String name;
	
	@JsonIgnore 
	private String joinLocation;

	@JsonIgnore 
	private String startLocation;
	
	@JsonIgnore 
	private String finishLocation;
	
	@JsonIgnore 
	private Date updatedAt;

	@JsonIgnore 
	private Date createdAt;

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getJoinLocation() {
		return joinLocation;
	}
	
	public Location getJoinLoc() {
		Serialize serialize = new Serialize();
		Location location = serialize.deserialized(joinLocation);
		return location;
	}

	public void setJoinLocation(Location joinLocation) {
		Serialize serialize = new Serialize();
		Bukkit.broadcastMessage("Location:" + joinLocation.toString());
		String location = serialize.serialize(joinLocation);
		Bukkit.broadcastMessage("Serialized Location: " + location); 
		this.joinLocation = location;
		
	}
	
	public String getStartLocation() {
		return startLocation;
	}

	public Location getStartLoc() {
		Serialize serialize = new Serialize();
		Location location = serialize.deserialized(startLocation);
		return location;
	}

	public void setStartLocation(Location startLocation) {
		Serialize serialize = new Serialize();
		String location = serialize.serialize(startLocation);
		this.startLocation = location;
	}

	public String getFinishLocation() {
		return finishLocation;
	}
	
	public Location getFinishLoc() {
		Bukkit.broadcastMessage("ADK.1");
		Serialize serialize = new Serialize();
		Bukkit.broadcastMessage("ADK.2");
		Location location = serialize.deserialized(finishLocation);
		Bukkit.broadcastMessage("ADK.3");
		return location;
	}

	public void setFinishLocation(Location finishLocation) {
		Serialize serialize = new Serialize();
		String location = serialize.serialize(finishLocation);
		this.finishLocation = location;
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
