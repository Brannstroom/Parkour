package no.brannstrom.Parkour.handlers;

import net.md_5.bungee.api.ChatColor;

public class InfoKeeper {
	
	public static String parkourJoined = ChatColor.GREEN + "Du startet " + ChatColor.DARK_GREEN + ChatColor.BOLD + "<parkour>" + ChatColor.RESET + ChatColor.GREEN + " parkouren.";
	public static String parkourCreated = ChatColor.GREEN + "Du lagde en parkour med navnet " + ChatColor.DARK_GREEN + ChatColor.BOLD + "<parkour>" + ChatColor.RESET + ChatColor.GREEN + ". Bruk settp, setstart, setfinish og createholo for å gjøre ferdig parkouren.";
	public static String setParkourJoinLocation = ChatColor.GREEN + "Du satt tp lokasjon for " + ChatColor.DARK_GREEN + ChatColor.BOLD + "<parkour>" + ChatColor.RESET + ChatColor.GREEN + " parkouren.";
	public static String setParkourStartLocation = ChatColor.GREEN + "Du satt start lokasjon for " + ChatColor.DARK_GREEN + ChatColor.BOLD + "<parkour>" + ChatColor.RESET + ChatColor.GREEN + " parkouren.";
	public static String setParkourFinishLocation = ChatColor.GREEN + "Du satt slutt lokasjon for " + ChatColor.DARK_GREEN + ChatColor.BOLD + "<parkour>" + ChatColor.RESET + ChatColor.GREEN + " parkouren.";
	public static String finishedParkour = ChatColor.GREEN + "Du fullførte " + ChatColor.DARK_GREEN + ChatColor.BOLD + "<parkour>" + ChatColor.RESET + ChatColor.GREEN + " med tiden " + ChatColor.DARK_GREEN + "<time>" + ChatColor.GREEN + ".";
	public static String noParkours = ChatColor.GREEN + "Ingen parkour eksisterer. Lag en parkour med /parkour create <navn>.";
	public static String improvedTime = ChatColor.GREEN + "Du fullførte " + ChatColor.DARK_GREEN + ChatColor.BOLD + "<parkour>" + ChatColor.RESET + ChatColor.GREEN + " med tiden " + ChatColor.RESET + "<time>" + ChatColor.GREEN + " og forbedret din forrige rekord med " + ChatColor.RESET + "<improvement>" + ChatColor.GREEN + ".";
	public static String parkourStartedHotbar = ChatColor.GREEN + "Du startet parkouren";
	public static String parkourFinishedHotbar = ChatColor.GREEN +  "Du fullførte parkouren";
	public static String parkourRemoved = ChatColor.GREEN + "Du fjernet " + ChatColor.DARK_GREEN + ChatColor.BOLD + "<parkour>" + ChatColor.RESET + ChatColor.GREEN + " parkouren.";
	public static String newParkourRecord = "<player>" + ChatColor.GREEN + " satt ny rekord på " + ChatColor.DARK_GREEN + ChatColor.BOLD + "<parkour>" + ChatColor.RESET + ChatColor.GREEN + " parkouren med en tid på " + ChatColor.RESET + "<time>" + ChatColor.GREEN + ", en forbedring på " + ChatColor.RESET + "<improvement>" + ChatColor.GREEN + ".";
	public static String firstRecord = "<player>" + ChatColor.GREEN + " var den første til å fullføre " + ChatColor.DARK_GREEN + ChatColor.BOLD + "<parkour>" + ChatColor.RESET + ChatColor.GREEN + " parkouren, og gjorde det med tiden " + ChatColor.RESET + "<time>" + ChatColor.GREEN + ".";
	public static String firstTimeFinishingParkour = ChatColor.GREEN + "Du fullførte " + ChatColor.DARK_GREEN + ChatColor.BOLD + "<parkour>" + ChatColor.RESET + ChatColor.GREEN + " for første gang med en tid på " + ChatColor.RESET + "<time>" + ChatColor.GREEN + ".";
	public static String permission = ChatColor.RED + "Du har ikke tillatelse til å utføre denne kommandoen!";
	public static String parkourDontExist = ChatColor.RED + "Parkouren " + ChatColor.DARK_RED + ChatColor.BOLD + "<parkour>" + ChatColor.RESET + ChatColor.RED + " eksisterer ikke.";
	public static String parkourAlreadyExist = ChatColor.RED + "Parkouren " + ChatColor.DARK_RED + ChatColor.BOLD + "<parkour>" + ChatColor.RESET + ChatColor.RED + " eksisterer allerede.";
	
}
