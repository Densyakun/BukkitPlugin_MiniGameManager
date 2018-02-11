package io.github.densyakun.bukkit.minigamemanager;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
	public static final String param_is_not_enough = "パラメータが足りません";
	public static final String param_wrong_cmd = "パラメータが間違っています";
	public static final String cmd_player_only = "このコマンドはプレイヤーのみ実行できます";
	
	public static Main main;
	public static String msg_prefix;
	
	@Override
	public void onLoad() {
		main = this;
		msg_prefix = ChatColor.GREEN + "[" + getName() + "]";
		new MiniGameManager();
	}
	
	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getConsoleSender().sendMessage(msg_prefix + "有効");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (label.equalsIgnoreCase("game")) {
			if (args.length == 0) {
				sender.sendMessage(msg_prefix + ChatColor.GOLD + param_is_not_enough);
				sender.sendMessage(msg_prefix + ChatColor.GREEN + "/game (leave|[other])");
			} else if (args[0].equalsIgnoreCase("leave")) {
				if (sender instanceof Player) {
					Game game = MiniGameManager.minigamemanager.getPlayingGame(((Player) sender).getUniqueId());
					if (game == null) {
						sender.sendMessage(msg_prefix + ChatColor.RED + "ゲーム中ではありません");
					} else {
						game.removePlayer(((Player) sender).getUniqueId());
					}
				}
			} else if (args[0].equalsIgnoreCase("admin")) {
				if (sender.isOp() || sender.hasPermission("minigamemanager.admin")) {
					if (args.length == 1) {
						sender.sendMessage(msg_prefix + ChatColor.RED + param_is_not_enough);
						sender.sendMessage(msg_prefix + ChatColor.GREEN + "/game admin (stop)");
					} else if (args[1].equalsIgnoreCase("stop")) {
						if (args.length == 2) {
							if (sender instanceof Player) {
								Game game = MiniGameManager.minigamemanager.getPlayingGame(((Player) sender).getUniqueId());
								if (game == null) {
									sender.sendMessage(msg_prefix + ChatColor.RED + "ゲーム中ではありません");
								} else {
									game.stop();
								}
							}
						} else if (args[2].equalsIgnoreCase("all")) {
							MiniGameManager.minigamemanager.stop();
							sender.sendMessage(msg_prefix + ChatColor.AQUA + "全ゲームを終了しました");
						} else {
							MiniGameManager.minigamemanager.stop(args[2]);
							sender.sendMessage(msg_prefix + ChatColor.AQUA + args[2] + "を終了しました");
						}
					} else {
						sender.sendMessage(msg_prefix + ChatColor.RED + param_wrong_cmd);
						sender.sendMessage(msg_prefix + ChatColor.GREEN + "/game admin (stop)");
					}
				}
			} else {
				if (!MiniGameManager.minigamemanager.performMiniGameCommand(sender, args)) {
					sender.sendMessage(msg_prefix + ChatColor.RED + param_wrong_cmd);
					sender.sendMessage(msg_prefix + ChatColor.GREEN + "/game (leave|[other])");
				}
			}
		}
		return true;
	}
	
	@Override
	public void onDisable() {
		MiniGameManager.minigamemanager.stop();
		getServer().getConsoleSender().sendMessage(msg_prefix + "無効");
	}
	
	@EventHandler
	public void PlayerJoin(PlayerJoinEvent e) {
		e.getPlayer().setGameMode(GameMode.ADVENTURE);
	}
	
	@EventHandler
	public void PlayerQuit(PlayerQuitEvent e) {
		Game game = MiniGameManager.minigamemanager.getPlayingGame(e.getPlayer().getUniqueId());
		if (game != null) {
			game.removePlayer(e.getPlayer().getUniqueId());
		}
	}
}
