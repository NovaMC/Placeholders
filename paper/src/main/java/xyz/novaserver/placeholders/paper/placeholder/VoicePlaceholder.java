package xyz.novaserver.placeholders.paper.placeholder;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import su.plo.voice.PlasmoVoiceAPI;
import xyz.novaserver.placeholders.common.PlaceholdersPlugin;
import xyz.novaserver.placeholders.common.PlayerData;
import xyz.novaserver.placeholders.common.placeholder.type.Placeholder;
import xyz.novaserver.placeholders.common.placeholder.type.RelationalType;

import java.util.UUID;

public class VoicePlaceholder extends Placeholder implements RelationalType {
    private PlasmoVoiceAPI plasmoApi;

    public VoicePlaceholder(PlaceholdersPlugin plugin) {
        super(plugin, "voice", 5000);

        RegisteredServiceProvider<PlasmoVoiceAPI> provider = Bukkit.getServicesManager().getRegistration(PlasmoVoiceAPI.class);
        if (provider != null) {
            plasmoApi = provider.getProvider();
        }
    }

    @Override
    public String get(UUID viewer, UUID player) {
        PlayerData viewerData = getPlugin().getPlayerData(viewer);
        boolean hasVoice = plasmoApi != null && plasmoApi.hasVoiceChat(player);

        if (viewerData != null && viewerData.isResourcePackApplied()) {
            return hasVoice ? getPlugin().getRootValue("voice-rp") : getPlugin().getRootValue("novoice-rp");
        } else {
            return hasVoice ? getPlugin().getRootValue("voice-vanilla") : getPlugin().getRootValue("novoice-vanilla");
        }
    }
}
