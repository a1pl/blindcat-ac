package cc.co.evenprime.bukkit.nocheat.command;

import cc.co.evenprime.bukkit.nocheat.NoCheat;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

public class CommandHandler {
    private final List<Permission> perms;

    public CommandHandler(NoCheat plugin) {
        this.perms = new LinkedList<>(plugin.getDescription().getPermissions());
        Collections.sort(this.perms, new Comparator<Permission>() {
            public int compare(Permission o1, Permission o2) {
                String name1 = o1.getName();
                String name2 = o2.getName();
                if (name1.equals(name2)) {
                    return 0;
                } else if (name1.startsWith(name2)) {
                    return 1;
                } else {
                    return name2.startsWith(name1) ? -1 : name1.compareTo(name2);
                }
            }
        });
    }

    public boolean handleCommand(NoCheat plugin, CommandSender sender, Command command, String label, String[] args) {
        boolean result = false;
        if (command.getName().equalsIgnoreCase("nocheat") && args.length != 0) {
            if (args[0].equalsIgnoreCase("permlist") && args.length >= 2) {
                result = this.handlePermlistCommand(plugin, sender, args);
            } else if (args[0].equalsIgnoreCase("reload")) {
                result = this.handleReloadCommand(plugin, sender);
            } else if (args[0].equalsIgnoreCase("playerinfo") && args.length >= 2) {
                result = this.handlePlayerInfoCommand(plugin, sender, args);
            }
        } else {
            result = false;
        }

        return result;
    }

    private boolean handlePlayerInfoCommand(NoCheat plugin, CommandSender sender, String[] args) {
        Map<String, Object> map = plugin.getPlayerData(args[1]);
        String filter = "";
        if (args.length > 2) {
            filter = args[2];
        }

        sender.sendMessage("PlayerInfo for " + args[1]);

        for (Entry<String, Object> entry : map.entrySet()) {
            if (entry.getKey().contains(filter)) {
                sender.sendMessage(entry.getKey() + ": " + entry.getValue());
            }
        }

        return true;
    }

    private boolean handlePermlistCommand(NoCheat plugin, CommandSender sender, String[] args) {
        Player player = plugin.getServer().getPlayerExact(args[1]);
        if (player == null) {
            sender.sendMessage("Unknown player: " + args[1]);
            return true;
        }

        String prefix = "";
        if (args.length == 3) {
            prefix = args[2];
        }

        sender.sendMessage("Player " + player.getName() + " has the permission(s):");

        for (Permission permission : this.perms) {
            if (permission.getName().startsWith(prefix)) {
                sender.sendMessage(permission.getName() + ": " + player.hasPermission(permission));
            }
        }

        return true;
    }

    private boolean handleReloadCommand(NoCheat plugin, CommandSender sender) {
        if (sender instanceof Player && !sender.hasPermission("nocheat.admin.reload")) {
            sender.sendMessage("You lack the nocheat.admin.reload permission to use 'reload'");
        } else {
            sender.sendMessage("[NoCheat] Reloading configuration");
            plugin.reloadConfiguration();
            sender.sendMessage("[NoCheat] Configuration reloaded");
        }

        return true;
    }
}
