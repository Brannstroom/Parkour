package no.brannstrom.Parkour;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import no.brannstrom.Parkour.handlers.MemoryHandler;
import no.brannstrom.Parkour.listeners.ParkourListener;
import no.brannstrom.Parkour.model.Parkour;
import no.brannstrom.Parkour.model.ParkourStats;

public class ParkourPlugin extends JavaPlugin {
	
	public static ParkourPlugin instance;
	
	public FileConfiguration config;

	public File parkoursFile;
	public FileConfiguration parkours;
	
	public File parkourStatsFile;
	public FileConfiguration parkourStats;
	
	public void onEnable() {
		instance = this;
		loadListeners();
		
		config = getConfig();
		saveDefaultConfig();

		createParkourConfig();
		createParkourStatsConfig();
		
		saveParkourConfig();
		saveParkourStatsConfig();
	}

	public void onDisable() {
		restoreParkourConfig();
		restoreParkourStatsConfig();
	}

	private void loadListeners() {
		getServer().getPluginManager().registerEvents(new ParkourListener(), this);
	}
	
	public void createParkourConfig() {
		parkoursFile = new File(getDataFolder(), "parkours.yml");
		if(!parkoursFile.exists()) {
			parkoursFile.getParentFile().mkdirs();
			saveResource("parkours.yml", true);
		}

		parkours = new YamlConfiguration();
		try {
			parkours.load(parkoursFile);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}

	}

	public void saveParkourConfig() {
		try { 
			parkours.getConfigurationSection("parkours").getKeys(false).forEach(key -> {
				parkours.set("parkours." + key + ".join.world", null);
				parkours.set("parkours." + key + ".join.x", null);
				parkours.set("parkours." + key + ".join.y", null);
				parkours.set("parkours." + key + ".join.z", null);
				
				parkours.set("parkours." + key + ".startpoint.world", null);
				parkours.set("parkours." + key + ".startpoint.x", null);
				parkours.set("parkours." + key + ".startpoint.y", null);
				parkours.set("parkours." + key + ".startpoint.z", null);
				
				parkours.set("parkours." + key + ".finishpoint.world", null);
				parkours.set("parkours." + key + ".finishpoint.x", null);
				parkours.set("parkours." + key + ".finishpoint.y", null);
				parkours.set("parkours." + key + ".finishpoint.z", null);
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		for(Parkour parkour : MemoryHandler.parkours) {
			String name = parkour.getName();
			Location joinTeleport = parkour.getJoinTeleport();
			Location startPoint = parkour.getStartPoint();
			Location finishPoint = parkour.getFinishPoint();
			
			parkours.set("parkours." + name + ".join.world", joinTeleport.getWorld().toString());
			parkours.set("parkours." + name + ".join.x", joinTeleport.getX());
			parkours.set("parkours." + name + ".join.y", joinTeleport.getY());
			parkours.set("parkours." + name + ".join.z", joinTeleport.getZ());
			
			parkours.set("parkours." + name + ".startpoint.world", startPoint.getWorld().toString());
			parkours.set("parkours." + name + ".startpoint.x", startPoint.getX());
			parkours.set("parkours." + name + ".startpoint.y", startPoint.getY());
			parkours.set("parkours." + name + ".startpoint.z", startPoint.getZ());
			
			parkours.set("parkours." + name + ".finishpoint.world", finishPoint.getWorld().toString());
			parkours.set("parkours." + name + ".finishpoint.x", finishPoint.getX());
			parkours.set("parkours." + name + ".finishpoint.y", finishPoint.getY());
			parkours.set("parkours." + name + ".finishpoint.z", finishPoint.getZ());
		}
		try {
			parkours.save(parkoursFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void restoreParkourConfig() {
		try {
			parkours.getConfigurationSection("parkours").getKeys(false).forEach(key -> {
				Parkour parkour = new Parkour();
				Location joinTeleport = new Location(Bukkit.getWorld(parkours.getString("parkours." + key + ".join.world")), parkours.getDouble("parkours." + key + ".join.x"), parkours.getDouble("parkours." + key + ".join.y"), parkours.getDouble("parkours." + key + ".join.z"));
				Location startPoint = new Location(Bukkit.getWorld(parkours.getString("parkours." + key + ".startpoint.world")), parkours.getDouble("parkours." + key + ".startpoint.x"), parkours.getDouble("parkours." + key + ".startpoint.y"), parkours.getDouble("parkours." + key + ".startpoint.z"));
				Location finishPoint = new Location(Bukkit.getWorld(parkours.getString("parkours." + key + ".finishpoint.world")), parkours.getDouble("parkours." + key + ".finishpoint.x"), parkours.getDouble("parkours." + key + ".finishpoint.y"), parkours.getDouble("parkours." + key + ".finishpoint.z"));

				parkour.setName(key);
				parkour.setJoinTeleport(joinTeleport);
				parkour.setStartPoint(startPoint);
				parkour.setFinishPoint(finishPoint);
				
				MemoryHandler.parkours.add(parkour);
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/* --------------------------------------------------- */
	
	public void createParkourStatsConfig() {
		parkourStatsFile = new File(getDataFolder(), "parkourStats.yml");
		if(!parkourStatsFile.exists()) {
			parkourStatsFile.getParentFile().mkdirs();
			saveResource("parkourStats.yml", true);
		}

		parkourStats = new YamlConfiguration();
		try {
			parkourStats.load(parkourStatsFile);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}

	}

	public void saveParkourStatsConfig() {
		try { 
			parkourStats.getConfigurationSection("parkourstats").getKeys(false).forEach(key -> {
				parkourStats.set("parkourstats." + key, null);
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		for(Map.Entry<String, ParkourStats> entry : MemoryHandler.parkourStats.entrySet()) {
			ParkourStats stats = entry.getValue();
			List<String> names = stats.getNames();
			List<Long> times = stats.getTimes();
			for(int i = 0; i < names.size(); i++) {
				parkourStats.set("parkourstats." + entry.getKey() + "." + names.get(i),times.get(i));
			}
		}
		try {
			parkourStats.save(parkourStatsFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void restoreParkourStatsConfig() {
		try {
			parkourStats.getConfigurationSection("parkourstats").getKeys(false).forEach(key -> {
				ParkourStats stats = new ParkourStats();
				List<String> names = new ArrayList<>();
				List<Long> times= new ArrayList<>();
				parkourStats.getConfigurationSection("parkourstats." + key).getKeys(false).forEach(name -> {
					long time = parkours.getLong("parkourstats." + key + "." + name);
					names.add(name);
					times.add(time);
				});
				MemoryHandler.parkourStats.put(key, stats);
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
