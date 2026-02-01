package net.stefan.scalebound.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.BlockItem;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.stefan.scalebound.ScaleboundMod;
import net.stefan.scalebound.block.ModBlocks;

public final class ModItems {

    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(ScaleboundMod.MOD_ID);

    // Arm core (inert)
    public static final DeferredItem<Item> MECHANICAL_ARM_CORE =
            ITEMS.registerSimpleItem("mechanical_arm_core", new Item.Properties().stacksTo(1));

    // Grapple module (consumed in the workbench)
    public static final DeferredItem<Item> GRAPPLE_MODULE =
            ITEMS.registerSimpleItem("grapple_module", new Item.Properties().stacksTo(1));

    // Amethys dust testing items (fuel for arm)
    public static  final DeferredItem<Item> AMETHYST_DUST =
            ITEMS.registerSimpleItem("amethyst_dust", new Item.Properties().stacksTo(64));

    public static final DeferredItem<Item> MECHANICAL_ARM =
            ITEMS.register("mechanical_arm",
                    () -> new MechanicalArmItem(new Item.Properties()
                            .stacksTo(1)
                            .durability(350)
                    )
            );

    // BlockItem for the workbench (so you can place it)
    public static final DeferredItem<BlockItem> ARM_WORKBENCH_ITEM =
            ITEMS.registerSimpleBlockItem("arm_workbench", ModBlocks.ARM_WORKBENCH);


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}