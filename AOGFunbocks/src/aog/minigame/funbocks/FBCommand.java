package aog.minigame.funbocks;

import java.util.Arrays;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import aog.minigame.funbocks.instance.FBGame;
import aog.minigame.funbocks.instance.FBMap;
import aog.minigame.funbocks.instance.FBPlayer;
import aog.minigame.funbocks.instance.GridLocation;

public class FBCommand implements CommandExecutor {

	public static final int MAX_PLAYERS = 40;
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender s, Command c, String l, String[] args) {
		if (c.getName().equalsIgnoreCase("fb")) {

			if (!(s instanceof Player)) {
				s.sendMessage(Main.prefix
						+ "You must be a player to execute this command.");
				return false;
			}

			Player p = (Player) s;

			if (args.length == 0) {
				showOptions(p);
				return true;
			}

			String a1 = args[0];

			String a2 = "";
			if (args.length >= 2)
				a2 = args[1];

			String a3 = "";
			if (args.length >= 3)
				a3 = args[2];

			/**
			 * PLAYER COMMANDS
			 */
			if (a1.equalsIgnoreCase("join")) {

				if (Main.data.getBannedPlayers().contains(p.getUniqueId().toString())) {
					p.sendMessage(Main.prefix
							+ "Sorry but it seems you're banned from Funbocks :( . Try contacting a member of staff to resolve this.");
					return true;
				}

				if (Main.currentGame != null) {

					if (!Main.currentGame.getPlayers().containsKey(p.getUniqueId().toString())) {

						if (!Main.currentGame.isStarted()) {

							if(Main.currentGame.getPlayers().size() >= MAX_PLAYERS){
								p.sendMessage(Main.prefix
										+ ChatColor.RED
										+ "The Funbocks match is currently full!");
								return true;
							}
							
							if (Main.currentGame.getHost().getRawPlayer() == p) {
								p.sendMessage(Main.prefix
										+ ChatColor.RED
										+ "You're the host, you can't join as a player.");
								return false;
							}

							Main.currentGame.getPlayers().put(p.getUniqueId().toString(),
									new FBPlayer(p));

						} else
							p.sendMessage(Main.prefix + ChatColor.RED
									+ "The game has already begun! D:");

					} else
						p.sendMessage(Main.prefix + ChatColor.RED
								+ "You're already in the game! :D");

				} else
					p.sendMessage(Main.prefix + ChatColor.RED
							+ "No game has been started! :(");

			} else if (a1.equalsIgnoreCase("quit")
					|| a1.equalsIgnoreCase("leave")) {

				if (Main.currentGame != null) {

					if (Main.currentGame.getPlayers().containsKey(p.getUniqueId().toString())) {

						Main.currentGame.getPlayers().get(p.getUniqueId().toString())
								.leaveGame();

					} else
						p.sendMessage(Main.prefix + ChatColor.RED
								+ "You're not in the game! :O");

				} else
					p.sendMessage(Main.prefix + ChatColor.RED
							+ "No game has been started! :(");

			} else if (a1.equalsIgnoreCase("balance")
					|| a1.equalsIgnoreCase("bal") || a1.equalsIgnoreCase("$")
					|| a1.equalsIgnoreCase("£") || a1.equalsIgnoreCase("€")) {

				if (Main.currentGame != null) {

					if (Main.currentGame.getPlayers().containsKey(p.getUniqueId().toString())) {

						p.sendMessage(ChatColor.GREEN
								+ "Funbocks Balance: $"
								+ Main.currentGame.getPlayers()
										.get(p.getUniqueId().toString()).getBalance());

					} else if (Main.currentGame.getHost().getName()
							.equalsIgnoreCase(p.getName())) {

						p.sendMessage(ChatColor.GREEN
								+ "Funbocks Host Balance: $"
								+ Main.currentGame.getHost().getPoints());

					} else
						p.sendMessage(Main.prefix + ChatColor.RED
								+ "You're not in the game! :O");

				} else
					p.sendMessage(Main.prefix + ChatColor.RED
							+ "No game has been started! :(");

			} else if (a1.equalsIgnoreCase("spectate")) {
				
				if(Main.currentGame != null){
					
					if(Main.currentGame.getMap().getSpectate() != null){
						
						Location loc = Main.currentGame.getMap().getSpectate().getBukkitLocation();
						
						p.sendMessage(Main.prefix + "Teleported you to Spectating area for the Funbocks map '"
								+ Main.currentGame.getMap().getName()+ "'.");
						
						p.teleport(loc);
						
					}else{
						p.sendMessage(Main.prefix + ChatColor.RED + "Error: No spectating point has been set for this Funbocks map.");
					}
					
				}else{
					p.sendMessage(Main.prefix + ChatColor.RED + "There is currently no game to spectate! :[");
				}
				
			} else if (a1.equalsIgnoreCase("list")) {

				if (Main.currentGame != null) {

					if (Main.currentGame.getPlayers().isEmpty()) {
						p.sendMessage(Main.prefix
								+ ChatColor.RED
								+ "There are no players in the Funbocks game! :(");
						return true;
					}

					String list = "";

					for (FBPlayer fp : Main.currentGame.getPlayers().values())
						list += fp.getNamedPlayer() + ", ";

					list = list.substring(0, list.length() - 1);

					p.sendMessage(Main.prefix
							+ "List of all players currently playing.");
					if (Main.currentGame.isStarted())
						p.sendMessage(ChatColor.GRAY
								+ " The game started with "
								+ Main.currentGame.getOriginalStartingPlayers()
								+ " players. "
								+ Main.currentGame.getPlayers().size()
								+ " Remain.");

					p.sendMessage(ChatColor.YELLOW + list);

				} else
					p.sendMessage(Main.prefix + ChatColor.RED
							+ "No game has been started! :(");

			} else if (a1.equalsIgnoreCase("xp")) {
				
				System.out.println("Users xp: " + p.getExp() + " ( " + p.getExpToLevel() + ")");		
				
				for(int i = 1; i <= 5; i++){
				
					float xp = (float) ((0.999 / 5) * i);
					
					//System.out.println("XP Val: " + xp + " (" + i + ")");
								
					p.sendMessage("XP Val: " + xp + " (" + i + ")");
					
				}
				
			} else if (a1.equalsIgnoreCase("maplist")) {

				p.sendMessage(Main.prefix + "All Maps! "
						+ Main.data.getMaps().size() + " Maps.");

				for (FBMap map : Main.data.getMaps().values()) {
					p.sendMessage(ChatColor.GRAY + "Name: " + map.getName());
					p.sendMessage(ChatColor.GRAY + "Starting Points: "
							+ map.getStartPoints());
					p.sendMessage(ChatColor.GRAY + "Host Points: "
							+ map.getHostPoints());
					p.sendMessage("");
				}

			} else if (a1.equalsIgnoreCase("rounds")) {

				
				if (Main.data.getHighestKills().isEmpty()) {
					p.sendMessage(Main.prefix + "There are no highscores!");
					return true;
				}

				p.sendMessage(Main.prefix
						+ "The best of the best, top 5 round winners in Funbocks games.");

				int[] sortedRounds = new int[Main.data.getHighestRound().size()];

				int x = 0;
				for (int i : Main.data.getHighestRound().keySet()) {
					sortedRounds[x] = i;
					x++;
				}

				Arrays.sort(sortedRounds);

				int max = sortedRounds.length;
				
				if(max > 10){
					max = 10;
				}
				
				for (int i = max; i > 0; i--){
					try {
						String name = Bukkit.getOfflinePlayer(
								UUID.fromString(Main.data.getHighestRound()
										.get(sortedRounds[i]))).getName();
						p.sendMessage(ChatColor.GOLD + " ---- " + name
								+ "> survived to Round " + sortedRounds[i]
								+ ".");
					} catch (Exception e) {
						e.printStackTrace();
						p.sendMessage(ChatColor.RED
								+ " An error occurred tried to get this kill list.");
					}
				}

			} else if (a1.equalsIgnoreCase("kills")) {

				if (Main.data.getHighestKills().isEmpty()) {
					p.sendMessage(Main.prefix + "There are no highscores!");
					return true;
				}

				p.sendMessage(Main.prefix
						+ "The best of the best, top 5 killers in Funbocks games.");

				int[] sortedKills = new int[Main.data.getHighestKills().size()];

				int x = 0;
				for (int i : Main.data.getHighestKills().keySet()) {
					sortedKills[x] = i;
					x++;
				}

				Arrays.sort(sortedKills);

				int max = sortedKills.length;
				
				if(max > 10){
					max = 10;
				}
				
				for (int i = max; i > 0; i--){
					try {
						String name = Bukkit.getOfflinePlayer(
								UUID.fromString(Main.data.getHighestKills()
										.get(sortedKills[i]))).getName();
						p.sendMessage(ChatColor.GOLD + " ---- " + name
								+ " survived to Round" + sortedKills[i] + ".");
					} catch (Exception e) {
						e.printStackTrace();
						p.sendMessage(ChatColor.RED
								+ " An error occurred tried to get this kill list.");
					}
				}

			} else if (a1.equalsIgnoreCase("help")) {

				p.sendMessage(Main.prefix + "Funbocks help guide!");
				p.sendMessage(ChatColor.GRAY
						+ "Funbocks is a nickname for a game based around wave survival of monsters. It involves"
						+ " different round types, a shop system and endless fun for all. Originally concieved by Sorensic_Steel and modified"
						+ " by danslayerx with different concepts the game lives on!");
				p.sendMessage("");
				p.sendMessage(ChatColor.GRAY
						+ "Playing is easy, you can join when a game is announced globally in chat and the rules are "
						+ "simple! You fight against monsters that placed by the host, the host can only use so many monsters each round. Once"
						+ " all monsters are dead you are taken back to the shop starting point to spend money on more items. As rounds progress "
						+ "you can unlock more perks and items while the host gains more monsters to spawn in and bosses eventually appear.");

				p.sendMessage("");
				p.sendMessage(ChatColor.GRAY
						+ "Unique rounds include - Boss Rounds, Loot Rounds and PvP Rounds!");

				/**
				 * MOD COMMANDS
				 */

			} else if (a1.equalsIgnoreCase("kick")) {

				if (!p.hasPermission("HcRaid.MOD")) {
					p.sendMessage(Main.prefix
							+ "This command must be executed by an Moderator.");
					return true;
				}

				if (args.length <= 2) {
					p.sendMessage(Main.prefix
							+ "You need to specify a player to kick.");
					return true;
				}

				if (Main.currentGame != null) {

					if (Bukkit.getPlayer(a2) != null) {

						Player tar = Bukkit.getPlayer(a2);

						if (Main.currentGame.getPlayers().containsKey(
								tar.getUniqueId().toString())) {

							if (tar.isOp()) {
								p.sendMessage(Main.prefix + ChatColor.RED
										+ "You can't kick this person.");
								return true;
							}

							Main.currentGame.getPlayers().get(tar.getUniqueId().toString())
									.forceLeaveGame();

							p.sendMessage(Main.prefix + ChatColor.YELLOW
									+ "You kicked " + tar.getName()
									+ " from Funbocks.");

						} else
							p.sendMessage(Main.prefix + ChatColor.RED
									+ tar.getName() + " is not in the game! :O");

					} else
						p.sendMessage(Main.prefix
								+ "The player you specified is not online! :/");

				} else
					p.sendMessage(Main.prefix + ChatColor.RED
							+ "No game has been started! :(");

			} else if (a1.equalsIgnoreCase("ban")) {

				if (!p.hasPermission("HcRaid.MOD")) {
					p.sendMessage(Main.prefix
							+ "This command must be executed by an Moderator.");
					return true;
				}

				if (args.length <= 1) {
					p.sendMessage(Main.prefix
							+ "You need to specify the user to ban. /fb ban <user>");
					return true;
				}

				String id = null;
				
				Player tar = Bukkit.getPlayer(a2);
				
				if(tar == null){
					p.sendMessage(Main.prefix + "Player not found.");
					return true;
				}
				
				id = tar.getUniqueId().toString();

				Main.data.getBannedPlayers().add(id);

				p.sendMessage(Main.prefix + tar.getName()
						+ " is now banned from funbocks.");

			} else if (a1.equalsIgnoreCase("unban")) {

				if (!p.hasPermission("HcRaid.MOD")) {
					p.sendMessage(Main.prefix
							+ "This command must be executed by an Moderator.");
					return true;
				}

				if (args.length <= 2) {
					p.sendMessage(Main.prefix
							+ "You need to specify the user to unban. /fb unban <user> (For a list of banned users, do /fb banlist)");
					return true;
				}

				String id = null;
				HumanEntity p2 = Bukkit.getPlayer(a2);
				OfflinePlayer op2 = null;

				if (p2 != null) {
					id = p2.getUniqueId().toString();
					a2 = p2.getName();
				} else {
					op2 = Bukkit.getOfflinePlayer(a2);
				}

				if (op2 != null) {
					id = op2.getUniqueId().toString();
				}

				if (!Main.data.getBannedPlayers().contains(id)) {
					p.sendMessage(Main.prefix
							+ "This player was not found on the ban list, the names are case sensitive. Are you sure you "
							+ "entered the correct name? :/");
					return true;
				} else if (id == null) {
					p.sendMessage(Main.prefix
							+ "No user was found by the name of '" + a2 + "'.");
					return true;
				}

				Main.data.getBannedPlayers().remove(id);

				p.sendMessage(Main.prefix + a2
						+ " is no longer banned from Funbocks.");

			} else if (a1.equalsIgnoreCase("reward")) {
				
				if (!p.hasPermission("HcRaid.ADMIN")) {
					p.sendMessage(Main.prefix
							+ "This command must be executed by an Operator.");
					return true;
				}
				
				Core.showWinningMenu(p);
				
			} else if (a1.equalsIgnoreCase("banlist")) {

				if (!p.hasPermission("HcRaid.MOD")) {
					p.sendMessage(Main.prefix
							+ "This command must be executed by an Moderator.");
					return true;
				}

				if (Main.data.getBannedPlayers().isEmpty()) {
					p.sendMessage(Main.prefix + "There are no banned players.");
				} else {

					p.sendMessage(Main.prefix
							+ "All banned players, totalling: "
							+ Main.data.getBannedPlayers().size());
					for (String u : Main.data.getBannedPlayers()) {
						p.sendMessage(ChatColor.GRAY
								+ Bukkit.getPlayer(UUID.fromString(u)).getName());
					}
					p.sendMessage(Main.prefix
							+ "All banned players, totalling: "
							+ Main.data.getBannedPlayers().size());

				}

				/**
				 * ADMIN COMMANDS
				 */

			} else if (a1.equalsIgnoreCase("start")) {

				if (!p.hasPermission("HcRaid.ADMIN")) {
					p.sendMessage(Main.prefix
							+ "This command must be executed by an Administrator.");
					return true;
				}

				if (args.length <= 1) {
					p.sendMessage(Main.prefix
							+ "You need to specify the map to load. /fb start <mapName> (For a list of maps, do /fb maplist)");
					return true;
				}

				if (Main.currentGame != null) {
					p.sendMessage(Main.prefix
							+ "A game is already in progress.");
					p.sendMessage(ChatColor.GRAY + "Started By: "
							+ Main.currentGame.getHost().getName());
					p.sendMessage(ChatColor.GRAY + "Map: "
							+ Main.currentGame.getMap().getName());
					p.sendMessage(ChatColor.GRAY + "Alive Players: "
							+ Main.currentGame.getPlayers().size());
					return true;
				}

				if (Main.data.mapIsValid(a2)) {

					Main.currentGame = new FBGame(a2, p);

				} else {

					p.sendMessage(Main.prefix
							+ "Map not found, list of maps. [Case Sensitive]");
					for (FBMap map : Main.data.getMaps().values())
						p.sendMessage(ChatColor.GRAY + "> " + map.getName());

				}

			} else if (a1.equalsIgnoreCase("run")) {

				if (!p.hasPermission("HcRaid.ADMIN")) {
					p.sendMessage(Main.prefix
							+ "This command must be executed by an Administrator.");
					return true;
				}

				if (Main.currentGame != null) {

					if (Main.currentGame.getHost().getRawPlayer() == p
							|| p.isOp()) {

						if (!Main.currentGame.isStarted()) {

							Main.currentGame.run();

						} else
							p.sendMessage(Main.prefix + ChatColor.RED
									+ "The game has already begun!");

					} else
						p.sendMessage(Main.prefix + ChatColor.RED
								+ "You're not the host! :O");

				} else
					p.sendMessage(Main.prefix + ChatColor.RED
							+ "No game has been started! :(");

			} else if (a1.equalsIgnoreCase("end")) {

				if (!p.hasPermission("HcRaid.ADMIN")) {
					p.sendMessage(Main.prefix
							+ "This command must be executed by an Administrator.");
					return true;
				}

				if (Main.currentGame != null) {

					if (p.isOp()
							|| Main.currentGame.getHost().getRawPlayer() == p) {

						Main.currentGame.forceEnd();

						p.sendMessage(Main.prefix + "Ended game.");

					} else
						p.sendMessage(Main.prefix + ChatColor.RED
								+ "You're not the host! :O");

				} else
					p.sendMessage(Main.prefix + ChatColor.RED
							+ "No game has been started! :(");

			} else if (a1.equalsIgnoreCase("debug")) {

				for (FBMap map : Main.data.getMaps().values()) {
					p.sendMessage("MapName: " + map.getName());
					p.sendMessage("P Points: " + map.getStartPoints());
					p.sendMessage("H Points: " + map.getHostPoints());
					p.sendMessage("Shop Loc: " + map.getArena().toString());
					p.sendMessage("Arena Loc: " + map.getShop().toString());
				}

			} else if (a1.equalsIgnoreCase("spawn")) {

				if (!p.hasPermission("HcRaid.ADMIN")) {
					p.sendMessage(Main.prefix
							+ "This command must be executed by an Administrator.");
					return true;
				}

				Main.data.setSpawn(new GridLocation(p.getLocation()));

				p.sendMessage(Main.prefix
						+ "Set the spawn location for all players after a game has finished.");

			} else if (a1.equalsIgnoreCase("force")) {

				if (!p.hasPermission("HcRaid.ADMIN") && !Core.playerIsHost(p)) {
					p.sendMessage(Main.prefix
							+ "This command must be executed by an Administrator.");
					return true;
				}
				
				if(Main.currentGame != null && !Main.currentGame.isShopping()){
					p.sendMessage(Main.prefix + "Forcing round...");
					Main.currentGame.openShop();
					p.sendMessage(Main.prefix + "Forced Round.");
				}else{
					p.sendMessage(Main.prefix + ChatColor.RED + "Cannot force round when users are in shop.");
				}

			} else if (a1.equalsIgnoreCase("edit")) {

				if (!p.hasPermission("HcRaid.ADMIN")) {
					p.sendMessage(Main.prefix
							+ "This command must be executed by an Administrator.");
					return true;
				}

				if (args.length <= 2) {
					p.sendMessage(Main.prefix
							+ "Specifiy what you wish to do with the editor.");
					p.sendMessage(ChatColor.GRAY
							+ " > /fb edit new <mapName> - Create a new map. Stand where you want the arena spawn point to be.");
					p.sendMessage(ChatColor.GRAY
							+ " > /fb edit delete <mapName> - Deletes a map.");
					p.sendMessage(ChatColor.GRAY
							+ " > /fb edit setShop <mapName> - Sets the shop location.");
					p.sendMessage(ChatColor.GRAY
							+ " > /fb edit setArena <mapName> - Sets the arena location.");
					p.sendMessage(ChatColor.GRAY
							+ " > /fb edit setSpectate <mapName> - Sets the spectate location.");
					p.sendMessage(ChatColor.GRAY
							+ " > /fb edit setPoints <mapName> <points> - Sets the starting points.");
					p.sendMessage(ChatColor.GRAY
							+ " > /fb edit setHostPoints <mapName> <points> - Sets the host starting points.");
					return true;
				}

				if (a2.equalsIgnoreCase("new")) {

					FBMap map = new FBMap();

					map.setName(a3);

					Main.data.getMaps().put(a3, map);

					p.sendMessage(Main.prefix + "Created a new map called '"
							+ map.getName() + "'.");

				} else if (a2.equalsIgnoreCase("delete")) {

					if (Main.data.getMaps().containsKey(a3)) {

						Main.data.getMaps().remove(a3);

						p.sendMessage(Main.prefix + "Deleted Map '" + a3 + "'.");

					} else {

						p.sendMessage(Main.prefix
								+ "Map not found, list of maps. [Case Sensitive]");
						for (FBMap map : Main.data.getMaps().values())
							p.sendMessage(ChatColor.GRAY + "> " + map.getName());

					}

				} else if (a2.equalsIgnoreCase("setShop")) {

					if (Main.data.getMaps().containsKey(a3)) {

						Main.data.getMaps().get(a3)
								.setShop(new GridLocation(p.getLocation()));

						p.sendMessage(Main.prefix
								+ "Set the shop location for '" + a3 + "'.");

					} else {

						p.sendMessage("Map not found, list of maps. [Case Sensitive]");
						for (FBMap map : Main.data.getMaps().values())
							p.sendMessage(ChatColor.GRAY + "> " + map.getName());

					}

				} else if (a2.equalsIgnoreCase("setArena")) {

					if (Main.data.getMaps().containsKey(a3)) {

						Main.data.getMaps().get(a3)
								.setArena(new GridLocation(p.getLocation()));

						p.sendMessage(Main.prefix
								+ "Set the arena location for '" + a3 + "'.");

					} else {

						p.sendMessage("Map not found, list of maps. [Case Sensitive]");
						for (FBMap map : Main.data.getMaps().values())
							p.sendMessage(ChatColor.GRAY + "> " + map.getName());

					}

				} else if (a2.equalsIgnoreCase("setSpectate")) {

					if (Main.data.getMaps().containsKey(a3)) {

						Main.data.getMaps().get(a3)
								.setSpectate(new GridLocation(p.getLocation()));

						p.sendMessage(Main.prefix
								+ "Set the spectate location for '" + a3 + "'.");

					} else {

						p.sendMessage("Map not found, list of maps. [Case Sensitive]");
						for (FBMap map : Main.data.getMaps().values())
							p.sendMessage(ChatColor.GRAY + "> " + map.getName());

					}
					
				} else if (a2.equalsIgnoreCase("setPoints")) {

					if (args.length <= 3) {
						p.sendMessage(Main.prefix
								+ "You must specify the points required for starting.");
						return true;
					}

					int points = Integer.parseInt(args[3]);

					if (Main.data.getMaps().containsKey(a3)) {

						Main.data.getMaps().get(a3).setStartPoints(points);

						p.sendMessage(Main.prefix
								+ "Set the starting points for '" + a3 + "'.");

					} else {

						p.sendMessage("Map not found, list of maps. [Case Sensitive]");
						for (FBMap map : Main.data.getMaps().values())
							p.sendMessage(ChatColor.GRAY + "> " + map.getName());

					}

				} else if (a2.equalsIgnoreCase("setHostPoints")) {

					if (args.length <= 3) {
						p.sendMessage(Main.prefix
								+ "You must specify the points required for starting.");
						return false;
					}

					int points = Integer.parseInt(args[3]);

					if (Main.data.getMaps().containsKey(a3)) {

						Main.data.getMaps().get(a3).setHostPoints(points);

						p.sendMessage(Main.prefix + "Set the host points for '"
								+ a3 + "'.");

					} else {

						p.sendMessage("Map not found, list of maps. [Case Sensitive]");
						for (FBMap map : Main.data.getMaps().values())
							p.sendMessage(ChatColor.GRAY + "> " + map.getName());

					}

				}

			} else if (a1.equalsIgnoreCase("help")){
				showOptions(p);
			} else {

				p.sendMessage(Main.prefix
						+ "Invalid command, please type \"/fb help\" for help.");

			}

		}
		return false;
	}

	private void showOptions(Player p) {
		String r = ChatColor.AQUA + " > /fb ";
		ChatColor a = ChatColor.GRAY;

		if (p.hasPermission("HcRaid.ADMIN")) {
			p.sendMessage(Main.prefix + " === Commands === Admin**");
			p.sendMessage(r + "start <map name>" + a
					+ " - Starts a game, with the specified map.");
			p.sendMessage(r + "run" + a
					+ " - Runs the game that has been started.");
			p.sendMessage(r
					+ "end"
					+ a
					+ " - Ends an existing game, only one game may run at a time.");
			p.sendMessage(r + "force" + a
					+ " - Force a round to end, only use in emergency.");
			p.sendMessage(r + "edit" + a
					+ " - Brings up the editor for adding and editing maps.");
			p.sendMessage(r
					+ "spawn"
					+ a
					+ " - Set spawn location for players to be sent after games.");
		}

		if (p.hasPermission("HcRaid.MOD")) {
			p.sendMessage(Main.prefix + " === Commands === Mod*");
			p.sendMessage(r + "unban <player>" + a
					+ " - unbans a user from playing Funbocks. :O");
			p.sendMessage(r + "ban <player>" + a
					+ " - Bans a user from playing Funbocks. :O");
			p.sendMessage(r + "kick <player>" + a
					+ " - Kicks a user from the Funbocks. :(");
		}

		p.sendMessage(Main.prefix + " === Commands === Player");
		p.sendMessage(r + "join" + a + " - Join a Funbocks game. :D");
		p.sendMessage(r + "kills" + a + " - List the top kills. :D");
		p.sendMessage(r + "rounds" + a + " - List the top rounds. :D");
		p.sendMessage(r + "leave" + a + " - Leave a Funbocks game. :(");
		p.sendMessage(r + "spectate" + a + " - Spectate a Funbocks game. :]");
		p.sendMessage(r + "balance" + a
				+ " - Displays your balance in FB Dollars! :]");
		p.sendMessage(r + "list" + a
				+ " - List all players currently playing. :]");
		p.sendMessage(r + "maplist" + a + " - List all maps. :]");
		p.sendMessage(r + "help" + a + " - Explains how to play Funbocks. :]");
	}

}
