package dev.lockedthread.blayze.blayzecore.commands;

import dev.lockedthread.blayze.blayzecore.commands.tabcomplete.TabCompletable;
import dev.lockedthread.blayze.blayzecore.module.Module;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.stream.Collectors;

@BCommand.Name(aliases = "help")
public class CommandBlayzeCoreHelp extends BCommand implements TabCompletable {

    @Override
    public void execute(CommandSender commandSender, String label, String[] arguments) {
        if (arguments.length == 0) {
            for (Module module : Module.getEnabledModules()) {

            }
        } else {

        }
        for (Module module : Module.getEnabledModules()) {

        }
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, String label, String[] args) {
        return Module.getEnabledModules().stream().map(Module::getName).collect(Collectors.toList());
    }
}
