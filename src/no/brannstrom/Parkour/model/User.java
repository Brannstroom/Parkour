package no.brannstrom.Parkour.model;

import java.util.Date;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter @Setter @EqualsAndHashCode(of = {"uuid"})
public class User {

	private int id;

	private UUID uuid;

	private String username;

	private int urank;

	//Time in millis
	private int drank;

	private boolean vanishEnabled;

	private boolean socialAgentEnabled;

	private Date logoutTime;

	private Date updatedAt;

	private Date createdAt;
	
	public boolean isDonor() {
		if(getDrank() >= System.currentTimeMillis()/1000) return true;
		else return false;
	}
	
	public boolean isStab() {
		if(getUrank() >= 5) return true;
		else return false;
	}
	
	public boolean isAdmin() {
		if(getUrank() == 10) return true;
		else return false;
	}
}