package cc.co.evenprime.bukkit.nocheat;

import cc.co.evenprime.bukkit.nocheat.checks.WorkaroundsListener;
import cc.co.evenprime.bukkit.nocheat.checks.blockbreak.BlockBreakCheckListener;
import cc.co.evenprime.bukkit.nocheat.checks.blockplace.BlockPlaceCheckListener;
import cc.co.evenprime.bukkit.nocheat.checks.combined.ImprobableCheck;
import cc.co.evenprime.bukkit.nocheat.checks.fight.FightCheckListener;
import cc.co.evenprime.bukkit.nocheat.checks.inventory.InventoryCheckListener;
import cc.co.evenprime.bukkit.nocheat.checks.moving.MovingCheckListener;
import cc.co.evenprime.bukkit.nocheat.checks.net.NetCheckListener;
import cc.co.evenprime.bukkit.nocheat.command.CommandHandler;
import cc.co.evenprime.bukkit.nocheat.config.ConfigurationCacheStore;
import cc.co.evenprime.bukkit.nocheat.config.ConfigurationManager;
import cc.co.evenprime.bukkit.nocheat.config.NoCheatConfiguration;
import cc.co.evenprime.bukkit.nocheat.data.PlayerManager;
import cc.co.evenprime.bukkit.nocheat.debug.ActiveCheckPrinter;
import cc.co.evenprime.bukkit.nocheat.debug.LagMeasureTask;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class NoCheat extends JavaPlugin implements Listener {
    private ConfigurationManager conf;
    private CommandHandler commandHandler;
    private PlayerManager players;
    private List<EventManager> eventManagers;
    private LagMeasureTask lagMeasureTask;
    private Logger fileLogger;

    @Override
    public void onDisable() {
        PluginDescriptionFile pdfFile = this.getDescription();
        if (this.lagMeasureTask != null) {
            this.lagMeasureTask.cancel();
            this.lagMeasureTask = null;
        }

        if (this.conf != null) {
            this.conf.cleanup();
            this.conf = null;
        }

        this.getServer().getScheduler().cancelTasks(this);
        this.commandHandler = null;
        System.out.println("[blindcat] version [" + pdfFile.getVersion() + "] is disabled.");
    }

    @Override
    public void onEnable() {
        this.players = new PlayerManager(this);
        this.commandHandler = new CommandHandler(this);
        this.conf = new ConfigurationManager(this, this.getDataFolder());
        new ImprobableCheck(this);
        this.eventManagers = new ArrayList<>(8);
        this.eventManagers.add(new MovingCheckListener(this));
        this.eventManagers.add(new WorkaroundsListener());
        this.eventManagers.add(new BlockBreakCheckListener(this));
        this.eventManagers.add(new BlockPlaceCheckListener(this));
        this.eventManagers.add(new InventoryCheckListener(this));
        this.eventManagers.add(new FightCheckListener(this));
        if (Bukkit.getPluginManager().isPluginEnabled("ProtocolLib")) {
            NetCheckListener netCheckListener = new NetCheckListener(this);
            netCheckListener.register();
            this.eventManagers.add(netCheckListener);
        } else {
            System.out.println("[blindcat] ProtocolLib not found - net-level packet checks (timer/keepalive frequency) are disabled.");
        }

        if (this.lagMeasureTask == null) {
            this.lagMeasureTask = new LagMeasureTask(this);
            this.lagMeasureTask.start();
        }

        ActiveCheckPrinter.printActiveChecks(this, this.eventManagers);

        for (EventManager eventManager : this.eventManagers) {
            Bukkit.getPluginManager().registerEvents(eventManager, this);
        }

        Bukkit.getPluginManager().registerEvents(this, this);
        NoCheatConfiguration.writeInstructions(this.getDataFolder());
        System.out.println("[blindcat] version [" + this.getDescription().getVersion() + "] is enabled.");
    }

    public ConfigurationCacheStore getConfig(Player player) {
        return player != null ? this.getConfig(player.getWorld()) : this.conf.getConfigurationCacheForWorld(null);
    }

    public ConfigurationCacheStore getConfig(World world) {
        return world != null ? this.conf.getConfigurationCacheForWorld(world.getName()) : this.conf.getConfigurationCacheForWorld(null);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        this.commandHandler.handleCommand(this, sender, command, label, args);
        return true;
    }

    public boolean skipCheck() {
        return this.lagMeasureTask != null ? this.lagMeasureTask.skipCheck() : false;
    }

    public void reloadConfiguration() {
        this.conf.cleanup();
        this.conf = new ConfigurationManager(this, this.getDataFolder());
        this.players.cleanDataMap();
    }

    public void cleanDataMap() {
        this.players.cleanDataMap();
    }

    public Map<String, Object> getPlayerData(String playerName) {
        Map<String, Object> map = this.players.getPlayerData(playerName);
        map.put("nocheat.version", this.getDescription().getVersion());
        return map;
    }

    public NoCheatPlayer getPlayer(Player player) {
        return this.players.getPlayer(player);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void logEvent(NoCheatLogEvent event) {
        if (event.toConsole()) {
            System.out.println(Colors.removeColors(event.getPrefix() + event.getMessage()));
        }

        if (event.toChat()) {
            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                if (player.hasPermission("nocheat.admin.chatlog")) {
                    player.sendMessage(Colors.replaceColors(event.getPrefix() + event.getMessage()));
                }
            }
        }

        if (event.toFile()) {
            this.fileLogger.info(Colors.removeColors(event.getMessage()));
        }
    }

    public void setFileLogger(Logger logger) {
        this.fileLogger = logger;
    }
}
