package uk.co.harieo.GamesCore.players;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import uk.co.harieo.GamesCore.games.Game;

public class GamePlayerStore implements Listener {

	private static GamePlayerStore INSTANCE;

	private static Map<UUID, GamePlayer> cachedPlayers = new HashMap<>();

	/**
	 * Provides the instantiated instance of {@link GamePlayerStore} or instantiates it if not already done
	 *
	 * @param game to associate this instance with
	 * @return the instance of {@link GamePlayerStore}
	 */
	public static GamePlayerStore instance(Game game) {
		if (INSTANCE == null) {
			INSTANCE = new GamePlayerStore();
			Bukkit.getPluginManager().registerEvents(INSTANCE, game.getPlugin());
		}
		return INSTANCE;
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

	// Caching related events //
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		// To prevent unnecessary exposure of the class to an offline player, remove it from the cache
		cachedPlayers.remove(event.getPlayer().getUniqueId());
	}

}
