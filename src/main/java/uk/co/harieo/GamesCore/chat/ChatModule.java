package uk.co.harieo.GamesCore.chat;

import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import net.md_5.bungee.api.ChatColor;

public interface ChatModule extends Listener {

	/**
	 * @return the prefix to be used before every system message
	 */
	String getPrefix();

	/**
	 * Formats the String on the assumption that the String is an automated system message. Defaults to {@link
	 * #getPrefix()} + single character space + message.
	 */
	default String formatSystemMessage(String message) {
		return getPrefix() + " " + ChatColor.WHITE + message;
	}

	/**
	 * A method to handle announcing a player joining, which is a requirement for all games
	 *
	 * @param event to be handled by this module
	 */
	void announcePlayerJoin(PlayerJoinEvent event);

}
