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

public abstract class BCommand {

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
            execute(commandSender, label, arguments);
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
        PlayerOnly playerOnly = getClass().getAnnotation(PlayerOnly.class);
        if (playerOnly != null) {
            return true;
        } else {
            return false;
        }
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
    public @interface PlayerOnly {
    }
}
