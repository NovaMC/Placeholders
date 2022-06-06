package xyz.novaserver.placeholders.paper.chat.format;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public interface Formatter {
    Component get(Player source, Component content, Audience viewer);
}
