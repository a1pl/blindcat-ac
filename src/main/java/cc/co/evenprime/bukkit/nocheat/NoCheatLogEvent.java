package cc.co.evenprime.bukkit.nocheat;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class NoCheatLogEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private String message;
    private String prefix;
    private boolean toConsole;
    private boolean toChat;
    private boolean toFile;

    public NoCheatLogEvent(String prefix, String message, boolean toConsole, boolean toChat, boolean toFile) {
        this.prefix = prefix;
        this.message = message;
        this.toConsole = toConsole;
        this.toChat = toChat;
        this.toFile = toFile;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean toFile() {
        return this.toFile;
    }

    public void setToFile(boolean toFile) {
        this.toFile = toFile;
    }

    public boolean toChat() {
        return this.toChat;
    }

    public void setToChat(boolean toChat) {
        this.toChat = toChat;
    }

    public boolean toConsole() {
        return this.toConsole;
    }

    public void setToConsole(boolean toConsole) {
        this.toConsole = toConsole;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
