package uk.co.harieo.GamesCore;

public interface Game {

	String getGameName();

	int getMaximumPlayers();

	int getReservedSlots();

	boolean isBeta();

}
