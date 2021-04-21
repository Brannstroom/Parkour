package no.brannstrom.Parkour.model;

import java.util.List;

public class ParkourStats {
	
	List<String> names;
	
	List<Long> times;

	public List<String> getNames() {
		return names;
	}

	public void setNames(List<String> names) {
		this.names = names;
	}

	public List<Long> getTimes() {
		return times;
	}

	public void setTimes(List<Long> times) {
		this.times = times;
	}
	
}
