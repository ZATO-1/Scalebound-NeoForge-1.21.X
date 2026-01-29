package net.stefan.scalebound.menu;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.stefan.scalebound.ScaleboundMod;

public final class ModMenus {
    private ModMenus() {}

    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, ScaleboundMod.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<ArmWorkbenchMenu>> ARM_WORKBENCH_MENU =
            MENUS.register("arm_workbench", () -> IMenuTypeExtension.create(ArmWorkbenchMenu::new));
}
