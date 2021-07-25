package com.buuz135.raidmeter.client.renderer;

import com.buuz135.raidmeter.meter.RaidMeterObject;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.Screen;

public interface IMeterRenderer {

    public void render(MatrixStack matrixStack, RaidMeterObject meter, int width, int height);

}
