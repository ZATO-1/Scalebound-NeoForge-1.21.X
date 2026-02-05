package net.stefan.scalebound.client.render;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.stefan.scalebound.ScaleboundMod;

public final class ModModelLayers {
    private ModModelLayers() {}

    public static final ModelLayerLocation MARIONETTE =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(ScaleboundMod.MOD_ID, "marionette"), "main");
}
