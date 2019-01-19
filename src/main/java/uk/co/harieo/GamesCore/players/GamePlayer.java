package uk.co.harieo.GamesCore.players;

import org.bukkit.entity.Player;

import java.util.UUID;

import uk.co.harieo.GamesCore.teams.Team;

public class GamePlayer {

	private Player player;
	private UUID uuid;

	private int score;
	private Team team;
	private boolean isPlaying; // Example of someone who would not be playing is a spectator

	private boolean isFake;

	GamePlayer(Player player) {
		this.player = player;
		this.uuid = player.getUniqueId();
		this.isPlaying = true;
		this.isFake = false;
	}

	GamePlayer() {
		this.isFake = true;
		this.isPlaying = false; // Stops this being used as a real player to a limited extent
	}

	// Teams //
	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public boolean hasTeam() {
		return team != null;
	}

	// Score //
	public int getScore() {
		return score;
	}

	public void addScore(int toAdd) {
		score += toAdd;
	}

	public void subtractScore(int toSubtract) {
		score -= toSubtract;
	}

	// Spectating //
	public boolean isPlaying() {
		return isPlaying && player.isOnline();
	}

	public void setIsPlaying(boolean isPlaying) {
		this.isPlaying = isPlaying;
	}

	// Misc. Variables //
	public boolean isFake() {
		return isFake;
	}

	public UUID getUniqueId() {
		return uuid;
	}

	public Player toBukkit() {
		return player;
	}

}
