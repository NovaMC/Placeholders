package xyz.novaserver.placeholders.paper.placeholder;

import me.clip.placeholderapi.PlaceholderAPI;
import xyz.novaserver.placeholders.common.PlaceholderPlayer;
import xyz.novaserver.placeholders.common.placeholder.type.AbstractRelationalPlaceholder;
import xyz.novaserver.placeholders.paper.PlaceholdersPaper;

import java.util.UUID;

public class VoicePlaceholder extends AbstractRelationalPlaceholder {
    private final PlaceholdersPaper plugin;

    public VoicePlaceholder(PlaceholdersPaper plugin) {
        super("voice");
        this.plugin = plugin;
    }

    @Override
    public String get(UUID viewer, UUID player) {
        PlaceholderPlayer pPlayer = PlaceholderPlayer.getPlayerMap().get(viewer);
        String text = "%plasmovoice_installed%";
        String output = "";

        text = PlaceholderAPI.setPlaceholders(plugin.getServer().getPlayer(player), text);

        if (pPlayer != null && !pPlayer.getPlatform().equals(PlaceholderPlayer.Platform.BEDROCK)) {
            if (text.equals("true")) {
                output = plugin.getPlaceholderMap().get("voice-java");
            } else if (text.equals("false")) {
                output = plugin.getPlaceholderMap().get("novoice-java");
            }
        } else {
            if (text.equals("true")) {
                output = plugin.getPlaceholderMap().get("voice-bedrock");
            } else if (text.equals("false")) {
                output = plugin.getPlaceholderMap().get("novoice-bedrock");
            }
        }

        return output;
    }
}
