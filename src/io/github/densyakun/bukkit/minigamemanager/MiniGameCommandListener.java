package io.github.densyakun.bukkit.minigamemanager;

import org.bukkit.command.CommandSender;

public interface MiniGameCommandListener {
	
	public boolean MiniGameCommand(CommandSender sender, String[] args);
}
