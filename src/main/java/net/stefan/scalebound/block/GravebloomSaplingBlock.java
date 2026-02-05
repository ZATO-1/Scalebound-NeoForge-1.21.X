package net.stefan.scalebound.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.stefan.scalebound.ScaleboundMod;
import net.stefan.scalebound.world.ModTreeGrowers;

public class GravebloomSaplingBlock extends SaplingBlock {

    public GravebloomSaplingBlock(Properties properties) {
        // We still pass your grower, but we override growth logic below.
        super(ModTreeGrowers.GRAVEBLOOM, properties);
    }

    // === Night particles (client-side) ===
    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        super.animateTick(state, level, pos, random);

        if (!level.isNight()) return;
        if (random.nextInt(10) != 0) return;

        double x = pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 0.4;
        double y = pos.getY() + 0.6 + random.nextDouble() * 0.3;
        double z = pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 0.4;

        level.addParticle(ParticleTypes.SOUL, x, y, z, 0.0, 0.01, 0.0);
    }

    // === Bonemeal growth (server-side) ===
    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        safeAdvanceOrGrow(level, random, pos, state);
    }

    // === Natural random-tick growth (server-side) ===
    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        // Vanilla saplings only sometimes advance; keep it similar
        if (random.nextInt(7) == 0) { // approx vanilla-ish pace
            safeAdvanceOrGrow(level, random, pos, state);
        }
    }

    private void safeAdvanceOrGrow(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        // Stage 0 -> Stage 1
        if (state.getValue(STAGE) == 0) {
            level.setBlock(pos, state.setValue(STAGE, 1), 3);
            return;
        }

        // Stage 1 -> Try to place the tree feature
        var lookup = level.registryAccess().lookupOrThrow(Registries.CONFIGURED_FEATURE);
        var holderOpt = lookup.get(ModTreeGrowers.GRAVEBLOOM_TREE_KEY);

        if (holderOpt.isEmpty()) {
            ScaleboundMod.LOGGER.warn("[Gravebloom] Missing configured feature: {}", ModTreeGrowers.GRAVEBLOOM_TREE_KEY);
            return;
        }

        // Remove sapling temporarily
        level.setBlock(pos, Blocks.AIR.defaultBlockState(), 4);

        ConfiguredFeature<?, ?> feature = holderOpt.get().value();

        boolean placed;
        try {
            placed = feature.place(level, level.getChunkSource().getGenerator(), random, pos);
        } catch (Exception e) {
            ScaleboundMod.LOGGER.error("[Gravebloom] Feature placement crashed at {}", pos, e);
            placed = false;
        }

        // If placement fails, restore sapling instead of deleting it
        if (!placed) {
            level.setBlock(pos, state, 4);
        }
    }
}
