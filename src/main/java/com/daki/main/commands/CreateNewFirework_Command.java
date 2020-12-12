package com.daki.main.commands;

import com.daki.main.CIF;
import com.daki.main.Cache;
import com.daki.main.UI.Icons;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.ResultSet;
import java.util.*;
import java.util.List;

public class CreateNewFirework_Command implements CommandExecutor, Listener {

    public static HashMap<Player, List<String>> answers = new HashMap<>();
    public static HashMap<Player, Conversation> ongoingConversations = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (!(commandSender instanceof Player)) return true;
        Player player = (Player) commandSender;

        if (Cache.getResizedImagesPath().equals("")) {
            commandSender.sendMessage(ChatColor.RED + "Resized-images-path in the config is empty. Set it before continuing.");
            return true;
        }

        ConversationFactory cf = new ConversationFactory(CIF.getInstance());
        Conversation conversation = cf.withFirstPrompt(new PromptOne()).withLocalEcho(true).buildConversation(player);
        conversation.addConversationAbandonedListener(listener);
        conversation.begin();
        ongoingConversations.put(player, conversation);

        return true;

    }

    @EventHandler
    public void promptTwoImageNameClickListener(InventoryClickEvent e) {

        Player player = (Player) e.getWhoClicked();

        if (player.isConversing()) {
            if (PromptTwo.imagePaperPages.contains(e.getInventory())) {
                e.setCancelled(true);
                if (e.getCurrentItem() != null) {
                    if (e.getCurrentItem().equals(Icons.getNextPageItemStack())) {
                        if (PromptTwo.whatPageAreTheyOn.get(player) + 1 <= PromptTwo.imagePaperPages.size() - 1) {
                            PromptTwo.openSpecificImagePaperPage(player, PromptTwo.whatPageAreTheyOn.get(player) + 1);
                        }
                    } else if (e.getCurrentItem().equals(Icons.getPreviousPageItemStack())) {
                        if (PromptTwo.whatPageAreTheyOn.get(player) - 1 >= 0) {
                            PromptTwo.openSpecificImagePaperPage(player, PromptTwo.whatPageAreTheyOn.get(player) - 1);
                        }
                    } else {
                        List<String> names = Arrays.asList(new File(CIF.getInstance().getDataFolder() + "/images/").list());
                        if (names.contains(e.getCurrentItem().getItemMeta().getDisplayName())) {
                            player.acceptConversationInput(e.getCurrentItem().getItemMeta().getDisplayName());
                            player.closeInventory();
                        }
                    }
                }
            }
        }

    }

    @EventHandler
    public void promptTwoInventoryCloseListener(InventoryCloseEvent e) {

        Player player = (Player) e.getPlayer();

        if (!e.getReason().equals(InventoryCloseEvent.Reason.PLUGIN)) {
            if (player.isConversing()) {
                player.abandonConversation(ongoingConversations.get(player));
            }
        }

    }

    static ConversationAbandonedListener listener = abandonedEvent -> {
        Player player = (Player) abandonedEvent.getContext().getForWhom();
        ongoingConversations.remove(player);
        answers.remove(player);
        if (abandonedEvent.gracefulExit()) {
            player.sendMessage(ChatColor.GREEN + "All the answers were saved.");
        } else {
            player.sendMessage(ChatColor.RED + "Firework creation stopped.");
        }
    };

    public static void createFireworkFromAnswers(List<String> input) {

        new BukkitRunnable() {
            @Override
            public void run() {

                String name = input.get(0);
                String image = input.get(1);
                Integer power = Integer.parseInt(input.get(2));
                Integer cooldown = Integer.parseInt(input.get(3));
                Integer recharge = Integer.parseInt(input.get(4));
                Integer chargeLimit = Integer.parseInt(input.get(5));
                String fireworkDimensions = input.get(6);
                String resizedImageDimensions = input.get(7);
                String itemDisplayName = ChatColor.translateAlternateColorCodes('&', input.get(8));
                String serializedItem = null;

                ItemStack fireworkItem = new ItemStack(Material.FIREWORK_ROCKET);

                FireworkMeta fireworkMeta = (FireworkMeta) fireworkItem.getItemMeta();
                fireworkMeta.setPower(power);
                fireworkMeta.setDisplayName(itemDisplayName);
                fireworkItem.setItemMeta(fireworkMeta);

                Boolean keyAvailable = false;

                while (!keyAvailable) {
                    try {

                        Random r = new Random();
                        String fireworkKey = String.valueOf(r.nextInt(1000000));

                        NBTItem fireworkNBT = new NBTItem(fireworkItem);
                        fireworkNBT.setString("firework-key", fireworkKey);
                        fireworkItem = fireworkNBT.getItem();

                        serializedItem = Methods.serializeItemStack(fireworkItem);

                        String statement = "SELECT COUNT(Item) FROM Fireworks WHERE Item = '" + serializedItem + "';";
                        ResultSet rs = Database.connection.prepareStatement(statement).executeQuery();
                        rs.next();
                        if (rs.getInt(1) == 0) keyAvailable = true;

                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }

                Integer resizedImageWidth = Methods.getDimensionsFromDimensionString(resizedImageDimensions)[0];
                Integer resizedImageHeight = Methods.getDimensionsFromDimensionString(resizedImageDimensions)[1];

                String resizedImageName = getImageNameWithDimensions(image, resizedImageWidth, resizedImageHeight);

                resizeImageIfNotExisting(image, resizedImageName, resizedImageWidth, resizedImageHeight);

                String statement = "INSERT INTO Fireworks(Name, ImageName, Cooldown, Recharge, ChargeLimit, FireworkDimensions, ResizedImageDimensions, Item) VALUES ("
                        + "'" + name + "', "
                        + "'" + resizedImageName + "', "
                        + cooldown + ", "
                        + recharge + ", "
                        + chargeLimit + ", "
                        + "'" + fireworkDimensions + "', "
                        + "'" + resizedImageDimensions + "', "
                        + "'" + serializedItem + "'"
                        + ");";

                try {
                    Database.connection.prepareStatement(statement).executeUpdate();
                    Main.getInstance().getLogger().info("Firework successfully inserted into the database.");
                } catch (Exception exception) {
                    exception.printStackTrace();
                }

                Firework firework = new Firework();
                firework.setName(name);
                firework.setImage(resizedImageName);
                firework.setCooldown(cooldown);
                firework.setRecharge(recharge);
                firework.setChargeLimit(chargeLimit);
                firework.setFireworkDimensions(fireworkDimensions);
                firework.setResizedImageDimensions(resizedImageDimensions);
                firework.setItem(serializedItem);

                FireworkManager.loadFirework(firework);

                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.getInventory().addItem(fireworkItem);
                }

            }
        }.runTaskAsynchronously(Main.getInstance());

    }

    public static String getImageNameWithDimensions(String imageName, Integer width, Integer height) {

        StringBuilder formatName = new StringBuilder(imageName);
        formatName.delete(0, formatName.lastIndexOf(".") + 1);

        StringBuilder newName = new StringBuilder(imageName);
        newName.reverse();
        newName.delete(0, formatName.length() + 1);
        newName.reverse();
        newName.append("_").append(width).append("x").append(height).append(".").append(formatName);

        return newName.toString();

    }

    public static void resizeImageIfNotExisting(String imageName, String resizedImageName, Integer wantedWidth, Integer wantedHeight) {

        try {

            Main.getInstance().getLogger().info("Image resizing started.");

            Image image = ImageIO.read(new File(Main.getInstance().getDataFolder() + "/images/" + imageName));

            image = image.getScaledInstance(wantedWidth, wantedHeight, Image.SCALE_DEFAULT);

            BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);

            Graphics2D bGr = bufferedImage.createGraphics();
            bGr.drawImage(image, 0, 0, null);
            bGr.dispose();

            StringBuilder formatName = new StringBuilder(imageName);
            formatName.delete(0, formatName.lastIndexOf(".") + 1);

            if (!LoadedInMemory.getResizedImagesPath().endsWith("\\"))
                LoadedInMemory.setResizedImagesPath(LoadedInMemory.getResizedImagesPath().concat("\\"));

            File path = new File(LoadedInMemory.getResizedImagesPath() + resizedImageName);

            ImageIO.write(bufferedImage, formatName.toString(), path);

            Main.getInstance().getLogger().info("Image successfully resized. It's written at " + path);

        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

}

