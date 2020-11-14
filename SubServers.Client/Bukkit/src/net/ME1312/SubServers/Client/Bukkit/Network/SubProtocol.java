package net.ME1312.SubServers.Client.Bukkit.Network;

import net.ME1312.Galaxi.Library.Callback.Callback;
import net.ME1312.Galaxi.Library.Container.Pair;
import net.ME1312.Galaxi.Library.Map.ObjectMap;
import net.ME1312.Galaxi.Library.Util;
import net.ME1312.Galaxi.Library.Version.Version;
import net.ME1312.SubData.Client.SubDataClient;
import net.ME1312.SubData.Client.SubDataProtocol;
import net.ME1312.SubServers.Client.Bukkit.Event.SubNetworkConnectEvent;
import net.ME1312.SubServers.Client.Bukkit.Event.SubNetworkDisconnectEvent;
import net.ME1312.SubServers.Client.Bukkit.Network.Packet.*;
import net.ME1312.SubServers.Client.Bukkit.SubAPI;
import net.ME1312.SubServers.Client.Bukkit.SubPlugin;
import net.ME1312.SubServers.Client.Common.Network.Packet.*;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * SubServers Protocol Class
 */
public class SubProtocol extends SubDataProtocol {
    private static SubProtocol instance;

    @SuppressWarnings("deprecation")
    protected SubProtocol() {
        SubPlugin plugin = SubAPI.getInstance().getInternals();

        setName("SubServers 2");
        addVersion(new Version("2.16a+"));


        // 00-0F: Object Link Packets
        registerPacket(0x0002, PacketLinkServer.class);
        registerPacket(0x0002, new PacketLinkServer(plugin));


        // 10-2F: Download Packets
        registerPacket(0x0010, PacketDownloadLang.class);
        registerPacket(0x0011, PacketDownloadPlatformInfo.class);
        registerPacket(0x0012, PacketDownloadProxyInfo.class);
        registerPacket(0x0013, PacketDownloadHostInfo.class);
        registerPacket(0x0014, PacketDownloadGroupInfo.class);
        registerPacket(0x0015, PacketDownloadServerInfo.class);
        registerPacket(0x0016, PacketDownloadPlayerInfo.class);
        registerPacket(0x0017, PacketCheckPermission.class);
        registerPacket(0x0018, PacketCheckPermissionResponse.class);

        registerPacket(0x0010, new PacketDownloadLang(plugin));
        registerPacket(0x0011, new PacketDownloadPlatformInfo());
        registerPacket(0x0012, new PacketDownloadProxyInfo());
        registerPacket(0x0013, new PacketDownloadHostInfo());
        registerPacket(0x0014, new PacketDownloadGroupInfo());
        registerPacket(0x0015, new PacketDownloadServerInfo());
        registerPacket(0x0016, new PacketDownloadPlayerInfo());
        registerPacket(0x0017, new PacketCheckPermission());
        registerPacket(0x0018, new PacketCheckPermissionResponse());


        // 30-4F: Control Packets
        registerPacket(0x0030, PacketCreateServer.class);
        registerPacket(0x0031, PacketAddServer.class);
        registerPacket(0x0032, PacketStartServer.class);
        registerPacket(0x0033, PacketUpdateServer.class);
        registerPacket(0x0034, PacketEditServer.class);
        registerPacket(0x0035, PacketRestartServer.class);
        registerPacket(0x0036, PacketCommandServer.class);
        registerPacket(0x0037, PacketStopServer.class);
        registerPacket(0x0038, PacketRemoveServer.class);
        registerPacket(0x0039, PacketDeleteServer.class);

        registerPacket(0x0030, new PacketCreateServer());
        registerPacket(0x0031, new PacketAddServer());
        registerPacket(0x0032, new PacketStartServer());
        registerPacket(0x0033, new PacketUpdateServer());
        registerPacket(0x0034, new PacketEditServer());
        registerPacket(0x0035, new PacketRestartServer());
        registerPacket(0x0036, new PacketCommandServer());
        registerPacket(0x0037, new PacketStopServer());
        registerPacket(0x0038, new PacketRemoveServer());
        registerPacket(0x0039, new PacketDeleteServer());


        // 70-7F: External Misc Packets
      //registerPacket(0x0070, PacketInExRunEvent.class);
      //registerPacket(0x0071, PacketInExReset.class);
      //registerPacket(0x0072, PacketInExReload.class);

        registerPacket(0x0070, new PacketInExRunEvent(plugin));
        registerPacket(0x0071, new PacketInExReset());
        registerPacket(0x0072, new PacketInExReload(plugin));
    }

