package cc.co.evenprime.bukkit.nocheat.checks;

import cc.co.evenprime.bukkit.nocheat.NoCheat;
import cc.co.evenprime.bukkit.nocheat.NoCheatLogEvent;
import cc.co.evenprime.bukkit.nocheat.NoCheatPlayer;
import cc.co.evenprime.bukkit.nocheat.actions.Action;
import cc.co.evenprime.bukkit.nocheat.actions.ParameterName;
import cc.co.evenprime.bukkit.nocheat.actions.types.ActionList;
import cc.co.evenprime.bukkit.nocheat.actions.types.ConsolecommandAction;
import cc.co.evenprime.bukkit.nocheat.actions.types.DummyAction;
import cc.co.evenprime.bukkit.nocheat.actions.types.LogAction;
import cc.co.evenprime.bukkit.nocheat.actions.types.SpecialAction;
import cc.co.evenprime.bukkit.nocheat.config.ConfigurationCacheStore;
import cc.co.evenprime.bukkit.nocheat.data.Statistics;
import java.util.Locale;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandException;

public abstract class Check {
    private final String name;
    private final String groupId;
    protected final NoCheat plugin;

    public Check(NoCheat plugin, String groupId, String name) {
        this.plugin = plugin;
        this.groupId = groupId;
        this.name = name;
    }

    protected final boolean executeActions(NoCheatPlayer player, ActionList actionList, double violationLevel) {
        boolean special = false;
        Action[] actions = actionList.getActions(violationLevel);
        long time = System.currentTimeMillis() / 1000L;
        ConfigurationCacheStore cc = player.getConfigurationStore();

        for (Action ac : actions) {
            if (player.getExecutionHistory().executeAction(this.groupId, ac, time)) {
                if (ac instanceof LogAction && !player.getPlayer().hasPermission(actionList.permissionSilent)) {
                    this.executeLogAction((LogAction)ac, this, player, cc);
                } else if (ac instanceof SpecialAction) {
                    special = true;
                } else if (ac instanceof ConsolecommandAction) {
                    this.executeConsoleCommand((ConsolecommandAction)ac, this, player, cc);
                } else if (ac instanceof DummyAction) {
                }
            }
        }

        return special;
    }

    protected void incrementStatistics(NoCheatPlayer player, Statistics.Id id, double vl) {
        player.getDataStore().getStatistics().increment(id, vl);
    }

    private final void executeLogAction(LogAction l, Check check, NoCheatPlayer player, ConfigurationCacheStore cc) {
        if (cc.logging.active) {
            Bukkit.getServer()
                .getPluginManager()
                .callEvent(
                    new NoCheatLogEvent(
                        cc.logging.prefix,
                        l.getLogMessage(player, check),
                        cc.logging.toConsole && l.toConsole(),
                        cc.logging.toChat && l.toChat(),
                        cc.logging.toFile && l.toFile()
                    )
                );
        }
    }

    private final void executeConsoleCommand(ConsolecommandAction action, Check check, NoCheatPlayer player, ConfigurationCacheStore cc) {
        String command = action.getCommand(player, check);

        try {
            this.plugin.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
        } catch (CommandException e) {
            System.out
                .println("[NoCheat] failed to execute the command '" + command + "': " + e.getMessage() + ", please check if everything is setup correct.");
        } catch (Exception var8) {
        }
    }

    public String getParameter(ParameterName wildcard, NoCheatPlayer player) {
        if (wildcard == ParameterName.PLAYER) {
            return player.getName();
        } else if (wildcard == ParameterName.CHECK) {
            return this.name;
        } else if (wildcard == ParameterName.LOCATION) {
            Location l = player.getPlayer().getLocation();
            return String.format(Locale.US, "%.2f,%.2f,%.2f", l.getX(), l.getY(), l.getZ());
        } else {
            return wildcard == ParameterName.WORLD ? player.getPlayer().getWorld().getName() : "the Author was lazy and forgot to define " + wildcard + ".";
        }
    }
}