class PromptOne extends StringPrompt {
    //Name
    @Override
    public String getPromptText(ConversationContext context) {
        CreateNewFirework.answers.put((Player) context.getForWhom(), new ArrayList<>());
        return ChatColor.YELLOW + "If you want to cancel the firework creation at any point, type \"cancel\". Things that you type now won't show up in chat." + "\n" + "Name the firework you are creating. (This name is used in the database and is not the display name of the firework item. Must be unique from other previously made ones)";
    }

    @Override
    public Prompt acceptInput(ConversationContext context, String input) {
        Player player = (Player) context.getForWhom();
        if (input.equalsIgnoreCase("cancel")) {
            player.abandonConversation(CreateNewFirework.ongoingConversations.get(player));
            return END_OF_CONVERSATION;
        }
        List<String> previousAnswers = CreateNewFirework.answers.get(player);
        previousAnswers.add(input);
        CreateNewFirework.answers.put(player, previousAnswers);
        return new PromptTwo();
    }

}

class PromptTwo extends StringPrompt {
    //Image name
    static List<Inventory> imagePaperPages = new ArrayList<>();
    static HashMap<Player, Integer> whatPageAreTheyOn = new HashMap<>();

    @Override
    public String getPromptText(ConversationContext context) {

        imagePaperPages.clear();

        Player player = (Player) context.getForWhom();

        List<ItemStack> paperItemStacks = new ArrayList<>();

        for (String name : new File(Main.getInstance().getDataFolder() + "/images/").list()) {

            ItemStack item = new ItemStack(Material.PAPER);
            ItemMeta meta = item.getItemMeta();

            meta.setDisplayName(name);
            item.setItemMeta(meta);
            paperItemStacks.add(item);
        }

        Integer counter = paperItemStacks.size();

        while (counter > 0) {

            Inventory page = Bukkit.createInventory(null, 54, "Select a firework image!");
            imagePaperPages.add(page);
            counter -= 45;

        }

        for (Inventory page : imagePaperPages) {

            Integer listSize = Math.min(paperItemStacks.size(), 45);
            for (Integer i = 0; i < listSize; i++) {
                page.setItem(i, paperItemStacks.get(0));
                paperItemStacks.remove(0);
            }

            page.setItem(45, Methods.getPreviousPageItemStack());
            page.setItem(53, Methods.getNextPageItemStack());

        }

        openSpecificImagePaperPage(player, 0);

        return ChatColor.YELLOW + "Select a firework image!";
    }

