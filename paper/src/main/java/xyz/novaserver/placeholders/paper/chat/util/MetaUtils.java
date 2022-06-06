package xyz.novaserver.placeholders.paper.chat.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class MetaUtils {
    public static Component getPrefix(Player player) {
        final String prefix = loadUser(player).getCachedData().getMetaData().getPrefix();
        return asComponent(prefix != null ? prefix : "");
    }

    public static Component getSuffix(Player player) {
        final String suffix = loadUser(player).getCachedData().getMetaData().getSuffix();
        return asComponent(suffix != null ? suffix : "");
    }

    public static Component getMeta(Player player, String meta) {
        final String value = loadUser(player).getCachedData().getMetaData().getMetaValue(meta);
        return asComponent(value != null ? value : "");
    }

    private static Component asComponent(String s) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(s);
    }

    private static User loadUser(Player player) {
        if (!player.isOnline())
            throw new IllegalStateException("Player is offline!");

        return getLuckPerms().getUserManager().getUser(player.getUniqueId());
    }

    private static LuckPerms getLuckPerms() {
        final RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        Validate.notNull(provider);
        return provider.getProvider();
    }
}
