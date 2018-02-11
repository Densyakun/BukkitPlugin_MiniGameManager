package io.github.densyakun.bukkit.minigamemanager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MiniGameManager implements Runnable {
	public static MiniGameManager minigamemanager;
	
	List<Game> games = new ArrayList<Game>();
	boolean run = false;
	List<MiniGameCommandListener> cmdlisteners = new ArrayList<MiniGameCommandListener>();
	
	public MiniGameManager() {
		minigamemanager = this;
	}
	
	void registerGame(Game game) {
		games.add(game);
		runnerstart();
	}
	
	void unregisterGame(Game game) {
		games.remove(game);
	}
	
	public void stop() {
		run = false;
		
		for (int a = 0; a < games.size(); a++)
			games.get(a).stop();
		
		games.clear();
	}
	
	public void stop(String name) {
		for (int a = 0; a < games.size();) {
			if (games.get(a).name.equalsIgnoreCase(name))
				games.remove(a).stop();
			else
				a++;
		}
	}
	
	public void addMiniGameCommandListener(MiniGameCommandListener listener) {
		cmdlisteners.add(listener);
	}
	
	public boolean performMiniGameCommand(CommandSender sender, String[] args) {
		for (int a = 0; a < cmdlisteners.size(); a++)
			if (cmdlisteners.get(a).MiniGameCommand(sender, args))
				return true;
		
		return false;
	}
	
	public List<Game> getPlayingGames() {
		return games;
	}
	
	public Game getPlayingGame(UUID uuid) {
		for (int a = 0; a < games.size(); a++)
			if (!games.get(a).ended)
				for (int b = 0; b < games.get(a).getPlayers().size(); b++)
					if (games.get(a).getPlayers().get(b).getUniqueId().equals(uuid))
						return games.get(a);
		
		return null;
	}
	
	public boolean joinGame(Player player, Game game) {
		if (getPlayingGame(player.getUniqueId()) == null)
			return game.addPlayer(player);
		
		return false;
	}
	
	void runnerstart() {
		if (!run) {
			run = true;
			new Thread(this).start();
		}
	}
	
	@Override
	public void run() {
		while (run) {
			Bukkit.getScheduler().scheduleSyncDelayedTask(Main.main, new Runnable() {
				public void run() {
					Iterator<Game> a = games.iterator();
					while (a.hasNext())
						a.next().countsec();
				}
			});
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
