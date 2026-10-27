package cc.co.evenprime.bukkit.nocheat.actions;

public abstract class Action {
    public final int delay;
    public final int repeat;
    public final String name;

    public Action(String name, int delay, int repeat) {
        this.name = name;
        this.delay = delay;
        this.repeat = repeat;
    }
}
