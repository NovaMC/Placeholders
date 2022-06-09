package xyz.novaserver.placeholders.paper.chat.format;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;
import xyz.novaserver.placeholders.paper.util.MetaUtils;

public class ConsoleFormatter implements Formatter {
    @Override
    public Component get(Player source, Component content, Audience viewer) {
        TextColor nameColor = MetaUtils.getMeta(source, "prefix-color").color();

        Component prefix = MetaUtils.getMeta(source, "tag").color(nameColor);
        Component displayName = source.displayName().color(nameColor);

        Component separator = Component.text(": ").color(nameColor);
        Component message = content.color(MetaUtils.getMeta(source, "message-color").color());

        return prefix
                .append(displayName)
                .append(separator)
                .append(message);
    }
}
