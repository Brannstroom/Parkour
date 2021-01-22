package no.brannstrom.Parkour;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.Lists;

import no.brannstrom.Parkour.commands.ParkourCommand;
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
		loadCommands();
		
		config = getConfig();
		saveDefaultConfig();

		createParkourConfig();
		createParkourStatsConfig();
		
		restoreParkourConfig();
		restoreParkourStatsConfig();
	}

	public void onDisable() {
		saveParkourConfig();
		saveParkourStatsConfig();
	}

	private void loadListeners() {
		getServer().getPluginManager().registerEvents(new ParkourListener(), this);
	}
	

	private void loadCommands() {
		registerCommand("parkour", new ParkourCommand());
	}
	
	public void registerCommand(String name, CommandExecutor executor, String... aliases) {
		try {
			Constructor<PluginCommand> constructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
			constructor.setAccessible(true);

			PluginCommand command = constructor.newInstance(name, this);

			command.setExecutor(executor);
			command.setAliases(Lists.newArrayList(aliases));
			if (executor instanceof TabCompleter) {
				command.setTabCompleter((TabCompleter) executor);
			}
			this.getCommandMap().register("bounty", command);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private CommandMap getCommandMap() {
		try {
			org.bukkit.Server server = Bukkit.getServer();
			Field commandMap = server.getClass().getDeclaredField("commandMap");
			commandMap.setAccessible(true);
			return (CommandMap) commandMap.get(server);
		} catch (IllegalAccessException | NoSuchFieldException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private void createParkourConfig() {
		parkoursFile = new File(getDataFolder(), "parkours.yml");
		if(!parkoursFile.exists()) {
			parkoursFile.getParentFile().mkdirs();
			saveResource("parkours.yml", false);
		}

		parkours = new YamlConfiguration();
		try {
			parkours.load(parkoursFile);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}

	private void saveParkourConfig() {
		try { 
			parkours.getConfigurationSection("parkours").getKeys(false).forEach(key -> {
				parkours.set("parkours." + key + ".join.world", null);
				parkours.set("parkours." + key + ".join.x", null);
				parkours.set("parkours." + key + ".join.y", null);
				parkours.set("parkours." + key + ".join.z", null);
				parkours.set("parkours." + key + ".join.pitch", null);
				parkours.set("parkours." + key + ".join.yaw", null);
				
				parkours.set("parkours." + key + ".startpoint.world", null);
				parkours.set("parkours." + key + ".startpoint.x", null);
				parkours.set("parkours." + key + ".startpoint.y", null);
				parkours.set("parkours." + key + ".startpoint.z", null);
				parkours.set("parkours." + key + ".startpoint.pitch", null);
				parkours.set("parkours." + key + ".startpoint.yaw", null);
				
				parkours.set("parkours." + key + ".finishpoint.world", null);
				parkours.set("parkours." + key + ".finishpoint.x", null);
				parkours.set("parkours." + key + ".finishpoint.y", null);
				parkours.set("parkours." + key + ".finishpoint.z", null);
				parkours.set("parkours." + key + ".finishpoint.pitch", null);
				parkours.set("parkours." + key + ".finishpoint.yaw", null);
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		for(Parkour parkour : MemoryHandler.parkours) {
			String name = parkour.getName();
			Location joinTeleport = parkour.getJoinTeleport();
			Location startPoint = parkour.getStartPoint();
			Location finishPoint = parkour.getFinishPoint();
			
			parkours.set("parkours." + name + ".join.world", joinTeleport.getWorld().getName());
			parkours.set("parkours." + name + ".join.x", joinTeleport.getX());
			parkours.set("parkours." + name + ".join.y", joinTeleport.getY());
			parkours.set("parkours." + name + ".join.z", joinTeleport.getZ());
			parkours.set("parkours." + name + ".join.pitch", joinTeleport.getPitch());
			parkours.set("parkours." + name + ".join.yaw", joinTeleport.getYaw());
			
			parkours.set("parkours." + name + ".startpoint.world", startPoint.getWorld().getName());
			parkours.set("parkours." + name + ".startpoint.x", startPoint.getX());
			parkours.set("parkours." + name + ".startpoint.y", startPoint.getY());
			parkours.set("parkours." + name + ".startpoint.z", startPoint.getZ());
			parkours.set("parkours." + name + ".startpoint.pitch", startPoint.getPitch());
			parkours.set("parkours." + name + ".startpoint.yaw", startPoint.getYaw());
			
			parkours.set("parkours." + name + ".finishpoint.world", finishPoint.getWorld().getName());
			parkours.set("parkours." + name + ".finishpoint.x", finishPoint.getX());
			parkours.set("parkours." + name + ".finishpoint.y", finishPoint.getY());
			parkours.set("parkours." + name + ".finishpoint.z", finishPoint.getZ());
			parkours.set("parkours." + name + ".finishpoint.pitch", finishPoint.getPitch());
			parkours.set("parkours." + name + ".finishpoint.yaw", finishPoint.getYaw());
		}
		try {
			parkours.save(parkoursFile);
		} catch (IOException e) {
			System.out.println("[Parkour] Error: Parkours not saved becuase of error, or because there was no parkours to be saved.");
		}
	}

	private void restoreParkourConfig() {
		try {
			parkours.getConfigurationSection("parkours").getKeys(false).forEach(key -> {
				Parkour parkour = new Parkour();
				
				Location joinTeleport = new Location(Bukkit.getWorld(parkours.getString("parkours." + key + ".join.world")),
						parkours.getDouble("parkours." + key + ".join.x"),
						parkours.getDouble("parkours." + key + ".join.y"),
						parkours.getDouble("parkours." + key + ".join.z"),
						Float.valueOf(String.valueOf(parkours.getDouble("parkours." + key + ".join.yaw"))),
						Float.valueOf(String.valueOf(parkours.getDouble("parkours." + key + ".join.pitch"))));
				
				Location startPoint = new Location(Bukkit.getWorld(parkours.getString("parkours." + key + ".startpoint.world")),
						parkours.getDouble("parkours." + key + ".startpoint.x"),
						parkours.getDouble("parkours." + key + ".startpoint.y"),
						parkours.getDouble("parkours." + key + ".startpoint.z"),
						Float.valueOf(String.valueOf(parkours.getDouble("parkours." + key + ".startpoint.yaw"))),
						Float.valueOf(String.valueOf(parkours.getDouble("parkours." + key + ".startpoint.pitch"))));
				
				Location finishPoint = new Location(Bukkit.getWorld(parkours.getString("parkours." + key + ".finishpoint.world")),
						parkours.getDouble("parkours." + key + ".finishpoint.x"),
						parkours.getDouble("parkours." + key + ".finishpoint.y"),
						parkours.getDouble("parkours." + key + ".finishpoint.z"),
						Float.valueOf(String.valueOf(parkours.getDouble("parkours." + key + ".finishpoint.yaw"))),
						Float.valueOf(String.valueOf(parkours.getDouble("parkours." + key + ".finishpoint.pitch"))));
				
				parkour.setName(key);
				parkour.setJoinTeleport(joinTeleport);
				parkour.setStartPoint(startPoint);
				parkour.setFinishPoint(finishPoint);
				
				MemoryHandler.parkours.add(parkour);
			});
		} catch (Exception e) {
			System.out.println("[Parkour] Error: Parkour config not restores because of error, or because no parkours exist.");
		}
	}
	
	/* --------------------------------------------------- */
	
	private void createParkourStatsConfig() {
		parkourStatsFile = new File(getDataFolder(), "parkourstats.yml");
		if(!parkourStatsFile.exists()) {
			parkourStatsFile.getParentFile().mkdirs();
			saveResource("parkourstats.yml", false);
		}

		parkourStats = new YamlConfiguration();
		try {
			parkourStats.load(parkourStatsFile);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}

	private void saveParkourStatsConfig() {
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
			System.out.println("[Parkour] Error: Parkourstats config not saved because of error, or because there was no parkourstats to be saved.");
		}
	}

	private void restoreParkourStatsConfig() {
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
			System.out.println("[Parkour] Error: Parkourstats config not restores because of error, or because no parkourstats exist.");
		}
	}
	
}
