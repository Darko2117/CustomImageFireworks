package com.daki.main.commands;

import com.daki.main.CIF;
import com.daki.main.Cache;
import com.daki.main.Methods;
import com.daki.main.database.Database;
import com.daki.main.firework.Firework;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.scheduler.BukkitRunnable;

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

    @EventHandler
    public void promptTwoInventoryCloseListener(InventoryCloseEvent event) {

        Player player = (Player) event.getPlayer();

        if (!event.getReason().equals(InventoryCloseEvent.Reason.PLUGIN)) {
            if (player.isConversing()) {
                player.abandonConversation(ongoingConversations.get(player));
            }
        }

    }

    public static void createFireworkFromAnswers(List<String> input) {

        new BukkitRunnable() {
            @Override
            public void run() {

                String name = input.get(0);
                String imageName = input.get(1);
                String power = input.get(2);
                String cooldown = input.get(3);
                String fireworkDimensions = input.get(4);
                String resizedImageDimensions = input.get(5);
                String ID = "";

                Boolean keyAvailable = false;
                while (!keyAvailable) {
                    try {

                        ID = String.valueOf(new Random().nextInt(1000000000));

                        String statement = "SELECT COUNT(ID) FROM fireworks WHERE ID = '" + ID + "';";
                        ResultSet rs = Database.connection.prepareStatement(statement).executeQuery();
                        rs.next();
                        if (rs.getInt(1) == 0) keyAvailable = true;

                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }

                Integer resizedImageWidth = Methods.getDimensionsFromDimensionString(resizedImageDimensions)[0];
                Integer resizedImageHeight = Methods.getDimensionsFromDimensionString(resizedImageDimensions)[1];

                String resizedImageName = Methods.getImageNameWithDimensions(imageName, resizedImageWidth, resizedImageHeight);

                Methods.resizeImageIfNotExisting(imageName, resizedImageName, resizedImageWidth, resizedImageHeight);

                String statement = "INSERT INTO fireworks(ID, Name, ImageName, Power, Cooldown, FireworkDimensions, ResizedImageDimensions) VALUES ("
                        + "'" + ID + "', "
                        + "'" + name + "', "
                        + "'" + imageName + "', "
                        + "'" + power + "', "
                        + "'" + cooldown + "', "
                        + "'" + fireworkDimensions + "', "
                        + "'" + resizedImageDimensions + "'"
                        + ");";

                try {

                    Database.connection.prepareStatement(statement).executeUpdate();
                    CIF.getInstance().getLogger().info("Firework successfully inserted into the database.");

                    Firework firework = new Firework(ID, name, imageName, power, cooldown, fireworkDimensions, resizedImageDimensions);
                    Cache.addLoadedFirework(firework);

                } catch (Exception exception) {
                    exception.printStackTrace();
                }

            }
        }.runTaskAsynchronously(CIF.getInstance());

    }

}

class PromptOne extends StringPrompt {
    //Name
    @Override
    public String getPromptText(ConversationContext context) {
        CreateNewFirework_Command.answers.put((Player) context.getForWhom(), new ArrayList<>());
        return ChatColor.YELLOW + "If you want to cancel the firework creation at any point, type \"cancel\". Things that you type now won't show up in chat." + "\n" + "Name the firework you are creating.";
    }

    @Override
    public Prompt acceptInput(ConversationContext context, String input) {
        Player player = (Player) context.getForWhom();
        if (input.equalsIgnoreCase("cancel")) {
            player.abandonConversation(CreateNewFirework_Command.ongoingConversations.get(player));
            return END_OF_CONVERSATION;
        }
        List<String> previousAnswers = CreateNewFirework_Command.answers.get(player);
        previousAnswers.add(input);
        CreateNewFirework_Command.answers.put(player, previousAnswers);
        return new PromptTwo();
    }

}

class PromptTwo extends StringPrompt {
    //Image name
    @Override
    public String getPromptText(ConversationContext context) {
        Player player = (Player) context.getForWhom();
        player.openInventory(Cache.getChooseImageNamePanels().get(player).getInventoryPages().get(Cache.getWhatPageOfChooseImageNamePanelIsPlayerOn().get(player)));
        return ChatColor.YELLOW + "Select a firework image!";
    }

    @Override
    public Prompt acceptInput(ConversationContext context, String input) {
        Player player = (Player) context.getForWhom();
        if (input.equalsIgnoreCase("cancel")) {
            player.abandonConversation(CreateNewFirework_Command.ongoingConversations.get(player));
            return END_OF_CONVERSATION;
        }
        List<String> previousAnswers = CreateNewFirework_Command.answers.get(player);
        previousAnswers.add(input);
        CreateNewFirework_Command.answers.put(player, previousAnswers);
        return new PromptThree();
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
            player.abandonConversation(CreateNewFirework_Command.ongoingConversations.get(player));
            return END_OF_CONVERSATION;
        }
        try {
            Integer.parseInt(input);
        } catch (Exception exception) {
            return new PromptThree();
        }
        List<String> previousAnswers = CreateNewFirework_Command.answers.get(player);
        previousAnswers.add(input);
        CreateNewFirework_Command.answers.put(player, previousAnswers);
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
            player.abandonConversation(CreateNewFirework_Command.ongoingConversations.get(player));
            return END_OF_CONVERSATION;
        }
        try {
            Integer.parseInt(input);
        } catch (Exception exception) {
            return new PromptFour();
        }
        List<String> previousAnswers = CreateNewFirework_Command.answers.get(player);
        previousAnswers.add(input);
        CreateNewFirework_Command.answers.put(player, previousAnswers);
        return new PromptFive();
    }

}

class PromptFive extends StringPrompt {
    //Firework dimensions
    @Override
    public String getPromptText(ConversationContext context) {
        return ChatColor.YELLOW + "Enter the dimensions of the firework. (In blocks. Format is WidthxHeight. Example: 30x20)";
    }

    @Override
    public Prompt acceptInput(ConversationContext context, String input) {
        Player player = (Player) context.getForWhom();
        if (input.equalsIgnoreCase("cancel")) {
            player.abandonConversation(CreateNewFirework_Command.ongoingConversations.get(player));
            return END_OF_CONVERSATION;
        }

        if (Methods.getDimensionsFromDimensionString(input) == null) return new PromptFive();

        List<String> previousAnswers = CreateNewFirework_Command.answers.get(player);
        previousAnswers.add(input);
        CreateNewFirework_Command.answers.put(player, previousAnswers);
        return new PromptSix();
    }

}

class PromptSix extends StringPrompt {
    //Resized image dimensions
    @Override
    public String getPromptText(ConversationContext context) {
        return ChatColor.YELLOW + "What are the dimensions of the resized image gonna be? (Resized images are created when creating the firework. This also represents the amount of particles. Format is WidthxHeight. Example: 100x100)";
    }

    @Override
    public Prompt acceptInput(ConversationContext context, String input) {
        Player player = (Player) context.getForWhom();
        if (input.equalsIgnoreCase("cancel")) {
            player.abandonConversation(CreateNewFirework_Command.ongoingConversations.get(player));
            return END_OF_CONVERSATION;
        }

        if (Methods.getDimensionsFromDimensionString(input) == null) return new PromptSix();

        List<String> previousAnswers = CreateNewFirework_Command.answers.get(player);
        previousAnswers.add(input);
        CreateNewFirework_Command.answers.put(player, previousAnswers);
        CreateNewFirework_Command.createFireworkFromAnswers(CreateNewFirework_Command.answers.get(player));
        return END_OF_CONVERSATION;
    }

}