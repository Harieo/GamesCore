package uk.co.harieo.GamesCore.voting;

import org.bukkit.entity.Player;

import java.util.*;
import java.util.function.BiConsumer;
import uk.co.harieo.GamesCore.games.Game;
import uk.co.harieo.GamesCore.timers.GenericTimer;

public class TimedVote implements Vote {

	private Map<String, Integer> options = new HashMap<>();
	private GenericTimer timer;
	private boolean isVotingOpen = true;

	private BiConsumer<Player, String> onVote;

	/**
	 * An implementation of {@link Vote} which activates a {@link GenericTimer} that counts down, closing the vote
	 * after time has expired.
	 *
	 * @param game that this vote is for
	 * @param options for the vote
	 * @param voteDurationInSeconds until the vote closes
	 */
	public TimedVote(Game game, List<String> options, int voteDurationInSeconds) {
		for (String option : options) {
			this.options.put(option, 0);
		}

		this.timer = new GenericTimer(game, voteDurationInSeconds, v -> this.isVotingOpen = false);
		timer.beginTimer();
	}

	/**
	 * @return the active timer for this vote
	 */
	public GenericTimer getTimer() {
		return timer;
	}

	/**
	 * Sets a function to occur when a player has voted
	 *
	 * @param onVote the consumer of the player and which option they voted for
	 */
	public void setOnVote(BiConsumer<Player, String> onVote) {
		this.onVote = onVote;
	}

	@Override
	public boolean canPlayerVote(Player player) {
		return true;
	}

	@Override
	public void submitVote(Player player, String option) {
		int votes = options.get(option);
		options.replace(option, votes + 1);
		if (onVote != null) {
			onVote.accept(player, option);
		}
	}

	@Override
	public boolean isVotingOpen() {
		return isVotingOpen;
	}

	@Override
	public int getAmountOfVotes(String option) {
		return options.get(option);
	}

	@Override
	public Set<String> getOptions() {
		return options.keySet();
	}

	@Override
	public Map<String, Integer> getVotes() {
		return options;
	}

}
