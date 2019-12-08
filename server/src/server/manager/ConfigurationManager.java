package server.manager;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.esotericsoftware.minlog.Log;
import server.configs.CharConfiguration;
import server.configs.ServerConfiguration;
import shared.util.AOJson;

/**
 * Configuration Manager
 * It's a singleton class that provides the server configuration objects generated by JSON files
 */
public class ConfigurationManager {

    private static ConfigurationManager manager;

    private ServerConfiguration serverConfiguration;
    private CharConfiguration charConfiguration;

    public ServerConfiguration getServerConfig() {
        return serverConfiguration;
    }

    public CharConfiguration getCharConfig() {
        return charConfiguration;
    }

    private ConfigurationManager() {
        loadServerConfig();
        loadCharsConfig();
    }

    private void loadServerConfig() {
        Json json = new AOJson();

        try {
            // DO NOT USE 'Gdx.Files', because 'Gdx.Files' in the launcher is always NULL!
            serverConfiguration = json.fromJson(ServerConfiguration.class, new FileHandle(ServerConfiguration.PATH));
        } catch (Exception ex) {
            Log.warn("Server Configuration", "File not found! Creating new one with default values...", ex);
            serverConfiguration = new ServerConfiguration();
            serverConfiguration.loadDefaultValues();
            serverConfiguration.save();
        }
    }

    private void loadCharsConfig() {
        Json json = new AOJson();

        try {
            charConfiguration = json.fromJson(CharConfiguration.class, new FileHandle(CharConfiguration.PATH));
        } catch (Exception ex) {
            Log.warn("Char Class Configuration", "File not found! Creating new one with default values...", ex);
            charConfiguration = new CharConfiguration();
            charConfiguration.loadDefaultValues();
            charConfiguration.save();
        }
    }

    public static ConfigurationManager getInstance() {
        if (manager == null) {
            manager = new ConfigurationManager();
        }

        return manager;
    }
}
