package net.stefan.scalebound.event;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import net.stefan.scalebound.ScaleboundMod;
import net.stefan.scalebound.item.ModItems;

@EventBusSubscriber(modid = ScaleboundMod.MOD_ID)
public final class ArmEvents {
    private ArmEvents() {}

    // 1.21+: Attribute modifiers are keyed by ResourceLocation
    private static final ResourceLocation ARM_ATTACK_SPEED_ID =
            ResourceLocation.fromNamespaceAndPath(ScaleboundMod.MOD_ID, "mechanical_arm_attack_speed");

    private static boolean hasArmOffhand(Player player) {
        ItemStack offhand = player.getOffhandItem();
        return offhand.is(ModItems.MECHANICAL_ARM.get());
    }

    @SubscribeEvent
    public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        Player player = event.getEntity();
        if (!hasArmOffhand(player)) return;

        event.setNewSpeed(event.getNewSpeed() * 1.35f);
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();

        // Do attribute mutation server-side only
        if (player.level().isClientSide) return;

        var inst = player.getAttribute(Attributes.ATTACK_SPEED);
        if (inst == null) return;

        boolean equipped = hasArmOffhand(player);
        boolean hasMod = inst.getModifier(ARM_ATTACK_SPEED_ID) != null;

        if (equipped && !hasMod) {
            inst.addPermanentModifier(new AttributeModifier(
                    ARM_ATTACK_SPEED_ID,
                    0.35, // tune later
                    AttributeModifier.Operation.ADD_VALUE
            ));
        } else if (!equipped && hasMod) {
            inst.removeModifier(ARM_ATTACK_SPEED_ID);
        }
    }
}
