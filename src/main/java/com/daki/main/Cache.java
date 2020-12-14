package com.daki.main;

import com.daki.main.UI.panels.AllFireworksPanel;
import com.daki.main.UI.panels.ChooseImageNamePanel;
import com.daki.main.UI.panels.StartingPanel;
import com.daki.main.firework.Firework;
import org.bukkit.entity.Player;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cache {

    static String databaseHost = null;
    static String databasePort = null;
    static String databaseName = null;
    static String databaseUsername = null;
    static String databasePassword = null;
    static String imagesPath = null;
    static String resizedImagesPath = null;
    static Integer timesToDrawImage = null;
    static List<Firework> loadedFireworks = new ArrayList<>();
    static HashMap<Player, StartingPanel> startingPanels = new HashMap<>();
    static HashMap<Player, ChooseImageNamePanel> chooseImageNamePanels = new HashMap<>();
    static HashMap<Player, AllFireworksPanel> allFireworksPanels = new HashMap<>();
    static HashMap<Player, Integer> whatPageOfChooseImageNamePanelIsPlayerOn = new HashMap<>();
    static HashMap<Player, Integer> whatPageOfAllFireworksPanelIsPlayerOn = new HashMap<>();
    static HashMap<Player, org.bukkit.entity.Firework> launchedFireworks = new HashMap<>();
    static HashMap<String, BufferedImage> loadedImages = new HashMap<>();

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

    public static String getImagesPath() {
        return imagesPath;
    }

    public static void setImagesPath(String imagesPath) {
        Cache.imagesPath = imagesPath;
    }

    public static String getResizedImagesPath() {
        return resizedImagesPath;
    }

    public static void setResizedImagesPath(String resizedImagesPath) {
        Cache.resizedImagesPath = resizedImagesPath;
    }

    public static Integer getTimesToDrawImage() {
        return timesToDrawImage;
    }

    public static void setTimesToDrawImage(Integer timesToDrawImage) {
        Cache.timesToDrawImage = timesToDrawImage;
    }

    public static List<Firework> getLoadedFireworks() {
        return loadedFireworks;
    }

    public static void setLoadedFireworks(List<Firework> loadedFireworks) {
        Cache.loadedFireworks = loadedFireworks;
    }

    public static void addLoadedFirework(Firework firework) {
        loadedFireworks.add(firework);
    }

    public static void removeLoadedFirework(Firework firework) {
        loadedFireworks.remove(firework);
    }

    public static void clearLoadedFireworks() {
        loadedFireworks.clear();
    }

    public static Firework getFireworkByID(String ID) {
        for (Firework firework : loadedFireworks) {
            if (firework.getID().equals(ID)) {
                return firework;
            }
        }
        return null;
    }

    public static HashMap<Player, StartingPanel> getStartingPanels() {
        return startingPanels;
    }

    public static void setStartingPanels(HashMap<Player, StartingPanel> startingPanels) {
        Cache.startingPanels = startingPanels;
    }

    public static void addStartingPanel(Player player, StartingPanel startingPanel) {
        Cache.startingPanels.put(player, startingPanel);
    }

    public static void removeStartingPanel(Player player) {
        Cache.startingPanels.remove(player);
    }

    public static void clearStartingPanels() {
        Cache.startingPanels.clear();
    }

    public static HashMap<Player, ChooseImageNamePanel> getChooseImageNamePanels() {
        return chooseImageNamePanels;
    }

    public static void setChooseImageNamePanels(HashMap<Player, ChooseImageNamePanel> chooseImageNamePanels) {
        Cache.chooseImageNamePanels = chooseImageNamePanels;
    }

    public static void addChooseImageNamePanel(Player player, ChooseImageNamePanel chooseImageNamePanel) {
        Cache.chooseImageNamePanels.put(player, chooseImageNamePanel);
    }

    public static void removeChooseImageNamePanel(Player player) {
        Cache.chooseImageNamePanels.remove(player);
    }

    public static void clearChooseImageNamePanels() {
        Cache.chooseImageNamePanels.clear();
    }

    public static HashMap<Player, AllFireworksPanel> getAllFireworksPanels() {
        return allFireworksPanels;
    }

    public static void setAllFireworksPanels(HashMap<Player, AllFireworksPanel> allFireworksPanels) {
        Cache.allFireworksPanels = allFireworksPanels;
    }

    public static void addAllFireworksPanel(Player player, AllFireworksPanel allFireworksPanel) {
        allFireworksPanels.put(player, allFireworksPanel);
    }

    public static void removeAllFireworksPanel(Player player) {
        allFireworksPanels.remove(player);
    }

    public static void clearAllFireworksPanels() {
        allFireworksPanels.clear();
    }

    public static HashMap<Player, Integer> getWhatPageOfChooseImageNamePanelIsPlayerOn() {
        return whatPageOfChooseImageNamePanelIsPlayerOn;
    }

    public static void setWhatPageOfChooseImageNamePanelIsPlayerOn(HashMap<Player, Integer> whatPageOfChooseImageNamePanelIsPlayerOn) {
        Cache.whatPageOfChooseImageNamePanelIsPlayerOn = whatPageOfChooseImageNamePanelIsPlayerOn;
    }

    public static void addWhatPageOfChooseImageNamePanelIsPlayerOn(Player player, Integer integer) {
        Cache.whatPageOfChooseImageNamePanelIsPlayerOn.put(player, integer);
    }

    public static void removeWhatPageOfChooseImageNamePanelIsPlayerOn(Player player) {
        Cache.whatPageOfChooseImageNamePanelIsPlayerOn.remove(player);
    }

    public static void clearWhatPageOfChooseImageNamePanelIsPlayerOn() {
        Cache.whatPageOfChooseImageNamePanelIsPlayerOn.clear();
    }

    public static HashMap<Player, Integer> getWhatPageOfAllFireworksPanelIsPlayerOn() {
        return whatPageOfAllFireworksPanelIsPlayerOn;
    }

    public static void setWhatPageOfAllFireworksPanelIsPlayerOn(HashMap<Player, Integer> whatPageOfAllFireworksPanelIsPlayerOn) {
        Cache.whatPageOfAllFireworksPanelIsPlayerOn = whatPageOfAllFireworksPanelIsPlayerOn;
    }

    public static void addWhatPageOfAllFireworksPanelIsPlayerOn(Player player, Integer integer) {
        whatPageOfAllFireworksPanelIsPlayerOn.put(player, integer);
    }

    public static void removeWhatPageOfAllFireworksPanelIsPlayerOn(Player player) {
        whatPageOfAllFireworksPanelIsPlayerOn.remove(player);
    }

    public static void clearWhatPageOfAllFireworksPanelIsPlayerOn() {
        whatPageOfAllFireworksPanelIsPlayerOn.clear();
    }

    public static HashMap<Player, org.bukkit.entity.Firework> getLaunchedFireworks() {
        return launchedFireworks;
    }

    public static void setLaunchedFireworks(HashMap<Player, org.bukkit.entity.Firework> launchedFireworks) {
        Cache.launchedFireworks = launchedFireworks;
    }

    public static void addLaunchedFirework(Player player, org.bukkit.entity.Firework firework) {
        launchedFireworks.put(player, firework);
    }

    public static void removeLaunchedFirework(Player player, org.bukkit.entity.Firework firework) {
        launchedFireworks.remove(player, firework);
    }

    public static void clearLaunchedFireworks() {
        launchedFireworks.clear();
    }

    public static Player getPlayerFromLaunchedFirework(org.bukkit.entity.Firework firework) {
        for (Map.Entry<Player, org.bukkit.entity.Firework> entry : launchedFireworks.entrySet()) {
            if (entry.getValue().equals(firework)) return entry.getKey();
        }
        return null;
    }

    public static HashMap<String, BufferedImage> getLoadedImages() {
        return loadedImages;
    }

    public static void setLoadedImages(HashMap<String, BufferedImage> loadedImages) {
        Cache.loadedImages = loadedImages;
    }

    public static void loadImage(String imageName) {

        try {

            Image image = ImageIO.read(new File(Cache.getResizedImagesPath() + imageName));

            BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);

            Graphics2D bGr = bufferedImage.createGraphics();
            bGr.drawImage(image, 0, 0, null);
            bGr.dispose();

            loadedImages.put(imageName, bufferedImage);

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

    }

    public static void unloadImage(String imageName) {
        loadedImages.remove(imageName);
    }

}