package cc.co.evenprime.bukkit.nocheat.data;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

public class Statistics {
    private final Map<Statistics.Id, Double> statisticVLs = new HashMap<>(Statistics.Id.values().length);
    private final Map<Statistics.Id, Integer> statisticFails = new HashMap<>(Statistics.Id.values().length);

    public Statistics() {
        for (Statistics.Id id : Statistics.Id.values()) {
            this.statisticVLs.put(id, 0.0);
            this.statisticFails.put(id, 0);
        }
    }

    public void increment(Statistics.Id id, double vl) {
        Double stored = this.statisticVLs.get(id);
        if (stored == null) {
            stored = 0.0;
        }

        this.statisticVLs.put(id, stored + vl);
        Integer failed = this.statisticFails.get(id);
        if (failed == null) {
            failed = 0;
        }

        this.statisticFails.put(id, failed + 1);
    }

    public Map<String, Object> get() {
        Map<String, Object> map = new TreeMap<>();

        for (Entry<Statistics.Id, Double> entry : this.statisticVLs.entrySet()) {
            map.put(entry.getKey().toString() + ".vl", entry.getValue().intValue());
        }

        for (Entry<Statistics.Id, Integer> entry : this.statisticFails.entrySet()) {
            map.put(entry.getKey().toString() + ".failed", entry.getValue());
        }

        return map;
    }

    public enum Id {
        BB_DIRECTION("blockbreak.direction"),
        BB_NOSWING("blockbreak.noswing"),
        BB_REACH("blockbreak.reach"),
        BB_FASTBREAK("blockbreak.fastbreak"),
        BP_DIRECTION("blockplace.direction"),
        BP_REACH("blockplace.reach"),
        BP_FASTPLACE("blockplace.fastplace"),
        CHAT_COLOR("chat.color"),
        CHAT_SPAM("chat.spam"),
        FI_DIRECTION("fight.direction"),
        FI_NOSWING("fight.noswing"),
        FI_REACH("fight.reach"),
        FI_SPEED("fight.speed"),
        FI_VELOCITY("fight.velocity"),
        FI_ANGLE("fight.angle"),
        FI_CRITICAL("fight.critical"),
        FI_GODMODE("fight.godmode"),
        FI_SELFHIT("fight.selfhit"),
        FI_FASTHEAL("fight.fastheal"),
        NET_FLYINGFREQUENCY("net.flyingfrequency"),
        NET_KEEPALIVEFREQUENCY("net.keepalivefrequency"),
        NET_ATTACKFREQUENCY("net.attackfrequency"),
        COMBINED_IMPROBABLE("combined.improbable"),
        NET_GROUNDSPOOF("net.groundspoof"),
        INV_DROP("inventory.drop"),
        INV_BOW("inventory.instantbow"),
        INV_EAT("inventory.instanteat"),
        MOV_RUNNING("moving.running"),
        MOV_FLYING("moving.flying"),
        MOV_MOREPACKETS("moving.morepackets"),
        MOV_NOFALL("moving.nofall"),
        MOV_SNEAKING("moving.sneaking"),
        MOV_SWIMMING("moving.swimming"),
        MOV_WATERWALK("moving.waterwalk"),
        MOV_PASSABLE("moving.passable");

        private final String name;

        Id(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }
}
