package com.buuz135.raidmeter.client;

import com.buuz135.raidmeter.storage.ClientRaidMeterData;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class MeterBarRenderer {


    @SubscribeEvent
    public static void render(RenderGameOverlayEvent event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR) {
            ClientRaidMeterData.DATA.getMeters().values().forEach(raidMeter -> {
                        raidMeter.getMeterRenderType().getRendererSupplier().get().render(event.getMatrixStack(), raidMeter, event.getWindow().getScaledWidth(), event.getWindow().getScaledHeight());
                    }
            );
        }
    }
}
