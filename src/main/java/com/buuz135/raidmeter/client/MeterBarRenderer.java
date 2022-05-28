package com.buuz135.raidmeter.client;

import com.buuz135.raidmeter.storage.ClientRaidMeterData;
import com.buuz135.raidmeter.util.MeterPosition;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import java.util.concurrent.atomic.AtomicInteger;

//@Mod.EventBusSubscriber(Dist.CLIENT)
public class MeterBarRenderer {


    //@SubscribeEvent
    public static void render(RenderGameOverlayEvent event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.LAYER) {
            for (MeterPosition value : MeterPosition.values()) {
                AtomicInteger index = new AtomicInteger();
                ClientRaidMeterData.DATA.getMeters().values().forEach(raidMeter -> {
                    if (value == raidMeter.getMeterPosition() && Minecraft.getInstance().player != null && raidMeter.getVisibleToPlayers().contains(Minecraft.getInstance().player.getUUID().toString())) {
                        raidMeter.getMeterRenderType().getRendererSupplier().get().render(event.getMatrixStack(), raidMeter, event.getWindow().getGuiScaledWidth(), event.getWindow().getGuiScaledHeight(), index.get());
                        index.incrementAndGet();
                    }
                });
            }
        }
    }
}
