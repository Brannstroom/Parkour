package no.brannstrom.Parkour.handlers;

import net.md_5.bungee.api.ChatColor;

public class InfoKeeper {
	
	public static String parkourJoined = ChatColor.GREEN + "Du startet " + ChatColor.DARK_GREEN + "<parkour>" + ChatColor.GREEN + " parkouren.";
	public static String parkourCreated = ChatColor.GREEN + "Du lagde en parkour med navnet " + ChatColor.DARK_GREEN + "<parkour>" + ChatColor.GREEN + ". Bruk settp, setstart og setfinish for å gjøre ferdig parkouren.";
	public static String setParkourJoinLocation = ChatColor.GREEN + "Du satt tp lokasjon for " + ChatColor.DARK_GREEN + "<parkour>" + ChatColor.GREEN + " parkouren.";
	public static String setParkourStartLocation = ChatColor.GREEN + "Du satt start lokasjon for " + ChatColor.DARK_GREEN + "<parkour>" + ChatColor.GREEN + " parkouren.";
	public static String setParkourFinishLocation = ChatColor.GREEN + "Du satt slutt lokasjon for " + ChatColor.DARK_GREEN + "<parkour>" + ChatColor.GREEN + " parkouren.";
	public static String finishedParkour = ChatColor.GREEN + "Du fullførte " + ChatColor.DARK_GREEN + "<parkour>" + ChatColor.GREEN + " med tiden " + ChatColor.DARK_GREEN + "<time>" + ChatColor.GREEN + ".";
	public static String noParkours = ChatColor.GREEN + "Ingen parkour eksisterer. Lag en parkour med /parkour create <navn>.";
	public static String improvedTime = ChatColor.GREEN + "Du fullførte " + ChatColor.DARK_GREEN + "<parkour>" + ChatColor.GREEN + " med tiden " + ChatColor.DARK_GREEN + "<time>" + ChatColor.GREEN + ", en forbedring på " + ChatColor.DARK_GREEN + "<improvement>" + ChatColor.GREEN + ".";
	public static String parkourStartedHotbar = ChatColor.GREEN + "Du startet parkouren";
	public static String parkourFinishedHotbar = ChatColor.GREEN +  "Du fullførte parkouren";
	public static String parkourRemoved = ChatColor.GREEN + "Du fjernet " + ChatColor.DARK_GREEN + "<parkour>" + ChatColor.GREEN + " parkouren.";
	public static String newParkourRecord = "<player>" + ChatColor.GREEN + " satt ny rekord på " + ChatColor.DARK_GREEN + "<parkour>" + ChatColor.DARK_GREEN + " parkouren med en tid på " + ChatColor.DARK_GREEN + "<time>" + ChatColor.GREEN + ", en forbedring på " + ChatColor.DARK_GREEN + "<improvement>" + ChatColor.GREEN + ".";
	public static String permission = ChatColor.RED + "Du har ikke tillatelse til å utføre denne kommandoen!";
	public static String parkourDontExist = ChatColor.RED + "Parkouren " + ChatColor.DARK_RED + "<parkour>" + ChatColor.RED + " eksisterer ikke.";
	public static String parkourAlreadyExist = ChatColor.RED + "Parkouren " + ChatColor.DARK_RED + "<parkour>" + ChatColor.RED + " eksisterer allerede.";
	
}
