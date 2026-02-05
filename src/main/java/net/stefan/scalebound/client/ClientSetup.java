package net.stefan.scalebound.client;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

import net.stefan.scalebound.ScaleboundMod;
import net.stefan.scalebound.block.ModBlocks;

@EventBusSubscriber(modid = ScaleboundMod.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class ClientSetup {
    private ClientSetup() {}

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.GILDED_WITHER_ROSE.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.GRAVEBLOOM_SAPLING.get(), RenderType.cutout());
        });
    }


}
