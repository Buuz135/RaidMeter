package com.buuz135.raidmeter.meter;

import com.buuz135.raidmeter.event.RaidMeterEvent;
import com.buuz135.raidmeter.util.MeterPosition;
import com.buuz135.raidmeter.util.MeterRenderType;
import net.minecraft.client.Minecraft;
import net.minecraft.item.DyeColor;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.INBTSerializable;

import java.awt.*;

public class RaidMeterObject implements INBTSerializable<CompoundNBT> {

    private String id;
    private String name;
    private int maxProgress;
    private int currentProgress;
    private int currentVisualProgress;
    private MeterPosition meterPosition;
    private MeterRenderType meterRenderType;
    private int color;

    public RaidMeterObject(String id, String name, int maxProgress, int currentProgress, MeterPosition meterPosition, MeterRenderType meterRenderType) {
        this.name = name;
        this.maxProgress = maxProgress;
        this.currentProgress = currentProgress;
        this.meterPosition = meterPosition;
        this.meterRenderType = meterRenderType;
        this.currentVisualProgress = currentProgress;
        this.id = id;
        this.color = DyeColor.CYAN.getTextColor();
    }

    public int getMaxProgress() {
        return maxProgress;
    }

    public int getCurrentProgress() {
        return currentProgress;
    }

    public int getCurrentVisualProgress() {
        return currentVisualProgress;
    }

    public String getName() {
        return name;
    }

    public MeterPosition getMeterPosition() {
        return meterPosition;
    }

    public MeterRenderType getMeterRenderType() {
        return meterRenderType;
    }

    public void setCurrentVisualProgress(int currentVisualProgress) {
        this.currentVisualProgress = currentVisualProgress;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
    }

    public void setCurrentProgress(int currentProgress) {
        this.currentProgress = currentProgress;
    }

    public void add(int amount){
        RaidMeterEvent.Add add = new RaidMeterEvent.Add(this, amount);
        MinecraftForge.EVENT_BUS.post(add);
        setCurrentProgress(add.getAmount() + this.getCurrentProgress());
    }

    public void setMeterPosition(MeterPosition meterPosition) {
        this.meterPosition = meterPosition;
    }

    public void setMeterRenderType(MeterRenderType meterRenderType) {
        this.meterRenderType = meterRenderType;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public boolean renderTick(){
        int increaseValue = 1;
        if (this.currentProgress < this.currentVisualProgress){
            increaseValue *= -1;
        }
        if (this.currentVisualProgress != this.currentProgress){
            this.currentVisualProgress =  currentVisualProgress + increaseValue;
            return true;
        }
        return false;
    }

    public boolean tick(){
        boolean render = renderTick();
        if (this.currentVisualProgress >= this.maxProgress){
            this.currentProgress = 0;
            MinecraftForge.EVENT_BUS.post(new RaidMeterEvent.Complete(this));
            return true;
        }
        return render;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT compoundNBT = new CompoundNBT();
        compoundNBT.putString("Id", id);
        compoundNBT.putString("Name", name);
        compoundNBT.putInt("MaxProgress", maxProgress);
        compoundNBT.putInt("CurrentProgress", currentProgress);
        compoundNBT.putInt("VisualProgress", currentVisualProgress);
        compoundNBT.putString("MeterPosition", meterPosition.name());
        compoundNBT.putString("MeterType", meterRenderType.name());
        compoundNBT.putInt("Color", color);
        return compoundNBT;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.id = nbt.getString("Id");
        this.name = nbt.getString("Name");
        this.maxProgress = nbt.getInt("MaxProgress");
        this.currentProgress = nbt.getInt("CurrentProgress");
        this.currentVisualProgress = nbt.getInt("VisualProgress");
        this.meterPosition = MeterPosition.valueOf(nbt.getString("MeterPosition"));
        this.meterRenderType = MeterRenderType.valueOf(nbt.getString("MeterType"));
        this.color = nbt.getInt("Color");
    }
}
