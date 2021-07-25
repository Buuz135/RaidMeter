package com.buuz135.raidmeter.util;

import com.buuz135.raidmeter.client.renderer.HorizontalRendererType;
import com.buuz135.raidmeter.client.renderer.IMeterRenderer;
import com.buuz135.raidmeter.client.renderer.VerticalThiccRendererType;

import java.util.function.Supplier;

public enum MeterRenderType {
    HORIZONTAL_THIN(HorizontalRendererType::new),
    VERTICAL_THICC(VerticalThiccRendererType::new);

    private final Supplier<IMeterRenderer> rendererSupplier;

    MeterRenderType(Supplier<IMeterRenderer> rendererSupplier) {
        this.rendererSupplier = rendererSupplier;
    }

    public Supplier<IMeterRenderer> getRendererSupplier() {
        return rendererSupplier;
    }
}
