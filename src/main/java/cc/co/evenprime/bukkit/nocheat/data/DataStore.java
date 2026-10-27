package cc.co.evenprime.bukkit.nocheat.data;

import cc.co.evenprime.bukkit.nocheat.DataItem;
import java.util.HashMap;
import java.util.Map;

public class DataStore {
    private final Map<String, DataItem> dataMap = new HashMap<>();
    private final Statistics statistics = new Statistics();
    private final long timestamp = System.currentTimeMillis();

    public <T extends DataItem> T get(String id) {
        return (T)this.dataMap.get(id);
    }

    public void set(String id, DataItem data) {
        this.dataMap.put(id, data);
    }

    public Map<String, Object> collectData() {
        Map<String, Object> map = this.statistics.get();
        map.put("nocheat.starttime", this.timestamp);
        map.put("nocheat.endtime", System.currentTimeMillis());
        return map;
    }

    public Statistics getStatistics() {
        return this.statistics;
    }
}
