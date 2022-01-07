package com.buuz135.raidmeter.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public interface IMessage {

    RaidMeterSyncMessage fromBytes(FriendlyByteBuf packetBuffer);

    void toBytes(ByteBuf buf);

    void handle(Supplier<NetworkEvent.Context> contextSupplier);
}
