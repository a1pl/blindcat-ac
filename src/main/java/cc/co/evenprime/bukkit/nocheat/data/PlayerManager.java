package cc.co.evenprime.bukkit.nocheat.data;

import cc.co.evenprime.bukkit.nocheat.NoCheat;
import cc.co.evenprime.bukkit.nocheat.NoCheatPlayer;
import cc.co.evenprime.bukkit.nocheat.player.NoCheatPlayerImpl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.bukkit.entity.Player;

public class PlayerManager {
    private final Map<String, NoCheatPlayerImpl> players = new HashMap<>();
    private final NoCheat plugin;

    public PlayerManager(NoCheat plugin) {
        this.plugin = plugin;
    }

    public NoCheatPlayer getPlayer(Player player) {
        NoCheatPlayerImpl p = this.players.get(player.getName().toLowerCase());
        if (p == null) {
            p = new NoCheatPlayerImpl(player, this.plugin);
            this.players.put(player.getName().toLowerCase(), p);
        }

        p.setLastUsedTime(System.currentTimeMillis());
        p.refresh(player);
        return p;
    }

    public void cleanDataMap() {
        long time = System.currentTimeMillis();
        List<String> removals = new ArrayList<>(5);

        for (Entry<String, NoCheatPlayerImpl> e : this.players.entrySet()) {
            if (e.getValue().shouldBeRemoved(time)) {
                removals.add(e.getKey());
            }
        }

        for (String key : removals) {
            this.players.remove(key);
        }
    }

    public Map<String, Object> getPlayerData(String playerName) {
        NoCheatPlayer player = this.players.get(playerName.toLowerCase());
        return player != null ? player.getDataStore().collectData() : new HashMap<>();
    }
}
