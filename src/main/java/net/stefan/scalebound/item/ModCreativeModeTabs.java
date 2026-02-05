package net.stefan.scalebound.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.stefan.scalebound.ScaleboundMod;

import java.util.function.Supplier;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ScaleboundMod.MOD_ID);

    public static final Supplier<CreativeModeTab> DUST_TAB = CREATIVE_MODE_TAB.register("dust_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.AMETHYST_DUST.get()))
                    .title(Component.translatable("creative_mode.dust_tab.title"))
                    .displayItems((itemDisplayParameters, output) ->{

                        output.accept(ModItems.AMETHYST_DUST);
                        output.accept(ModItems.SILVER_HAMMER);
                        output.accept(ModItems.ARM_WORKBENCH_ITEM);

                        output.accept(ModItems.MARIONETTE_HEAD);
                        output.accept(ModItems.MARIONETTE_CORE);
                        output.accept(ModItems.MARIONETTE_HEART);

                        output.accept(ModItems.LEAD_BLOCK);
                        output.accept(ModItems.LEAD_ORE);
                        output.accept(ModItems.SILVER_BLOCK);
                        output.accept(ModItems.SILVER_ORE);
                        output.accept(ModItems.RAW_LEAD);
                        output.accept(ModItems.RAW_SILVER);

                        output.accept(ModItems.GILDED_WITHER_ROSE_BLOCK);
                        output.accept(ModItems.ROTTEN_LEATHER);

                    }).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
