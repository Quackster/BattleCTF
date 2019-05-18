package org.alexdev.battlectf;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import org.alexdev.battlectf.commands.CommandHandler;
import org.alexdev.battlectf.listeners.BlockListener;
import org.alexdev.battlectf.listeners.InteractListener;
import org.alexdev.battlectf.listeners.PlayerListener;
import org.alexdev.battlectf.managers.arena.ArenaManager;
import org.alexdev.battlectf.managers.configuration.ConfigurationManager;
import org.alexdev.battlectf.managers.players.PlayerManager;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class BattleCTF extends JavaPlugin {
    private static BattleCTF instance;
    private Logger logger;

    private boolean worldEditSupport;
    private WorldEditPlugin worldEdit;

    @Override
    public void onEnable() {
        instance = this;
        logger = getLogger();
        logger.info("Starting plugin");

        // Setup worldedit support
        setupWorldEdit();

        // Load singletons
        ConfigurationManager.getInstance();
        PlayerManager.getInstance();
        ArenaManager.getInstance();

        // Load configuration
        saveDefaultConfig();
        ConfigurationManager.getInstance().readConfig(getConfig());

        // Command handling
        CommandExecutor myCommands = new CommandHandler();
        getCommand("battlectf").setExecutor(myCommands);

        // Load arenas
        ArenaManager.getInstance().loadArenas();
        this.logger.info("There are " + ArenaManager.getInstance().getArenas().size() + " arenas loaded");

        // Reload players
        PlayerManager.getInstance().reloadPlayers();
        this.logger.info("There are " + PlayerManager.getInstance().getPlayers().size() + " players stored");

        this.registerListeners();
        logger.info("Finished");
    }

    private void setupWorldEdit() {
        Plugin plugin = getServer().getPluginManager().getPlugin("WorldEdit");
        if (plugin == null || !(plugin instanceof WorldEditPlugin))
            return;

        worldEditSupport = true;
        worldEdit = (WorldEditPlugin) plugin;
        getServer().getConsoleSender().sendMessage("Enabled WorldEdit support!");
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
        ArenaManager.getInstance().getArenas().clear();
    }

    /**
     * Get the java plugin instance.
     *
     * @return the plugin instance
     */
    public static BattleCTF getInstance() {
        return instance;
    }

    /**
     * Get the worldedit instance.
     *
     * @return the worldedit instance
     */
    public WorldEditPlugin getWorldEdit() {
        return worldEdit;
    }
}
