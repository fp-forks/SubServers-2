package net.ME1312.SubServers.Bungee.Library.Config;

import org.json.JSONObject;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

/**
 * YAML Config Class
 */
@SuppressWarnings("unused")
public class YAMLConfig {
    private File file;
    private Yaml yaml;
    private YAMLSection config;

    /**
     * Creates/Loads a YAML Formatted Config
     *
     * @param file
     * @throws IOException
     * @throws YAMLException
     */
    @SuppressWarnings("unchecked")
    public YAMLConfig(File file) throws IOException, YAMLException {
        if (file.exists()) {
            this.config = new YAMLSection((Map<String, ?>) (this.yaml = new Yaml(getDumperOptions())).load(new FileInputStream(this.file = file)), null, null, yaml);
        } else {
            this.config = new YAMLSection(null, null, null, yaml);
        }
    }

    /**
     * Get Config Contents
     *
     * @return Config Contents
     */
    public YAMLSection get() {
        return config;
    }

    /**
     * Set Config Contents
     *
     * @param value Value
     */
    public void set(YAMLSection value) {
        config = value;
    }

    /**
     * Reload Config Contents
     *
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public void reload() throws IOException {
        config = new YAMLSection((Map<String, Object>) yaml.load(new FileInputStream(file)), null, null, yaml);
    }

    /**
     * Save Config Contents
     *
     * @throws IOException
     */
    public void save() throws IOException {
        FileWriter writer = new FileWriter(file);
        yaml.dump(config.map, writer);
        writer.close();
    }

    @Override
    public String toString() {
        return yaml.dump(config.map);
    }

    /**
     * Converts Config Contents to JSON
     *
     * @return JSON Formatted Config Contents
     */
    public JSONObject toJSON() {
        return new JSONObject(config.map);
    }

    protected static DumperOptions getDumperOptions() {
        DumperOptions options = new DumperOptions();
        options.setAllowUnicode(true);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setIndent(4);

        return options;
    }
}