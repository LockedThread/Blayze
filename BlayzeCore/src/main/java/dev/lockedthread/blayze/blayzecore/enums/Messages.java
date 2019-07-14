package dev.lockedthread.blayze.blayzecore.enums;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

public enum Messages {

    COMMAND_NO_PERMISSION("&cSorry, you don't have permission to execute {command}."),
    COMMAND_PLAYER_ONLY("&cSorry, you can only execute this command as a Player.");


    private String message;

    Messages(String message) {
        this.message = message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getKey() {
        return name().toLowerCase().replace("_", "-");
    }

    public String getMessage() {
        return getMessage(true);
    }

    public String getMessage(String... strings) {
        String message = getMessage(true);
        for (int i = 0; i < strings.length; i += 2) {
            message = message.replace(strings[i], strings[i + 1]);
        }
        return message;
    }

    public String getMessage(boolean color) {
        if (color) {
            return ChatColor.translateAlternateColorCodes('&', message);
        } else {
            return message;
        }
    }

    public static void load(ConfigurationSection section) {
        for (Messages message : values()) {
            if (section.isSet(message.getKey())) {
                message.setMessage(section.getString(message.getKey()));
            } else {
                section.set(message.getKey(), message.getMessage(false));
            }
        }
    }
}
