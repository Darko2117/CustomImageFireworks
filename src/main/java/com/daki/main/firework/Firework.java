package com.daki.main.firework;

public class Firework {

    String ID;
    String name;
    String imageName;
    String power;
    String cooldown;
    String fireworkDimensions;
    String resizedImageDimensions;

    public Firework(String ID, String name, String imageName, String power, String cooldown, String fireworkDimensions, String resizedImageDimensions) {

       this.ID = ID;
       this.name = name;
       this.imageName = imageName;
       this.power = power;
       this.cooldown = cooldown;
       this.fireworkDimensions = fireworkDimensions;
       this.resizedImageDimensions = resizedImageDimensions;


    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public String getCooldown() {
        return cooldown;
    }

    public void setCooldown(String cooldown) {
        this.cooldown = cooldown;
    }

    public String getFireworkDimensions() {
        return fireworkDimensions;
    }

    public void setFireworkDimensions(String fireworkDimensions) {
        this.fireworkDimensions = fireworkDimensions;
    }

    public String getResizedImageDimensions() {
        return resizedImageDimensions;
    }

    public void setResizedImageDimensions(String resizedImageDimensions) {
        this.resizedImageDimensions = resizedImageDimensions;
    }

}