    @Override
    public Prompt acceptInput(ConversationContext context, String input) {
        Player player = (Player) context.getForWhom();
        if (input.equalsIgnoreCase("cancel")) {
            player.abandonConversation(CreateNewFirework.ongoingConversations.get(player));
            return END_OF_CONVERSATION;
        }
        List<String> previousAnswers = CreateNewFirework.answers.get(player);
        previousAnswers.add(input);
        CreateNewFirework.answers.put(player, previousAnswers);
        return new PromptThree();
    }

    static void openSpecificImagePaperPage(Player player, Integer page) {

        //player.closeInventory();
        player.openInventory(imagePaperPages.get(page));
        whatPageAreTheyOn.put(player, page);

    }

}

class PromptThree extends StringPrompt {
    //Power
    @Override
    public String getPromptText(ConversationContext context) {
        return ChatColor.YELLOW + "What is the power of the firework gonna be? (Each level of power is half a second of flight time)";
    }

    @Override
    public Prompt acceptInput(ConversationContext context, String input) {
        Player player = (Player) context.getForWhom();
        if (input.equalsIgnoreCase("cancel")) {
            player.abandonConversation(CreateNewFirework.ongoingConversations.get(player));
            return END_OF_CONVERSATION;
        }
        try {
            Integer.parseInt(input);
        } catch (Exception exception) {
            return new PromptThree();
        }
        List<String> previousAnswers = CreateNewFirework.answers.get(player);
        previousAnswers.add(input);
        CreateNewFirework.answers.put(player, previousAnswers);
        return new PromptFour();
    }

}

class PromptFour extends StringPrompt {
    //Cooldown
    @Override
    public String getPromptText(ConversationContext context) {
        return ChatColor.YELLOW + "What is the cooldown on the firework gonna be? (How many ticks a player has to wait before launching another one)";
    }

    @Override
    public Prompt acceptInput(ConversationContext context, String input) {
        Player player = (Player) context.getForWhom();
        if (input.equalsIgnoreCase("cancel")) {
            player.abandonConversation(CreateNewFirework.ongoingConversations.get(player));
            return END_OF_CONVERSATION;
        }
        try {
            Integer.parseInt(input);
        } catch (Exception exception) {
            return new PromptFour();
        }
        List<String> previousAnswers = CreateNewFirework.answers.get(player);
        previousAnswers.add(input);
        CreateNewFirework.answers.put(player, previousAnswers);
        return new PromptFive();
    }

}

