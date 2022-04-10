package xyz.novaserver.placeholders.paper.listener;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.neznamy.tab.api.TabAPI;
import me.neznamy.tab.api.TabPlayer;
import me.neznamy.tab.api.team.UnlimitedNametagManager;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import ninja.leaping.configurate.ConfigurationNode;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitTask;
import xyz.novaserver.placeholders.common.data.PlayerData;
import xyz.novaserver.placeholders.paper.Main;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChatListener implements Listener {
    private final Main plugin;
    private final TabAPI tabAPI = TabAPI.getInstance();

    private final Map<UUID, BukkitTask> taskMap = new HashMap<>();

    public ChatListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerChat(AsyncChatEvent event) {
        final ConfigurationNode node = plugin.getPlaceholders().getConfig().getNode("chat-display");

        if (event.isCancelled()) return;
        if (!node.getNode("enabled").getBoolean(true)) return;
        if (!(tabAPI.getTeamManager() instanceof UnlimitedNametagManager manager)) return;

        UUID uuid = event.getPlayer().getUniqueId();
        TabPlayer tabPlayer = tabAPI.getPlayer(uuid);
        PlayerData playerData = plugin.getPlaceholders().getData(uuid);

        // Raw contents of the message shortened to 16 characters
        String raw = PlainTextComponentSerializer.plainText().serialize(event.message());
        if (raw.length() > 17) {
            raw = raw.substring(0, 15).strip().concat("...");
        }

        // Set message on player
        playerData.setLastMessage(raw);

        // Cancel and clear task from the map
        if (taskMap.containsKey(uuid)) {
            taskMap.get(uuid).cancel();
            taskMap.remove(uuid);
        }

        // Set chat placeholder above name
        manager.setLine(tabPlayer, "abovename", "%rel_nova_chat_display%");

        // Clear message
        BukkitTask task = Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {
            manager.resetLine(tabPlayer, "abovename");
            playerData.setLastMessage("");
            taskMap.remove(uuid);
        }, node.getNode("time").getLong());
        taskMap.put(uuid, task);
    }
}
