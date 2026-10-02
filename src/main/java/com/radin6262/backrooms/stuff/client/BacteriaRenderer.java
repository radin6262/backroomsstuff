package com.radin6262.backrooms.stuff.client;

import com.radin6262.backrooms.stuff.entity.Bacteria;

import software.bernie.geckolib.renderer.GeoEntityRenderer;

import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class BacteriaRenderer extends GeoEntityRenderer<Bacteria> {

    public BacteriaRenderer(
            EntityRendererProvider.Context context
    ) {
        super(
                context,
                new BacteriaModel()
        );
    }
}