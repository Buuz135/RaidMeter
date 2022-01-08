package com.buuz135.raidmeter.client;

import com.buuz135.raidmeter.storage.ClientRaidMeterData;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.gui.OverlayRegistry;

public class ModOverlayRegistry {

    public static void register(){
        OverlayRegistry.registerOverlayTop("raidMeter", (gui, mStack, partialTicks, screenWidth, screenHeight) -> {
            ClientRaidMeterData.DATA.getMeters().values().forEach(raidMeter -> {
                        if (Minecraft.getInstance().player != null && raidMeter.getVisibleToPlayers().contains(Minecraft.getInstance().player.getUUID().toString())) {
                            raidMeter.getMeterRenderType().getRendererSupplier().get().render(mStack, raidMeter, screenWidth, screenHeight);
                        }
                    }
            );
        });
    }
}
