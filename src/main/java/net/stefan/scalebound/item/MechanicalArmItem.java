package net.stefan.scalebound.item;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.EquipmentSlot;

public class MechanicalArmItem extends Item {

    private static final double RANGE = 18.0;
    private static final int COOLDOWN_TICKS = 20; // 1 second
    private static final int DURABILITY_COST = 1;

    public MechanicalArmItem(Properties props) {
        super(props);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        // We only want offhand usage for this design
        if (hand != InteractionHand.OFF_HAND) {
            return InteractionResultHolder.pass(stack);
        }

        // client: let server do physics, but return success so animation feels responsive
        if (level.isClientSide) {
            return InteractionResultHolder.success(stack);
        }

        // cooldown check
        if (player.getCooldowns().isOnCooldown(this)) {
            return InteractionResultHolder.fail(stack);
        }

        ServerLevel serverLevel = (ServerLevel) level;

        Vec3 start = player.getEyePosition(1.0f);
        Vec3 look = player.getLookAngle();
        Vec3 end = start.add(look.scale(RANGE));

        // 1) Entity raycast first (feels good for "hooking" mobs)
        AABB searchBox = player.getBoundingBox()
                .expandTowards(look.scale(RANGE))
                .inflate(1.0);

        EntityHitResult entityHit = ProjectileUtil.getEntityHitResult(
                serverLevel,
                player,
                start,
                end,
                searchBox,
                e -> e != player && !e.isSpectator() && e.isPickable()
        );

        if (entityHit != null) {
            Entity target = entityHit.getEntity();
            pullEntityToPlayer(player, target);

            // costs + cooldown
            player.getCooldowns().addCooldown(this, COOLDOWN_TICKS);
            damageArm(stack, player);
            return InteractionResultHolder.success(stack);
        }

        // 2) Block raycast
        BlockHitResult blockHit = level.clip(new ClipContext(
                start,
                end,
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                player
        ));

        if (blockHit.getType() == HitResult.Type.BLOCK) {
            pullPlayerToPoint(player, blockHit.getLocation());

            player.getCooldowns().addCooldown(this, COOLDOWN_TICKS);
            damageArm(stack, player);
            return InteractionResultHolder.success(stack);
        }

        return InteractionResultHolder.pass(stack);
    }

    private static void pullPlayerToPoint(Player player, Vec3 target) {
        Vec3 dir = target.subtract(player.position());
        double dist = dir.length();

        // Too close = don't yank
        if (dist < 2.0) return;

        // Strength scales with distance but clamps
        double strength = Math.min(1.6, dist * 0.12);

        Vec3 vel = dir.normalize().scale(strength).add(0.0, 0.18, 0.0);

        player.setDeltaMovement(vel);
        player.hurtMarked = true;   // forces motion sync
        player.hasImpulse = true;
        player.fallDistance = 0.0f; // reduces fall damage jank
    }

    private static void pullEntityToPlayer(Player player, Entity target) {
        Vec3 dir = player.position().subtract(target.position());
        double dist = dir.length();
        if (dist < 1.0) return;

        double strength = Math.min(1.4, dist * 0.10);

        Vec3 vel = dir.normalize().scale(strength).add(0.0, 0.12, 0.0);
        target.setDeltaMovement(vel);
        target.hurtMarked = true;
    }

    private static void damageArm(ItemStack stack, Player player) {
        stack.hurtAndBreak(DURABILITY_COST, player, EquipmentSlot.OFFHAND);
    }

}
