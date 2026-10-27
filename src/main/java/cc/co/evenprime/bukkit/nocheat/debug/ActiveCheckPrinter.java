package cc.co.evenprime.bukkit.nocheat.debug;

import cc.co.evenprime.bukkit.nocheat.EventManager;
import cc.co.evenprime.bukkit.nocheat.NoCheat;
import cc.co.evenprime.bukkit.nocheat.config.ConfigurationCacheStore;
import java.util.List;
import org.bukkit.World;

public class ActiveCheckPrinter {
    public static void printActiveChecks(NoCheat plugin, List<EventManager> eventManagers) {
        boolean introPrinted = false;
        String intro = "[NoCheat] Active Checks: ";

        for (World world : plugin.getServer().getWorlds()) {
            StringBuilder line = new StringBuilder("  ").append(world.getName()).append(": ");
            int length = line.length();
            ConfigurationCacheStore cc = plugin.getConfig(world);
            if (cc.logging.showactivechecks) {
                for (EventManager em : eventManagers) {
                    if (em.getActiveChecks(cc).size() != 0) {
                        for (String active : em.getActiveChecks(cc)) {
                            line.append(active).append(' ');
                        }

                        if (!introPrinted) {
                            System.out.println(intro);
                            introPrinted = true;
                        }

                        System.out.println(line.toString());
                        line = new StringBuilder(length);

                        for (int i = 0; i < length; i++) {
                            line.append(' ');
                        }
                    }
                }
            }
        }
    }
}
