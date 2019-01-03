package uk.co.harieo.GamesCore.players;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.*;
import uk.co.harieo.GamesCore.games.Game;

public class GamePlayerStore implements Listener {

	private static Map<Game, GamePlayerStore> INSTANCES = new HashMap<>();

	private Map<UUID, GamePlayer> cachedPlayers = new HashMap<>();

	/**
	 * Provides the instantiated instance of {@link GamePlayerStore} associated with the stated {@param game} or
	 * instantiates it if not already done. Note: This system creates a new instance for each {@link Game} to separate
	 * players from different systems with different values.
	 *
	 * @param game to associate this instance with
	 * @return the instance of {@link GamePlayerStore}
	 */
	public static GamePlayerStore instance(Game game) {
		GamePlayerStore instance;
		if (!INSTANCES.containsKey(game)) {
			instance = new GamePlayerStore();
			INSTANCES.put(game, instance);
			Bukkit.getPluginManager().registerEvents(instance, game.getPlugin());
		} else {
			instance = INSTANCES.get(game);
		}

		return instance;
	}

	/**
	 * Retrieves an instance of {@link GamePlayer} from the store or instantiates a new one if none is stored
	 *
	 * @param player associated with the instance of {@link GamePlayer}
	 * @return the found or newly instantiated instance
	 */
	public GamePlayer get(Player player) {
		UUID uuid = player.getUniqueId();
		if (cachedPlayers.containsKey(uuid)) {
			return cachedPlayers.get(uuid);
		} else {
			GamePlayer gamePlayer = new GamePlayer(player);
			cachedPlayers.put(uuid, gamePlayer);
			return gamePlayer;
		}
	}

	public Collection<GamePlayer> getAll() {
		return cachedPlayers.values();
	}

	// Caching related events //
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		// To prevent unnecessary exposure of the class to an offline player, remove it from the cache
		cachedPlayers.remove(event.getPlayer().getUniqueId());
	}

}
