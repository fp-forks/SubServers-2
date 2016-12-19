package net.ME1312.SubServers.Proxy.Event;

import net.ME1312.SubServers.Proxy.Host.Host;
import net.ME1312.SubServers.Proxy.Host.Server;
import net.ME1312.SubServers.Proxy.Library.SubEvent;
import net.md_5.bungee.api.plugin.Cancellable;
import net.md_5.bungee.api.plugin.Event;

import java.util.UUID;

public class SubAddServerEvent extends Event implements SubEvent, Cancellable {
    private boolean cancelled = false;
    private UUID player;
    private Host host;
    private Server server;

    /**
     * Server Add Event
     *
     * @param player Player Adding Server
     * @param server Server Starting
     */
    public SubAddServerEvent(UUID player, Host host, Server server) {
        this.player = player;
        this.host = host;
        this.server = server;
    }

    /**
     * Gets the Server to be Added
     * @return The Server to be Added
     */
    public Server getServer() { return server; }

    /**
     * Gets the Host of the Server
     *
     * @return The Host of the Server or null if isn't a SubServer
     */
    public Host getHost() {
        return host;
    }

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
