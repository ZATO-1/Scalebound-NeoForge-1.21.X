package net.stefan.scalebound.event;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

import net.stefan.scalebound.ScaleboundMod;

@EventBusSubscriber(modid = ScaleboundMod.MOD_ID)
public final class WitherRoseRitualEvents {
    private WitherRoseRitualEvents() {}

    private static final String NBT_HITS = "scalemod_rose_hits";
    private static final int REQUIRED_HITS = 5;

    @SubscribeEvent
    public static void onLivingDamagePost(LivingDamageEvent.Post event) {
        LivingEntity target = event.getEntity();

        // Only Wither Skeletons
        if (!(target instanceof WitherSkeleton)) return;

        // Server only
        Level level = target.level();
        if (level.isClientSide) return;

        // Must be damaged by a player
        if (!(event.getSource().getEntity() instanceof Player player)) return;

        // Player must be holding a poppy in main hand
        ItemStack held = player.getMainHandItem();
        if (!held.is(Items.POPPY)) return;

        // Optional: ignore "0 damage" hits (uncomment if needed)
        // if (event.getAmount() <= 0.0F) return;

        CompoundTag data = target.getPersistentData();
        int hits = data.getInt(NBT_HITS) + 1;
        data.putInt(NBT_HITS, hits);

        if (hits >= REQUIRED_HITS) {
            data.putInt(NBT_HITS, 0);

            // Consume 1 poppy
            held.shrink(1);

            // Reward wither rose
            ItemStack reward = new ItemStack(Items.WITHER_ROSE, 1);
            if (!player.getInventory().add(reward)) {
                player.drop(reward, false);
            }
        }
    }
}
