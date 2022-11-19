package me.titan.stt.player;

import me.titan.stt.objects.ChatInput;
import me.titan.stt.objects.PlayerSelection;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerCache {

	public static Map<UUID, PlayerCache> players = new HashMap<>();
	UUID uuid;

	PlayerSelection currentSelection;

	ChatInput currentChatInput;

	public PlayerCache(UUID id){
		this.uuid = id;
	}

	public static PlayerCache getPlayerCache(UUID id){
		if(players.containsKey(id)) {
			return players.get(id);
		}
		PlayerCache pc = new PlayerCache(id);
		players.put(id,pc);
		return pc;
	}

	public PlayerSelection getCurrentSelection() {
		return currentSelection;
	}

	public void setCurrentSelection(PlayerSelection currentSelection) {
		this.currentSelection = currentSelection;
	}

	public UUID getUuid() {
		return uuid;
	}

	public ChatInput getCurrentChatInput() {
		return currentChatInput;
	}

	public void setCurrentChatInput(ChatInput currentChatInput) {
		this.currentChatInput = currentChatInput;
	}
}
