package xyz.novaserver.placeholders.velocity.command;

import com.velocitypowered.api.command.SimpleCommand;
import xyz.novaserver.placeholders.common.command.CommandExecutor;

import java.util.List;

public class VelocityCommand implements SimpleCommand {
    private final CommandExecutor executor;

    public VelocityCommand(CommandExecutor executor) {
        this.executor = executor;
    }

    @Override
    public void execute(Invocation invocation) {
        executor.execute(invocation.arguments(), invocation.source());
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        return executor.suggest(invocation.arguments());
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission(executor.getPermission());
    }
}
