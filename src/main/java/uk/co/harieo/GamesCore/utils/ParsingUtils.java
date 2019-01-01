package uk.co.harieo.GamesCore.utils;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class ParsingUtils {

	/**
	 * More efficiently checks whether a String can be parsed into an integer without generating {@link
	 * NumberFormatException}
	 *
	 * Credit to Stackoverflow user 26609/jonas-klemming for this method
	 *
	 * @param string to check if is an integer
	 * @return whether the String can be parsed into an integer
	 */
	public static boolean isInteger(String string) {
		if (string == null) {
			return false;
		} else if (string.isEmpty()) {
			return false;
		}

		int length = string.length();
		int i = 0;

		if (string.charAt(0) == '-') {
			if (length == 1) {
				return false;
			}
			i = 1;
		}

		for (; i < length; i++) {
			char c = string.charAt(i);
			if (c < '0' || c > '9') {
				return false;
			}
		}
		return true;
	}

	/**
	 * Forms a String representation of the X, Y and Z coordinates of a location
	 *
	 * @param location containing the coordinates
	 * @return the String representation of the coordinates
	 */
	public static String formStringCoordinate(Location location) {
		return "(" + location.getBlockX() + "," + location.getBlockY() + "," + location.getBlockZ() + ")";
	}

	/**
	 * Using 2 locations that are in line on the X or Z vectors, parses all locations in that line between both
	 * locations and returns as a list. This method will discount the given locations, assuming they are borders, and
	 * will not be included in the returned list.
	 *
	 * The Y value is not referenced in this method so only 2 dimensions will be retrieved.
	 *
	 * @param firstLocation in line with the second location
	 * @param secondLocation in line with the first location
	 * @return list of in between locations connecting the two stated locations, excluding the two stated locations
	 */
	public static List<Location> parseInlineLocations(Location firstLocation, Location secondLocation) {
		List<Location> locations = new ArrayList<>();

		if (firstLocation.getBlockX() == secondLocation.getBlockX()) {
			// We need to go through the Z values as X is in line
			if (firstLocation.getBlockZ() < secondLocation.getBlockZ()) {
				// Remember that these coordinates are on the border so we want to discount them
				for (int z = firstLocation.getBlockZ() + 1; z < secondLocation.getBlockZ(); z++) {
					locations.add(new Location(firstLocation.getWorld(), firstLocation.getBlockX(),
							firstLocation.getBlockY(), z));
				}
			} else if (firstLocation.getBlockZ() > secondLocation.getBlockZ()) {
				for (int z = firstLocation.getBlockZ() - 1; z > secondLocation.getBlockZ(); z--) {
					locations.add(new Location(firstLocation.getWorld(), firstLocation.getBlockX(),
							firstLocation.getBlockY(), z));
				}
			} else {
				throw new RuntimeException("Z and X values on tile creation were equal, which is an error ");
			}
		} else if (firstLocation.getBlockZ() == secondLocation.getBlockZ()) {
			// We need to go through the X values as Z is in line
			if (firstLocation.getBlockX() < secondLocation.getBlockX()) {
				// Remember that these coordinates are on the border so we want to discount them
				for (int x = firstLocation.getBlockX() + 1; x < secondLocation.getBlockX(); x++) {
					locations.add(new Location(firstLocation.getWorld(), x,
							firstLocation.getBlockY(), firstLocation.getBlockZ()));
				}
			} else if (firstLocation.getBlockX() > secondLocation.getBlockX()) {
				for (int x = firstLocation.getBlockX() - 1; x > secondLocation.getBlockX(); x--) {
					locations.add(new Location(firstLocation.getWorld(), x,
							firstLocation.getBlockY(), firstLocation.getBlockZ()));
				}
			} else {
				throw new RuntimeException("Z and X values on tile creation were equal");
			}
		} else {
			throw new RuntimeException("Neither Z nor X were in line on tile creation");
		}

		return locations;
	}

	/**
	 * Checks whether two locations are perpendicular to each other on the X or Z axis and makes sure they are
	 * more than 5 blocks apart on that axis (therefore, are not next to each other)
	 *
	 * @param firstLocation to be compared to the second
	 * @param secondLocation to be compared to the first
	 * @return whether the locations are in line and far apart
	 */
	public static boolean areFarInline(Location firstLocation, Location secondLocation) {
		if (firstLocation.getBlockX() == secondLocation.getBlockX()) {
			if (firstLocation.getBlockZ() > secondLocation.getBlockZ()) {
				if (firstLocation.getBlockZ() - secondLocation.getBlockZ() < 5) {
					return false;
				}
			} else if (firstLocation.getBlockZ() < secondLocation.getBlockZ()) {
				if (secondLocation.getBlockZ() - firstLocation.getBlockZ() < 5) {
					return false;
				}
			}
		} else if (firstLocation.getBlockZ() == secondLocation.getBlockZ()) {
			if (firstLocation.getBlockX() > secondLocation.getBlockX()) {
				if (firstLocation.getBlockX() - secondLocation.getBlockX() < 5) {
					return false;
				}
			} else if (firstLocation.getBlockX() < secondLocation.getBlockX()) {
				if (secondLocation.getBlockX() - firstLocation.getBlockX() < 5) {
					return false;
				}
			}
		}

		return firstLocation.getBlockX() == secondLocation.getBlockX() || firstLocation.getBlockZ() == secondLocation
				.getBlockZ();
	}

}
