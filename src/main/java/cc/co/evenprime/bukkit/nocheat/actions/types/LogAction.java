package cc.co.evenprime.bukkit.nocheat.actions.types;

import cc.co.evenprime.bukkit.nocheat.NoCheatPlayer;
import cc.co.evenprime.bukkit.nocheat.checks.Check;

public class LogAction extends ActionWithParameters {
    private final boolean toChat;
    private final boolean toConsole;
    private final boolean toFile;

    public LogAction(String name, int delay, int repeat, boolean toChat, boolean toConsole, boolean toFile, String message) {
        super(name, delay, repeat, message);
        this.toChat = toChat;
        this.toConsole = toConsole;
        this.toFile = toFile;
    }

    public String getLogMessage(NoCheatPlayer player, Check check) {
        return super.getMessage(player, check);
    }

    public boolean toChat() {
        return this.toChat;
    }

    public boolean toConsole() {
        return this.toConsole;
    }

    public boolean toFile() {
        return this.toFile;
    }

    @Override
    public String toString() {
        return "log:"
            + this.name
            + ":"
            + this.delay
            + ":"
            + this.repeat
            + ":"
            + (this.toConsole ? "c" : "")
            + (this.toChat ? "i" : "")
            + (this.toFile ? "f" : "");
    }
}
