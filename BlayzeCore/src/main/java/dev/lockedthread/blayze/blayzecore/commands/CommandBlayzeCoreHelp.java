package dev.lockedthread.blayze.blayzecore.commands;

import com.google.common.base.Joiner;
import dev.lockedthread.blayze.blayzecore.commands.tabcomplete.TabCompletable;
import dev.lockedthread.blayze.blayzecore.module.Module;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@BCommand.Name(aliases = "help")
@BCommand.Description(description = "Displays information about a module")
public class CommandBlayzeCoreHelp extends BCommand implements TabCompletable {

    @Override
    public void execute(CommandSender commandSender, String label, String[] arguments) {
        if (arguments.length == 0) {
            commandSender.sendMessage("");
            commandSender.sendMessage(ChatColor.RED + "Modules: " + ChatColor.GRAY + "(click for info)");
            for (Module module : Module.getEnabledModules()) {
                TextComponent component = new TextComponent(" " + module.getName());
                component.setColor(net.md_5.bungee.api.ChatColor.YELLOW);
                component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/blayzecore help " + module.getName()));
                commandSender.sendMessage(component);
            }
            commandSender.sendMessage("");
        } else if (arguments.length == 1) {
            Optional<Module> optionalModule = Module.getEnabledModules().stream().filter(m -> m.getName().equalsIgnoreCase(arguments[0])).findFirst();
            if (optionalModule.isPresent()) {
                Module module = optionalModule.get();
                PluginDescriptionFile moduleDescription = module.getDescription();
                commandSender.sendMessage(ChatColor.YELLOW + "Module: " + ChatColor.WHITE + module.getName() + " - " + moduleDescription.getVersion());
                String description = moduleDescription.getDescription();
                if (description != null && !description.isEmpty()) {
                    commandSender.sendMessage(ChatColor.YELLOW + " Description: " + description);
                }
                List<String> author = moduleDescription.getAuthors();
                if (!author.isEmpty()) {
                    commandSender.sendMessage(ChatColor.YELLOW + " Authors: " + Joiner.on(", ").skipNulls().join(author));
                }
                Set<BCommand> commandSet = module.getCommandSet(false);
                if (commandSet != null) {
                    commandSender.sendMessage(ChatColor.YELLOW + "Commands:");
                    for (BCommand bCommand : commandSet) {
                        String subCommandString = null;
                        Map<String, BCommand> subCommands = bCommand.getSubCommands(false);
                        if (subCommands != null) {
                            subCommandString = "[" + Joiner.on("/").skipNulls().join(subCommands.keySet()) + "]";
                        }
                        if (bCommand.getDescription() != null) {
                            commandSender.sendMessage(ChatColor.YELLOW + " " + bCommand.getName() + (subCommandString != null ? subCommandString : "") + " - " + bCommand.getDescription());
                        } else {
                            commandSender.sendMessage(ChatColor.YELLOW + " " + bCommand.getName() + (subCommandString != null ? subCommandString : ""));
                        }
                    }
                }
            } else {
                commandSender.sendMessage(ChatColor.RED + "Unable to find Blayze Module with name " + arguments[0]);
            }
        } else {
            commandSender.sendMessage(ChatColor.RED + "Incorrect syntax.");
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, String label, String[] args) {
        return Module.getEnabledModules().stream().map(Module::getName).collect(Collectors.toList());
    }
}
