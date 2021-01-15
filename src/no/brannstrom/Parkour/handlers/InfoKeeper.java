package no.brannstrom.Parkour.handlers;

import org.bukkit.configuration.file.FileConfiguration;

import net.md_5.bungee.api.ChatColor;
import no.brannstrom.Parkour.ParkourPlugin;

public class InfoKeeper {
	
	static FileConfiguration config = ParkourPlugin.getPlugin(ParkourPlugin.class).getConfig();

	public static String adminPermission = config.getString("Admin Permission");
	
	public static String parkourJoined = ChatColor.translateAlternateColorCodes('&', config.getString("Parkour Joined"));
	public static String parkourCreated = ChatColor.translateAlternateColorCodes('&', config.getString("Parkour Created"));
	public static String setParkourJoinLocation = ChatColor.translateAlternateColorCodes('&', config.getString("Set Parkour Join Location"));
	public static String setParkourStartLocation = ChatColor.translateAlternateColorCodes('&', config.getString("Set Parkour Start Location"));
	public static String setParkourFinishLocation = ChatColor.translateAlternateColorCodes('&', config.getString("Set Parkour Finish Location"));
	public static String finishedParkour = ChatColor.translateAlternateColorCodes('&', config.getString("Finished Parkour"));
}
