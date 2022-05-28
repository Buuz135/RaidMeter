package com.buuz135.raidmeter.util;

public enum MeterPosition {
    TOP_CENTER(0.5, 0),
    TOP_LEFT(0,0),
    TOP_RIGHT(1, 0),
    BOTTOM_CENTER(0.5, 1),
    BOTTOM_RIGHT(1, 1),
    BOTTOM_LEFT(0, 1),

    MIDDLE_LEFT(0, 0.45),
    MIDDLE_RIGHT(1,0.45),
    MIDDLE_CENTER_LEFT(0.45, 0.45),
    MIDDLE_CENTER_RIGHT(0.55, 0.45)
    ;


    private final double x;
    private final double y;

    MeterPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
