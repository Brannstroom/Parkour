package no.brannstrom.Parkour.model;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class Serialize {
	
	public String serialize(Location location) {
		Bukkit.broadcastMessage("inserialize");
		Bukkit.broadcastMessage(location.getWorld().getName()+";"+location.getBlockX()+";"+location.getBlockY()+";"+location.getBlockZ()+";"+location.getYaw()+";"+location.getPitch());
		return location.getWorld().getName()+";"+location.getBlockX()+";"+location.getBlockY()+";"+location.getBlockZ()+";"+location.getYaw()+";"+location.getPitch();
	}
	
	public Location deserialized(String serialized) {
		Bukkit.broadcastMessage("POP.1");

		Bukkit.broadcastMessage("Ser: " + serialized);
		String[] stringList = serialized.split(";");
		Bukkit.broadcastMessage("POP.2");
		Bukkit.broadcastMessage(serialized);
		Bukkit.broadcastMessage("0: " + stringList[0]);
		Bukkit.broadcastMessage("1: " + stringList[1]);
		Bukkit.broadcastMessage("2: " + stringList[2]);
		Bukkit.broadcastMessage("3: " + stringList[3]);
		Bukkit.broadcastMessage("4: " + stringList[4]);
		Bukkit.broadcastMessage("5: " + stringList[5]);
		
		Location location = new Location(Bukkit.getWorld(stringList[0]),Integer.valueOf(stringList[1]),Integer.valueOf(stringList[2]),Integer.valueOf(stringList[3]),Float.valueOf(stringList[4]),Float.valueOf(stringList[5]));
		Bukkit.broadcastMessage("POP.3");
		return location;
	}

}