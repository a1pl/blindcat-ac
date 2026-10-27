package cc.co.evenprime.bukkit.nocheat.actions.types;

import cc.co.evenprime.bukkit.nocheat.NoCheatPlayer;
import cc.co.evenprime.bukkit.nocheat.actions.Action;
import cc.co.evenprime.bukkit.nocheat.actions.ParameterName;
import cc.co.evenprime.bukkit.nocheat.checks.Check;
import java.util.ArrayList;

public abstract class ActionWithParameters extends Action {
    private final ArrayList<Object> messageParts = new ArrayList<>();

    public ActionWithParameters(String name, int delay, int repeat, String message) {
        super(name, delay, repeat);
        this.parseMessage(message);
    }

    private void parseMessage(String message) {
        String[] parts = message.split("\\[", 2);
        if (parts.length != 2) {
            this.messageParts.add(message);
        } else {
            String[] parts2 = parts[1].split("\\]", 2);
            if (parts2.length != 2) {
                this.messageParts.add(message);
            } else {
                ParameterName w = ParameterName.get(parts2[0]);
                if (w != null) {
                    this.messageParts.add(parts[0]);
                    this.messageParts.add(w);
                    this.parseMessage(parts2[1]);
                } else {
                    this.messageParts.add(message);
                }
            }
        }
    }

    protected String getMessage(NoCheatPlayer player, Check check) {
        StringBuilder log = new StringBuilder(100);

        for (Object part : this.messageParts) {
            if (part instanceof String) {
                log.append((String)part);
            } else {
                log.append(check.getParameter((ParameterName)part, player));
            }
        }

        return log.toString();
    }
}
