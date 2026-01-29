package net.stefan.scalebound.menu;

import net.minecraft.world.SimpleContainer;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;

import net.stefan.scalebound.blockentity.ArmWorkbenchBlockEntity;
import net.stefan.scalebound.item.ModItems;
import net.stefan.scalebound.menu.ModMenus;

public class ArmWorkbenchMenu extends AbstractContainerMenu {

    private final Container container;
    private final ContainerLevelAccess access;

    // Client constructor (called via IMenuTypeExtension, reads BlockPos)
    public ArmWorkbenchMenu(int id, Inventory playerInv, FriendlyByteBuf buf) {
        this(id, playerInv, getClientContainer(playerInv, buf), ContainerLevelAccess.NULL);
    }

    // Server constructor (from BlockEntity)
    public ArmWorkbenchMenu(int id, Inventory playerInv, ArmWorkbenchBlockEntity be, ContainerLevelAccess access) {
        this(id, playerInv, (Container) be, access);
    }

    private ArmWorkbenchMenu(int id, Inventory playerInv, Container container, ContainerLevelAccess access) {
        super(ModMenus.ARM_WORKBENCH_MENU.get(), id);
        this.container = container;
        this.access = access;

        // Input slots
        this.addSlot(new Slot(container, 0, 44, 35) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.is(ModItems.MECHANICAL_ARM_CORE.get());
            }
        });

        this.addSlot(new Slot(container, 1, 62, 35) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                // For now: only grapple module (later: allow any module tag)
                return stack.is(ModItems.GRAPPLE_MODULE.get());
            }
        });

        // Output slot
        this.addSlot(new Slot(container, 2, 116, 35) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }

            @Override
            public void onTake(Player player, ItemStack stack) {
                // Consume inputs (Option A)
                ArmWorkbenchMenu.this.consumeInputs();
                super.onTake(player, stack);
            }
        });

        // Player inventory (standard)
        addPlayerInventory(playerInv);
        addPlayerHotbar(playerInv);

        // Ensure output is computed on open
        this.slotsChanged(container);
    }

    private static Container getClientContainer(Inventory playerInv, FriendlyByteBuf buf) {
        BlockPos pos = buf.readBlockPos();
        if (playerInv.player.level().getBlockEntity(pos) instanceof Container c) {
            return c;
        }
        // Fallback (shouldn't happen): empty 3-slot container so client doesn't crash
        return new SimpleContainer(3);
    }

    @Override
    public void slotsChanged(Container container) {
        super.slotsChanged(container);

        // Only compute output on server. It will sync to client automatically.
        Player player = this.getCarried().isEmpty() ? null : null; // ignore
        if (this.access == ContainerLevelAccess.NULL) return; // client menu: do nothing

        ItemStack core = container.getItem(0);
        ItemStack mod = container.getItem(1);

        ItemStack result = ItemStack.EMPTY;

        // Core + Grapple Module => Grapple Arm (your existing mechanical_arm item)
        if (!core.isEmpty() && !mod.isEmpty()
                && core.is(ModItems.MECHANICAL_ARM_CORE.get())
                && mod.is(ModItems.GRAPPLE_MODULE.get())) {

            result = new ItemStack(ModItems.MECHANICAL_ARM.get());
        }

        container.setItem(2, result);
    }

    private void consumeInputs() {
        ItemStack core = container.getItem(0);
        ItemStack mod = container.getItem(1);

        if (!core.isEmpty()) core.shrink(1);
        if (!mod.isEmpty()) mod.shrink(1);

        container.setItem(0, core.isEmpty() ? ItemStack.EMPTY : core);
        container.setItem(1, mod.isEmpty() ? ItemStack.EMPTY : mod);
        container.setItem(2, ItemStack.EMPTY);

        this.slotsChanged(container);
    }

    @Override
    public boolean stillValid(Player player) {
        return container.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack copy = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot == null || !slot.hasItem()) return ItemStack.EMPTY;

        ItemStack stack = slot.getItem();
        copy = stack.copy();

        // slot indices:
        // 0..2 workbench slots
        // 3..(3+26) player inv
        // last 9 hotbar

        if (index == 2) {
            // shift-click output -> move to player inventory
            if (!this.moveItemStackTo(stack, 3, this.slots.size(), true)) return ItemStack.EMPTY;
            slot.onQuickCraft(stack, copy);
        } else if (index < 3) {
            // shift-click inputs -> move to player inventory
            if (!this.moveItemStackTo(stack, 3, this.slots.size(), false)) return ItemStack.EMPTY;
        } else {
            // from player inventory -> try inputs
            if (stack.is(ModItems.MECHANICAL_ARM_CORE.get())) {
                if (!this.moveItemStackTo(stack, 0, 1, false)) return ItemStack.EMPTY;
            } else if (stack.is(ModItems.GRAPPLE_MODULE.get())) {
                if (!this.moveItemStackTo(stack, 1, 2, false)) return ItemStack.EMPTY;
            } else {
                return ItemStack.EMPTY;
            }
        }

        if (stack.isEmpty()) slot.set(ItemStack.EMPTY);
        else slot.setChanged();

        return copy;
    }

    private void addPlayerInventory(Inventory inv) {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(inv, 9 + col + row * 9, 8 + col * 18, 84 + row * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory inv) {
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(inv, col, 8 + col * 18, 142));
        }
    }
}
