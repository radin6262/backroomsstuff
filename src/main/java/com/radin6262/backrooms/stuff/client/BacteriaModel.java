package com.radin6262.backrooms.stuff.client;

import com.radin6262.backrooms.stuff.BackroomsStuff;
import com.radin6262.backrooms.stuff.entity.Bacteria;

import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

public class BacteriaModel extends GeoModel<Bacteria> {

    // Fixed path: points directly to assets/backroomsstuff/geo/entity/model.geo.json
    private static final ResourceLocation MODEL =
            ResourceLocation.fromNamespaceAndPath(
                    BackroomsStuff.MODID,
                    "geo/entity/bacteria.geo.json"
            );

    // Fixed path: points directly to assets/backroomsstuff/textures/entity/bacteria.png
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(
                    BackroomsStuff.MODID,
                    "textures/entity/bacteria.png"
            );

    // Fixed path: points directly to assets/backroomsstuff/animations/entity/bacteria.animation.json
    private static final ResourceLocation ANIMATIONS =
            ResourceLocation.fromNamespaceAndPath(
                    BackroomsStuff.MODID,
                    "animations/entity/bacteria.animation.json"
            );

    @Override
    public ResourceLocation getModelResource(Bacteria bacteria) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(Bacteria bacteria) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(Bacteria bacteria) {
        return ANIMATIONS;
    }
}
