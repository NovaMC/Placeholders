package xyz.novaserver.placeholders.paper.listener;

import com.google.common.reflect.TypeToken;
import io.papermc.paper.event.player.AsyncChatEvent;
import me.neznamy.tab.api.TabAPI;
import me.neznamy.tab.api.TabPlayer;
import me.neznamy.tab.api.team.UnlimitedNametagManager;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitTask;
import xyz.novaserver.placeholders.common.PlaceholderPlayer;
import xyz.novaserver.placeholders.paper.PlaceholdersPaper;

import java.util.*;

public class ChatListener implements Listener {
    private final PlaceholdersPaper plugin;
    private final TabAPI tabAPI = TabAPI.getInstance();
    private ConfigurationNode node;

    private final Map<UUID, List<BukkitTask>> taskMap = new HashMap<>();

    public ChatListener(PlaceholdersPaper plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerChat(AsyncChatEvent event) {
        this.node = plugin.getConfiguration().getNode("chat-display");

        if (!node.getNode("enabled").getBoolean(true)) return;
        if (!(tabAPI.getTeamManager() instanceof UnlimitedNametagManager)) return;
        if (event.isCancelled()) return;

        UUID uuid = event.getPlayer().getUniqueId();
        PlaceholderPlayer pPlayer = PlaceholderPlayer.getPlayerMap().get(uuid);

        // Raw contents of the message shortened to 16 characters
        String raw = PlainTextComponentSerializer.plainText().serialize(event.message());
        if (raw.length() > 17) {
            raw = raw.substring(0, 15).strip().concat("...");
        }

        // Color message and set on player
        pPlayer.setLastMessage(node.getNode("color").getString("") + raw);

        // Cancel and clear all tasks from the map
        if (taskMap.containsKey(uuid)) {
            List<BukkitTask> taskList = taskMap.get(uuid);
            if (!taskList.isEmpty()) {
                taskList.forEach(BukkitTask::cancel);
                taskMap.get(uuid).clear();
            }
        } else {
            taskMap.put(uuid, new ArrayList<>());
        }

        try {
            runChatDisplay(uuid, pPlayer);
        } catch (ObjectMappingException e) {
            e.printStackTrace(); // Should never happen
        }
    }

    private void runChatDisplay(UUID uuid, PlaceholderPlayer pPlayer) throws ObjectMappingException {
        TabPlayer tabPlayer = tabAPI.getPlayer(uuid);
        UnlimitedNametagManager manager = (UnlimitedNametagManager) tabAPI.getTeamManager();

        List<String> prefixes = node.getNode("prefixes").getList(TypeToken.of(String.class), Collections.emptyList());
        final long time = node.getNode("time").getLong();
        final long update = time / prefixes.size();

        // Set prefix as a workaround
        pPlayer.setMessagePrefix(prefixes.get(0));

        // Set chat placeholder above name
        manager.setLine(tabPlayer, "abovename", "%rel_nova_chat_display%");

        // Task to animate icon
        int current = 0;
        for (String prefix : prefixes) {
            BukkitTask task = Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {
                pPlayer.setMessagePrefix(prefix);
            }, update * current);

            taskMap.get(uuid).add(task);
            current++;
        }

        // Clear message
        BukkitTask task = Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {
            manager.resetLine(tabPlayer, "abovename");
            pPlayer.setLastMessage("");
            pPlayer.setMessagePrefix("");
            taskMap.get(uuid).clear();
        }, time);
        taskMap.get(uuid).add(task);
    }
}
