package uk.co.harieo.GamesCore.timers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.function.Consumer;
import uk.co.harieo.GamesCore.games.Game;

public class GameStartTimer extends BukkitRunnable {

	private int timeInSeconds; // This is the main count being used by this timer
	private int lastPlayerCount; // This is so that if players leave between runs, it can be detected

	private final Game game; // Associated with this timer
	private final int longerTime; // For when players are more than minimum but less than higher bound
	private final int shorterTime; // For when players are more than higher bound

	private Consumer<Integer> onTimeReduction; // To perform a function when the timeInSeconds is reduced
	private Consumer<Integer> onTimeIncrease; // To perform a function when the timeInSeconds is increased
	private Consumer<Integer> onRun; // Adds custom functions to run() after normal checks have been made
	private Consumer<Void> onTimerEnd; // To begin a process when the timer ends

	/**
	 * A timer to be used to determine when the game should begin from being in the Lobby state
	 *
	 * @param game that this timer is being used for to determine acceptable player counts
	 * @param longerTimeSeconds time in seconds to be used when the player count is below higher bound
	 * @param shorterTimeSeconds time in seconds to be used when the player count is above higher bound
	 */
	public GameStartTimer(Game game, int longerTimeSeconds, int shorterTimeSeconds) {
		this.game = game;
		this.longerTime = longerTimeSeconds;
		this.shorterTime = shorterTimeSeconds;

		this.lastPlayerCount = Bukkit.getOnlinePlayers().size();
		// Player counts above the higher bound reduces timer to shorter time as the game is reasonably full
		if (lastPlayerCount >= game.getMaximumPlayers() || lastPlayerCount >= game.getHigherBoundPlayerAmount()) {
			timeInSeconds = shorterTimeSeconds;
		} else {
			timeInSeconds = longerTimeSeconds;
		}
	}

	/**
	 * Begins the timer, counting down with an interval of 20 ticks
	 */
	public void beginTimer() {
		runTaskTimer(game.getPlugin(), 20, 20);
	}

	/**
	 * @return the time left on this timer in seconds
	 */
	public int getTimeLeft() {
		return timeInSeconds;
	}

	@Override
	public void run() {
		int currentPlayerCount = Bukkit.getOnlinePlayers().size();

		// Check if the timer has now ended //
		if (timeInSeconds == 0) {
			if (onTimerEnd != null) {
				onTimerEnd.accept(null);
			}
			cancel();
			return;
		}

		// Check for conditions that require the time to be changed more than 1 second //
		if (currentPlayerCount >= game.getHigherBoundPlayerAmount()) {
			// If the timeInSeconds is more than shorterTime, setting it to shorterTime would be an increase in time
			if (timeInSeconds > shorterTime) {
				timeInSeconds = shorterTime;
				if (onTimeReduction != null) {
					onTimeReduction.accept(shorterTime);
				}
			}
		} else if (currentPlayerCount < lastPlayerCount && currentPlayerCount < game.getHigherBoundPlayerAmount()) {
			// If the player count has dropped and is now below the higher count with the time less than the shorterTime
			if (timeInSeconds <= shorterTime) {
				timeInSeconds = longerTime; // Reset the time to wait for more players
				if (onTimeIncrease != null) {
					onTimeIncrease.accept(longerTime);
				}
			}
		} else if (currentPlayerCount < game.getMinimumPlayers()) {
			if (timeInSeconds < longerTime) {
				timeInSeconds = longerTime;
				if (onTimeIncrease != null) {
					onTimeIncrease.accept(longerTime);
				}
			}
		}

		if (onRun != null) {
			onRun.accept(timeInSeconds); // Perform custom functions
		}

		lastPlayerCount = currentPlayerCount;
		timeInSeconds--;
	}

	/**
	 * Consumer to be called on every call of {@link #run()} from this runnable
	 *
	 * @param onRun function to be called
	 */
	public void setOnRun(Consumer<Integer> onRun) {
		this.onRun = onRun;
	}

	/**
	 * Consumer to be called when time of more than 1 second is taken from the timer
	 *
	 * @param onTimeReduction function to be called
	 */
	public void setTimeReductionEvent(Consumer<Integer> onTimeReduction) {
		this.onTimeReduction = onTimeReduction;
	}

	/**
	 * Consumer to be called when time of more than 1 second is added to the timer
	 *
	 * @param onTimeIncrease function to be called
	 */
	public void setTimeIncreaseEvent(Consumer<Integer> onTimeIncrease) {
		this.onTimeIncrease = onTimeIncrease;
	}

	/**
	 * Consumer to be called when the timer reaches 0
	 *
	 * @param onTimerEnd function to be called
	 */
	public void setTimerEndEvent(Consumer<Void> onTimerEnd) {
		this.onTimerEnd = onTimerEnd;
	}

	/**
	 * Broadcasts the time remaining using {@link uk.co.harieo.GamesCore.chat.ChatModule} and plays a sound for each
	 * player
	 */
	public void pingTime() {
		Bukkit.broadcastMessage(game.chatModule()
				.formatSystemMessage("Game will start in " + ChatColor.GREEN + timeInSeconds + " seconds..."));
		for (Player player : Bukkit.getOnlinePlayers()) {
			player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, (float) 0.5, (float) 0.5);
		}
	}

}
