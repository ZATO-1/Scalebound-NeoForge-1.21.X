package net.stefan.scalebound.world;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.stefan.scalebound.ScaleboundMod;

import java.util.Optional;

public final class ModTreeGrowers {
    private ModTreeGrowers() {}

    public static final ResourceKey<ConfiguredFeature<?, ?>> GRAVEBLOOM_TREE_KEY =
            ResourceKey.create(
                    Registries.CONFIGURED_FEATURE,
                    ResourceLocation.fromNamespaceAndPath(ScaleboundMod.MOD_ID, "gravebloom_tree")
            );

    public static final TreeGrower GRAVEBLOOM =
            new TreeGrower(
                    "gravebloom",
                    Optional.of(GRAVEBLOOM_TREE_KEY),
                    Optional.empty(),
                    Optional.empty()
            );
}
