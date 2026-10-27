package cc.co.evenprime.bukkit.nocheat.config;

public class LoggingConfig {
    public final boolean active;
    public final boolean showactivechecks;
    public final boolean toFile;
    public final boolean toConsole;
    public final boolean toChat;
    public final String prefix;
    public final boolean debugmessages;

    public LoggingConfig(NoCheatConfiguration data) {
        this.active = data.getBoolean("logging.active");
        this.showactivechecks = data.getBoolean("logging.showactivechecks");
        this.debugmessages = data.getBoolean("logging.debugmessages");
        this.prefix = data.getString("logging.prefix");
        this.toFile = data.getBoolean("logging.file");
        this.toConsole = data.getBoolean("logging.console");
        this.toChat = data.getBoolean("logging.ingamechat");
    }
}
