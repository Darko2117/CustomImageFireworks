package com.daki.main.firework;

import com.daki.main.CIF;
import com.daki.main.Cache;
import com.daki.main.Methods;
import com.destroystokyo.paper.ParticleBuilder;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FireworkExplodeEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.awt.image.BufferedImage;

public class FireworkExplosionListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onFireworkExplode(FireworkExplodeEvent event) {

        new BukkitRunnable() {
            @Override
            public void run() {

                org.bukkit.entity.Firework fireworkEntity = event.getEntity();
                Player player = Cache.getPlayerFromLaunchedFirework(fireworkEntity);
                String ID = fireworkEntity.getFireworkMeta().getLore().get(1).substring(4);
                Firework firework = Cache.getFireworkByID(ID);
                String resizedImageName = Methods.getImageNameWithDimensions(firework.getImageName(), firework.getResizedImageDimensions());

                Methods.resizeImageIfNotExisting(firework.getImageName(), firework.getResizedImageDimensions());

                Cache.loadImage(resizedImageName);
                BufferedImage image = Cache.getLoadedImages().get(resizedImageName);
                Cache.unloadImage(resizedImageName);

                Location location = event.getEntity().getLocation();

                StringBuilder imageDimensions = new StringBuilder(firework.getFireworkDimensions());

                Integer imageAreaX = Integer.parseInt(imageDimensions.substring(0, imageDimensions.indexOf("x")));
                Integer imageAreaY = Integer.parseInt(imageDimensions.substring(imageDimensions.indexOf("x") + 1, imageDimensions.length()));

                Float yaw = Math.abs(player.getLocation().getYaw());

                while (yaw > 360) yaw -= 360;

                String facing;

                if (yaw >= 45 && yaw < 135) facing = "west";
                else if (yaw >= 135 && yaw < 225) facing = "north";
                else if (yaw >= 225 && yaw < 315) facing = "east";
                else facing = "south";

                for (Integer i = 0; i < Cache.getTimesToDrawImage(); i++) {
                    drawImage(image, location, imageAreaX, imageAreaY, facing);
                }

                CIF.getInstance().getLogger().info(player.getName() + " used a firework with the image " + firework.getImageName() + ". Drawn " + Cache.getTimesToDrawImage() + " times.");

            }
        }.runTaskAsynchronously(CIF.getInstance());

    }

    private static void drawImage(BufferedImage image, Location location, Integer imageAreaX, Integer imageAreaZ, String facing) {

        switch (facing) {

            case "west":
                for (Integer z = 0; z < image.getWidth(); z++) {
                    for (Integer x = 0; x < image.getHeight(); x++) {

                        Integer clr = image.getRGB(z, x);

                        ParticleBuilder particle = new ParticleBuilder(Particle.REDSTONE);
                        particle.color((clr & 0x00ff0000) >> 16, (clr & 0x0000ff00) >> 8, clr & 0x000000ff);
                        Location location1 = location.clone();
                        location1.setZ(location1.getZ() + (double) (imageAreaX / 2) - ((double) z * ((double) imageAreaZ / (double) image.getHeight())));
                        location1.setX(location1.getX() + (double) (imageAreaZ / 2) - ((double) x * ((double) imageAreaX / (double) image.getWidth())));
                        particle.location(location1);
                        particle.spawn();

                    }
                }
                break;

            case "north":
                for (Integer x = 0; x < image.getWidth(); x++) {
                    for (Integer z = 0; z < image.getHeight(); z++) {

                        Integer clr = image.getRGB(x, z);

                        ParticleBuilder particle = new ParticleBuilder(Particle.REDSTONE);
                        particle.color((clr & 0x00ff0000) >> 16, (clr & 0x0000ff00) >> 8, clr & 0x000000ff);
                        Location location1 = location.clone();
                        location1.setX(location1.getX() - (double) (imageAreaX / 2) + ((double) x * ((double) imageAreaX / (double) image.getWidth())));
                        location1.setZ(location1.getZ() + (double) (imageAreaZ / 2) - ((double) z * ((double) imageAreaZ / (double) image.getHeight())));
                        particle.location(location1);
                        particle.spawn();

                    }
                }
                break;

            case "east":
                for (Integer z = 0; z < image.getWidth(); z++) {
                    for (Integer x = 0; x < image.getHeight(); x++) {

                        Integer clr = image.getRGB(z, x);

                        ParticleBuilder particle = new ParticleBuilder(Particle.REDSTONE);
                        particle.color((clr & 0x00ff0000) >> 16, (clr & 0x0000ff00) >> 8, clr & 0x000000ff);
                        Location location1 = location.clone();
                        location1.setZ(location1.getZ() - (double) (imageAreaX / 2) + ((double) z * ((double) imageAreaZ / (double) image.getHeight())));
                        location1.setX(location1.getX() - (double) (imageAreaZ / 2) + ((double) x * ((double) imageAreaX / (double) image.getWidth())));
                        particle.location(location1);
                        particle.spawn();

                    }
                }
                break;

            case "south":
                for (Integer x = 0; x < image.getWidth(); x++) {
                    for (Integer z = 0; z < image.getHeight(); z++) {

                        Integer clr = image.getRGB(x, z);

                        ParticleBuilder particle = new ParticleBuilder(Particle.REDSTONE);
                        particle.color((clr & 0x00ff0000) >> 16, (clr & 0x0000ff00) >> 8, clr & 0x000000ff);
                        Location location1 = location.clone();
                        location1.setX(location1.getX() + (double) (imageAreaX / 2) - ((double) x * ((double) imageAreaX / (double) image.getWidth())));
                        location1.setZ(location1.getZ() - (double) (imageAreaZ / 2) + ((double) z * ((double) imageAreaZ / (double) image.getHeight())));
                        particle.location(location1);
                        particle.spawn();

                    }
                }
                break;

        }

    }

}
