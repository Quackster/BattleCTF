package org.alexdev.battlectf.managers.configuration;

import java.io.File;
import java.util.logging.Logger;

import org.alexdev.battlectf.BattleCTF;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigurationManager {
    private static ConfigurationManager instance;
    private Logger logger;// = BattleCTF.P.getLogger();

    public ConfigurationManager() {
        this.logger = BattleCTF.getInstance().getLogger();
    }

    /**
     * Set configuration from values in a file configuration.
     *
     * @param savedConfig the existing file configuration
     */
    public void readConfig(FileConfiguration savedConfig) {
        BattleCTF.getInstance().saveDefaultConfig();
        YamlConfiguration.loadConfiguration(new File(BattleCTF.getInstance().getDataFolder().getAbsolutePath(), "file.test"));
        //this.variable = savedConfig.getBoolean("variable",false);
    }

    /**
     * Get configuration singleton.
     *
     * @return the configuration instance
     */
    public static ConfigurationManager getInstance() {
        if (instance == null) {
            instance = new ConfigurationManager();
        }

        return instance;
    }
}
