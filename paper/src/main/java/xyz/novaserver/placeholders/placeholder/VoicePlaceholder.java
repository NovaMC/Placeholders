package xyz.novaserver.placeholders.placeholder;

import ninja.leaping.configurate.ConfigurationNode;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import xyz.novaserver.placeholders.common.Placeholders;
import xyz.novaserver.placeholders.common.data.PlayerData;
import xyz.novaserver.placeholders.placeholder.type.Placeholder;
import xyz.novaserver.placeholders.placeholder.type.RelationalType;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

public class VoicePlaceholder extends Placeholder implements RelationalType {
    private Object plasmoApi;
    private Method hasVoiceChat;

    public VoicePlaceholder(Placeholders placeholders) {
        super(placeholders, "voice", 2000);

        if (Bukkit.getPluginManager().isPluginEnabled("PlasmoVoice")) {
            try {
                Class<?> classPlasmoApi = Class.forName("su.plo.voice.PlasmoVoiceAPI");
                RegisteredServiceProvider<?> provider = Bukkit.getServicesManager().getRegistration(classPlasmoApi);
                if (provider != null) {
                    plasmoApi = provider.getProvider();
                    hasVoiceChat = classPlasmoApi.getMethod("hasVoiceChat", UUID.class);
                }
            } catch (ClassNotFoundException | NoSuchMethodException | NullPointerException ignored) {
                // PlasmoVoice not loaded
            }
        }
    }

    private boolean hasVoiceChat(UUID uuid) {
        try {
            return plasmoApi != null && (boolean) hasVoiceChat.invoke(plasmoApi, uuid);
        } catch (IllegalAccessException | InvocationTargetException ignored) {
            // PlasmoVoice not loaded
            return false;
        }
    }

    @Override
    public String get(PlayerData viewer, PlayerData player) {
        final ConfigurationNode node = getPlaceholders().getConfig().getNode("status");
        final boolean hasVoice = hasVoiceChat(player.getUuid());

        if (!viewer.isBedrock()) {
            return hasVoice ? node.getNode("voice", "java").getString()
                    : node.getNode("no-voice", "java").getString();
        } else {
            return hasVoice ? node.getNode("voice", "bedrock").getString()
                    : node.getNode("no-voice", "bedrock").getString();
        }
    }
}
