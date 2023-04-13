package com.buuz135.raidmeter.client;

import com.buuz135.raidmeter.storage.ClientRaidMeterData;
import com.buuz135.raidmeter.util.MeterPosition;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.atomic.AtomicInteger;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModOverlayRegistry {

    @SubscribeEvent
    public static void register(RegisterGuiOverlaysEvent event){
        event.registerAboveAll("raidmeter", (gui, mStack, partialTicks, screenWidth, screenHeight) -> {
            for (MeterPosition value : MeterPosition.values()) {
                AtomicInteger index = new AtomicInteger();
                ClientRaidMeterData.DATA.getMeters().values().forEach(raidMeter -> {
                    if (value == raidMeter.getMeterPosition() && Minecraft.getInstance().player != null && raidMeter.getVisibleToPlayers().contains(Minecraft.getInstance().player.getUUID().toString())) {
                        if (raidMeter.getDisplayFor() == -1 || raidMeter.getDisplayFor() > raidMeter.getCurrentVisualDisplayedFor()){
                            raidMeter.getMeterRenderType().getRendererSupplier().get().render(mStack, raidMeter, screenWidth, screenHeight, index.get());
                        }
                        index.incrementAndGet();
                    }
                });
            }
        });
    }
}
