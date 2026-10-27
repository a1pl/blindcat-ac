package cc.co.evenprime.bukkit.nocheat.actions.types;

import cc.co.evenprime.bukkit.nocheat.NoCheatPlayer;
import cc.co.evenprime.bukkit.nocheat.checks.Check;

public class ConsolecommandAction extends ActionWithParameters {
    public ConsolecommandAction(String name, int delay, int repeat, String command) {
        super(name, delay, repeat, command);
    }

    public String getCommand(NoCheatPlayer player, Check check) {
        return super.getMessage(player, check);
    }

    @Override
    public String toString() {
        return "cmd:" + this.name + ":" + this.delay + ":" + this.repeat;
    }
}
