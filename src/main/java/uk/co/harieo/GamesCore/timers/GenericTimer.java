package uk.co.harieo.GamesCore.timers;

import org.bukkit.scheduler.BukkitRunnable;

import java.util.function.Consumer;

public class GenericTimer extends BukkitRunnable {

	private int timeInSeconds;
	private Consumer<Void> onTimerEnd;

	/**
	 * A basic timer that counts down in seconds from the specified time without any other conditions being implemented
	 * onto the timer. An example of such a condition would be {@link GameStartTimer} which implements player count
	 * conditions based on upper and lower bounds.
	 *
	 * @param secondsToCount for the timer to start at in seconds
	 * @param onTimerEnd a function to be called when the timer reaches 0
	 */
	public GenericTimer(int secondsToCount, Consumer<Void> onTimerEnd) {
		if (secondsToCount <= 0) {
			throw new IllegalArgumentException("Cannot create a timer with less than 1 second to count");
		}

		this.timeInSeconds = secondsToCount;
		this.onTimerEnd = onTimerEnd;
	}

	@Override
	public void run() {
		if (timeInSeconds <= 0) {
			onTimerEnd.accept(null);
			cancel();
		} else {
			timeInSeconds--;
		}
	}

}
