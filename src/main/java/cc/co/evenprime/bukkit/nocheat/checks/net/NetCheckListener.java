package cc.co.evenprime.bukkit.nocheat.checks.net;

import cc.co.evenprime.bukkit.nocheat.EventManager;
import cc.co.evenprime.bukkit.nocheat.NoCheat;
import cc.co.evenprime.bukkit.nocheat.NoCheatPlayer;
import cc.co.evenprime.bukkit.nocheat.config.ConfigurationCacheStore;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.PacketType.Play.Client;
import com.comphenix.protocol.events.ListenerOptions;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import java.util.LinkedList;
import java.util.List;

public class NetCheckListener extends PacketAdapter implements EventManager {
    private final FlyingFrequencyCheck flyingFrequencyCheck;
    private final KeepAliveFrequencyCheck keepAliveFrequencyCheck;
    private final AttackFrequencyCheck attackFrequencyCheck;
    private final GroundSpoofCheck groundSpoofCheck;
    private final NoCheat plugin;

    public NetCheckListener(NoCheat plugin) {
        super(
            PacketAdapter.params(plugin, Client.POSITION, Client.POSITION_LOOK, Client.LOOK, Client.KEEP_ALIVE, Client.USE_ENTITY)
                .listenerPriority(ListenerPriority.NORMAL)
                .options(ListenerOptions.SYNC)
        );
        this.flyingFrequencyCheck = new FlyingFrequencyCheck(plugin);
        this.keepAliveFrequencyCheck = new KeepAliveFrequencyCheck(plugin);
        this.attackFrequencyCheck = new AttackFrequencyCheck(plugin);
        this.groundSpoofCheck = new GroundSpoofCheck(plugin);
        this.plugin = plugin;
    }

    public void register() {
        ProtocolLibrary.getProtocolManager().addPacketListener(this);
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        if (event.getPlayer() != null) {
            NoCheatPlayer player = this.plugin.getPlayer(event.getPlayer());
            NetConfig cc = NetCheck.getConfig(player);
            if (event.getPacketType() == Client.KEEP_ALIVE) {
                if (cc.keepAliveFrequencyCheck && !player.hasPermission("nocheat.checks.net.keepalivefrequency")) {
                    this.keepAliveFrequencyCheck.check(player, NetCheck.getData(player), cc);
                }
            } else if (event.getPacketType() == Client.USE_ENTITY) {
                if (cc.attackFrequencyCheck && !player.hasPermission("nocheat.checks.net.attackfrequency")) {
                    this.attackFrequencyCheck.check(player, NetCheck.getData(player), cc);
                }
            } else {
                NetData data = NetCheck.getData(player);
                if (cc.flyingFrequencyCheck && !player.hasPermission("nocheat.checks.net.flyingfrequency")) {
                    this.flyingFrequencyCheck.check(player, data, cc);
                }

                if (cc.groundSpoofCheck && !player.hasPermission("nocheat.checks.net.groundspoof")) {
                    boolean packetOnGround = event.getPacket().getBooleans().optionRead(0).orElse(Boolean.FALSE);
                    this.groundSpoofCheck.check(player, data, cc, packetOnGround);
                }
            }
        }
    }

    @Override
    public List<String> getActiveChecks(ConfigurationCacheStore cc) {
        LinkedList<String> s = new LinkedList<>();
        NetConfig n = NetCheck.getConfig(cc);
        if (n.flyingFrequencyCheck) {
            s.add("net.flyingfrequency");
        }

        if (n.keepAliveFrequencyCheck) {
            s.add("net.keepalivefrequency");
        }

        if (n.attackFrequencyCheck) {
            s.add("net.attackfrequency");
        }

        if (n.groundSpoofCheck) {
            s.add("net.groundspoof");
        }

        return s;
    }
}
