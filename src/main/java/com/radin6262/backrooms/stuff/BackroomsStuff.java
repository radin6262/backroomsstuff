package com.radin6262.backrooms.stuff;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(BackroomsStuff.MODID)
public class BackroomsStuff {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "backroomsstuff";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "backroomsstuff" namespace
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    // Create a Deferred Register to hold Items which will all be registered under the "backroomsstuff" namespace
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    // Create a Deferred Register to hold CreativeModeTabs which will all be registered under the "backroomsstuff" namespace
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    // Backrooms building blocks.
    public static final DeferredBlock<Block> FLOOR_1 = BLOCKS.registerSimpleBlock("floor_1", solidBlock());
    public static final DeferredBlock<Block> FLOOR_2 = BLOCKS.registerSimpleBlock("floor_2", solidBlock());
    public static final DeferredBlock<Block> FLOOR_3 = BLOCKS.registerSimpleBlock("floor_3", solidBlock());
    public static final DeferredBlock<Block> WALL = BLOCKS.registerSimpleBlock("wall", solidBlock());
    public static final DeferredBlock<Block> RIM = BLOCKS.registerSimpleBlock("rim", solidBlock());
    public static final DeferredBlock<Block> ROOF_1 = BLOCKS.registerSimpleBlock("roof_1", panelBlock());
    public static final DeferredBlock<Block> OUTLET_1 = BLOCKS.registerSimpleBlock("outlet_1", panelBlock());
    public static final DeferredBlock<Block> OUTLET_1_DAMAGED = BLOCKS.registerSimpleBlock("outlet_1_damaged", panelBlock());
    public static final DeferredBlock<Block> CURVE = BLOCKS.registerSimpleBlock("curve", panelBlock());
    public static final DeferredBlock<Block> WALL_DETAIL = BLOCKS.registerSimpleBlock("wall_detail", panelBlock());
    public static final DeferredBlock<Block> WALL_DIAGONAL = BLOCKS.registerSimpleBlock("wall_diagonal", panelBlock());
    public static final DeferredBlock<Block> DAMAGED_WALL = BLOCKS.registerSimpleBlock("damaged_wall", panelBlock());

    // Block items.
    public static final DeferredItem<BlockItem> FLOOR_1_ITEM = ITEMS.registerSimpleBlockItem("floor_1", FLOOR_1);
    public static final DeferredItem<BlockItem> FLOOR_2_ITEM = ITEMS.registerSimpleBlockItem("floor_2", FLOOR_2);
    public static final DeferredItem<BlockItem> FLOOR_3_ITEM = ITEMS.registerSimpleBlockItem("floor_3", FLOOR_3);
    public static final DeferredItem<BlockItem> WALL_ITEM = ITEMS.registerSimpleBlockItem("wall", WALL);
    public static final DeferredItem<BlockItem> RIM_ITEM = ITEMS.registerSimpleBlockItem("rim", RIM);
    public static final DeferredItem<BlockItem> ROOF_1_ITEM = ITEMS.registerSimpleBlockItem("roof_1", ROOF_1);
    public static final DeferredItem<BlockItem> OUTLET_1_ITEM = ITEMS.registerSimpleBlockItem("outlet_1", OUTLET_1);
    public static final DeferredItem<BlockItem> OUTLET_1_DAMAGED_ITEM = ITEMS.registerSimpleBlockItem("outlet_1_damaged", OUTLET_1_DAMAGED);
    public static final DeferredItem<BlockItem> CURVE_ITEM = ITEMS.registerSimpleBlockItem("curve", CURVE);
    public static final DeferredItem<BlockItem> WALL_DETAIL_ITEM = ITEMS.registerSimpleBlockItem("wall_detail", WALL_DETAIL);
    public static final DeferredItem<BlockItem> WALL_DIAGONAL_ITEM = ITEMS.registerSimpleBlockItem("wall_diagonal", WALL_DIAGONAL);
    public static final DeferredItem<BlockItem> DAMAGED_WALL_ITEM = ITEMS.registerSimpleBlockItem("damaged_wall", DAMAGED_WALL);

    // Creative tab containing all Backrooms blocks.
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> BACKROOMS_TAB =
            CREATIVE_MODE_TABS.register("backrooms", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.backroomsstuff"))
                    .withTabsBefore(CreativeModeTabs.BUILDING_BLOCKS)
                    .icon(() -> FLOOR_1_ITEM.get().getDefaultInstance())
                    .displayItems((parameters, output) -> {
                        output.accept(FLOOR_1_ITEM.get());
                        output.accept(FLOOR_2_ITEM.get());
                        output.accept(FLOOR_3_ITEM.get());
                        output.accept(WALL_ITEM.get());
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

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public BackroomsStuff(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register the Deferred Register to the mod event bus so blocks get registered
        BLOCKS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so items get registered
        ITEMS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so tabs get registered
        CREATIVE_MODE_TABS.register(modEventBus);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (BackroomsStuff) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (Config.LOG_DIRT_BLOCK.getAsBoolean()) {
            LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));
        }

        LOGGER.info("{}{}", Config.MAGIC_NUMBER_INTRODUCTION.get(), Config.MAGIC_NUMBER.getAsInt());

        Config.ITEM_STRINGS.get().forEach((item) -> LOGGER.info("ITEM >> {}", item));
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }
}
