package net.stefan.scalebound.potion;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.alchemy.Potion;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.stefan.scalebound.ScaleboundMod;

public final class ModPotions {
    private ModPotions() {}

    public static final DeferredRegister<Potion> POTIONS =
            DeferredRegister.create(Registries.POTION, ScaleboundMod.MOD_ID);

    public static final DeferredHolder<Potion, Potion> NECROTIC_ICHOR =
            POTIONS.register("necrotic_ichor",
                    () -> new Potion(new MobEffectInstance(MobEffects.WITHER, 20 * 10, 0)));

    public static final DeferredHolder<Potion, Potion> REFINED_NECROTIC_ICHOR =
            POTIONS.register("refined_necrotic_ichor",
                    () -> new Potion(new MobEffectInstance(MobEffects.WITHER, 20 * 20, 1)));
}
