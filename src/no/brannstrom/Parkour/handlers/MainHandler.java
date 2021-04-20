package no.brannstrom.Parkour.handlers;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import no.brannstrom.Parkour.model.User;


public class MainHandler {

	public static void sendActionBar(Player p, String message) {
		p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
	}
	
	public static String formatTime(long millis) {
		return new SimpleDateFormat("mm:ss:SSS").format(new Date(millis));
	}
	
	public static String getPrefixName(User user) {
		if (user.getUrank() > 1 || user.getDrank() >= System.currentTimeMillis()/1000) {
			return getPrefix(user) + " " + user.getUsername();
		} else {
			return getPrefix(user) + user.getUsername();
		}
	}

	public static String getPrefix(User user) {
		return getPrefix(user.getUrank(), user.getDrank());
	}

	public static String getPrefix(int rank, int donor) {
		if (rank == 3) {
			return getRankColor(rank, donor) + "[Bygger]";
		} else if (rank == 5) {
			return getRankColor(rank, donor) + "[Mod]";
		} else if (rank == 10) {
			return getRankColor(rank, donor) + "[Admin]";
		} else if (donor >= System.currentTimeMillis()/1000) {
			return getRankColor(rank, donor) + "[+]";
		} else {
			return getRankColor(rank, donor) + "";
		}
	}

	public static String getPrefixWithUser(int rank, int donor) {
		if (rank == 3) {
			return getRankColor(rank, donor) + "[Bygger]";
		} else if (rank == 5) {
			return getRankColor(rank, donor) + "[Mod]";
		} else if (rank == 10) {
			return getRankColor(rank, donor) + "[Admin]";
		} else if (donor >= System.currentTimeMillis()/1000) {
			return getRankColor(rank, donor) + "[+]";
		} else {
			return getRankColor(rank, donor) + "Bruker";
		}
	}

	public static String getPrefixNoColor(int rank, int donor) {
		if (rank == 3) {
			return "[Bygger]";
		} else if (rank == 5) {
			return "[Mod]";
		} else if (rank == 10) {
			return "[Admin]";
		} else if (donor >= System.currentTimeMillis()/1000) {
			return "[+]";
		} else {
			return "";
		}
	}

	public static ChatColor getRankColor(User user) {
		return getRankColor(user.getUrank(), user.getDrank());
	}

	public static ChatColor getRankColor(int rank, int donor) {
		if (rank == 3) {
			return ChatColor.LIGHT_PURPLE;
		} else if (rank == 5) {
			return ChatColor.BLUE;
		} else if (rank == 10) {
			return ChatColor.GOLD;
		} else if (donor >= System.currentTimeMillis()/1000) {
			return ChatColor.DARK_PURPLE;
		} else {
			return ChatColor.RESET;
		}
	}
}
