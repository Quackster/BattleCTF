package org.alexdev.battlectf;

import org.alexdev.battlectf.commands.CommandHandler;
import org.alexdev.battlectf.listeners.BlockListener;
import org.alexdev.battlectf.listeners.InteractListener;
import org.alexdev.battlectf.listeners.PlayerListener;
import org.alexdev.battlectf.managers.configuration.ConfigurationManager;
import org.alexdev.battlectf.managers.players.PlayerManager;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class BattleCTF extends JavaPlugin {
    private static BattleCTF instance;
    private Logger logger;

    @Override
    public void onEnable() {
        instance = this;
        logger = getLogger();
        logger.info("Starting plugin");

        // Load singletons
        ConfigurationManager.getInstance();
        PlayerManager.getInstance();

        // Load configuration
        saveDefaultConfig();
        ConfigurationManager.getInstance().readConfig(getConfig());

        // Command handling
        CommandExecutor myCommands = new CommandHandler();
        getCommand("battlectf").setExecutor(myCommands);

        // Reload players
        PlayerManager.getInstance().reloadPlayers();
        this.logger.info("There are " + PlayerManager.getInstance().getPlayers().size() + " players stored");

        this.registerListeners();
        logger.info("Finished");
    }

    /**
     * Register the listeners.
     */
    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new BlockListener(), this);
        getServer().getPluginManager().registerEvents(new InteractListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
    }

    @Override
    public void onDisable() {
        PlayerManager.getInstance().getPlayers().clear();
    }

    /**
     * Get the java plugin instance.
     *
     * @return the plugin instance
     */
    public static BattleCTF getInstance() {
        return instance;
    }
}
