package com.radin6262.backrooms.stuff.entity;

import com.radin6262.backrooms.stuff.BackroomsStuff;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class ModEntities {

    // 1. Using standard generic syntax guarantees compatibility across IDE environments
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(Registries.ENTITY_TYPE, BackroomsStuff.MODID);

    // 2. Supplying the explicit lambda signature ensures 'register' infers properly
    public static final Supplier<EntityType<Bacteria>> BACTERIA =
            ENTITY_TYPES.register(
                    "bacteria",
                    () -> EntityType.Builder.<Bacteria>of(
                                    Bacteria::new,
                                    MobCategory.CREATURE
                            )
                            .sized(1.0F, 2.0F)
                            .clientTrackingRange(8)
                            .updateInterval(3)
                            .build("bacteria")
            );

    private ModEntities() {
    }

    // 3. This will now successfully hook into your mod initialization event bus
    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
