package uk.co.harieo.GamesCore.scoreboards;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

import java.util.*;
import uk.co.harieo.GamesCore.games.Game;

public class GameBoard {

	private String displayName;
	private DisplaySlot slot;
	private Map<UUID, Scoreboard> scoreboards = new HashMap<>();
	private Map<UUID, Objective> objectives = new HashMap<>();
	private Map<UUID, BukkitRunnable> runnables = new HashMap<>();
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
	 * @param game that is using this Scoreboard
	 * @param player that this scoreboard is being used for
	 * @param time in ticks for every update of the scoreboard
	 */
	public void render(Game game, Player player, int time) {
		UUID uuid = player.getUniqueId();

		ScoreboardManager manager = Bukkit.getScoreboardManager();
		Scoreboard scoreboard;
		Objective objective;

		if (scoreboards.containsKey(uuid)) {
			scoreboard = scoreboards.get(uuid);
		} else {
			scoreboard = manager.getNewScoreboard();
			scoreboards.put(uuid, scoreboard);
		}

		if (objectives.containsKey(uuid)) {
			objective = objectives.get(uuid);
		} else {
			objective = scoreboard.registerNewObjective("main", "dummy", displayName);
			objective.setDisplaySlot(slot);
			objectives.put(uuid, objective);
		}

		BukkitRunnable runnable = new BukkitRunnable() {
			@Override
			public void run() {
				if (!player.isOnline()) {
					cancel();
					runnables.remove(player.getUniqueId());
					return;
				}

				elements.forEach((line, element) -> {
					Score score = objective.getScore(element.getText(player));
					score.setScore(line);
					player.setScoreboard(scoreboard);
				});
			}
		};
		runnable.runTaskTimer(game.getPlugin(), 0, time);
		runnables.put(uuid, runnable);
	}

	/**
	 * Stops updating the Scoreboard for the stated Player. Note: This does not reset their scoreboard.
	 *
	 * @param player to cancel for
	 */
	public void cancelScoreboard(Player player) {
		if (runnables.containsKey(player.getUniqueId())) {
			runnables.get(player.getUniqueId()).cancel();
			runnables.remove(player.getUniqueId());
		}
	}

}
