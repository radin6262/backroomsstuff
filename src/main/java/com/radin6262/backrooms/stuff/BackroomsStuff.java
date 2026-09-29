package com.radin6262.backrooms.stuff;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

@Mod(BackroomsStuff.MODID)
public final class BackroomsStuff {
    public static final String MODID = "backroomsstuff";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    // Floors use the three distinct supplied source textures.
    public static final DeferredBlock<Block> FLOOR_1 = BLOCKS.registerSimpleBlock("floor_1", solidBlock());
    public static final DeferredBlock<Block> WALL = BLOCKS.registerSimpleBlock("wall", solidBlock());
    public static final DeferredBlock<Block> RIM = BLOCKS.registerSimpleBlock("rim", solidBlock());

    public static final DeferredBlock<Block> ROOF_1 = BLOCKS.registerSimpleBlock("roof_1", panelBlock());
    public static final DeferredBlock<Block> OUTLET_1 = BLOCKS.register("outlet_1", () -> new WallMountedBlock(panelBlock()));
    public static final DeferredBlock<Block> OUTLET_1_DAMAGED = BLOCKS.register("outlet_1_damaged", () -> new WallMountedBlock(panelBlock()));
    public static final DeferredBlock<Block> CURVE = BLOCKS.registerSimpleBlock("curve", panelBlock());
    public static final DeferredBlock<Block> WALL_DETAIL = BLOCKS.registerSimpleBlock("wall_detail", panelBlock());
    public static final DeferredBlock<Block> WALL_DIAGONAL = BLOCKS.registerSimpleBlock("wall_diagonal", panelBlock());
    public static final DeferredBlock<Block> DAMAGED_WALL = BLOCKS.registerSimpleBlock("damaged_wall", panelBlock());

    // Two wallpaper variants: normal and one with a bottom notch/foot reaching the floor.
    public static final DeferredBlock<Block> WALLPAPER = BLOCKS.registerSimpleBlock("wallpaper", panelBlock());
    public static final DeferredBlock<Block> WALLPAPER_NOTCHED = BLOCKS.registerSimpleBlock("wallpaper_notched", panelBlock());

    public static final DeferredItem<BlockItem> FLOOR_1_ITEM = ITEMS.registerSimpleBlockItem("floor_1", FLOOR_1);
    public static final DeferredItem<BlockItem> WALL_ITEM = ITEMS.registerSimpleBlockItem("wall", WALL);
    public static final DeferredItem<BlockItem> RIM_ITEM = ITEMS.registerSimpleBlockItem("rim", RIM);
    public static final DeferredItem<BlockItem> ROOF_1_ITEM = ITEMS.registerSimpleBlockItem("roof_1", ROOF_1);
    public static final DeferredItem<BlockItem> OUTLET_1_ITEM = ITEMS.registerSimpleBlockItem("outlet_1", OUTLET_1);
    public static final DeferredItem<BlockItem> OUTLET_1_DAMAGED_ITEM = ITEMS.registerSimpleBlockItem("outlet_1_damaged", OUTLET_1_DAMAGED);
    public static final DeferredItem<BlockItem> CURVE_ITEM = ITEMS.registerSimpleBlockItem("curve", CURVE);
    public static final DeferredItem<BlockItem> WALL_DETAIL_ITEM = ITEMS.registerSimpleBlockItem("wall_detail", WALL_DETAIL);
    public static final DeferredItem<BlockItem> WALL_DIAGONAL_ITEM = ITEMS.registerSimpleBlockItem("wall_diagonal", WALL_DIAGONAL);
    public static final DeferredItem<BlockItem> DAMAGED_WALL_ITEM = ITEMS.registerSimpleBlockItem("damaged_wall", DAMAGED_WALL);
    public static final DeferredItem<BlockItem> WALLPAPER_ITEM = ITEMS.registerSimpleBlockItem("wallpaper", WALLPAPER);
    public static final DeferredItem<BlockItem> WALLPAPER_NOTCHED_ITEM = ITEMS.registerSimpleBlockItem("wallpaper_notched", WALLPAPER_NOTCHED);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> BACKROOMS_TAB =
            CREATIVE_MODE_TABS.register("backrooms", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.backroomsstuff"))
                    .icon(() -> FLOOR_1_ITEM.get().getDefaultInstance())
                    .displayItems((parameters, output) -> {
                        output.accept(FLOOR_1_ITEM.get());
                        output.accept(WALL_ITEM.get());
                        output.accept(WALLPAPER_ITEM.get());
                        output.accept(WALLPAPER_NOTCHED_ITEM.get());
                        output.accept(RIM_ITEM.get());
                        output.accept(ROOF_1_ITEM.get());
                        output.accept(OUTLET_1_ITEM.get());
                        output.accept(OUTLET_1_DAMAGED_ITEM.get());
                        output.accept(CURVE_ITEM.get());
                        output.accept(WALL_DETAIL_ITEM.get());
                        output.accept(WALL_DIAGONAL_ITEM.get());
                        output.accept(DAMAGED_WALL_ITEM.get());
                    })
                    .build());

    public BackroomsStuff(IEventBus modEventBus, ModContainer modContainer) {
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
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

    /** A thin horizontal-facing decorative block that faces the player on placement. */
    private static final class WallMountedBlock extends Block {
        private WallMountedBlock(Properties properties) {
            super(properties);
            registerDefaultState(stateDefinition.any().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH));
        }

        @Override
        protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
            builder.add(BlockStateProperties.HORIZONTAL_FACING);
        }

        @Override
        public BlockState getStateForPlacement(BlockPlaceContext context) {
            return defaultBlockState().setValue(
                    BlockStateProperties.HORIZONTAL_FACING,
                    context.getHorizontalDirection().getOpposite()
            );
        }
    }
}
