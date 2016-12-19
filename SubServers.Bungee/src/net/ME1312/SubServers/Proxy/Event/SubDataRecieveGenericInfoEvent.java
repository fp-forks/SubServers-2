package net.ME1312.SubServers.Proxy.Event;

import net.ME1312.SubServers.Proxy.Library.SubEvent;
import net.ME1312.SubServers.Proxy.Library.Version.Version;
import net.md_5.bungee.api.plugin.Event;
import org.json.JSONObject;

public class SubDataRecieveGenericInfoEvent extends Event implements SubEvent{
    private String handle;
    private Version version;
    private JSONObject content;

    /**
     * SubData Generic Info Event
     *
     * @param handle Content Handle
     * @param version Content Version
     * @param content Content
     */
    public SubDataRecieveGenericInfoEvent(String handle, Version version, JSONObject content) {
        this.handle = handle;
        this.version = version;
        this.content = content;
    }

    /**
     * Get Content Handle
     *
     * @return Content Handle
     */
    public String getHandle() {
        return handle;
    }

    /**
     * Get Content Version
     *
     * @return Content Version
     */
    public Version getVersion() {
        return version;
    }

    /**
     * Get Content
     *
     * @return Content
     */
    public JSONObject getContent() {
        return content;
    }
}
