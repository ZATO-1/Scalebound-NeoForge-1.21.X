package net.stefan.scalebound.brewing;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.stefan.scalebound.item.ModItems;
import net.stefan.scalebound.potion.ModPotions;

public final class ModBrewingRecipes {
    private ModBrewingRecipes() {}

    public static void register(RegisterBrewingRecipesEvent event) {
        var builder = event.getBuilder();

        // Convert your DeferredHolder potions -> Holder<Potion>
        Holder<Potion> necrotic = BuiltInRegistries.POTION.getHolderOrThrow(
                ResourceKey.create(Registries.POTION, ModPotions.NECROTIC_ICHOR.getId())
        );

        Holder<Potion> refined = BuiltInRegistries.POTION.getHolderOrThrow(
                ResourceKey.create(Registries.POTION, ModPotions.REFINED_NECROTIC_ICHOR.getId())
        );

        // 1) Awkward + Necrotic Ichor -> Necrotic Ichor potion
        builder.addMix(Potions.AWKWARD, ModItems.NECROTIC_ICHOR.get(), necrotic);

        // 2) Necrotic Ichor potion + Amethyst Dust -> Refined Necrotic Ichor potion
        builder.addMix(necrotic, ModItems.AMETHYST_DUST.get(), refined);
    }
}
