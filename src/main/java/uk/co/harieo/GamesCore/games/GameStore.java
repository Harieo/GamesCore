package uk.co.harieo.GamesCore.games;

import java.util.HashMap;
import java.util.Map;

public class GameStore {

	private static GameStore INSTANCE;

	private Map<Integer, Game> registeredGames = new HashMap<>();

	/**
	 * Registers a new game to the {@link GameStore} on the assumption that the game is ready to accept players
	 *
	 * @param gameInstance to be registered
	 */
	public void registerGame(Game gameInstance) {
		// Logic Error Prevention //
		String exceptionError = null;
		if (gameInstance.getReservedSlots() > gameInstance.getMaximumPlayers()) {
			exceptionError = "Reserved slots cannot exceed maximum slots";
		} else if (gameInstance.getHigherBoundPlayerAmount() > gameInstance.getMaximumPlayers()) {
			exceptionError = "Higher bound cannot exceed the total maximum";
		} else if (gameInstance.getHigherBoundPlayerAmount() < gameInstance.getMinimumPlayers()) {
			exceptionError = "Higher bound cannot be less than lower bound";
		} else if (gameInstance.getMaximumPlayers() <= 0 || gameInstance.getMinimumPlayers() <= 0) {
			exceptionError = "Maximum and Minimum players may not be less than or equal to 0";
		}

		// Check if any errors were generated //
		if (exceptionError != null) {
			throw new IllegalArgumentException(exceptionError + " for " + gameInstance.getGameName());
		}

		// Assign a game number //
		int i = 1;
		while (INSTANCE.registeredGames.containsKey(i)) { // While the number is taken by another game
			i++; // Increment the number until it isn't taken
		}

		// Add the game to the store //
		gameInstance.assignGameNumber(i);
		registeredGames.put(i, gameInstance);
	}

	/**
	 * Removes a {@link Game} from the cache by its game number
	 *
	 * @param gameNumber of the {@link Game} to be removed
	 */
	public void unregisterGame(int gameNumber) {
		registeredGames.remove(gameNumber);
	}

	/**
	 * @return a list of registered game
	 */
	public Map<Integer, Game> getRegisteredGames() {
		return registeredGames;
	}

	/**
	 * @return the created instance of {@link GameStore} or creates a new instance of there is no instantiation stored
	 */
	public static GameStore instance() {
		if (INSTANCE == null) {
			INSTANCE = new GameStore();
		}
		return INSTANCE;
	}

}
