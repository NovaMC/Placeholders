package xyz.novaserver.placeholders.paper.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.novaserver.placeholders.core.command.PHCommand;
import xyz.novaserver.placeholders.core.command.Source;
import xyz.novaserver.placeholders.paper.PlaceholdersPaper;

import java.util.List;

public class PaperPHCommand implements TabExecutor {
    private final PHCommand command;

    public PaperPHCommand(PlaceholdersPaper plugin) {
        this.command = new PHCommand(plugin);
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!hasPermission(sender)) {
            sender.sendMessage(Component.text("You don't have permission to use this command!").color(NamedTextColor.RED));
            return true;
        }
        this.command.execute(args, new Source() {
            @Override
            public void sendMessage(Component message) {
                sender.sendMessage(message);
            }
        });
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return this.command.suggest(args);
    }

    private boolean hasPermission(CommandSender sender) {
        return sender.hasPermission(command.getPermission());
    }
}
