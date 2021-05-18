package xyz.msws.skywars.utils;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

/**
 * Represents a configurable message
 */
public interface Sendable {

    /**
     * Returns the default message
     *
     * @return the default message
     */
    String getDefault();

    /**
     * Returns the current configured message
     *
     * @return the configured message
     */
    String getConfigured();

    /**
     * Loads the messages from the YML config
     *
     * @param file {@link YamlConfiguration} to load messages from
     */
    void load(YamlConfiguration file);

    /**
     * Loads the messages from the given file
     *
     * @param file {@link File} to load messages from
     */
    default void load(File file) {
        load(YamlConfiguration.loadConfiguration(file));
    }

    /**
     * Formats and returns the configured message
     *
     * @param objects Objects to format with {@link String#format(String, Object...)}
     * @return formatted configured message
     */
    default String format(Object... objects) {
        return String.format(getConfigured(), objects);
    }
}
