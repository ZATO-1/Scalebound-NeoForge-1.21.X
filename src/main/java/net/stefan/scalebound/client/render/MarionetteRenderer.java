package net.stefan.scalebound.client.render;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

import net.stefan.scalebound.ScaleboundMod;
import net.stefan.scalebound.client.model.Marionette;
import net.stefan.scalebound.entity.MarionetteEntity;

public class MarionetteRenderer extends MobRenderer<MarionetteEntity, Marionette<MarionetteEntity>> {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(ScaleboundMod.MOD_ID, "textures/entity/marionette.png");

    public MarionetteRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new Marionette<>(ctx.bakeLayer(Marionette.LAYER_LOCATION)), 0.4f);
    }

    @Override
    public ResourceLocation getTextureLocation(MarionetteEntity entity) {
        return TEXTURE;
    }
}
