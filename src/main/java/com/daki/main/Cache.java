package com.daki.main;

import org.bukkit.scheduler.BukkitRunnable;

public class Cache {

    static String databaseHost = null;
    static String databasePort = null;
    static String databaseName = null;
    static String databaseUsername = null;
    static String databasePassword = null;
    static String resizedImagesPath = null;

    public static String getDatabaseHost() {
        return databaseHost;
    }

    public static void setDatabaseHost(String databaseHost) {
        Cache.databaseHost = databaseHost;
    }

    public static String getDatabasePort() {
        return databasePort;
    }

    public static void setDatabasePort(String databasePort) {
        Cache.databasePort = databasePort;
    }

    public static String getDatabaseName() {
        return databaseName;
    }

    public static void setDatabaseName(String databaseName) {
        Cache.databaseName = databaseName;
    }

    public static String getDatabaseUsername() {
        return databaseUsername;
    }

    public static void setDatabaseUsername(String databaseUsername) {
        Cache.databaseUsername = databaseUsername;
    }

    public static String getDatabasePassword() {
        return databasePassword;
    }

    public static void setDatabasePassword(String databasePassword) {
        Cache.databasePassword = databasePassword;
    }

    public static String getResizedImagesPath() {
        return resizedImagesPath;
    }

    public static void setResizedImagesPath(String resizedImagesPath) {
        Cache.resizedImagesPath = resizedImagesPath;
    }

    public static void reloadCachedConfigData() {

        databaseHost = CIF.getInstance().getConfig().getString("Database.host");
        databasePort = CIF.getInstance().getConfig().getString("Database.port");
        databaseName = CIF.getInstance().getConfig().getString("Database.databaseName");
        databaseUsername = CIF.getInstance().getConfig().getString("Database.username");
        databasePassword = CIF.getInstance().getConfig().getString("Database.password");

        resizedImagesPath = CIF.getInstance().getConfig().getString("Resized-images-path");

    }

    public static void reloadCachedDatabaseData() {
        new BukkitRunnable() {
            @Override
            public void run() {

            }
        }.runTaskAsynchronously(CIF.getInstance());
    }

}