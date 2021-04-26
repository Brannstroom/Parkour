package no.brannstrom.Parkour.model;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class Serialize {
	
	public String serialize(Location location) {
		return location.getWorld().getName()+";"+location.getBlockX()+";"+location.getBlockY()+";"+location.getBlockZ()+";"+location.getYaw()+";"+location.getPitch();
	}
	
	public Location deserialize(String serialized) {
		String[] stringList = serialized.split(";");
		
		return new Location(Bukkit.getWorld(stringList[0]),Integer.valueOf(stringList[1]),Integer.valueOf(stringList[2]),Integer.valueOf(stringList[3]),Float.valueOf(stringList[4]),Float.valueOf(stringList[5]));
	}

}