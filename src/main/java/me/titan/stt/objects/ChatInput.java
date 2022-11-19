package me.titan.stt.objects;

import java.util.UUID;
import java.util.function.Consumer;

public class ChatInput {

	final UUID player;
	Consumer<String> onInput;

	String lastInput;
	boolean active = true;

	public ChatInput(UUID player, Consumer<String> onInput) {
		this.player = player;
		this.onInput = onInput;
	}

	public UUID getPlayer() {
		return player;
	}

	public Consumer<String> getOnInput() {
		return onInput;
	}

	public void setOnInput(Consumer<String> onInput) {
		this.onInput = onInput;
	}

	public String getLastInput() {
		return lastInput;
	}

	public void setLastInput(String lastInput) {
		this.lastInput = lastInput;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
}
