package com.buuz135.raidmeter.storage;

import com.buuz135.raidmeter.meter.RaidMeterObject;
import com.buuz135.raidmeter.util.MeterPosition;
import com.buuz135.raidmeter.util.MeterRenderType;
import net.minecraft.nbt.CompoundTag;

import java.util.HashMap;

public class ClientRaidMeterData {

    public static final ClientRaidMeterData DATA = new ClientRaidMeterData();

    private final HashMap<String, RaidMeterObject> raidMeters;

    public ClientRaidMeterData() {
        this.raidMeters = new HashMap<>();
    }

    public HashMap<String, RaidMeterObject> getMeters() {
        return this.raidMeters;
    }

    public void deserialize(CompoundTag nbt) {
        this.raidMeters.clear();
        for (String name : nbt.getAllKeys()) {
            RaidMeterObject object = new RaidMeterObject(name, "", 100, 100, MeterPosition.TOP_CENTER, MeterRenderType.HORIZONTAL_THIN);
            object.deserializeNBT(nbt.getCompound(name));
            this.raidMeters.put(name, object);
        }
    }


}
