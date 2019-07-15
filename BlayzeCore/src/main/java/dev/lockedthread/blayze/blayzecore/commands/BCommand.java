package dev.lockedthread.blayze.blayzecore.commands;

import dev.lockedthread.blayze.blayzecore.enums.Messages;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public abstract class BCommand {

    private Map<String, BCommand> subCommands;

    public abstract void execute(CommandSender commandSender, String label, String[] arguments);

    public void perform(CommandSender commandSender, String label, String[] arguments) {
        if (isPlayerOnly() && !(commandSender instanceof Player)) {
            sendMessage(commandSender, Messages.COMMAND_PLAYER_ONLY);
            return;
        }

        boolean hasPermission;
        if (commandSender.hasPermission("blayzecore.admin")) {
            hasPermission = true;
        } else {
            String permission = getPermission();
            hasPermission = permission == null || commandSender.hasPermission(permission);
        }
        if (hasPermission) {
            if (hasSubCommands()) {
                String[] args = arguments.length == 0 ? arguments : Arrays.copyOfRange(arguments, 0, arguments.length);
                BCommand bCommand = subCommands.get(arguments[0].toLowerCase());
                if (bCommand != null) {
                    bCommand.perform(commandSender, arguments[0], args);
                }
            } else {
                execute(commandSender, label, arguments);
            }
        } else {
            sendMessage(commandSender, Messages.COMMAND_NO_PERMISSION.getMessage("{command}", "/" + label));
        }
    }

    public void sendMessage(CommandSender sender, String message) {
        sendMessage(sender, message, true);
    }

    public void sendMessage(CommandSender sender, String message, boolean color) {
        if (color) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        } else {
            sender.sendMessage(message);
        }
    }

    public void sendMessage(CommandSender sender, Enum<?> anEnum) {
        sendMessage(sender, anEnum.toString(), false);
    }

    public boolean isPlayerOnly() {
        return getClass().getAnnotation(PlayerOnly.class) != null;
    }

    @NotNull
    public String getName() {
        return getAliases()[0];
    }

    public String[] getAliases() {
        Name annotation = getClass().getAnnotation(Name.class);
        if (annotation != null) {
            return annotation.aliases();
        }
        throw new RuntimeException("Unable to find Name annotation for Command class \"" + getClass().getName() + "\"");
    }

    @Nullable
    public String getPermission() {
        Permission annotation = getClass().getAnnotation(Permission.class);
        if (annotation != null) {
            return annotation.permission();
        }
        return null;
    }

    public void addSubCommands(BCommand... bCommands) {
        for (BCommand bCommand : bCommands) {
            for (String alias : bCommand.getAliases()) {
                getSubCommands().put(alias.toLowerCase(), bCommand);
            }
        }
    }

    @Nullable
    public Map<String, BCommand> getSubCommands(boolean checkNullity) {
        return subCommands == null ? checkNullity ? (subCommands = new HashMap<>()) : null : subCommands;
    }

    public boolean hasSubCommands() {
        return subCommands != null && !subCommands.isEmpty();
    }

    @NotNull
    public Map<String, BCommand> getSubCommands() {
        //noinspection ConstantConditions
        return getSubCommands(true);
    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Name {
        String[] aliases();
    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Permission {
        String permission();
    }


    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Description {
        String description();
    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface PlayerOnly {
    }


}
