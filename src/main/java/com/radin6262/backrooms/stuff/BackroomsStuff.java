package com.radin6262.backrooms.stuff;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import software.bernie.geckolib.GeckoLib;



import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SoundType;

import com.radin6262.backrooms.stuff.entity.ModEntityAttributes;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import com.radin6262.backrooms.stuff.entity.ModEntities;

@Mod(BackroomsStuff.MODID)
public final class BackroomsStuff {
    public static final String MODID = "backroomsstuff";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(MODID);

    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(MODID);

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final DeferredBlock<Block> FLOOR_1 =
            BLOCKS.registerSimpleBlock("floor_1", solidBlock());

    public static final DeferredBlock<Block> WALL =
            BLOCKS.registerSimpleBlock("wall", solidBlock());

    public static final DeferredBlock<Block> RIM =
            BLOCKS.registerSimpleBlock("rim", solidBlock());

    public static final DeferredBlock<Block> ROOF_1 =
            BLOCKS.registerSimpleBlock("roof_1", panelBlock());

    public static final DeferredBlock<Block> OUTLET_1 =
            BLOCKS.register("outlet_1", () -> new WallMountedBlock(solidBlock()));

    public static class OAK_LADDER extends LadderBlock {
        public OAK_LADDER(Properties properties) {
            super(properties);
        }

        @Override
        public boolean isLadder(BlockState state, LevelReader level, BlockPos pos, LivingEntity entity) {
            // Explicitly tell NeoForge this functions as a climbable surface
            return true;
        }
    }

    public static final DeferredBlock<Block> OAK_LADDER = BLOCKS.registerBlock("oak_ladder",
            OAK_LADDER::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD)
                    .strength(0.4F)
                    .sound(SoundType.LADDER)
                    .noOcclusion()
        );


    public static final DeferredBlock<Block> NOTCHED_BACKROOM_WALLS =
            BLOCKS.register("notched_backroom_walls",
                    () -> new Block(BlockBehaviour.Properties.of()
                            // 1. Copies your base solid block traits (destroy time, blast resistance, etc.)
                            .destroyTime(2.0f)
                            .explosionResistance(6.0f)
                            // 2. CRITICAL: Prevents the block from choking out its own upper vertex lighting
                            .noOcclusion()
                    )
            );

    public static final DeferredItem<BlockItem> FLOOR_1_ITEM =
            ITEMS.registerSimpleBlockItem("floor_1", FLOOR_1);

    public static final DeferredItem<BlockItem> WALL_ITEM =
            ITEMS.registerSimpleBlockItem("wall", WALL);

    public static final DeferredItem<BlockItem> RIM_ITEM =
            ITEMS.registerSimpleBlockItem("rim", RIM);

    public static final DeferredItem<BlockItem> ROOF_1_ITEM =
            ITEMS.registerSimpleBlockItem("roof_1", ROOF_1);

    public static final DeferredItem<BlockItem> OUTLET_1_ITEM =
            ITEMS.registerSimpleBlockItem("outlet_1", OUTLET_1);

    public static final DeferredItem<BlockItem> OAK_LADDER_ITEM =
            ITEMS.registerSimpleBlockItem("oak_ladder", OAK_LADDER);

    public static final DeferredItem<BlockItem> NOTCHED_BACKROOM_WALLS_ITEM =
            ITEMS.registerSimpleBlockItem(
                    "notched_backroom_walls",
                    NOTCHED_BACKROOM_WALLS
            );

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> BACKROOMS_TAB =
            CREATIVE_MODE_TABS.register("backrooms", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.backroomsstuff"))
                    .icon(() -> FLOOR_1_ITEM.get().getDefaultInstance())
                    .displayItems((parameters, output) -> {
                        output.accept(FLOOR_1_ITEM.get());
                        output.accept(WALL_ITEM.get());
                        output.accept(RIM_ITEM.get());
                        output.accept(ROOF_1_ITEM.get());
                        output.accept(OUTLET_1_ITEM.get());
                        output.accept(OAK_LADDER_ITEM.get());
                        output.accept(NOTCHED_BACKROOM_WALLS_ITEM.get());
                    })
                    .build());

    public BackroomsStuff(IEventBus modEventBus, ModContainer modContainer) {
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        ModEntities.register(modEventBus);
//        modEventBus.addListener(ModEntityAttributes::register);
        CREATIVE_MODE_TABS.register(modEventBus);

    }

    private static BlockBehaviour.Properties solidBlock() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.STONE)
                .strength(1.5F);
    }

    private static BlockBehaviour.Properties panelBlock() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.STONE)
                .strength(0.5F)
                .noOcclusion();
    }

    /**
     * Thin horizontal-facing decorative block used by the outlet.
     * The collision shape matches the Blockbench outlet model.
     */
    private static final class WallMountedBlock extends Block {

        private static final VoxelShape NORTH_SHAPE = Shapes.box(
                5.0 / 16.0,
                3.0 / 16.0,
                15.0 / 16.0,
                10.0 / 16.0,
                11.0 / 16.0,
                16.0 / 16.0
        );

        private static final VoxelShape EAST_SHAPE = Shapes.box(
                0.0 / 16.0,
                3.0 / 16.0,
                5.0 / 16.0,
                1.0 / 16.0,
                11.0 / 16.0,
                10.0 / 16.0
        );

        private static final VoxelShape SOUTH_SHAPE = Shapes.box(
                6.0 / 16.0,
                3.0 / 16.0,
                0.0 / 16.0,
                11.0 / 16.0,
                11.0 / 16.0,
                1.0 / 16.0
        );

        private static final VoxelShape WEST_SHAPE = Shapes.box(
                15.0 / 16.0,
                3.0 / 16.0,
                6.0 / 16.0,
                16.0 / 16.0,
                11.0 / 16.0,
                11.0 / 16.0
        );

        private WallMountedBlock(Properties properties) {
            super(properties);

            registerDefaultState(
                    stateDefinition.any()
                            .setValue(
                                    net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING,
                                    Direction.NORTH
                            )
            );
        }

        @Override
        protected void createBlockStateDefinition(
                StateDefinition.Builder<Block, BlockState> builder
        ) {
            builder.add(
                    net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING
            );
        }

        @Override
        public BlockState getStateForPlacement(BlockPlaceContext context) {
            return defaultBlockState().setValue(
                    net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING,
                    context.getHorizontalDirection().getOpposite()
            );
        }

        @Override
        protected VoxelShape getShape(
                BlockState state,
                BlockGetter level,
                net.minecraft.core.BlockPos pos,
                CollisionContext context
        ) {
            return switch (
                    state.getValue(
                            net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING
                    )
            ) {
                case NORTH -> NORTH_SHAPE;
                case EAST -> EAST_SHAPE;
                case SOUTH -> SOUTH_SHAPE;
                case WEST -> WEST_SHAPE;
                default -> NORTH_SHAPE;
            };
        }
    }
}
