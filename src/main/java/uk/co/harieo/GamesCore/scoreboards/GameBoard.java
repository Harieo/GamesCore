package uk.co.harieo.GamesCore.scoreboards;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Score;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GameBoard {

	private String displayName;
	private DisplaySlot slot;
	private Map<UUID, GameBoardImpl> impls = new HashMap<>();
	private Map<Integer, RenderableElement> elements = new HashMap<>();

	public GameBoard(String displayName, DisplaySlot displaySlot) {
		this.displayName = displayName;
		this.slot = displaySlot;
	}

	/**
	 * Sets a line on the scoreboard using {@link RenderableElement}
	 *
	 * Lines start from the top of the scoreboard, line 1 would be score 15 when referencing {@link Score}
	 *
	 * @param element to be set
	 */
	public void addLine(RenderableElement element) {
		int line = 15; // Start at the top and go down
		while (elements.containsKey(line)) {
			line--; // Decrease line until the line is not taken
		}

		if (line < 1) { // This is the limit for Scoreboard in Bukkit
			throw new IllegalStateException("Lines cannot exceed 15 in GameBoard");
		}

		elements.put(line, element);
	}

	/**
	 * Adds a blank instance of {@link ConstantElement} to act as white space
	 *
	 * Lines start from the top of the scoreboard, line 1 would be score 15 when referencing {@link Score}
	 */
	public void addBlankLine() {
		int line = 15; // Start at the top and go down
		while (elements.containsKey(line)) {
			line--; // Decrease line until the line is not taken
		}

		if (line < 1) { // This is the limit for Scoreboard in Bukkit
			throw new IllegalStateException("Lines cannot exceed 15 in GameBoard");
		}

		// Each blank line needs to be different so pick a unique colour using line number
		RenderableElement blankElement = new ConstantElement(ChatColor.values()[line].toString());
		elements.put(line, blankElement);
	}

	/**
	 * Clears all elements from this scoreboard
	 */
	public void clearLines() {
		elements.clear();
	}

	/**
	 * Sets all values into the Scoreboard and assigns it to the Player
	 *
	 * @param plugin that is using this Scoreboard
	 * @param player that this scoreboard is being used for
	 * @param time in ticks for every update of the scoreboard
	 */
	public void render(JavaPlugin plugin, Player player, int time) {
		GameBoardImpl impl = new GameBoardImpl(plugin, player, elements, displayName, slot, time);
		impls.put(player.getUniqueId(), impl);
		impl.start();
	}

	/**
	 * Stops updating the Scoreboard for the stated Player. Note: This does not reset their scoreboard.
	 *
	 * @param player to cancel for
	 */
	public void cancelScoreboard(Player player) {
		if (impls.containsKey(player.getUniqueId())) {
			impls.get(player.getUniqueId()).stop();
		}
	}

}
