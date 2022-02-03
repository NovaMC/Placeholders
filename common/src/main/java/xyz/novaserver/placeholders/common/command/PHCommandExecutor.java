package xyz.novaserver.placeholders.common.command;

import com.google.common.collect.ImmutableList;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import xyz.novaserver.placeholders.common.PlaceholdersPlugin;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PHCommandExecutor extends CommandExecutor {
    public PHCommandExecutor(PlaceholdersPlugin plugin) {
        super(plugin);
    }

    @Override
    public void execute(String[] args, Object source) {
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if (getPlugin().reloadConfiguration()) {
                getPlugin().sendMessage(source, Component.text("Successfully reloaded the config!").color(NamedTextColor.GREEN));
            }
        }
    }

    @Override
    public List<String> suggest(String[] args) {
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
    public String getPermission() {
        return "placeholders.admin";
    }
}
