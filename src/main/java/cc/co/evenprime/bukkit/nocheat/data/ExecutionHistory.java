package cc.co.evenprime.bukkit.nocheat.data;

import cc.co.evenprime.bukkit.nocheat.actions.Action;
import java.util.HashMap;
import java.util.Map;

public class ExecutionHistory {
    private final Map<String, Map<Action, ExecutionHistory.ExecutionHistoryEntry>> executionHistories = new HashMap<>();

    public boolean executeAction(String check, Action action, long time) {
        Map<Action, ExecutionHistory.ExecutionHistoryEntry> executionHistory = this.executionHistories.get(check);
        if (executionHistory == null) {
            executionHistory = new HashMap<>();
            this.executionHistories.put(check, executionHistory);
        }

        ExecutionHistory.ExecutionHistoryEntry entry = executionHistory.get(action);
        if (entry == null) {
            entry = new ExecutionHistory.ExecutionHistoryEntry(60);
            executionHistory.put(action, entry);
        }

        entry.addCounter(time);
        if (entry.getCounter() > action.delay && entry.getLastExecution() <= time - action.repeat) {
            entry.setLastExecution(time);
            return true;
        } else {
            return false;
        }
    }

    private static class ExecutionHistoryEntry {
        private final int[] executionTimes;
        private long lastExecution = 0L;
        private int totalEntries = 0;
        private long lastClearedTime = 0L;

        private ExecutionHistoryEntry(int monitoredTimeFrame) {
            this.executionTimes = new int[monitoredTimeFrame];
        }

        private void addCounter(long time) {
            if (time - this.lastClearedTime > 0L) {
                this.clearTimes(this.lastClearedTime + 1L, time - this.lastClearedTime);
                this.lastClearedTime = time + 1L;
            }

            this.executionTimes[(int)(time % this.executionTimes.length)]++;
            this.totalEntries++;
        }

        private void clearTimes(long start, long length) {
            if (length > 0L) {
                if (length > this.executionTimes.length) {
                    length = this.executionTimes.length;
                }

                int j = (int)start % this.executionTimes.length;

                for (int i = 0; i < length; i++) {
                    if (j == this.executionTimes.length) {
                        j = 0;
                    }

                    this.totalEntries = this.totalEntries - this.executionTimes[j];
                    this.executionTimes[j] = 0;
                    j++;
                }
            }
        }

        public int getCounter() {
            return this.totalEntries;
        }

        public long getLastExecution() {
            return this.lastExecution;
        }

        public void setLastExecution(long time) {
            this.lastExecution = time;
        }
    }
}
