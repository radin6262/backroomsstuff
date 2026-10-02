package com.radin6262.backrooms.stuff.client;

import com.radin6262.backrooms.stuff.BackroomsStuff;
import com.radin6262.backrooms.stuff.entity.ModEntities;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(
        modid = BackroomsStuff.MODID,
        value = Dist.CLIENT
)
public final class BacteriaClient {

    private BacteriaClient() {
    }

    @SubscribeEvent
    public static void registerRenderers(
            EntityRenderersEvent.RegisterRenderers event
    ) {
        event.registerEntityRenderer(
                ModEntities.BACTERIA.get(),
                BacteriaRenderer::new
        );
    }
}
