package xyz.novaserver.placeholders.velocity.command;

import com.velocitypowered.api.command.SimpleCommand;
import xyz.novaserver.placeholders.common.command.AbstractCommand;

import java.util.List;

public class VelocityCommand implements SimpleCommand {
    private final AbstractCommand command;

    public VelocityCommand(AbstractCommand command) {
        this.command = command;
    }

    @Override
    public void execute(Invocation invocation) {
        String[] args = invocation.arguments();
        command.execute(args, message -> invocation.source().sendMessage(message));
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
