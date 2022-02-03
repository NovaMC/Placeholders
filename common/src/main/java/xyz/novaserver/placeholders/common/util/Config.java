package xyz.novaserver.placeholders.common.util;

import com.google.common.io.ByteStreams;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;

import java.io.*;

public class Config {
    private final Object plugin;
    private final File configFile;
    private final String defaultFile;
    private ConfigurationNode rootNode;

    public Config(Object plugin, File configFile, String defaultFile) {
        this.plugin = plugin;
        this.configFile = configFile;
        this.defaultFile = defaultFile;
    }

    public boolean loadConfig() {
        if (!configFile.getParentFile().exists()) {
            configFile.getParentFile().mkdirs();
        }

        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
                try (InputStream is = plugin.getClass().getResourceAsStream("/" + defaultFile);
                     OutputStream os = new FileOutputStream(configFile)) {
                    ByteStreams.copy(is, os);
                }
            }
            catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        YAMLConfigurationLoader loader = YAMLConfigurationLoader.builder().setPath(configFile.toPath()).build();

        try {
            rootNode = loader.load();
            return true;
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public ConfigurationNode getRoot() {
        return rootNode;
    }
}
