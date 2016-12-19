package net.ME1312.SubServers.Proxy.Event;

import net.ME1312.SubServers.Proxy.Host.SubServer;
import net.ME1312.SubServers.Proxy.Library.SubEvent;
import net.md_5.bungee.api.plugin.Cancellable;
import net.md_5.bungee.api.plugin.Event;

import java.util.UUID;

public class SubStartEvent extends Event implements SubEvent, Cancellable {
    private boolean cancelled = false;
    private UUID player;
    private SubServer server;

    /**
     * Server Start Event
     *
     * @param player Player Starting Server
     * @param server Server Starting
     */
    public SubStartEvent(UUID player, SubServer server) {
        this.player = player;
        this.server = server;
    }

    /**
     * Gets the Server Effected
     * @return The Server Effected
     */
    public SubServer getServer() { return server; }

    /**
     * Gets the player that triggered the Event
     * @return The Player that triggered this Event or null if Console
     */
    public UUID getPlayer() { return player; }

    /**
     * Gets the Cancelled Status
     * @return Cancelled Status
     */
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Sets the Cancelled Status
     */
    public void setCancelled(boolean value) {
        cancelled = value;
    }
}
