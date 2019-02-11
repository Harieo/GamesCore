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
					team.setPrefix(elements.get(slot).getText(player));
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
