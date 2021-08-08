package xyz.novaserver.placeholders.core.command;

import java.util.List;

public interface AbstractCommand {
    void execute(String[] args, Source source);

    List<String> suggest(String[] args);

    String getPermission();
}
