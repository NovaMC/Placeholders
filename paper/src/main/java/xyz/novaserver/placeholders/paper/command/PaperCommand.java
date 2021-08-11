package xyz.novaserver.placeholders.paper.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.novaserver.placeholders.common.command.AbstractCommand;

import java.util.List;

public class PaperCommand implements TabExecutor {
    private final AbstractCommand command;

    public PaperCommand(AbstractCommand command) {
        this.command = command;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!hasPermission(sender)) {
            sender.sendMessage(Component.text("You don't have permission to use this command!").color(NamedTextColor.RED));
            return true;
        }
        this.command.execute(args, sender::sendMessage);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return this.command.suggest(args);
    }

    private boolean hasPermission(CommandSender sender) {
        return sender.hasPermission(command.getPermission());
    }
}
