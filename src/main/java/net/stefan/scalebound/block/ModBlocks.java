package net.stefan.scalebound.block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.stefan.scalebound.ScaleboundMod;
import net.stefan.scalebound.item.ModItems;

public final class ModBlocks {
    private ModBlocks() {}

    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(ScaleboundMod.MOD_ID);

    public static final DeferredBlock<Block> ARM_WORKBENCH =
            BLOCKS.register("arm_workbench", () -> new ArmWorkbenchBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.METAL)
                            .strength(3.5f)
                            .sound(SoundType.METAL)
            ));

    public static final DeferredBlock<Block> SILVER_BLOCK =
            BLOCKS.register("silver_block", () -> new Block(
                    BlockBehaviour.Properties.of()
                        .strength(4f).requiresCorrectToolForDrops().sound(SoundType.METAL)));

    //private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        //ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    //}

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
