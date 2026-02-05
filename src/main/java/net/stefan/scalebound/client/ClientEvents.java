package net.stefan.scalebound.client;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

import net.stefan.scalebound.ScaleboundMod;
import net.stefan.scalebound.client.model.Marionette;
import net.stefan.scalebound.client.render.MarionetteRenderer;
import net.stefan.scalebound.entity.ModEntities;

@EventBusSubscriber(modid = ScaleboundMod.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class ClientEvents {
    private ClientEvents() {}

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.MARIONETTE.get(), MarionetteRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(Marionette.LAYER_LOCATION, Marionette::createBodyLayer);
    }
}
