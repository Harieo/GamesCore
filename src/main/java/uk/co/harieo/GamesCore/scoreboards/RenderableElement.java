package uk.co.harieo.GamesCore.scoreboards;

import org.bukkit.entity.Player;

public interface RenderableElement {

	/**
	 * @param player that this element is being rendered for
	 * @return the text this element will display to the player
	 */
	String getText(Player player);

}
