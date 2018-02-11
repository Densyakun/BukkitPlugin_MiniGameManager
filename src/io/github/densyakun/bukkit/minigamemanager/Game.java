package io.github.densyakun.bukkit.minigamemanager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class Game implements Listener {
	protected String name;
	int time = -1;
	int entrytime = 1;
	int starttime = 1;
	int endtime = 1;
	int stoptime = 1;
	protected List<Player> players = new ArrayList<Player>();
	int minplayers = 1;
	int maxplayers = 0;
	boolean entered = false;
	boolean started = false;
	boolean ended = false;
	boolean stopped = false;
	
	protected String msg_entryafter = "エントリー終了まで...time秒";
	protected String msg_entry = "エントリー終了";
	protected String msg_startafter = "ゲーム開始まで...time秒";
	protected String msg_start = "ゲーム開始!";
	protected String msg_endafter = "ゲーム終了まで...time秒";
	protected String msg_end = "ゲーム終了!";
	protected String msg_stopafter = "time秒後にテレポートします...";
	
	public Game(String name) {
		this.name = name;
		MiniGameManager.minigamemanager.registerGame(this);
	}
	
	public void countsec() {
		if (entered) {
			time++;
			if (started) {
				if (time < entrytime + starttime + endtime) {
					if (time == entrytime + starttime) {
						for (int a = 0; a < players.size(); a++) {
							players.get(a).sendMessage(Main.msg_prefix + ChatColor.GOLD + msg_endafter.replaceAll("time", String.valueOf(endtime)));
						}
					} else if ((5 <= endtime) && (((entrytime + starttime + endtime) - 5) <= time)) {
						for (int a = 0; a < players.size(); a++) {
							players.get(a).sendMessage(Main.msg_prefix + ChatColor.GOLD + msg_endafter.replaceAll("time", String.valueOf(entrytime + starttime + endtime - time)));
						}
					}
				} else {
					if (time == entrytime + starttime + endtime) {
						end();
					}
					if (time < entrytime + starttime + endtime + stoptime) {
						if (time == entrytime + starttime + endtime) {
							for (int a = 0; a < players.size(); a++) {
								players.get(a).sendMessage(Main.msg_prefix + ChatColor.GOLD + msg_stopafter.replaceAll("time", String.valueOf(stoptime)));
							}
						} else if ((5 <= stoptime) && (((entrytime + starttime + endtime + stoptime) - 5) <= time)) {
							for (int a = 0; a < players.size(); a++) {
								players.get(a).sendMessage(Main.msg_prefix + ChatColor.GOLD + msg_stopafter.replaceAll("time", String.valueOf(entrytime + starttime + endtime + stoptime - time)));
							}
						}
					} else {
						stop();
					}
				}
			} else {
				if (time < entrytime + starttime) {
					if (time == entrytime) {
						for (int a = 0; a < players.size(); a++) {
							players.get(a).sendMessage(Main.msg_prefix + ChatColor.GOLD + msg_startafter.replaceAll("time", String.valueOf(starttime)));
						}
					} else if ((5 <= starttime) && (((entrytime + starttime) - 5) <= time)) {
						for (int a = 0; a < players.size(); a++) {
							players.get(a).sendMessage(Main.msg_prefix + ChatColor.GOLD + msg_startafter.replaceAll("time", String.valueOf(entrytime + starttime - time)));
						}
					}
				} else {
					start();
				}
			}
		} else {
			if (players.size() < minplayers) {
				time = -1;
			} else {
				time++;
				if (time < entrytime) {
					if (time == 0) {
						for (int a = 0; a < players.size(); a++) {
							players.get(a).sendMessage(Main.msg_prefix + ChatColor.GOLD + msg_entryafter.replaceAll("time", String.valueOf(entrytime)));
						}
					} else if ((5 <= entrytime) && ((entrytime - 5) <= time)) {
						for (int a = 0; a < players.size(); a++) {
							players.get(a).sendMessage(Main.msg_prefix + ChatColor.GOLD + msg_entryafter.replaceAll("time", String.valueOf(entrytime - time)));
						}
					}
				} else {
					entered();
				}
			}
		}
	}
	
	public boolean isEntered() {
		return entered;
	}
	
	public boolean isStarted() {
		return started;
	}
	
	public boolean isEnded() {
		return ended;
	}
	
	public boolean isStopped() {
		return stopped;
	}
	
	public void entered() {
		entered = true;
		time = entrytime;
		
		for (int a = 0; a < players.size(); a++)
			players.get(a).sendMessage(Main.msg_prefix + ChatColor.GOLD + msg_entry);
	}
	
	public void start() {
		if (!entered)
			entered();
		
		started = true;
		time = entrytime + starttime;
		
		for (int a = 0; a < players.size(); a++)
			players.get(a).sendMessage(Main.msg_prefix + ChatColor.GOLD + msg_start);
	}
	
	public void end() {
		if (!started)
			start();
		
		ended = true;
		time = entrytime + starttime + endtime;
		
		for (int a = 0; a < players.size(); a++)
			players.get(a).sendMessage(Main.msg_prefix + ChatColor.GOLD + msg_end);
	}
	
	public void stop() {
		if (!ended)
			end();
		
		stopped = true;
		time = entrytime + starttime + endtime + stoptime;
		
		players.clear();
		MiniGameManager.minigamemanager.unregisterGame(this);
	}
	
	public int getTime() {
		return time;
	}
	
	public void setEntrytime(int entrytime) {
		this.entrytime = entrytime;
	}
	
	public int getEntrytime() {
		return entrytime;
	}
	
	public void setStarttime(int starttime) {
		this.starttime = starttime;
	}
	
	public int getStarttime() {
		return starttime;
	}
	
	public void setEndtime(int endtime) {
		this.endtime = endtime;
	}
	
	public int getEndtime() {
		return endtime;
	}
	
	public void setStoptime(int stoptime) {
		this.stoptime = stoptime;
	}
	
	public int getStoptime() {
		return stoptime;
	}
	
	public List<Player> getPlayers() {
		return players;
	}
	
	public int getMinplayers() {
		return minplayers;
	}
	
	public void setMinplayers(int minplayers) {
		if (0 < minplayers)
			this.minplayers = minplayers;
		else
			this.minplayers = 0;
	}
	
	public int getMaxplayers() {
		return maxplayers;
	}
	
	public void setMaxplayers(int maxplayers) {
		if (0 < maxplayers)
			this.maxplayers = maxplayers;
		else
			this.maxplayers = 0;
	}
	
	protected boolean addPlayer(Player player) {
		if (isJoinable()) {
			players.add(player);
			for (int a = 0; a < players.size(); a++)
				players.get(a).sendMessage(ChatColor.GOLD + player.getDisplayName() + ChatColor.AQUA + "さんがゲームに参加");
			
			if (players.size() == maxplayers)
				entered();
			
			return true;
		}
		
		return false;
	}
	
	public boolean isJoinable() {
		return !entered && (maxplayers == 0 || players.size() < maxplayers);
	}
	
	public void removePlayer(UUID uuid) {
		for (int a = 0; a < players.size(); a++) {
			if (uuid.equals(players.get(a).getUniqueId())) {
				for (int b = 0; b < players.size(); b++)
					players.get(b).sendMessage(ChatColor.GOLD + players.get(a).getDisplayName() + ChatColor.AQUA + "さんがゲームから退出");
				
				players.remove(a);
				if (players.size() == 0)
					stop();
				
				break;
			}
		}
	}
}
