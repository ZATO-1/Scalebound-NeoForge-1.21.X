package net.stefan.scalebound.block;

import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.stefan.scalebound.ScaleboundMod;
import net.stefan.scalebound.item.ModItems;

import net.stefan.scalebound.block.MarionetteCoreBlock;
import net.stefan.scalebound.block.MarionetteHeadBlock;


public final class ModBlocks {
    private ModBlocks() {}

    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(ScaleboundMod.MOD_ID);

                    //======NEW CRAFTING TABLE======//

    public static final DeferredBlock<Block> ARM_WORKBENCH =
            BLOCKS.register("arm_workbench", () -> new ArmWorkbenchBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.METAL)
                            .strength(3.5f)
                            .sound(SoundType.METAL)
            ));

                        //======MINERALS======//

    public static final DeferredBlock<Block> SILVER_ORE =
            BLOCKS.register("silver_ore", () -> new DropExperienceBlock(UniformInt.of(2,4),
                    BlockBehaviour.Properties.of()
                            .strength(3f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final DeferredBlock<Block> LEAD_ORE =
            BLOCKS.register("lead_ore", () -> new DropExperienceBlock(UniformInt.of(2,4),
                    BlockBehaviour.Properties.of()
                            .strength(3f).requiresCorrectToolForDrops().sound(SoundType.STONE)));


    public static final DeferredBlock<Block> SILVER_BLOCK =
            BLOCKS.register("silver_block", () -> new Block(
                    BlockBehaviour.Properties.of()
                        .strength(4f).requiresCorrectToolForDrops().sound(SoundType.METAL)));

    public static final DeferredBlock<Block> LEAD_BLOCK =
            BLOCKS.register("lead_block", () -> new Block(
                    BlockBehaviour.Properties.of()
                            .strength(4f).requiresCorrectToolForDrops().sound(SoundType.METAL)));

                        //======MISC======//

    public static final DeferredBlock<Block> GILDED_WITHER_ROSE =
            BLOCKS.register("gilded_wither_rose",
                    () -> new GildedWitherRoseBlock(BlockBehaviour.Properties.of()
                            .noCollission()
                            .instabreak()
                            .randomTicks() // IMPORTANT: enables randomTick
                    )
            );

                    //======PLANTS n GRAVEBLOOM======//

    public static final DeferredBlock<Block> GRAVEBLOOM_SAPLING =
            BLOCKS.register("gravebloom_sapling",
                    () -> new GravebloomSaplingBlock(BlockBehaviour.Properties.of()
                            .noCollission()
                            .instabreak()
                            .randomTicks()
                            .sound(SoundType.GRASS)
                    )
            );

    public static final DeferredBlock<Block> GRAVEBLOOM_LOG =
            BLOCKS.register("gravebloom_log",
                    () -> new RotatedPillarBlock(BlockBehaviour.Properties.of()
                            .strength(2.0f)
                            .sound(SoundType.WOOD)
                            .requiresCorrectToolForDrops()
                    )
            );

    public static final DeferredBlock<Block> GRAVEBLOOM_PLANKS =
            BLOCKS.register("gravebloom_planks",
                    () -> new Block(BlockBehaviour.Properties.of()
                            .strength(2.0f, 3.0f)
                            .sound(SoundType.WOOD)
                    )
            );



    //======MARIONETTE======//


    public static final DeferredBlock<Block> MARIONETTE_CORE =
            BLOCKS.register("marionette_core",
                    () -> new MarionetteCoreBlock(BlockBehaviour.Properties.of()
                            .strength(2.0f)
                            .sound(SoundType.WOOD)));

    public static final DeferredBlock<Block> MARIONETTE_HEAD =
            BLOCKS.register("marionette_head",
                    () -> new MarionetteHeadBlock(BlockBehaviour.Properties.of()
                            .strength(1.0f)
                            .sound(SoundType.WOOD)));


    //private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        //ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    //}

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
