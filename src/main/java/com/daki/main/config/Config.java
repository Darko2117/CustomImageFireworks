package com.daki.main.config;

import com.daki.main.CIF;
import com.daki.main.Cache;
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

        //Images

        if (!config.contains("Images-path")) {
            config.set("Images-path", new File(CIF.getInstance().getDataFolder() + "/images/").getAbsolutePath() + "/");
            CIF.getInstance().getLogger().info("Images-path not found in the config, creating it now.");
        }

        //----------------------------------------------------------------------------------------------------

        //Resized images

        if (!config.contains("Resized-images-path")) {
            config.set("Resized-images-path", new File(CIF.getInstance().getDataFolder() + "/resized-images/").getAbsolutePath() + "/");
            CIF.getInstance().getLogger().info("Resized-images-path not found in the config, creating it now.");
        }

        //----------------------------------------------------------------------------------------------------

        //How many times the image is drawn

        if (!config.contains("Times-to-draw-image")) {
            config.set("Times-to-draw-image", 3);
            CIF.getInstance().getLogger().info("Times-to-draw-image not found in the config, creating it now.");
        }

        //----------------------------------------------------------------------------------------------------

        CIF.getInstance().saveConfig();

    }

    public static void reloadCachedConfigData() {

        Cache.setDatabaseHost(CIF.getInstance().getConfig().getString("Database.host"));
        Cache.setDatabasePort(CIF.getInstance().getConfig().getString("Database.port"));
        Cache.setDatabaseName(CIF.getInstance().getConfig().getString("Database.databaseName"));
        Cache.setDatabaseUsername(CIF.getInstance().getConfig().getString("Database.username"));
        Cache.setDatabasePassword(CIF.getInstance().getConfig().getString("Database.password"));

        Cache.setImagesPath(CIF.getInstance().getConfig().getString("Images-path"));
        Cache.setResizedImagesPath(CIF.getInstance().getConfig().getString("Resized-images-path"));
        Cache.setTimesToDrawImage(CIF.getInstance().getConfig().getInt("Times-to-draw-image"));

        if (!Cache.getImagesPath().endsWith("/")) {
            CIF.getInstance().getConfig().set("Images-path", Cache.getImagesPath().concat("/"));
            CIF.getInstance().saveConfig();
        }

        if (!Cache.getResizedImagesPath().endsWith("/")) {
            CIF.getInstance().getConfig().set("Resized-images-path", Cache.getResizedImagesPath().concat("/"));
            CIF.getInstance().saveConfig();
        }

    }

}
