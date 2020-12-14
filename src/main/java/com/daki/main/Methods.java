package com.daki.main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class Methods {

    public static Integer[] getDimensionsFromDimensionString(String dimensionString) {

        Integer[] results = new Integer[2];

        StringBuilder tester = new StringBuilder(dimensionString);
        if (tester.indexOf("x") == -1) return null;

        try {
            results[0] = Integer.parseInt(tester.substring(0, tester.indexOf("x")));
            results[1] = Integer.parseInt(tester.substring(tester.indexOf("x") + 1, tester.length()));
        } catch (Exception exception) {
            return null;
        }

        return results;

    }

    public static String getImageNameWithDimensions(String imageName, String dimensions) {

        StringBuilder formatName = new StringBuilder(imageName);
        formatName.delete(0, formatName.lastIndexOf(".") + 1);

        StringBuilder newName = new StringBuilder(imageName);
        newName.reverse();
        newName.delete(0, formatName.length() + 1);
        newName.reverse();
        newName.append("_").append(dimensions).append(".").append(formatName);

        return newName.toString();

    }

    public static void resizeImageIfNotExisting(String imageName, String resizedImageDimensions) {

        try {

            Integer width = Methods.getDimensionsFromDimensionString(resizedImageDimensions)[0];
            Integer height = Methods.getDimensionsFromDimensionString(resizedImageDimensions)[1];

            String resizedImageName = Methods.getImageNameWithDimensions(imageName, resizedImageDimensions);

            for (File file : new File(Cache.getResizedImagesPath()).listFiles()) {
                if (file.getName().equals(resizedImageName)) {
                    return;
                }
            }

            CIF.getInstance().getLogger().info("Image resizing started.");

            Image image = ImageIO.read(new File(Cache.getImagesPath() + imageName));

            image = image.getScaledInstance(width, height, Image.SCALE_DEFAULT);

            BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);

            Graphics2D bGr = bufferedImage.createGraphics();
            bGr.drawImage(image, 0, 0, null);
            bGr.dispose();

            StringBuilder formatName = new StringBuilder(imageName);
            formatName.delete(0, formatName.lastIndexOf(".") + 1);

            File path = new File(Cache.getResizedImagesPath() + resizedImageName);

            ImageIO.write(bufferedImage, formatName.toString(), path);

            CIF.getInstance().getLogger().info("Image successfully resized. It's written at " + path);

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

    }

}
