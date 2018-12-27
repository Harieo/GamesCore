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

	GamePlayer(Player player) {
		this.player = player;
		this.uuid = player.getUniqueId();
		this.isPlaying = true;
	}

	// Teams //
	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
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
		return isPlaying;
	}

	public void setIsPlaying(boolean isPlaying) {
		this.isPlaying = isPlaying;
	}

	// Misc. Variables //
	public UUID getUniqueId() {
		return uuid;
	}

	public Player toBukkit() {
		return player;
	}

}
