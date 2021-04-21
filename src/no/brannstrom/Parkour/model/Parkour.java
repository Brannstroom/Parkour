package no.brannstrom.Parkour.model;

import java.util.Date;
import java.util.UUID;

public class Parkour {
	
	private UUID id;

	private String name;
	
	private String coordinatesJoin;
	
	private String coordinatesStart;
	
	private String coordinatesFinish;
	
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

	public String getCoordinatesJoin() {
		return coordinatesJoin;
	}

	public void setCoordinatesJoin(String coordinatesJoin) {
		this.coordinatesJoin = coordinatesJoin;
	}

	public String getCoordinatesStart() {
		return coordinatesStart;
	}

	public void setCoordinatesStart(String coordinatesStart) {
		this.coordinatesStart = coordinatesStart;
	}

	public String getCoordinatesFinish() {
		return coordinatesFinish;
	}

	public void setCoordinatesFinish(String coordinatesFinish) {
		this.coordinatesFinish = coordinatesFinish;
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
