package src.net;

import src.net.packets.Packet;

public interface PacketListener {
    void receivedPacket(Packet.Types type);
}
