package xyz.novaserver.placeholders.placeholder;

import ninja.leaping.configurate.ConfigurationNode;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import su.plo.voice.PlasmoVoiceAPI;
import xyz.novaserver.placeholders.common.Placeholders;
import xyz.novaserver.placeholders.common.data.PlayerData;
import xyz.novaserver.placeholders.placeholder.type.Placeholder;
import xyz.novaserver.placeholders.placeholder.type.RelationalType;

public class VoicePlaceholder extends Placeholder implements RelationalType {
    private PlasmoVoiceAPI plasmoApi;

    public VoicePlaceholder(Placeholders plugin) {
        super(plugin, "voice", 2000);

        RegisteredServiceProvider<PlasmoVoiceAPI> provider = Bukkit.getServicesManager().getRegistration(PlasmoVoiceAPI.class);
        if (provider != null) {
            plasmoApi = provider.getProvider();
        }
    }

    @Override
    public String get(PlayerData viewer, PlayerData player) {
        final ConfigurationNode node = getPlaceholders().getConfig();
        final boolean hasVoice = plasmoApi != null && plasmoApi.hasVoiceChat(player.getUuid());


        if (viewer.isResourcePackApplied()) {
            return hasVoice ? node.getNode("status", "voice", "rp").getString()
                    : node.getNode("status", "no-voice", "rp").getString();
        } else {
            return hasVoice ? node.getNode("status", "voice", "default").getString()
                    : node.getNode("status", "no-voice", "default").getString();
        }
    }
}
