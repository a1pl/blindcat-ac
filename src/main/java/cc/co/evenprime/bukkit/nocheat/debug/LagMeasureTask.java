package cc.co.evenprime.bukkit.nocheat.debug;

import cc.co.evenprime.bukkit.nocheat.NoCheat;
import org.bukkit.World;

public class LagMeasureTask implements Runnable {
    private int ingameseconds = 1;
    private long lastIngamesecondTime = System.currentTimeMillis();
    private long lastIngamesecondDuration = 2000L;
    private boolean skipCheck = false;
    private int lagMeasureTaskId = -1;
    private final NoCheat plugin;

    public LagMeasureTask(NoCheat plugin) {
        this.plugin = plugin;
    }

    public void start() {
        this.lagMeasureTaskId = this.plugin.getServer().getScheduler().scheduleSyncRepeatingTask(this.plugin, this, 20L, 20L);
    }

    @Override
    public void run() {
        try {
            boolean oldStatus = this.skipCheck;
            this.skipCheck = this.lastIngamesecondDuration > 2000L;
            if (this.plugin.getConfig((World)null).logging.debugmessages) {
                if (oldStatus != this.skipCheck && this.skipCheck) {
                    System.out.println("[NoCheat] detected server lag, some checks will not work.");
                } else if (oldStatus != this.skipCheck && !this.skipCheck) {
                    System.out.println("[NoCheat] server lag seems to have stopped, reenabling checks.");
                }
            }

            long time = System.currentTimeMillis();
            this.lastIngamesecondDuration = time - this.lastIngamesecondTime;
            if (this.lastIngamesecondDuration < 1000L) {
                this.lastIngamesecondDuration = 1000L;
            } else if (this.lastIngamesecondDuration > 3600000L) {
                this.lastIngamesecondDuration = 3600000L;
            }

            this.lastIngamesecondTime = time;
            this.ingameseconds++;
            if (this.ingameseconds % 62 == 0) {
                this.plugin.cleanDataMap();
            }
        } catch (Exception var4) {
        }
    }

    public void cancel() {
        if (this.lagMeasureTaskId != -1) {
            try {
                this.plugin.getServer().getScheduler().cancelTask(this.lagMeasureTaskId);
            } catch (Exception e) {
                System.out.println("NoCheat: Couldn't cancel LagMeasureTask: " + e.getMessage());
            }

            this.lagMeasureTaskId = -1;
        }
    }

    public boolean skipCheck() {
        return this.skipCheck;
    }
}
