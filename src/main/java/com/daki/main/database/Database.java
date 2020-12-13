package com.daki.main.database;

import com.daki.main.CIF;
import com.daki.main.Cache;
import com.daki.main.firework.Firework;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Database {

    public static Connection connection;

    public static void reinitialize(Boolean async) {

        if (async) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (!reconnect()) {
                        CIF.getInstance().getLogger().severe("Connection to the database failed.");
                        return;
                    }
                    if (!createTables()) {
                        CIF.getInstance().getLogger().severe("Creating database tables failed.");
                        return;
                    }
                    reloadCachedDatabaseData();
                }
            }.runTaskAsynchronously(CIF.getInstance());
        } else {
            if (!reconnect()) {
                CIF.getInstance().getLogger().severe("Connection to the database failed.");
                return;
            }
            if (!createTables()) {
                CIF.getInstance().getLogger().severe("Creating database tables failed.");
                return;
            }
            reloadCachedDatabaseData();
        }

    }

    public static Boolean reconnect() {

        String url = "jdbc:mysql://" + Cache.getDatabaseHost() + ":" + Cache.getDatabasePort() + "/" + Cache.getDatabaseName();

        try {
            connection = DriverManager.getConnection(url, Cache.getDatabaseUsername(), Cache.getDatabasePassword());
            CIF.getInstance().getLogger().info("Connected to the database!");
            return true;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return false;
        }

    }

    public static Boolean createTables() {

        List<String> columns = new ArrayList<>();
        String query;
        List<String> confirmationMessages = new ArrayList<>();

        //fireworks table

        query = "CREATE TABLE IF NOT EXISTS fireworks(ID INT NOT NULL,PRIMARY KEY (ID))";

        try {
            connection.prepareStatement(query).executeUpdate();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return false;
        }

        columns.add("ALTER TABLE fireworks ADD Name TEXT NOT NULL");
        columns.add("ALTER TABLE fireworks ADD ImageName TEXT NOT NULL");
        columns.add("ALTER TABLE fireworks ADD Power INT NOT NULL");
        columns.add("ALTER TABLE fireworks ADD Cooldown INT NOT NULL");
        columns.add("ALTER TABLE fireworks ADD FireworkDimensions TEXT NOT NULL");
        columns.add("ALTER TABLE fireworks ADD ResizedImageDimensions TEXT NOT NULL");
        for (String statement : columns) {
            try {
                connection.prepareStatement(statement).executeUpdate();
                confirmationMessages.add(statement + " executed!");
            } catch (Throwable ignored) {
            }
        }

        //users table

        query = "CREATE TABLE IF NOT EXISTS users(" + "ID INT NOT NULL AUTO_INCREMENT," + "PRIMARY KEY (ID))";

        try {
            connection.prepareStatement(query).executeUpdate();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return false;
        }

        columns.clear();
        columns.add("ALTER TABLE users ADD UUID TEXT NOT NULL");
        columns.add("ALTER TABLE users ADD Username TEXT NOT NULL");
        columns.add("ALTER TABLE users ADD AvailableFireworks TEXT NOT NULL");
        for (String statement : columns) {
            try {
                connection.prepareStatement(statement).executeUpdate();
                confirmationMessages.add(statement + " executed!");
            } catch (Throwable ignored) {
            }
        }

        //Confirmation messages, because I want it to look good in the log and not get interrupted by something else

        for (String message : confirmationMessages) {
            CIF.getInstance().getLogger().info(message);
        }

        return true;

    }


    public static void reloadCachedDatabaseData() {

        String statement = "SELECT * FROM fireworks;";

        Cache.clearLoadedFireworks();

        try {

            ResultSet rs = Database.connection.prepareStatement(statement).executeQuery();

            while (rs.next()) {

                String ID = rs.getString("ID");
                String name = rs.getString("Name");
                String imageName = rs.getString("ImageName");
                String power = rs.getString("Power");
                String cooldown = rs.getString("Cooldown");
                String fireworkDimensions = rs.getString("FireworkDimensions");
                String resizedImageDimensions = rs.getString("ResizedImageDimensions");

                Firework firework = new Firework(ID, name, imageName, power, cooldown, fireworkDimensions, resizedImageDimensions);

                Cache.addLoadedFirework(firework);

            }

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

    }

}