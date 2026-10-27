package cc.co.evenprime.bukkit.nocheat;

import org.bukkit.ChatColor;

public class Colors {
    public static String replaceColors(String text) {
        for (ChatColor c : ChatColor.values()) {
            text = text.replace("&" + c.getChar(), c.toString());
        }

        return text;
    }

    public static String removeColors(String text) {
        for (ChatColor c : ChatColor.values()) {
            text = text.replace("&" + c.getChar(), "");
        }

        return text;
    }
}
