package org.alexdev.battlectf;

import org.alexdev.battlectf.commands.Commands;
import org.alexdev.battlectf.listeners.BlockListener;
import org.alexdev.battlectf.managers.configuration.ConfigurationManager;
import org.bukkit.command.CommandExecutor;
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

        // Load configuration
        saveDefaultConfig();
        ConfigurationManager.getInstance().readConfig(getConfig());

        CommandExecutor myCommands = new Commands();
        getCommand("myplugin").setExecutor(myCommands);

        this.registerListeners();
        logger.info("Finished");
    }

    /**
     * Register the listeners.
     */
    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new BlockListener(), this);
    }

    @Override
    public void onDisable() {

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
