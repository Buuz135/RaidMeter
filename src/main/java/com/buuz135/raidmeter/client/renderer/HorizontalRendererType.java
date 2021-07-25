package com.buuz135.raidmeter.client.renderer;

import com.buuz135.raidmeter.RaidMeter;
import com.buuz135.raidmeter.meter.RaidMeterObject;
import com.buuz135.raidmeter.util.MeterPosition;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class HorizontalRendererType implements IMeterRenderer {

    private final static int BAR_WIDTH = 175;
    private final static int BAR_HEIGHT = 15;
    private final ResourceLocation resourceLocation = new ResourceLocation(RaidMeter.MODID, "textures/gui/bars.png");

    @Override
    public void render(MatrixStack matrixStack, RaidMeterObject meter, int width, int height) {
        if (Minecraft.getInstance().world != null){
            MeterPosition position = meter.getMeterPosition();
            int x = (int) (position.getX() * width);
            int y = (int) (position.getY() * height) + 4;
            float fontX = 0;
            if (position.getY() == 1){
                y -= (BAR_HEIGHT * 2) + 4;
            }
            if (position.getX() == 1){
                x -= BAR_WIDTH + 6;
                fontX = x + BAR_WIDTH - Minecraft.getInstance().fontRenderer.getStringWidth(meter.getName()) - 4;
            } else if (position.getX() != 0){
                x -= BAR_WIDTH / 2;
                fontX = x + BAR_WIDTH - BAR_WIDTH /2F - Minecraft.getInstance().fontRenderer.getStringWidth(meter.getName()) /2F;
            } else {
                x += 6;
                fontX = x + 6;
            }
            if (position == MeterPosition.BOTTOM_CENTER){
                y -= 45;
            }
            Minecraft.getInstance().fontRenderer.drawStringWithShadow(matrixStack, meter.getName(), fontX, y, meter.getColor());
            y += 10;
            Minecraft.getInstance().getTextureManager().bindTexture(resourceLocation);
            AbstractGui.blit(matrixStack, x, y, 0, 0, BAR_WIDTH, BAR_HEIGHT, 256, 256);
            Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation(RaidMeter.MODID, "textures/gui/bar_inside.png"));
            Color color = new Color(meter.getColor());
            RenderSystem.color3f(color.getRed() /255f, color.getGreen() /255f, color.getBlue() /255f);
            AbstractGui.blit(matrixStack, x + 5, y + 3, 1- Minecraft.getInstance().world.getGameTime() % 256,  (float) Math.sin(Minecraft.getInstance().world.getGameTime() / 15D) * 25, (int) ((BAR_WIDTH - 10) * (meter.getCurrentVisualProgress() / (double) meter.getMaxProgress())), BAR_HEIGHT - 6, 256, 256);
        }
    }
}
