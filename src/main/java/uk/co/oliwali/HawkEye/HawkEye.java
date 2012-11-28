package uk.co.oliwali.HawkEye;

import java.util.ArrayList;
import java.util.List;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import uk.co.oliwali.HawkEye.commands.BaseCommand;
import uk.co.oliwali.HawkEye.commands.DeleteCommand;
import uk.co.oliwali.HawkEye.commands.HelpCommand;
import uk.co.oliwali.HawkEye.commands.HereCommand;
import uk.co.oliwali.HawkEye.commands.PageCommand;
import uk.co.oliwali.HawkEye.commands.PreviewApplyCommand;
import uk.co.oliwali.HawkEye.commands.PreviewCancelCommand;
import uk.co.oliwali.HawkEye.commands.PreviewCommand;
import uk.co.oliwali.HawkEye.commands.RebuildCommand;
import uk.co.oliwali.HawkEye.commands.RollbackCommand;
import uk.co.oliwali.HawkEye.commands.SearchCommand;
import uk.co.oliwali.HawkEye.commands.ToolBindCommand;
import uk.co.oliwali.HawkEye.commands.ToolCommand;
import uk.co.oliwali.HawkEye.commands.ToolResetCommand;
import uk.co.oliwali.HawkEye.commands.TptoCommand;
import uk.co.oliwali.HawkEye.commands.UndoCommand;
import uk.co.oliwali.HawkEye.database.DataManager;
import uk.co.oliwali.HawkEye.listeners.MonitorBlockListener;
import uk.co.oliwali.HawkEye.listeners.MonitorEntityListener;
import uk.co.oliwali.HawkEye.listeners.MonitorPlayerListener;
import uk.co.oliwali.HawkEye.listeners.MonitorWorldListener;
import uk.co.oliwali.HawkEye.listeners.ToolListener;
import uk.co.oliwali.HawkEye.util.Config;
import uk.co.oliwali.HawkEye.util.Util;

public class HawkEye extends JavaPlugin {

	public String name;
	public String version;
	public Config config;
	public static Server server;
	public MonitorBlockListener monitorBlockListener = new MonitorBlockListener(this);
	public MonitorEntityListener monitorEntityListener = new MonitorEntityListener(this);
	public MonitorPlayerListener monitorPlayerListener = new MonitorPlayerListener(this);
	public MonitorWorldListener monitorWorldListener = new MonitorWorldListener(this);
	public ToolListener toolListener = new ToolListener();
	public static List<BaseCommand> commands = new ArrayList<BaseCommand>();
	public static WorldEditPlugin worldEdit = null;
	public static ContainerAccessManager containerManager;

	/**
	 * Safely shuts down HawkEye
	 */
	@Override
	public void onDisable() {
		DataManager.close();
		Util.info("Version " + version + " disabled!");
	}

	/**
	 * Starts up HawkEye initiation process
	 */
	@Override
	public void onEnable() {

		//Set up config and permissions
        PluginManager pm = getServer().getPluginManager();
		server = getServer();
		name = getDescription().getName();
        version = getDescription().getVersion();

		Util.info("Starting HawkEye " + version + " initiation process...");

		//Load config and permissions
        config = new Config(this);

        new SessionManager();

        //Initiate database connection
        try {
			new DataManager(this);
		} catch (Exception e) {
			Util.severe("Error initiating HawkEye database connection, disabling plugin");
			pm.disablePlugin(this);
			return;
		}

		checkDependencies(pm);

		containerManager = new ContainerAccessManager();

	    registerListeners(pm);

	    registerCommands();

        Util.info("Version " + version + " enabled!");

	}

	/**
	 * Checks if required plugins are loaded
	 * @param pm PluginManager
	 */
	private void checkDependencies(PluginManager pm) {

        //Check if WorldEdit is loaded
        Plugin we = pm.getPlugin("WorldEdit");
        if (we != null) {
        	worldEdit = (WorldEditPlugin)we;
        	Util.info("WorldEdit found, selection rollbacks enabled");
        }
        else Util.info("WARNING! WorldEdit not found, WorldEdit selection rollbacks disabled until WorldEdit is available");

	}

	/**
	 * Registers event listeners
	 * @param pm PluginManager
	 */
	private void registerListeners(PluginManager pm) {

		monitorBlockListener.registerEvents();
		monitorPlayerListener.registerEvents();
		monitorEntityListener.registerEvents();
		monitorWorldListener.registerEvents();
		pm.registerEvents(toolListener, this);

	}

	/**
	 * Registers commands for use by the command manager
	 */
	private void registerCommands() {

        //Add commands
        commands.add(new HelpCommand());
        commands.add(new ToolBindCommand());
        commands.add(new ToolResetCommand());
        commands.add(new ToolCommand());
        commands.add(new SearchCommand());
        commands.add(new PageCommand());
        commands.add(new TptoCommand());
        commands.add(new HereCommand());
        commands.add(new PreviewApplyCommand());
        commands.add(new PreviewCancelCommand());
        commands.add(new PreviewCommand());
        commands.add(new RollbackCommand());
        // if (worldEdit != null) commands.add(new WorldEditRollbackCommand());
        commands.add(new UndoCommand());
        commands.add(new RebuildCommand());
        commands.add(new DeleteCommand());

	}

	/**
	 * Command manager for HawkEye
	 * @param sender - {@link CommandSender}
	 * @param cmd - {@link Command}
	 * @param commandLabel - String
	 * @param args[] - String[]
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String args[]) {
		if (cmd.getName().equalsIgnoreCase("hawk")) {
			if (args.length == 0)
				args = new String[]{"help"};
			outer:
			for (BaseCommand command : commands.toArray(new BaseCommand[0])) {
				String[] cmds = command.name.split(" ");
				for (int i = 0; i < cmds.length; i++)
					if (i >= args.length || !cmds[i].equalsIgnoreCase(args[i])) continue outer;
				return command.run(this, sender, args, commandLabel);
			}
			new HelpCommand().run(this, sender, args, commandLabel);
			return true;
		}
		return false;

	}

}
