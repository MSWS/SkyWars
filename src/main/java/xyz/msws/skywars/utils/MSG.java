package xyz.msws.skywars.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.concurrent.TimeUnit;

/**
 * Helper class for sending messages
 */
public class MSG {

    /**
     * Sends the specified string colored
     * Formats the string if additional parameters are given
     *
     * @param sender  {@link CommandSender} recipient
     * @param message Message to send
     * @param format  Objects to format message with
     */
    public static void tell(CommandSender sender, String message, Object... format) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format(message, format)));
    }

    /**
     * Sends a {@link Sendable} message to the target
     * Formats the message if additional parameters are given
     *
     * @param sender  {@link CommandSender} recipient
     * @param message Sendable to send
     * @param format  Objects to format message with
     */
    public static void tell(CommandSender sender, Sendable message, Object... format) {
        tell(sender, message.format(format));
    }

    /**
     * Logs the specified message to console
     *
     * @param message Message to send
     * @param format  Objects to format message with
     */
    public static void log(String message, Object... format) {
        tell(Bukkit.getConsoleSender(), message, format);
    }

    /**
     * Sends the specified string with <b>no coloring</b>
     * Formats the string if additional parameters are given
     *
     * @param sender  {@link CommandSender} recipient
     * @param message Message to send
     * @param format  Objects to format message with
     */
    public static void tellRaw(CommandSender sender, String message, Object... format) {
        sender.sendMessage(String.format(message, format));
    }

    /**
     * Returns the string with {@link ChatColor#translateAlternateColorCodes(char, String)} applied
     *
     * @param s String to color
     * @return Colored string
     */
    public static String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    /**
     * Returns the string with {@link ChatColor#stripColor(String)} applied
     *
     * @param s String to strip
     * @return Stripped string
     */
    public static String stripColor(String s) {
        return ChatColor.stripColor(s);
    }

    /**
     * Simplifies duration descriptions
     *
     * @param ms Milliseconds for the duration
     * @return Formatted description of duration
     */
    public static String getDuration(long ms) {
        TimeUnit unit = TimeUnit.DAYS;
        for (TimeUnit u : TimeUnit.values()) {
            if (ms < u.toMillis(1))
                break;
            unit = u;
        }

        double p = (double) ms / unit.toMillis(1);

        if (p == 1) // If it's exactly 1 unit, don't have an S at the end of the unit
            return "1 " + unit.toString().toLowerCase().substring(0, unit.toString().length() - 1);

        if (p == (int) p) // If it's exactly a number of units, don't specify decimals
            return (int) p + " " + unit.toString().toLowerCase();

        return String.format("%.2f %s", (double) ms / unit.toMillis(1), unit.toString().toLowerCase());
    }
}
