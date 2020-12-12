package com.daki.main.config;

import com.daki.main.CIF;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Config {

    public static void checkAndSaveDefault() {

        FileConfiguration config = CIF.getInstance().getConfig();

        //Directories

        List<String> directories = new ArrayList<>();
        directories.add("images");
        directories.add("resized-images");

        for (String directory : directories) {
            File file = new File(CIF.getInstance().getDataFolder() + "/" + directory);
            if (!file.exists()) {
                if (file.mkdir())
                    CIF.getInstance().getLogger().info(file.getName() + " directory created!");
            }
        }

        //----------------------------------------------------------------------------------------------------

        //Database

        if (!config.contains("Database.host")) {
            config.set("Database.host", "localhost");
            CIF.getInstance().getLogger().info("Database.host not found in the config, creating it now.");
        }
        if (!config.contains("Database.port")) {
            config.set("Database.port", "3306");
            CIF.getInstance().getLogger().info("Database.port not found in the config, creating it now.");
        }
        if (!config.contains("Database.databaseName")) {
            config.set("Database.databaseName", "cif");
            CIF.getInstance().getLogger().info("Database.databaseName not found in the config, creating it now.");
        }
        if (!config.contains("Database.username")) {
            config.set("Database.username", "root");
            CIF.getInstance().getLogger().info("Database.username not found in the config, creating it now.");
        }
        if (!config.contains("Database.password")) {
            config.set("Database.password", "");
            CIF.getInstance().getLogger().info("Database.password not found in the config, creating it now.");
        }

        //----------------------------------------------------------------------------------------------------

        //Resized images

        if (!config.contains("Resized-images-path")) {
            config.set("Resized-images-path", new File(CIF.getInstance().getDataFolder() + "/resized-images/").getAbsolutePath() + "/");
        }

        CIF.getInstance().saveConfig();

    }

}
