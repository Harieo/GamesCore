package uk.co.harieo.GamesCore.games;

import org.bukkit.plugin.java.JavaPlugin;

import uk.co.harieo.GamesCore.chat.ChatModule;

public interface Game {

	/**
	 * @return the name of this game
	 */
	String getGameName();

	/**
	 * @return the state of this game
	 */
	GameState getState();

	/**
	 * @return the {@link JavaPlugin} extended over this game
	 */
	JavaPlugin getPlugin();

	/**
	 * Sets the state of the game
	 *
	 * @param state to set the state to
	 */
	void setState(GameState state);

	/**
	 * @return the chat module which handles system messages for this game
	 */
	ChatModule chatModule();

	/**
	 * @return the maximum amount of players that may join
	 */
	int getMaximumPlayers();

	/**
	 * @return the amount of slots reserved for ranked players
	 */
	int getReservedSlots();

	/**
	 * @return the amount of players that is ideal to start a game, separate to the minimum which isn't ideal but
	 * acceptable
	 */
	int getHigherBoundPlayerAmount();

	/**
	 * @return the minimum amount of players to start the game with
	 */
	int getMinimumPlayers();

	/**
	 * Whether this game is in beta or not. Beta games will display alerts to warn people that they are unstable.
	 *
	 * @return whether this game is in beta
	 */
	boolean isBeta();

	/**
	 * Called when the Games Core assigns a game number to this game upon it being registered with {@link GameStore}
	 *
	 * @param gameNumber that has been assigned
	 */
	void assignGameNumber(int gameNumber);

	/**
	 * Starts the game regardless of all factors
	 */
	void startGame();

}
