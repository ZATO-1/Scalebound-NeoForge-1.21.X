package net.stefan.scalebound.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.stefan.scalebound.entity.MarionetteEntity;
import net.stefan.scalebound.entity.ModEntities;

import javax.annotation.Nullable;

public class MarionetteHeadBlock extends Block {

    public MarionetteHeadBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    /**
     * Trigger build check when placed by a player/item.
     * This is the most reliable hook for "golem-like" constructions.
     */
    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);

        if (level.isClientSide) return;
        if (!(level instanceof ServerLevel serverLevel)) return;

        tryBuild(serverLevel, pos);
    }

    /**
     * Also trigger when neighbors change, so placement order doesn't matter.
     */
    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston);

        if (level.isClientSide) return;
        if (!(level instanceof ServerLevel serverLevel)) return;

        tryBuild(serverLevel, pos);
    }

    /**
     * Shape (current):
     * [HEAD]
     * [CORE]
     */
    private void tryBuild(ServerLevel level, BlockPos headPos) {
        BlockPos corePos = headPos.below();
        BlockState coreState = level.getBlockState(corePos);

        // Must be the Marionette Core block directly below the head
        if (!coreState.is(ModBlocks.MARIONETTE_CORE.get())) return;

        // Consume blocks
        level.setBlock(headPos, Blocks.AIR.defaultBlockState(), 3);
        level.setBlock(corePos, Blocks.AIR.defaultBlockState(), 3);

        // Spawn entity
        MarionetteEntity marionette = ModEntities.MARIONETTE.get().create(level);
        if (marionette == null) return;

        marionette.moveTo(
                corePos.getX() + 0.5,
                corePos.getY(),
                corePos.getZ() + 0.5,
                level.random.nextFloat() * 360F,
                0F
        );

        // Brief "awakening" pause (2 seconds)
        marionette.setNoAi(true);
        level.addFreshEntity(marionette);

        // Particles
        level.sendParticles(ParticleTypes.CLOUD,
                corePos.getX() + 0.5, corePos.getY() + 1.0, corePos.getZ() + 0.5,
                30, 0.35, 0.35, 0.35, 0.02);

        level.sendParticles(ParticleTypes.ENCHANT,
                corePos.getX() + 0.5, corePos.getY() + 1.0, corePos.getZ() + 0.5,
                40, 0.6, 0.8, 0.6, 0.0);

        // Sound
        level.playSound(null, corePos, SoundEvents.IRON_GOLEM_REPAIR, SoundSource.BLOCKS,
                1.0F, 0.9F + level.random.nextFloat() * 0.2F);

        // Re-enable AI after 40 ticks (2s)
        level.getServer().tell(new TickTask(level.getServer().getTickCount() + 40, () -> {
            if (marionette.isAlive()) {
                marionette.setNoAi(false);
            }
        }));
    }
}