class PromptFive extends StringPrompt {
    //Recharge
    @Override
    public String getPromptText(ConversationContext context) {
        return ChatColor.YELLOW + "What's the recharge rate of the firework gonna be? (How many ticks need to pass for a player to get another charge)";
    }

    @Override
    public Prompt acceptInput(ConversationContext context, String input) {
        Player player = (Player) context.getForWhom();
        if (input.equalsIgnoreCase("cancel")) {
            player.abandonConversation(CreateNewFirework.ongoingConversations.get(player));
            return END_OF_CONVERSATION;
        }
        try {
            Integer.parseInt(input);
        } catch (Exception exception) {
            return new PromptFive();
        }
        List<String> previousAnswers = CreateNewFirework.answers.get(player);
        previousAnswers.add(input);
        CreateNewFirework.answers.put(player, previousAnswers);
        return new PromptSix();
    }

}

class PromptSix extends StringPrompt {
    //Charge limit
    @Override
    public String getPromptText(ConversationContext context) {
        return ChatColor.YELLOW + "What's the charge limit gonna be? (How many charges of the firework a player can have in the fireworks UI)";
    }

    @Override
    public Prompt acceptInput(ConversationContext context, String input) {
        Player player = (Player) context.getForWhom();
        if (input.equalsIgnoreCase("cancel")) {
            player.abandonConversation(CreateNewFirework.ongoingConversations.get(player));
            return END_OF_CONVERSATION;
        }
        try {
            Integer.parseInt(input);
        } catch (Exception exception) {
            return new PromptSix();
        }
        List<String> previousAnswers = CreateNewFirework.answers.get(player);
        previousAnswers.add(input);
        CreateNewFirework.answers.put(player, previousAnswers);
        return new PromptSeven();
    }

}

class PromptSeven extends StringPrompt {
    //Firework dimensions
    @Override
    public String getPromptText(ConversationContext context) {
        return ChatColor.YELLOW + "Enter the dimensions of the firework. (In blocks. Format is WidthxHeight. Example: 30x20)";
    }

    @Override
    public Prompt acceptInput(ConversationContext context, String input) {
        Player player = (Player) context.getForWhom();
        if (input.equalsIgnoreCase("cancel")) {
            player.abandonConversation(CreateNewFirework.ongoingConversations.get(player));
            return END_OF_CONVERSATION;
        }

        if (Methods.getDimensionsFromDimensionString(input) == null) return new PromptSeven();

        List<String> previousAnswers = CreateNewFirework.answers.get(player);
        previousAnswers.add(input);
        CreateNewFirework.answers.put(player, previousAnswers);
        return new PromptEight();
    }

}

class PromptEight extends StringPrompt {
    //Resized image dimensions
    @Override
    public String getPromptText(ConversationContext context) {
        return ChatColor.YELLOW + "What are the dimensions of the resized image gonna be? (Resized images are created when creating the firework. This also represents the amount of particles. Format is WidthxHeight. Example: 100x100)";
    }

    @Override
    public Prompt acceptInput(ConversationContext context, String input) {
        Player player = (Player) context.getForWhom();
        if (input.equalsIgnoreCase("cancel")) {
            player.abandonConversation(CreateNewFirework.ongoingConversations.get(player));
            return END_OF_CONVERSATION;
        }

        if (Methods.getDimensionsFromDimensionString(input) == null) return new PromptEight();

        List<String> previousAnswers = CreateNewFirework.answers.get(player);
        previousAnswers.add(input);
        CreateNewFirework.answers.put(player, previousAnswers);
        return new PromptNine();
    }

}

class PromptNine extends StringPrompt {
    //Item display name
    @Override
    public String getPromptText(ConversationContext context) {
        return ChatColor.YELLOW + "What's the display name of the firework item gonna be?";
    }

    @Override
    public Prompt acceptInput(ConversationContext context, String input) {
        Player player = (Player) context.getForWhom();
        if (input.equalsIgnoreCase("cancel")) {
            player.abandonConversation(CreateNewFirework.ongoingConversations.get(player));
            return END_OF_CONVERSATION;
        }
        List<String> previousAnswers = CreateNewFirework.answers.get(player);
        previousAnswers.add(input);
        CreateNewFirework.answers.put(player, previousAnswers);
        CreateNewFirework.createFireworkFromAnswers(CreateNewFirework.answers.get(player));
        return END_OF_CONVERSATION;
    }

}