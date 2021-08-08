package xyz.novaserver.placeholders.velocity.command;

import com.velocitypowered.api.command.SimpleCommand;
import net.kyori.adventure.text.Component;
import xyz.novaserver.placeholders.core.command.PHCommand;
import xyz.novaserver.placeholders.core.command.Source;
import xyz.novaserver.placeholders.velocity.PlaceholdersVelocity;

import java.util.List;

public class VelocityPHCommand implements SimpleCommand {
    private final PHCommand command;

    public VelocityPHCommand(PlaceholdersVelocity plugin) {
        this.command = new PHCommand(plugin);
    }

    @Override
    public void execute(Invocation invocation) {
        String[] args = invocation.arguments();
        command.execute(args, new Source() {
            @Override
            public void sendMessage(Component message) {
                invocation.source().sendMessage(message);
            }
        });
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        return command.suggest(invocation.arguments());
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission(command.getPermission());
    }
}
