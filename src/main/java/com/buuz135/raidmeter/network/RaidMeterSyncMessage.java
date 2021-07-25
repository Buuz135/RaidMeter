package com.buuz135.raidmeter.network;

import com.buuz135.raidmeter.storage.ClientRaidMeterData;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class RaidMeterSyncMessage implements IMessage {

    private CompoundNBT sync;

    public RaidMeterSyncMessage() {
    }

    public RaidMeterSyncMessage(CompoundNBT sync) {
        this.sync = sync;
    }

    @Override
    public RaidMeterSyncMessage fromBytes(ByteBuf buf) {
        PacketBuffer packetBuffer = new PacketBuffer(buf);
        sync = packetBuffer.readCompoundTag();
        return this;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        PacketBuffer packetBuffer = new PacketBuffer(buf);
        packetBuffer.writeCompoundTag(sync);
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> {
            ClientRaidMeterData.DATA.deserialize(sync);
        });
        contextSupplier.get().setPacketHandled(true);
    }
}
