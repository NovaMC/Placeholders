package xyz.novaserver.placeholders.paper.chat;

import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class FormatListener implements Listener {
    private final ChatManager chatManager;

    public FormatListener(ChatManager chatManager) {
        this.chatManager = chatManager;
    }

    @EventHandler
    public void onPlayerChat(AsyncChatEvent event) {
        if (chatManager.isEnabled()) {
            event.renderer(chatManager.getFancyRenderer());
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (chatManager.isEnabled()) {
            chatManager.getFancyRenderer().removeFormat(event.getPlayer().getUniqueId());
        }
    }
}
