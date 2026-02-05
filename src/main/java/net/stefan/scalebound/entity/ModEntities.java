package net.stefan.scalebound.entity;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.stefan.scalebound.ScaleboundMod;

public final class ModEntities {
    private ModEntities() {}

    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(Registries.ENTITY_TYPE, ScaleboundMod.MOD_ID);

    public static final DeferredHolder<EntityType<?>, EntityType<MarionetteEntity>> MARIONETTE =
            ENTITIES.register("marionette", () ->
                    EntityType.Builder.<MarionetteEntity>of(MarionetteEntity::new, MobCategory.CREATURE)
                            .sized(0.6f, 1.4f)
                            .build("marionette")
            );
}
