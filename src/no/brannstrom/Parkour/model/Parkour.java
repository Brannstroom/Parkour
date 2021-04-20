package no.brannstrom.Parkour.model;

import java.util.Date;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class Parkour {
	
	private UUID id;

	private String name;
	
	private int jointeleportx;
	private int jointeleporty;
	private int jointeleportz;
	private float jointeleportyaw;
	private float jointeleportpitch;
	
	private int startpointx;
	private int startpointy;
	private int startpointz;
	private float startpointyaw;
	private float startpointpitch;
	
	private int finishpointx;
	private int finishpointy;
	private int finishpointz;
	private float finishpointyaw;
	private float finishpointpitch;
	
	private Date updatedAt;
	
	private Date createdAt;
	
	public int getJoinTeleportX() {
		return jointeleportx;
	}

	public void setJoinTeleportX(int joinTeleportX) {
		this.jointeleportx = joinTeleportX;
	}

	public int getJoinTeleportY() {
		return jointeleporty;
	}

	public void setJoinTeleportY(int joinTeleportY) {
		this.jointeleporty = joinTeleportY;
	}

	public int getJoinTeleportZ() {
		return jointeleportz;
	}

	public void setJoinTeleportZ(int joinTeleportZ) {
		this.jointeleportz = joinTeleportZ;
	}

	public float getJoinTeleportYaw() {
		return jointeleportyaw;
	}

	public void setJoinTeleportYaw(float joinTeleportYaw) {
		this.jointeleportyaw = joinTeleportYaw;
	}

	public float getJoinTeleportPitch() {
		return jointeleportpitch;
	}

	public void setJoinTeleportPitch(float joinTeleportPitch) {
		this.jointeleportpitch = joinTeleportPitch;
	}

	public int getStartPointX() {
		return startpointx;
	}

	public void setStartPointX(int startPointX) {
		this.startpointx = startPointX;
	}

	public int getStartPointY() {
		return startpointy;
	}

	public void setStartPointY(int startPointY) {
		this.startpointy = startPointY;
	}

	public int getStartPointZ() {
		return startpointz;
	}

	public void setStartPointZ(int startPointZ) {
		this.startpointz = startPointZ;
	}

	public float getStartPointYaw() {
		return startpointyaw;
	}

	public void setStartPointYaw(float startPointYaw) {
		this.startpointyaw = startPointYaw;
	}

	public float getStartPointPitch() {
		return startpointpitch;
	}

	public void setStartPointPitch(float startPointPitch) {
		this.startpointpitch = startPointPitch;
	}

	public int getFinishPointX() {
		return finishpointx;
	}

	public void setFinishPointX(int finishPointX) {
		finishpointx = finishPointX;
	}

	public int getFinishPointY() {
		return finishpointy;
	}

	public void setFinishPointY(int finishPointY) {
		finishpointy = finishPointY;
	}

	public int getFinishPointZ() {
		return finishpointz;
	}

	public void setFinishPointZ(int finishPointZ) {
		finishpointz = finishPointZ;
	}

	public float getFinishPointYaw() {
		return finishpointyaw;
	}

	public void setFinishPointYaw(float finishPointYaw) {
		finishpointyaw = finishPointYaw;
	}

	public float getFinishPointPitch() {
		return finishpointpitch;
	}

	public void setFinishPointPitch(float finishPointPitch) {
		finishpointpitch = finishPointPitch;
	}

	public Location getJoinTeleport() {
		return new Location(Bukkit.getWorlds().get(0),jointeleportx,jointeleporty,jointeleportz,jointeleportyaw,jointeleportpitch);
	}
	
	public Location getStartPoint() {
		return new Location(Bukkit.getWorlds().get(0),startpointx,startpointy,startpointz,startpointyaw,startpointpitch);
	}
	
	public Location getFinishPoint() {
		return new Location(Bukkit.getWorlds().get(0),finishpointx,finishpointy,finishpointz,finishpointyaw,finishpointpitch);
	}
	
	public void setJoinTeleport(Location location) {
		setJoinTeleportX(location.getBlockX());
		setJoinTeleportY(location.getBlockY());
		setJoinTeleportZ(location.getBlockZ());
		setJoinTeleportYaw(location.getYaw());
		setJoinTeleportPitch(location.getPitch());
	}
	
	public void setStartPoint(Location location) {
		setStartPointX(location.getBlockX());
		setStartPointY(location.getBlockY());
		setStartPointZ(location.getBlockZ());
		setStartPointYaw(location.getYaw());
		setStartPointPitch(location.getPitch());
	}
	
	public void setFinishPoint(Location location) {
		setFinishPointX(location.getBlockX());
		setFinishPointY(location.getBlockY());
		setFinishPointZ(location.getBlockZ());
		setFinishPointYaw(location.getYaw());
		setFinishPointPitch(location.getPitch());
	}

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
