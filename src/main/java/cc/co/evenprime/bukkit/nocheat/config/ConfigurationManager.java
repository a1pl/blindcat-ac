package cc.co.evenprime.bukkit.nocheat.config;

import cc.co.evenprime.bukkit.nocheat.NoCheat;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class ConfigurationManager {
    private static final String configFileName = "config.yml";
    private final Map<String, ConfigurationCacheStore> worldnameToConfigCacheMap = new HashMap<>();
    private FileHandler fileHandler;
    private final NoCheat plugin;

    public ConfigurationManager(NoCheat plugin, File rootConfigFolder) {
        this.plugin = plugin;
        this.initializeConfig(rootConfigFolder);
    }

    private void initializeConfig(File rootConfigFolder) {
        NoCheatConfiguration root = new NoCheatConfiguration();
        root.setDefaults(new DefaultConfiguration());
        root.options().copyDefaults(true);
        root.options().copyHeader(true);
        File globalConfigFile = getGlobalConfigFile(rootConfigFolder);
        if (globalConfigFile.exists()) {
            try {
                root.load(globalConfigFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            root.save(globalConfigFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        root.regenerateActionLists();
        this.worldnameToConfigCacheMap.put(null, new ConfigurationCacheStore(root));
        this.plugin.setFileLogger(this.setupFileLogger(new File(rootConfigFolder, root.getString("logging.filename"))));
        Map<String, File> worldFiles = getWorldSpecificConfigFiles(rootConfigFolder);

        for (Entry<String, File> worldEntry : worldFiles.entrySet()) {
            File worldConfigFile = worldEntry.getValue();
            NoCheatConfiguration world = new NoCheatConfiguration();
            world.setDefaults(root);

            try {
                world.load(worldConfigFile);
                this.worldnameToConfigCacheMap.put(worldEntry.getKey(), new ConfigurationCacheStore(world));
                world.save(worldConfigFile);
            } catch (Exception e) {
                System.out.println("NoCheat: Couldn't load world-specific config for " + worldEntry.getKey());
                e.printStackTrace();
            }

            world.regenerateActionLists();
        }
    }

    private static File getGlobalConfigFile(File rootFolder) {
        return new File(rootFolder, "config.yml");
    }

    private static Map<String, File> getWorldSpecificConfigFiles(File rootFolder) {
        HashMap<String, File> files = new HashMap<>();
        if (rootFolder.isDirectory()) {
            for (File f : rootFolder.listFiles()) {
                if (f.isFile()) {
                    String filename = f.getName();
                    if (filename.matches(".+_config.yml$")) {
                        String worldname = filename.substring(0, filename.length() - ("config.yml".length() + 1));
                        files.put(worldname, f);
                    }
                }
            }
        }

        return files;
    }

    private Logger setupFileLogger(File logfile) {
        Logger l = Logger.getAnonymousLogger();
        l.setLevel(Level.INFO);
        l.setUseParentHandlers(false);

        for (Handler h : l.getHandlers()) {
            l.removeHandler(h);
        }

        if (this.fileHandler != null) {
            this.fileHandler.close();
            l.removeHandler(this.fileHandler);
            this.fileHandler = null;
        }

        try {
            try {
                logfile.getParentFile().mkdirs();
            } catch (Exception e) {
                e.printStackTrace();
            }

            this.fileHandler = new FileHandler(logfile.getCanonicalPath(), true);
            this.fileHandler.setLevel(Level.ALL);
            this.fileHandler.setFormatter(new ConfigurationManager.LogFileFormatter());
            l.addHandler(this.fileHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return l;
    }

    public void cleanup() {
        this.fileHandler.flush();
        this.fileHandler.close();
        Logger l = Logger.getLogger("NoCheat");
        l.removeHandler(this.fileHandler);
        this.fileHandler = null;
    }

    public ConfigurationCacheStore getConfigurationCacheForWorld(String worldname) {
        ConfigurationCacheStore cache = this.worldnameToConfigCacheMap.get(worldname);
        if (cache != null) {
            return cache;
        }

        cache = this.worldnameToConfigCacheMap.get(null);
        this.worldnameToConfigCacheMap.put(worldname, cache);
        return cache;
    }

    private static class LogFileFormatter extends Formatter {
        private final SimpleDateFormat date = new SimpleDateFormat("yy.MM.dd HH:mm:ss");

        public LogFileFormatter() {
        }

        @Override
        public String format(LogRecord record) {
            StringBuilder builder = new StringBuilder();
            Throwable ex = record.getThrown();
            builder.append(this.date.format(record.getMillis()));
            builder.append(" [");
            builder.append(record.getLevel().getLocalizedName().toUpperCase());
            builder.append("] ");
            builder.append(record.getMessage());
            builder.append('\n');
            if (ex != null) {
                StringWriter writer = new StringWriter();
                ex.printStackTrace(new PrintWriter(writer));
                builder.append(writer);
            }

            return builder.toString();
        }
    }
}
