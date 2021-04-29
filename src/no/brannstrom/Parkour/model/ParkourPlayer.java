package no.brannstrom.Parkour.model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter @Setter @EqualsAndHashCode(of = {"uuid"})
public class ParkourPlayer {

	private UUID uuid;
	
	private Long startTime;

	private Parkour parkour;
	
	private int taskId;
	
}
