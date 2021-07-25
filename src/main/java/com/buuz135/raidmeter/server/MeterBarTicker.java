package com.buuz135.raidmeter.server;

import com.buuz135.raidmeter.event.RaidMeterEvent;
import com.buuz135.raidmeter.meter.RaidMeterObject;
import com.buuz135.raidmeter.storage.ClientRaidMeterData;
import com.buuz135.raidmeter.storage.RaidMeterWorldSavedData;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class MeterBarTicker {

    @SubscribeEvent
    public static void render(TickEvent.WorldTickEvent event) {
        if (event.phase == TickEvent.Phase.END){
            RaidMeterWorldSavedData.getInstance(event.world).ifPresent(raidMeterWorldSavedData -> raidMeterWorldSavedData.getMeters().values().forEach(raidMeterObject -> {
                if (raidMeterObject.tick()){
                    raidMeterWorldSavedData.markDirty(event.world);
                }
            }));
        }
    }

}
