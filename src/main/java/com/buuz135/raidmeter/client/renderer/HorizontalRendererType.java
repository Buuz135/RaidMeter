package com.buuz135.raidmeter.client.renderer;

import com.buuz135.raidmeter.RaidMeter;
import com.buuz135.raidmeter.meter.RaidMeterObject;
import com.buuz135.raidmeter.util.MeterPosition;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;

public class HorizontalRendererType implements IMeterRenderer {

    private final static int BAR_WIDTH = 175;
    private final static int BAR_HEIGHT = 15;
    private final ResourceLocation resourceLocation = new ResourceLocation(RaidMeter.MODID, "textures/gui/bars.png");

    @Override
    public void render(PoseStack matrixStack, RaidMeterObject meter, int width, int height, int index) {
        if (Minecraft.getInstance().level != null){
            MeterPosition position = meter.getMeterPosition();
            int x = (int) (position.getX() * width);
            int y = (int) (position.getY() * height) + 4;
            float fontX = 0;
            if (position.getY() == 0){
                y += (BAR_HEIGHT * 2) * index;
            }
            if (position.getY() == 1){
                y -= ((BAR_HEIGHT * 2) + 2) * (index + 1);
            }
            if (position.getX() == 1){
                x -= (BAR_WIDTH + 6);
                fontX = x + BAR_WIDTH - Minecraft.getInstance().font.width(meter.getName()) - 4;
            } else if (position.getX() != 0){
                x -= BAR_WIDTH / 2;
                fontX = x + BAR_WIDTH - BAR_WIDTH /2F - Minecraft.getInstance().font.width(meter.getName()) /2F;
            } else {
                x += 6;
                fontX = x + 6;
            }
            if (position == MeterPosition.BOTTOM_CENTER){
                y -= 45;
            }
            Minecraft.getInstance().font.drawShadow(matrixStack, meter.getName(), fontX, y, meter.getColor());
            y += 10;
            RenderSystem.setShaderTexture(0, resourceLocation);
            GuiComponent.blit(matrixStack, x, y, 0, 0, BAR_WIDTH, BAR_HEIGHT, 256, 256);
            RenderSystem.setShaderTexture(0, new ResourceLocation(RaidMeter.MODID, "textures/gui/bar_inside.png"));
            Color color = new Color(meter.getColor());
            RenderSystem.setShaderColor(color.getRed() /255f, color.getGreen() /255f, color.getBlue() /255f, 1.0F);
            GuiComponent.blit(matrixStack, x + 5, y + 3, 1- Minecraft.getInstance().level.getGameTime() % 256,  (float) Math.sin(Minecraft.getInstance().level.getGameTime() / 15D) * 25, (int) ((BAR_WIDTH - 10) * (meter.getCurrentVisualProgress() / (double) meter.getMaxProgress())), BAR_HEIGHT - 6, 256, 256);
        }
    }
}
