package uk.co.harieo.GamesCore.voting;

import org.bukkit.entity.Player;

public interface Vote {

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

}
