package xyz.novaserver.placeholders.velocity.chat;

import com.google.common.reflect.TypeToken;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import xyz.novaserver.placeholders.velocity.Main;

import java.util.ArrayList;
import java.util.List;

public class ChatFilterListener {
    private final Main plugin;

    private final List<Character> blockedChars = new ArrayList<>();
    private boolean filterEnabled = false;

    public ChatFilterListener(Main plugin) {
        this.plugin = plugin;
        reload();
        plugin.getProxy().getEventManager().register(plugin, this);
    }

    @SuppressWarnings("UnstableApiUsage")
    public void reload() {
        final ConfigurationNode config = plugin.getPlaceholders().getConfig();
        blockedChars.clear();
        try {
            List<String> strings = config.getNode("blocked-chars").getList(TypeToken.of(String.class));
            strings.forEach(s -> {
                for (char c : s.toCharArray()) {
                    blockedChars.add(c);
                }
            });
        } catch (ObjectMappingException e) {
            throw new RuntimeException(e);
        }
        filterEnabled = config.getNode("enable-char-filter").getBoolean(false);
    }

    @Subscribe
    public void onChatMessage(PlayerChatEvent event) {
        // Return if filter is not enabled
        if (!filterEnabled) {
            return;
        }

        String message = event.getMessage();
        for (Character character : blockedChars) {
            message = message.replace(character, Character.MIN_VALUE);
        }

        if (message.isEmpty() || message.isBlank()) {
            event.setResult(PlayerChatEvent.ChatResult.denied());
        } else {
            event.setResult(PlayerChatEvent.ChatResult.message(message));
        }
    }
}
