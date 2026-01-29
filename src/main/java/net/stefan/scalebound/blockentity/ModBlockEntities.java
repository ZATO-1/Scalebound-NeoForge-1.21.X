package net.stefan.scalebound.blockentity;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.stefan.scalebound.ScaleboundMod;
import net.stefan.scalebound.block.ModBlocks;

public final class ModBlockEntities {
    private ModBlockEntities() {}

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, ScaleboundMod.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ArmWorkbenchBlockEntity>> ARM_WORKBENCH_BE =
            BLOCK_ENTITIES.register("arm_workbench",
                    () -> BlockEntityType.Builder.of(ArmWorkbenchBlockEntity::new, ModBlocks.ARM_WORKBENCH.get()).build(null)
            );
}
