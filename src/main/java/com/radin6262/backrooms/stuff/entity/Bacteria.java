package com.radin6262.backrooms.stuff.entity;

import java.util.EnumSet;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class Bacteria extends PathfinderMob implements GeoEntity {

    private static final double CHASE_RANGE_SQR = 256.0D; // 16 blocks
    private static final double MOVEMENT_SPEED = 0.34D;
    private static final double ATTACK_DAMAGE = 10.0D;

    private final AnimatableInstanceCache geoCache =
            GeckoLibUtil.createInstanceCache(this);

    public Bacteria(
            EntityType<? extends Bacteria> type,
            Level level
    ) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();

        // Combat / chasing
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.65D, true) {
            @Override
            public boolean canContinueToUse() {
                if (!super.canContinueToUse()) {
                    return false;
                }

                if (Bacteria.this.distanceToSqr(Bacteria.this.getTarget()) > CHASE_RANGE_SQR) {
                    Bacteria.this.setTarget(null);
                    return false;
                }

                return true;
            }
        });

        // Continuous wandering when there is no target
        this.goalSelector.addGoal(
                7,
                new ContinuousStrollGoal(this, 1.35D)
        );

        // Randomly look around while idle/wandering
        this.goalSelector.addGoal(
                9,
                new RandomLookAroundGoal(this)
        );

        // Target the nearest player
        this.targetSelector.addGoal(
                1,
                new NearestAttackableTargetGoal<>(this, Player.class, true)
        );
    }

    private static class ContinuousStrollGoal extends Goal {

        private static final double MIN_DISTANCE_SQR = 36.0D; // 6 blocks
        private static final double MAX_DISTANCE_SQR = 256.0D; // 16 blocks

        private static final double REACHED_DISTANCE_SQR = 9.0D; // 3 blocks

        private static final int RANDOM_HORIZONTAL_RANGE = 16;
        private static final int RANDOM_VERTICAL_RANGE = 7;

        private static final int MAX_POSITION_ATTEMPTS = 16;
        private static final int STUCK_TICKS_BEFORE_REROUTE = 5;

        private final PathfinderMob mob;
        private final double speedModifier;

        private Vec3 destination;
        private int stuckTicks;

        private ContinuousStrollGoal(
                PathfinderMob mob,
                double speedModifier
        ) {
            this.mob = mob;
            this.speedModifier = speedModifier;

            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        private boolean hasTarget() {
            return this.mob.getTarget() != null;
        }

        private boolean chooseDestination() {
            this.destination = null;

            for (int i = 0; i < MAX_POSITION_ATTEMPTS; i++) {
                Vec3 candidate = DefaultRandomPos.getPos(
                        this.mob,
                        RANDOM_HORIZONTAL_RANGE,
                        RANDOM_VERTICAL_RANGE
                );

                if (candidate == null) {
                    continue;
                }

                double distanceSqr =
                        this.mob.position().distanceToSqr(candidate);

                // Destination must be at least 6 blocks away
                // and no more than 16 blocks away.
                if (distanceSqr < MIN_DISTANCE_SQR ||
                        distanceSqr > MAX_DISTANCE_SQR) {
                    continue;
                }

                this.destination = candidate;
                return true;
            }

            return false;
        }

        private boolean moveToDestination() {
            if (this.destination == null) {
                return false;
            }

            boolean pathAccepted = this.mob.getNavigation().moveTo(
                    this.destination.x,
                    this.destination.y,
                    this.destination.z,
                    this.speedModifier
            );

            if (!pathAccepted) {
                this.destination = null;
            }

            return pathAccepted;
        }

        @Override
        public boolean canUse() {
            return !hasTarget() && chooseDestination();
        }

        @Override
        public boolean canContinueToUse() {
            return !hasTarget() && this.destination != null;
        }

        @Override
        public void start() {
            this.stuckTicks = 0;

            if (!moveToDestination()) {
                this.mob.getNavigation().stop();
            }
        }

        @Override
        public void tick() {
            if (hasTarget() || this.destination == null) {
                return;
            }

            double distanceSqr =
                    this.mob.position().distanceToSqr(this.destination);

            // We reached the destination, so immediately choose another.
            if (distanceSqr <= REACHED_DISTANCE_SQR ||
                    this.mob.getNavigation().isDone()) {

                if (chooseDestination()) {
                    moveToDestination();
                }

                return;
            }

            // If navigation is stuck, give it a few ticks before rerouting.
            if (this.mob.getNavigation().isStuck()) {
                this.stuckTicks++;

                if (this.stuckTicks >= STUCK_TICKS_BEFORE_REROUTE) {
                    this.stuckTicks = 0;

                    if (chooseDestination()) {
                        moveToDestination();
                    }
                }
            } else {
                this.stuckTicks = 0;
            }
        }

        @Override
        public void stop() {
            this.destination = null;
            this.stuckTicks = 0;
            this.mob.getNavigation().stop();
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, MOVEMENT_SPEED)
                .add(Attributes.ATTACK_DAMAGE, ATTACK_DAMAGE)
                .add(Attributes.FOLLOW_RANGE, 16.0D);
    }

    @Override
    public void registerControllers(
            AnimatableManager.ControllerRegistrar controllers
    ) {
        controllers.add(new AnimationController<>(
                this,
                "controller",
                5,
                event -> {
                    if (event.isMoving()) {
                        return event.setAndContinue(
                                RawAnimation.begin().thenLoop("walk")
                        );
                    }

                    return event.setAndContinue(
                            RawAnimation.begin().thenLoop("idle")
                    );
                }
        ));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }
}