package no.brannstrom.Parkour;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.Lists;

import no.brannstrom.Parkour.commands.ParkourCommand;
import no.brannstrom.Parkour.listeners.ParkourListener;

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
		
		getServer().getMessenger().registerOutgoingPluginChannel(this, "spillere:bungee");
	}

	public void onDisable() {
		
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
			this.getCommandMap().register("parkour", command);
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
	
}
