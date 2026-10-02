package com.radin6262.backrooms.stuff.entity;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

import com.radin6262.backrooms.stuff.BackroomsStuff;

@EventBusSubscriber(modid = BackroomsStuff.MODID)
public final class ModEntityAttributes {

    private ModEntityAttributes() {
    }

    @SubscribeEvent
    public static void register(EntityAttributeCreationEvent event) {
        event.put(
                ModEntities.BACTERIA.get(),
                createBacteriaAttributes()
        );
    }

    private static AttributeSupplier createBacteriaAttributes() {
        // 1. Inherits ALL baseline physics properties (Scale, Gravity, Movement Efficiency, etc.)
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.FOLLOW_RANGE, 32.0D)
                .add(Attributes.ATTACK_DAMAGE, 4.0D)
                .add(Attributes.STEP_HEIGHT, 0.6D)
                .build();
    }
}
