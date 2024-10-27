package com.buuz135.raidmeter.server;


import com.buuz135.raidmeter.storage.RaidMeterWorldSavedData;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class MeterBarTicker {

    @SubscribeEvent
    public static void render(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && event.haveTime()){
            RaidMeterWorldSavedData.getInstance(event.getServer().overworld()).ifPresent(raidMeterWorldSavedData -> raidMeterWorldSavedData.getMeters().values().forEach(raidMeterObject -> {
                if (raidMeterObject.tick(event.getServer().overworld())){
                    raidMeterWorldSavedData.markDirty(event.getServer().overworld());
                }
            }));
        }
    }

}
