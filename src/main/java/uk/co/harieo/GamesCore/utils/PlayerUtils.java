package uk.co.harieo.GamesCore.utils;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class PlayerUtils {

	/**
	 * Players a {@link Sound} to every online {@link Player} to their specific {@link org.bukkit.Location}
	 *
	 * @param sound to be played
	 * @param volume to play the sound at
	 * @param pitch to play the sound at
	 */
	public static void playLocalizedSound(Sound sound, float volume, float pitch) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			player.playSound(player.getLocation(), sound, volume, pitch);
		}
	}

}
