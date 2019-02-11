package uk.co.harieo.GamesCore.scoreboards;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

import java.util.HashMap;
import java.util.Map;

class GameBoardImpl {

	private Player player;
	private Map<Integer, RenderableElement> elements;
	private int updateTime;
	private Plugin plugin;

	private Scoreboard scoreboard;
	private Objective objective;
	private Map<Integer, Team> teams = new HashMap<>(15);

	private BukkitRunnable runnable;

	GameBoardImpl(Plugin plugin, Player player, Map<Integer, RenderableElement> elements, String displayName,
			DisplaySlot slot, int updateTime) {
		this.player = player;
		this.elements = elements;
		this.updateTime = updateTime;
		this.plugin = plugin;

		ScoreboardManager manager = Bukkit.getScoreboardManager();
		Scoreboard scoreboard = manager.getNewScoreboard();
		Objective objective = scoreboard.registerNewObjective("main", "dummy");
		objective.setDisplayName(displayName);
		objective.setDisplaySlot(slot);
		this.scoreboard = scoreboard;
		this.objective = objective;
	}

	void start() {
		for (int slot : elements.keySet()) {
			Team team = scoreboard.registerNewTeam("line" + slot);
			String lineColor = ChatColor.values()[slot].toString();
			team.addEntry(lineColor); // Same as the line it will be on
			teams.put(slot, team);
			objective.getScore(lineColor).setScore(slot);
		}

		player.setScoreboard(scoreboard);

		BukkitRunnable runnable = new BukkitRunnable() {
			@Override
			public void run() {
				if (!player.isOnline()) {
					cancel();
					return;
				}

				for (int slot : teams.keySet()) {
					Team team = teams.get(slot);

					// Teams only allow up to 16 chars so we'll split them in half and feed the second half into suffix
					String prefix = elements.get(slot).getText(player);
					String suffix = null; // Anything under 16 doesn't need editing
					if (prefix.length() > 32) { // Using scoreboard content isn't dynamic enough so max at 32
						cancel();
						throw new IllegalArgumentException("An element had more than 32 characters: " + prefix);
					} else if (prefix.length() > 16) { // Splitting is required now
						suffix = prefix.substring(15);
						prefix = prefix.substring(0, prefix.length() - suffix.length());
						suffix = ChatColor.getLastColors(prefix) + suffix; // Fix any color leakage due to the split
					}

					team.setPrefix(prefix);
					if (suffix != null) {
						team.setSuffix(suffix);
					}
				}
			}
		};
		runnable.runTaskTimer(plugin, 0, updateTime);
		this.runnable = runnable;
	}

	void stop() {
		runnable.cancel();
	}

}
