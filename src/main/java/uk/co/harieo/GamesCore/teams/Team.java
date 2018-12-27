package uk.co.harieo.GamesCore.teams;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.md_5.bungee.api.ChatColor;
import uk.co.harieo.GamesCore.games.Game;

public class Team implements Listener {

	private String teamName;
	private ChatColor teamColor;
	private List<UUID> teamMembers = new ArrayList<>();

	private int teamScore = 0;

	/**
	 * A construction that allows the storage of integer score, properties and list of members associated with a team
	 *
	 * @param game to register the listener associated with this team
	 * @param teamName of the team, to be used in chat
	 * @param teamColor of the team, to be used in chat
	 */
	public Team(Game game, String teamName, ChatColor teamColor) {
		this.teamName = teamName;
		this.teamColor = teamColor;
		Bukkit.getPluginManager().registerEvents(this, game.getPlugin());
	}

	/**
	 * @return the name of this team
	 */
	public String getTeamName() {
		return teamName;
	}

	/**
	 * @return the {@link ChatColor} for this team
	 */
	public ChatColor getTeamColor() {
		return teamColor;
	}

	/**
	 * @return a list of the members of this team by UUID
	 */
	public List<UUID> getTeamMembers() {
		return teamMembers;
	}

	/**
	 * @return the score associated to this team
	 */
	public int getTeamScore() {
		return teamScore;
	}

	/**
	 * Add a {@link Player} to the list of this team's members
	 *
	 * @param player to be added to this team
	 */
	public void addTeamMember(Player player) {
		teamMembers.add(player.getUniqueId());
	}

	/**
	 * Removes a player from this team based on their {@link UUID}
	 *
	 * @param uuid of the player to be removed
	 */
	public void removeTeamMember(UUID uuid) {
		teamMembers.remove(uuid);
	}

	/**
	 * Adds score to this team's overall score
	 *
	 * @param toAdd integer to be added to the score
	 */
	public void addScore(int toAdd) {
		teamScore += toAdd;
	}

	/**
	 * Subtracts score from this team's overall score
	 *
	 * @param toSubtract integer to be subtracted from the score
	 */
	public void subtractScore(int toSubtract) {
		teamScore -= toSubtract;
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		teamMembers.remove(event.getPlayer().getUniqueId());
	}

}
