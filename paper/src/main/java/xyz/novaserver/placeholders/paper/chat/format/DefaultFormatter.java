package xyz.novaserver.placeholders.paper.chat.format;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;
import org.geysermc.floodgate.api.FloodgateApi;
import xyz.novaserver.placeholders.paper.chat.util.MetaUtils;

public class DefaultFormatter implements Formatter {
    @Override
    public Component get(Player source, Component content, Audience viewer) {
        boolean isBedrock = false;
        if (viewer instanceof Player playerViewer) {
            isBedrock = FloodgateApi.getInstance().isFloodgatePlayer(playerViewer.getUniqueId());
        }

        Component prefix;
        TextColor nameColor;
        TextColor messageColor;

        if (!isBedrock) {
            prefix = MetaUtils.getPrefix(source);
            nameColor = MetaUtils.getMeta(source, "prefix-color").color();
            messageColor = MetaUtils.getMeta(source, "message-color").color();
        } else  {
            prefix = MetaUtils.getMeta(source, "tag");
            nameColor = MetaUtils.getMeta(source, "tag-color").color();
            messageColor = NamedTextColor.WHITE;
        }

        Component separator = Component.text(": ").color(nameColor);
        return prefix
                .append(source.displayName().color(nameColor))
                .append(separator)
                .append(content.color(messageColor));
    }
}
