package no.brannstrom.Parkour.model;

import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter @Setter @EqualsAndHashCode(of = {"uuid"})
public class Parkour {
	
	private UUID uuid;

	private String name;
	
	private String joinLocation;

	private String startLocation;
	
	private String finishLocation;
	
	private Date updatedAt;

	private Date createdAt;
}