    public static SubProtocol get() {
        if (instance == null)
            instance = new SubProtocol();

        return instance;
    }

    @SuppressWarnings("deprecation")
    private Logger getLogger(int channel) {
        SubPlugin plugin = SubAPI.getInstance().getInternals();
        Logger log = Logger.getAnonymousLogger();
        log.setUseParentHandlers(false);
        log.addHandler(new Handler() {
            private boolean open = true;
            private String prefix = "SubData" + ((channel != 0)? "/Sub-"+channel:"");

            @Override
            public void publish(LogRecord record) {
                if (open) {
                    if (plugin.isEnabled()) {
                        Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getLogger().log(record.getLevel(), prefix + " > " + record.getMessage(), record.getParameters()));
                    } else {
                        Bukkit.getLogger().log(record.getLevel(), prefix + " > " + record.getMessage(), record.getParameters());
                    }
                }
            }

            @Override
            public void flush() {

            }

            @Override
            public void close() throws SecurityException {
                open = false;
            }
        });

        return log;
    }

    @Override
    protected SubDataClient sub(Callback<Runnable> scheduler, Logger logger, InetAddress address, int port, ObjectMap<?> login) throws IOException {
        SubPlugin plugin = SubAPI.getInstance().getInternals();
        HashMap<Integer, SubDataClient> map = Util.getDespiteException(() -> Util.reflect(SubPlugin.class.getDeclaredField("subdata"), plugin), null);

        int channel = 1;
        while (map.keySet().contains(channel)) channel++;
        final int fc = channel;

        SubDataClient subdata = super.open(scheduler, getLogger(fc), address, port, login);
        map.put(fc, subdata);
        subdata.sendPacket(new PacketLinkServer(plugin, fc));
        subdata.on.closed(client -> map.remove(fc));

        return subdata;
    }

    @SuppressWarnings("deprecation")
    @Override
    public SubDataClient open(Callback<Runnable> scheduler, Logger logger, InetAddress address, int port) throws IOException {
        SubPlugin plugin = SubAPI.getInstance().getInternals();
        SubDataClient subdata = super.open(scheduler, logger, address, port);
        HashMap<Integer, SubDataClient> map = Util.getDespiteException(() -> Util.reflect(SubPlugin.class.getDeclaredField("subdata"), plugin), null);

        subdata.sendPacket(new PacketLinkServer(plugin, 0));
        subdata.sendPacket(new PacketDownloadLang());
        subdata.on.ready(client -> Bukkit.getPluginManager().callEvent(new SubNetworkConnectEvent((SubDataClient) client)));
        subdata.on.closed(client -> {
            SubNetworkDisconnectEvent event = new SubNetworkDisconnectEvent(client.value(), client.key());

            if (plugin.isEnabled()) {
                Bukkit.getPluginManager().callEvent(event);
                Util.isException(() -> Util.reflect(SubPlugin.class.getDeclaredMethod("connect", Pair.class), plugin, client));
            } else map.put(0, null);
        });

        return subdata;
    }

    @SuppressWarnings("deprecation")
    @Override
    public SubDataClient open(Logger logger, InetAddress address, int port) throws IOException {
        SubPlugin plugin = SubAPI.getInstance().getInternals();
        return open(event -> {
            if (plugin.isEnabled()) Bukkit.getScheduler().runTask(plugin, event);
            else event.run();
        }, logger, address, port);
    }

    public SubDataClient open(InetAddress address, int port) throws IOException {
        return open(getLogger(0), address, port);
    }
}
