package studio.trc.bukkit.litesignin.config;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import studio.trc.bukkit.litesignin.Main;
import studio.trc.bukkit.litesignin.util.SignInPluginProperties;

public class DefaultConfigurationFile
{
    private final static Map<ConfigurationType, FileConfiguration> cacheDefaultConfig = new HashMap();
    private final static Map<ConfigurationType, Boolean> isDefaultConfigLoaded = new HashMap();
    
    public static FileConfiguration getDefaultConfig(ConfigurationType type) {
        if (!isDefaultConfigLoaded.containsKey(type) || !isDefaultConfigLoaded.get(type)) {
            loadDefaultConfigurationFile(type);
            isDefaultConfigLoaded.put(type, true);
        }
        return cacheDefaultConfig.get(type);
    }
    
    public static void loadDefaultConfigurationFile(ConfigurationType type) {
        Locale lang = SignInPluginProperties.lang;
        String jarPath = "English";
        if (lang.equals(Locale.SIMPLIFIED_CHINESE) || lang.equals(Locale.CHINESE)) {
            jarPath = "Chinese";
        }
        String fileName = type.getFileName();
        if (type.equals(ConfigurationType.GUISETTINGS)) {
            String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
            if (version.startsWith("v1_7") || version.startsWith("v1_8") || version.startsWith("v1_9") || version.startsWith("v1_10") || version.startsWith("v1_11") || version.startsWith("v1_12")) {
                fileName = "GUISettings-OLDVERSION.yml";
            } else {
                fileName = "GUISettings-NEWVERSION.yml";
            }
        }
        try (Reader Config = new InputStreamReader(Main.getInstance().getClass().getResource("/Languages/" + jarPath + "/" + fileName).openStream(), "UTF-8")) {
            FileConfiguration configFile = new YamlConfiguration();
            configFile.load(Config);
            cacheDefaultConfig.put(type, configFile);
        } catch (IOException | InvalidConfigurationException ex) {
            ex.printStackTrace();
        }
    }
}
