package no.brannstrom.Parkour.handlers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import no.brannstrom.Parkour.model.Parkour;
import no.brannstrom.Parkour.model.Serialize;
import no.brannstrom.Parkour.model.User;

public class MainHandler {

	public static void sendActionBar(Player p, String message) {
		p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
	}
	
	public static String formatTime(long millis) { 
        long minutes = (millis / 1000) / 60;
        long seconds = (millis / 1000) % 60;      
        long milliseconds = millis % 1000;

        String min = "";
        String sec = "";
        String milli = "";
        
		if(minutes < 10) {
			min += "0";
		}
		if(seconds < 10) {
			sec += "0";
		}
		if(milliseconds < 100) {
			milli += "0";
		}
		if(milliseconds < 10) {
			milli += "0";
		}
		
		min += minutes;
		sec += seconds;
		milli += milliseconds;
        
		return min+":"+sec+"."+milli;
	}
	
	public static void broadcastMessage(Parkour parkour, String msg){
		
		Serialize serialize = new Serialize();
		Location location = serialize.deserialize(parkour.getJoinLocation());
		
		for (Player p : Bukkit.getServer().getOnlinePlayers()){
			try {
				if (p.getLocation().distance(location) <= 75){
					p.sendMessage(msg);
				}
			} catch (Exception ex){}
		}
	}
	
	public static String colorText(ChatColor chatColor, String string) {
		String[] args = string.split(" ");
		String result = "";
		for(String str : args) {
			result += chatColor + str + " ";
		}

		return result;
	}
	
	public static String getCombatTimer(Player player) {
		long timeLeft = MemoryHandler.combatTimer.get(player.getUniqueId()) - System.currentTimeMillis();
		return ChatColor.RED + "Du er i kamp! Vent " + ChatColor.DARK_RED + parseLong(timeLeft, false) + ChatColor.RED + " før du kan teleportere!";
	}
	
	public static String parseLong(long milliseconds, boolean abbreviate) {
		List<String> units = new ArrayList<String>();
		long amount;
		if(milliseconds < 999) return "1 sekund";
		amount = milliseconds / (7 * 24 * 60 * 60 * 1000);
		units.add(amount + "u");
		amount = milliseconds / (24 * 60 * 60 * 1000) % 7;
		units.add(amount + "d");
		amount = milliseconds / (60 * 60 * 1000) % 24;
		units.add(amount + "t");
		amount = milliseconds / (60 * 1000) % 60;
		units.add(amount + "m");
		amount = milliseconds / 1000 % 60;
		units.add(amount + "s");
		String[] array = new String[5];
		char end;
		for (String s : units) {
			end = s.charAt(s.length() - 1);
			switch (end) {
			case 'u':
				array[0] = s;
			case 'd':
				array[1] = s;
			case 't':
				array[2] = s;
			case 'm':
				array[3] = s;
			case 's':
				array[4] = s;
			}
		}
		units.clear();
		int i = 0;
		for (String s : array){
			if (!s.startsWith("0")){
				i++;
				if (i <= 2){
					units.add(s);
				}
			}
		}
		StringBuilder sb = new StringBuilder();
		String word, count, and;
		char c;
		for (String s : units) {
			if (!abbreviate) {
				c = s.charAt(s.length() - 1);
				count = s.substring(0, s.length() - 1);
				switch (c) {
				case 'u':
					word = "uke" + (count.equals("1") ? "" : "r");
					break;
				case 'd':
					word = "dag" + (count.equals("1") ? "" : "er");
					break;
				case 't':
					word = "time" + (count.equals("1") ? "" : "r");
					break;
				case 'm':
					word = "minutt" + (count.equals("1") ? "" : "er");
					break;
				default:
					word = "sekund" + (count.equals("1") ? "" : "er");
					break;
				}

				and = s.equals(units.get(units.size() - 1)) ? "" : s.equals(units.get(units.size() - 2)) ? " og " : ", ";
				sb.append(count + " " + word + and);
			} else {
				sb.append(s);
				if (!s.equals(units.get(units.size() - 1)))
					sb.append("-");
			}
		}
		return sb.toString().trim().length() == 0 ? null : sb.toString().trim();
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
