package xyz.novaserver.placeholders.paper.chat;

import io.papermc.paper.chat.ChatRenderer;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.novaserver.placeholders.paper.chat.format.ConsoleFormatter;
import xyz.novaserver.placeholders.paper.chat.format.DefaultFormatter;
import xyz.novaserver.placeholders.paper.chat.format.Formatter;

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
        } else if (hasFormat(source)) {
            // Grab custom format if the player has one
            formatter = formatMap.get(source.getUniqueId());
        }

        return formatter.get(source, message, viewer);
    }

    public void setFormat(Player player, Formatter formatter) {
        formatMap.put(player.getUniqueId(), formatter);
    }

    public void removeFormat(Player player) {
        formatMap.remove(player.getUniqueId());
    }

    public boolean hasFormat(Player p) {
        return formatMap.containsKey(p.getUniqueId());
    }
}
