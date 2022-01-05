package com.buuz135.raidmeter.network;

import com.buuz135.raidmeter.storage.ClientRaidMeterData;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class RaidMeterSyncMessage implements IMessage {

    private CompoundTag sync;

    public RaidMeterSyncMessage() {
    }

    public RaidMeterSyncMessage(CompoundTag sync) {
        this.sync = sync;
    }

    @Override
    public RaidMeterSyncMessage fromBytes(FriendlyByteBuf packetBuffer) {
        sync = packetBuffer.readNbt();
        return this;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        FriendlyByteBuf packetBuffer = new FriendlyByteBuf(buf);
        packetBuffer.writeNbt(sync);
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> {
            ClientRaidMeterData.DATA.deserialize(sync);
        });
        contextSupplier.get().setPacketHandled(true);
    }
}
