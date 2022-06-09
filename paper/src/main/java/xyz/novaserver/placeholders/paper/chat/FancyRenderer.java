package xyz.novaserver.placeholders.paper.chat;

import io.papermc.paper.chat.ChatRenderer;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.novaserver.placeholders.paper.chat.format.ConsoleFormatter;
import xyz.novaserver.placeholders.paper.chat.format.DefaultFormatter;
import xyz.novaserver.placeholders.paper.chat.format.Formatter;
import xyz.novaserver.placeholders.paper.util.MetaUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FancyRenderer implements ChatRenderer {
    private final ChatManager chatManager;
    private final Map<UUID, Formatter> formatMap = new HashMap<>();

    private final ConsoleFormatter consoleFormatter = new ConsoleFormatter();
    private final DefaultFormatter defaultFormatter = new DefaultFormatter();

    public FancyRenderer(ChatManager chatManager) {
        this.chatManager = chatManager;
    }

    @Override
    public @NotNull Component render(@NotNull Player source, @NotNull Component sourceDisplayName,
                                     @NotNull Component message, @NotNull Audience viewer) {
        Formatter formatter = defaultFormatter;

        if (!(viewer instanceof Player)) {
            // Console renderer
            formatter = consoleFormatter;
        } else if (hasFormat(source.getUniqueId())) {
            // Grab custom format if the player has one
            formatter = formatMap.get(source.getUniqueId());
        }

        // Get the formatted message from the current formatter
        Component formatted = formatter.get(source, message, viewer);

        // Add hover text
        String hoverText = chatManager.getConfig().getNode("clickable", "hover-text").getString();
        if (hoverText != null && !hoverText.isBlank()) {
            Component hoverComponent = MetaUtils.replacePlaceholders(source, MetaUtils.asComponent(hoverText));
            formatted = formatted.hoverEvent(formatted.asHoverEvent(c -> hoverComponent));
        }

        // Add clickable command
        String command = chatManager.getConfig().getNode("clickable", "command").getString();
        boolean shouldExecute = chatManager.getConfig().getNode("clickable", "should-execute").getBoolean(false);
        if (command != null && !command.isBlank()) {
            command = MetaUtils.replacePlaceholders(source, command);
            formatted = shouldExecute
                    ? formatted.clickEvent(ClickEvent.runCommand(command))
                    : formatted.clickEvent(ClickEvent.suggestCommand(command));
        }

        return formatted;
    }

    public void setFormat(UUID player, Formatter formatter) {
        formatMap.put(player, formatter);
    }

    public void removeFormat(UUID player) {
        formatMap.remove(player);
    }

    public boolean hasFormat(UUID player) {
        return formatMap.containsKey(player);
    }
}
