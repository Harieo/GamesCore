package uk.co.harieo.GamesCore.chat;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import net.md_5.bungee.api.ChatColor;

public interface ChatModule extends Listener {

	String getPrefix();

	default String formatSystemMessage(String message) {
		return getPrefix() + " " + ChatColor.WHITE + message;
	}

	@EventHandler
	void announcePlayerJoin(PlayerJoinEvent event);

}
