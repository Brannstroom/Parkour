package no.brannstrom.Parkour.handlers;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class MainHandler {

	public static void sendActionBar(Player p, String message) {
		p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
	}
	
	public static String formatTime(long millis) {
		return new SimpleDateFormat("mm:ss:SSS").format(new Date(millis));
	}
}
