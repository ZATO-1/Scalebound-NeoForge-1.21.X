package net.stefan.scalebound.event;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.stefan.scalebound.block.ModBlocks;
import net.stefan.scalebound.potion.ModPotions;

public final class RefinedIchorInteractions {
    private RefinedIchorInteractions() {}

    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (event.getLevel().isClientSide) return;

        var level = (ServerLevel) event.getLevel();
        var player = event.getEntity();
        InteractionHand hand = event.getHand();
        ItemStack held = player.getItemInHand(hand);

        // Must be a potion item
        if (!held.is(Items.POTION)) return;

        // Must be our refined potion
        PotionContents contents = held.get(DataComponents.POTION_CONTENTS);
        if (contents == null) return;

        // Compare the potion holder to our registry entry
        Potion refined = ModPotions.REFINED_NECROTIC_ICHOR.get();
        if (!contents.potion().isPresent() || contents.potion().get().value() != refined) return;

        BlockPos pos = event.getPos();

        // Only if clicking an oak sapling
        if (!level.getBlockState(pos).is(Blocks.OAK_SAPLING)) return;

        // Convert the sapling
        level.setBlock(pos, ModBlocks.GRAVEBLOOM_SAPLING.get().defaultBlockState(), 3);

        // Effects
        level.levelEvent(2005, pos, 0); // bonemeal particles
        level.playSound(null, pos, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS, 0.8f, 1.2f);

        // Consume potion (unless creative)
        if (!player.getAbilities().instabuild) {
            held.shrink(1);

            // Give back empty bottle
            ItemStack bottle = new ItemStack(Items.GLASS_BOTTLE);
            if (!player.getInventory().add(bottle)) {
                player.drop(bottle, false);
            }
        }

        // Stop vanilla from doing anything else
        event.setCancellationResult(InteractionResult.SUCCESS);
        event.setCanceled(true);
    }
}