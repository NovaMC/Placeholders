package xyz.novaserver.placeholders.common.command;

import xyz.novaserver.placeholders.common.Placeholders;

import java.util.List;

public abstract class CommandExecutor {
    private final Placeholders placeholders;

    public CommandExecutor(Placeholders placeholders) {
        this.placeholders = placeholders;
    }

    public Placeholders getPlaceholders() {
        return placeholders;
    }

    public abstract void execute(String[] args, Object source);

    public abstract List<String> suggest(String[] args);

    public abstract String getPermission();
}
