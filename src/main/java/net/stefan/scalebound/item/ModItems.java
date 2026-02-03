package net.stefan.scalebound.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.BlockItem;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.stefan.scalebound.ScaleboundMod;
import net.stefan.scalebound.block.ModBlocks;

public final class ModItems {
    private ModItems() {}

    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(ScaleboundMod.MOD_ID);


                                    //======MISC======//

    // Amethyst dust testing items (fuel for arm)
    public static  final DeferredItem<Item> AMETHYST_DUST =
            ITEMS.registerSimpleItem("amethyst_dust", new Item.Properties().stacksTo(64));

    // Amo for the combat module
    public static final DeferredItem<Item> BOLT =
            ITEMS.registerSimpleItem("bolt", new Item.Properties().stacksTo(16));

    public static final DeferredItem<Item> WITHER_ESSENCE =
            ITEMS.registerSimpleItem("wither_essence", new Item.Properties().stacksTo(64));



                                //======ADVANCED ITEMS======//

    public static final DeferredItem<Item> GILDED_WITHER_ROSE =
            ITEMS.registerSimpleItem("gilded_wither_rose", new Item.Properties().stacksTo(1));

    // Hammer used to turn amethyst into amethyst dust
    public static final DeferredItem<Item> HAMMER =
            ITEMS.registerSimpleItem("hammer", new Item.Properties().stacksTo(1));

                                //======MECHANICAL ARM======//

    // Arm core (inert)
    public static final DeferredItem<Item> MECHANICAL_ARM_CORE =
            ITEMS.registerSimpleItem("mechanical_arm_core", new Item.Properties().stacksTo(1));

    // Grapple module (consumed in the workbench)
    public static final DeferredItem<Item> GRAPPLE_MODULE =
            ITEMS.registerSimpleItem("grapple_module", new Item.Properties().stacksTo(1));

    public static final DeferredItem<Item> MECHANICAL_ARM =
            ITEMS.register("mechanical_arm",
                    () -> new MechanicalArmItem(new Item.Properties()
                            .stacksTo(1)
                            .durability(350)
                    )
            );

                                    //======ORES=====//

    // Raw silver acting like all the other ores
    public static  final DeferredItem<Item> RAW_SILVER =
            ITEMS.registerSimpleItem("raw_silver", new Item.Properties().stacksTo(64));

    public static  final DeferredItem<Item> RAW_LEAD =
            ITEMS.registerSimpleItem("raw_lead", new Item.Properties().stacksTo(64));



    // BlockItem for the workbench (so you can place it) ALSO seems that if I don't add the blocks here they just don't work, I don't understand why...
    public static final DeferredItem<BlockItem> ARM_WORKBENCH_ITEM =
            ITEMS.registerSimpleBlockItem("arm_workbench", ModBlocks.ARM_WORKBENCH);

    public static final DeferredItem<BlockItem> SILVER_ORE =
            ITEMS.registerSimpleBlockItem("silver_ore", ModBlocks.SILVER_ORE);

    public static final DeferredItem<BlockItem> LEAD_ORE =
            ITEMS.registerSimpleBlockItem("lead_ore", ModBlocks.LEAD_ORE);

    public static final DeferredItem<BlockItem> SILVER_BLOCK =
            ITEMS.registerSimpleBlockItem("silver_block", ModBlocks.SILVER_BLOCK);

    public static final DeferredItem<BlockItem> LEAD_BLOCK =
            ITEMS.registerSimpleBlockItem("lead_block", ModBlocks.LEAD_BLOCK);

    //public static final DeferredItem<BlockItem> GILDED_WITHER_ROSE_BLOCK =
            //ITEMS.registerSimpleBlockItem("gilded_wither_rose", ModBlocks.GILDED_WITHER_ROSE);



    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}