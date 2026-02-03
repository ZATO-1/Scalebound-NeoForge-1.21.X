package net.stefan.scalebound.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;

import net.stefan.scalebound.item.ModItems;

public class GildedWitherRoseBlock extends BushBlock {

    // Some 1.21 block classes want a codec pattern. BushBlock generally works fine,
    // but including CODEC keeps it consistent if your mappings demand it.
    public static final MapCodec<GildedWitherRoseBlock> CODEC = simpleCodec(GildedWitherRoseBlock::new);

    // 0 = GILDED, 1 = WHIPPING, 2 = SPENT
    public static final IntegerProperty STAGE = IntegerProperty.create("stage", 0, 2);

    // Used while WHIPPING
    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 12);

    public GildedWitherRoseBlock(Properties props) {
        super(props);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(STAGE, 0)
                .setValue(AGE, 0));
    }

    @Override
    protected MapCodec<? extends BushBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(STAGE, AGE);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
                                              Player player, InteractionHand hand, BlockHitResult hit) {

        // Activate with Ghast Tear
        if (stack.is(Items.GHAST_TEAR) && state.getValue(STAGE) == 0) {
            if (!level.isClientSide) {
                // consume tear (unless creative)
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }

                // become WHIPPING
                level.setBlock(pos, state.setValue(STAGE, 1).setValue(AGE, 0), Block.UPDATE_ALL);

                // feedback
                level.playSound(null, pos, SoundEvents.ENDERMAN_TELEPORT, SoundSource.BLOCKS, 0.6f, 1.3f);
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }

        return super.useItemOn(stack, state, level, pos, player, hand, hit);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(STAGE) != 1) return; // only WHIPPING produces

        int age = state.getValue(AGE);

        // drop chance per random tick
        // tune: higher = more drops
        if (random.nextFloat() < 0.20f) {
            spawnEssence(level, pos);
        }

        // advance age and eventually become SPENT
        age++;
        if (age >= 12) {
            level.setBlock(pos, state.setValue(STAGE, 2), Block.UPDATE_ALL);
        } else {
            level.setBlock(pos, state.setValue(AGE, age), Block.UPDATE_ALL);
        }
    }

    private static void spawnEssence(ServerLevel level, BlockPos pos) {
        ItemStack drop = new ItemStack(ModItems.WITHER_ESSENCE.get());

        double x = pos.getX() + 0.5;
        double y = pos.getY() + 0.8;
        double z = pos.getZ() + 0.5;

        ItemEntity entity = new ItemEntity(level, x, y, z, drop);
        entity.setDeltaMovement(0, 0.05, 0);
        level.addFreshEntity(entity);
    }

    // Allow placing on most solid tops (can tighten later when you add the crystal requirement)
    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return Block.isFaceFull(state.getCollisionShape(level, pos), net.minecraft.core.Direction.UP);
    }
}
