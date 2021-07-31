package xyz.novaserver.placeholders.command;

import com.google.common.collect.ImmutableList;
import com.velocitypowered.api.command.SimpleCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import xyz.novaserver.placeholders.Placeholders;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PlaceholdersCommand implements SimpleCommand {
    private final Placeholders plugin;

    public PlaceholdersCommand(Placeholders plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(Invocation invocation) {
        String[] args = invocation.arguments();

        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if (plugin.reloadConfig()) {
                invocation.source().sendMessage(Component.text("Successfully reloaded the config!").color(NamedTextColor.GREEN));
            }
        }
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        String[] args = invocation.arguments();
        Stream<String> possibilities = Stream.of("reload");

        if (args.length == 0) {
            return possibilities.collect(Collectors.toList());
        }
        else if (args.length == 1) {
            return possibilities
                    .filter(name -> name.regionMatches(true, 0, args[0], 0, args[0].length()))
                    .collect(Collectors.toList());
        }
        else {
            return ImmutableList.of();
        }
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission("placeholders.admin");
    }
}
