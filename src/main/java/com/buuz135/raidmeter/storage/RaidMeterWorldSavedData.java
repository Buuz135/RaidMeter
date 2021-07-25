package com.buuz135.raidmeter.storage;

import com.buuz135.raidmeter.RaidMeter;
import com.buuz135.raidmeter.meter.RaidMeterObject;
import com.buuz135.raidmeter.network.RaidMeterSyncMessage;
import com.buuz135.raidmeter.util.MeterPosition;
import com.buuz135.raidmeter.util.MeterRenderType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.fml.network.NetworkDirection;

import java.util.HashMap;
import java.util.Optional;

public class RaidMeterWorldSavedData extends WorldSavedData {

    public static final String NAME = "RainMeter";
    private HashMap<String,RaidMeterObject> meters;

    public static Optional<RaidMeterWorldSavedData> getInstance(IWorld world){
        if (world instanceof ServerWorld) {
            ServerWorld serverWorld = ((ServerWorld) world).getServer().getWorld(World.OVERWORLD);
            RaidMeterWorldSavedData data = serverWorld.getSavedData().getOrCreate(RaidMeterWorldSavedData::new, NAME);
            return Optional.of(data);
        }
        return Optional.empty();
    }

    public RaidMeterWorldSavedData() {
        super(NAME);
        this.meters = new HashMap<>();
    }

    public HashMap<String, RaidMeterObject> getMeters() {
        return meters;
    }

    public void markDirty(IWorld world) {
        super.markDirty();
        if (world instanceof ServerWorld) {
            ServerWorld serverWorld = ((ServerWorld) world).getServer().getWorld(World.OVERWORLD);
            serverWorld.getServer().getPlayerList().getPlayers().forEach(serverPlayerEntity -> RaidMeter.NETWORK.sendTo(new RaidMeterSyncMessage(this.write(new CompoundNBT())), serverPlayerEntity.connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT));
        }
    }

    @Override
    public void read(CompoundNBT nbt) {
        this.meters.clear();
        nbt.keySet().forEach(s -> {
            RaidMeterObject object = new RaidMeterObject(s, "", 100, 100, MeterPosition.TOP_CENTER, MeterRenderType.HORIZONTAL_THIN);
            object.deserializeNBT(nbt.getCompound(s));
            this.meters.put(s, object);
        });
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        meters.forEach((s, raidMeterObject) -> compound.put(s, raidMeterObject.serializeNBT()));
        return compound;
    }
}
