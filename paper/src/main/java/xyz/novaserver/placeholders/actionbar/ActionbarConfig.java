package xyz.novaserver.placeholders.actionbar;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMapper;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import xyz.novaserver.placeholders.common.util.Config;

import java.util.List;

@ConfigSerializable
public final class ActionbarConfig {
    private final Config config;
    private final ActionbarManager manager;

    public ActionbarConfig(ActionbarManager manager, Config config) {
        this.manager = manager;
        this.config = config;
    }

    public ConfigurationNode get() {
        return config.getRoot();
    }

    public void loadConfig() {
        boolean success = config.loadConfig();
        if (!success) {
            manager.getPlugin().logError("Failed to load Actionbars config file!", null);
        }
    }

    public void loadActionbars(List<Actionbar> actionbarList) {
        actionbarList.clear();
        config.getRoot().getNode("actionbars").getChildrenMap().values().forEach(node -> {
            try {
                actionbarList.add(Actionbar.loadFrom(node));
            } catch (ObjectMappingException e) {
                manager.getPlugin().logError("Mapping exception occurred while trying to deserialize config!", e);
            }
        });
    }

    static class Actionbar {
        private static final ObjectMapper<Actionbar> MAPPER;

        static {
            try {
                MAPPER = ObjectMapper.forClass(Actionbar.class);
            } catch (final ObjectMappingException e) {
                throw new ExceptionInInitializerError(e);
            }
        }

        public static Actionbar loadFrom(final ConfigurationNode node) throws ObjectMappingException {
            return MAPPER.bindToNew().populate(node);
        }

        private @Setting long interval;
        private @Setting String message;
        private @Setting List<Condition> conditions;
        private @Setting Bedrock bedrock;

        public long interval() {
            return interval;
        }

        public String message() {
            return message;
        }

        public Bedrock bedrock() {
            return bedrock;
        }

        @ConfigSerializable
        static class Bedrock {
            private @Setting long interval;
            private @Setting String message;

            public long interval() {
                return interval;
            }

            public String message() {
                return message;
            }
        }

        enum Condition {
            WORLDGUARD
        }

        @Override
        public String toString() {
            return "ActionbarConfig{" +
                    "interval=" + interval +
                    ", message='" + message + '\'' +
                    ", conditions=" + conditions +
                    ", bedrock=" + bedrock +
                    '}';
        }
    }
}


