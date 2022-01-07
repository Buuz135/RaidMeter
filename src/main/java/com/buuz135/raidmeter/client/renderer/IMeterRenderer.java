package com.buuz135.raidmeter.client.renderer;

import com.buuz135.raidmeter.meter.RaidMeterObject;
import com.mojang.blaze3d.vertex.PoseStack;

public interface IMeterRenderer {

    void render(PoseStack matrixStack, RaidMeterObject meter, int width, int height);

}
