package com.buuz135.raidmeter.event;

import com.buuz135.raidmeter.meter.RaidMeterObject;
import net.minecraftforge.eventbus.api.Event;

public class RaidMeterEvent extends Event {

    private final RaidMeterObject raidMeter;

    public RaidMeterEvent(RaidMeterObject raidMeter) {
        this.raidMeter = raidMeter;
    }

    public RaidMeterObject getRaidMeter() {
        return raidMeter;
    }

    public static class Complete extends RaidMeterEvent{

        public Complete(RaidMeterObject raidMeter) {
            super(raidMeter);
        }
    }

    public static class Add extends RaidMeterEvent{

        private int amount;

        public Add(RaidMeterObject raidMeter, int amount) {
            super(raidMeter);
            this.amount = amount;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }
    }
}
