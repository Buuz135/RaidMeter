package com.buuz135.raidmeter.client.renderer;

import com.buuz135.raidmeter.meter.RaidMeterObject;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;

public interface IMeterRenderer {

    void render(GuiGraphics guiGraphics, RaidMeterObject meter, int width, int height, int index);

}
