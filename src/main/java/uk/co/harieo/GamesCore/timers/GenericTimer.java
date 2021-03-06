package uk.co.harieo.GamesCore.timers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.function.Consumer;
import uk.co.harieo.GamesCore.games.Game;

/**
 * The purpose of this class is to offer a simple Runnable that counts down sequentially in seconds
 */
public class GenericTimer extends BukkitRunnable {

	private Game game;
	private int timeInSeconds;
	private Consumer<Void> onTimerEnd;
	private Consumer<Integer> onRun;

	/**
	 * A basic timer that counts down in seconds from the specified time without any other conditions being implemented
	 * onto the timer. An example of such a condition would be {@link GameStartTimer} which implements player count
	 * conditions based on upper and lower bounds.
	 *
	 * @param game associated with this timer
	 * @param secondsToCount for the timer to start at in seconds
	 * @param onTimerEnd a function to be called when the timer reaches 0
	 */
	public GenericTimer(Game game, int secondsToCount, Consumer<Void> onTimerEnd) {
		if (secondsToCount <= 0) {
			throw new IllegalArgumentException("Cannot create a timer with less than 1 second to count");
		}

		this.game = game;
		this.timeInSeconds = secondsToCount;
		this.onTimerEnd = onTimerEnd;
	}

	/**
	 * Begins the timer, counting down with an interval of 20 ticks
	 */
	public void beginTimer() {
		runTaskTimer(game.getPlugin(), 20, 20);
	}

	@Override
	public void run() {
		if (timeInSeconds <= 0) {
			onTimerEnd.accept(null);
			cancel();
		} else {
			onRun.accept(timeInSeconds);
			timeInSeconds--;
		}
	}

	/**
	 * @param onRun function that occurs when {@link #run()} is called
	 */
	public void setOnRun(Consumer<Integer> onRun) {
		this.onRun = onRun;
	}

	/**
	 * Sets the remaining time for this timer
	 *
	 * @param timeInSeconds to set the time to
	 */
	public void setTimeLeft(int timeInSeconds) {
		this.timeInSeconds = timeInSeconds;
	}

	/**
	 * @return the seconds of time remaining
	 */
	public int getTimeLeft() {
		return timeInSeconds;
	}

	/**
	 * Broadcasts the time remaining using {@link uk.co.harieo.GamesCore.chat.ChatModule} and plays a sound for each
	 * player
	 */
	public void pingTime() {
		Bukkit.broadcastMessage(game.chatModule()
				.formatSystemMessage("Time will end in " + ChatColor.GREEN + timeInSeconds + " second(s)..."));
		for (Player player : Bukkit.getOnlinePlayers()) {
			player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, (float) 0.5, (float) 0.5);
		}
	}

}
