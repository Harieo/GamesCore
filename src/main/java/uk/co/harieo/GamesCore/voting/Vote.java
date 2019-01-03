package uk.co.harieo.GamesCore.voting;

import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Set;

public interface Vote {

	/**
	 * Handles a player's vote and adds it to the number of votes for that option
	 *
	 * @param player who is voting
	 * @param option which they voted for
	 */
	void submitVote(Player player, String option);

	/**
	 * Checks whether a player may vote in this {@link Vote}
	 *
	 * @param player who wishes to vote
	 * @return whether they may submit their vote
	 */
	boolean canPlayerVote(Player player);

	/**
	 * @return whether this {@link Vote} is still accepting votes
	 */
	boolean isVotingOpen();

	/**
	 * Returns the amount of votes for a certain option
	 *
	 * @param option to retrieve the amount of votes for
	 * @return the amount of votes
	 */
	int getAmountOfVotes(String option);

	/**
	 * @return a set of options that players can vote for
	 */
	Set<String> getOptions();

	/**
	 * @return a map of options and the votes for those options
	 */
	Map<String, Integer> getVotes();

}
