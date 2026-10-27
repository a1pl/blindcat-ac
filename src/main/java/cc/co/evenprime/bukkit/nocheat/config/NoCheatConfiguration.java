package cc.co.evenprime.bukkit.nocheat.config;

import cc.co.evenprime.bukkit.nocheat.actions.Action;
import cc.co.evenprime.bukkit.nocheat.actions.types.ActionList;
import java.io.File;
import java.lang.reflect.Field;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.yaml.snakeyaml.DumperOptions;

public class NoCheatConfiguration extends YamlConfiguration {
    private ActionFactory factory;

    @Override
    public String saveToString() {
        try {
            Field op = YamlConfiguration.class.getDeclaredField("yamlOptions");
            op.setAccessible(true);
            DumperOptions options = (DumperOptions)op.get(this);
            options.setWidth(200);
        } catch (Exception var3) {
        }

        String result = super.saveToString();
        return result;
    }

    public static void writeInstructions(File rootConfigFolder) {
    }

    public void regenerateActionLists() {
        this.factory = new ActionFactory(((MemorySection)this.get("strings")).getValues(false));
    }

    public ActionList getActionList(String path, String permission) {
        String value = this.getString(path);
        return this.factory.createActionList(value, permission);
    }

    public void set(String path, ActionList list) {
        StringBuffer string = new StringBuffer();

        for (int treshold : list.getTresholds()) {
            if (treshold > 0) {
                string.append(" vl>").append(treshold);
            }

            for (Action action : list.getActions(treshold)) {
                string.append(" ").append(action);
            }
        }

        this.set(path, string.toString().trim());
    }
}
