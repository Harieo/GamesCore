package uk.co.harieo.GamesCore.scoreboards;

import org.bukkit.entity.Player;

public class ConstantElement implements RenderableElement {

	private String text;

	public ConstantElement(String text) {
		this.text = text;
	}

	@Override
	public String getText(Player player) {
		return text;
	}

}
