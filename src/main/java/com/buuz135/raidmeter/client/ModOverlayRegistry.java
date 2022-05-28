package com.buuz135.raidmeter.client;

import com.buuz135.raidmeter.storage.ClientRaidMeterData;
import com.buuz135.raidmeter.util.MeterPosition;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.gui.OverlayRegistry;

import java.util.concurrent.atomic.AtomicInteger;

public class ModOverlayRegistry {

    public static void register(){
        OverlayRegistry.registerOverlayTop("raidMeter", (gui, mStack, partialTicks, screenWidth, screenHeight) -> {
            for (MeterPosition value : MeterPosition.values()) {
                AtomicInteger index = new AtomicInteger();
                ClientRaidMeterData.DATA.getMeters().values().forEach(raidMeter -> {
                    if (value == raidMeter.getMeterPosition() && Minecraft.getInstance().player != null && raidMeter.getVisibleToPlayers().contains(Minecraft.getInstance().player.getUUID().toString())) {
                        raidMeter.getMeterRenderType().getRendererSupplier().get().render(mStack, raidMeter, screenWidth, screenHeight, index.get());
                        index.incrementAndGet();
                    }
                });
            }
        });
    }
}
