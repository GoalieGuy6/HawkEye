package uk.co.oliwali.HawkEye.util;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Permissions handler for HawkEye
 * Supports multiple permissions systems
 * @author oliverw92
 */
public class Permission {

	/**
	 * Private method for checking a users permission level.
	 * Permission checks from other classes should go through a separate method for each node.
	 * @param sender
	 * @param node
	 * @return true if the user has permission, false if not
	 */
	private static boolean hasPermission(CommandSender sender, String node) {
		if (!(sender instanceof Player)) return true;

		Player player = (Player) sender;
		if (Config.OpPermissions && player.isOp()) return true;

		return player.hasPermission(node);
	}

	/**
	 * Permission to view different pages
	 * @param player
	 * @return
	 */
	public static boolean page(CommandSender player) {
		return hasPermission(player, "hawkeye.page");
	}

	/**
	 * Permission to search the logs
	 * @param player
	 * @return
	 */
	public static boolean search(CommandSender player) {
		return hasPermission(player, "hawkeye.search");
	}

	/**
	 * Permission to search a specific data type
	 * @param player
	 * @return
	 */
	public static boolean searchType(CommandSender player, String type) {
		return hasPermission(player, "hawkeye.search." + type.toLowerCase());
	}

	/**
	 * Permission to teleport to the location of a result
	 * @param player
	 * @return
	 */
	public static boolean tpTo(CommandSender player) {
		return hasPermission(player, "hawkeye.tpto");
	}

	/**
	 * Permission to use the rollback command
	 * @param player
	 * @return
	 */
	public static boolean rollback(CommandSender player) {
		return hasPermission(player, "hawkeye.rollback");
	}

	/**
	 * Permission to the hawkeye tool
	 * @param player
	 * @return
	 */
	public static boolean tool(CommandSender player) {
		return hasPermission(player, "hawkeye.tool");
	}

	/**
	 * Permission to be notified of rule breaks
	 * @param player
	 * @return
	 */
	public static boolean notify(CommandSender player) {
		return hasPermission(player, "hawkeye.notify");
	}

	/**
	 * Permission to preview rollbacks
	 * @param player
	 * @return
	 */
	public static boolean preview(CommandSender player) {
		return hasPermission(player, "hawkeye.preview");
	}

    /**
     * Permission to bind a tool
     * @param player
     * @return
     */
	public static boolean toolBind(CommandSender player) {
		return hasPermission(player, "hawkeye.tool.bind");
	}

	/**
	 * Permission to rebuild
	 * @param player
	 * @return
	 */
	public static boolean rebuild(CommandSender player) {
		return hasPermission(player, "hawkeye.rebuild");
	}

	/**
	 * Permission to delete entires
	 * @param player
	 * @return
	 */
	public static boolean delete(CommandSender player) {
		return hasPermission(player, "hawkeye.delete");
	}

}
