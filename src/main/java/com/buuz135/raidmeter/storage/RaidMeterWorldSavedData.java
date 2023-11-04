package com.buuz135.raidmeter.storage;

import com.buuz135.raidmeter.RaidMeter;
import com.buuz135.raidmeter.meter.RaidMeterObject;
import com.buuz135.raidmeter.network.RaidMeterSyncMessage;
import com.buuz135.raidmeter.util.MeterPosition;
import com.buuz135.raidmeter.util.MeterRenderType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.network.NetworkDirection;

import java.util.HashMap;
import java.util.Optional;

public class RaidMeterWorldSavedData extends SavedData {

    public static final String NAME = "RainMeter";
    private HashMap<String,RaidMeterObject> meters;

    public static Optional<RaidMeterWorldSavedData> getInstance(LevelAccessor world){
        if (world instanceof ServerLevel) {
            ServerLevel serverWorld = ((ServerLevel) world).getServer().getLevel(Level.OVERWORLD);
            RaidMeterWorldSavedData data = serverWorld.getDataStorage().computeIfAbsent(RaidMeterWorldSavedData::loadNew, RaidMeterWorldSavedData::new, NAME);
            return Optional.of(data);
        }
        return Optional.empty();
    }

    public RaidMeterWorldSavedData() {
        super();
        this.meters = new HashMap<>();
    }

    public HashMap<String, RaidMeterObject> getMeters() {
        return meters;
    }

    public void markDirty(LevelAccessor world) {
        super.setDirty();
        if (world instanceof ServerLevel) {
            ServerLevel serverWorld = ((ServerLevel) world).getServer().getLevel(Level.OVERWORLD);
            serverWorld.getServer().getPlayerList().getPlayers().forEach(serverPlayerEntity -> RaidMeter.NETWORK.sendTo(new RaidMeterSyncMessage(this.save(new CompoundTag())), serverPlayerEntity.connection.connection, NetworkDirection.PLAY_TO_CLIENT));
        }
    }

    public static RaidMeterWorldSavedData loadNew(CompoundTag nbt) {
        RaidMeterWorldSavedData raidMeterWorldSavedData = new RaidMeterWorldSavedData();
        raidMeterWorldSavedData.load(nbt);
        return raidMeterWorldSavedData;
    }

    public void load(CompoundTag nbt) {
        this.meters.clear();
        nbt.getAllKeys().forEach(s -> {
            RaidMeterObject object = new RaidMeterObject(s, "", 100, 100, MeterPosition.TOP_CENTER, MeterRenderType.HORIZONTAL_THIN);
            object.deserializeNBT(nbt.getCompound(s));
            this.meters.put(s, object);
        });
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        meters.forEach((s, raidMeterObject) -> compound.put(s, raidMeterObject.serializeNBT()));
        return compound;
    }
}
