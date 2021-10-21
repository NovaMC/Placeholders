package xyz.novaserver.placeholders.paper.listener;

import com.google.common.reflect.TypeToken;
import io.papermc.paper.event.player.AsyncChatEvent;
import me.neznamy.tab.api.EnumProperty;
import me.neznamy.tab.api.TABAPI;
import me.neznamy.tab.api.TabPlayer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import xyz.novaserver.placeholders.common.PlaceholderPlayer;
import xyz.novaserver.placeholders.paper.PlaceholdersPaper;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class ChatListener implements Listener {
    private final PlaceholdersPaper plugin;
    private final ConfigurationNode node;

    public ChatListener(PlaceholdersPaper plugin) {
        this.plugin = plugin;
        this.node = plugin.getConfiguration().getNode("chat-display");
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerChat(AsyncChatEvent event) {
        if (!node.getNode("enabled").getBoolean(true)) return;
        if (!TABAPI.isUnlimitedNameTagModeEnabled()) return;

        UUID uuid = event.getPlayer().getUniqueId();
        PlaceholderPlayer pPlayer = PlaceholderPlayer.getPlayerMap().get(uuid);

        // Raw contents of the message shortened to 16 characters
        String raw = PlainTextComponentSerializer.plainText().serialize(event.message());
        if (raw.length() > 16) {
            raw = raw.substring(0, 15).concat("...");
        }

        // Color message and set on player
        pPlayer.setLastMessage(node.getNode("color").getString("") + raw);

        try {
            runChatDisplay(uuid, pPlayer);
        } catch (ObjectMappingException e) {
            e.printStackTrace();
        }
    }

    private void runChatDisplay(UUID uuid, PlaceholderPlayer pPlayer) throws ObjectMappingException {
        TabPlayer tabPlayer = TABAPI.getPlayer(uuid);

        List<String> prefixes = node.getNode("prefixes").getList(TypeToken.of(String.class), Collections.emptyList());
        final long time = 80L; // Display message for 5 seconds
        final long update = time / prefixes.size();

        // Set prefix as a workaround
        pPlayer.setMessagePrefix(prefixes.get(0));

        // Set chat placeholder above name
        tabPlayer.setValueTemporarily(EnumProperty.ABOVENAME, "%rel_nova_chat_display%");

        // Task to animate icon
        int current = 0;
        for (String prefix : prefixes) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                pPlayer.setMessagePrefix(prefix);
            }, update * current);
            current++;
        }

        // Clear message
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            tabPlayer.removeTemporaryValue(EnumProperty.ABOVENAME);
            pPlayer.setLastMessage("");
            pPlayer.setMessagePrefix("");
        }, time);
    }
}
