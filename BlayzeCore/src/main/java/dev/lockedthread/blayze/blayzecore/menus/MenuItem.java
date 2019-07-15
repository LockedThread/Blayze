package dev.lockedthread.blayze.blayzecore.menus;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public abstract class MenuItem {

    private ItemStack itemStack;
    private Consumer<InventoryClickEvent> clickConsumer;

    public MenuItem(ItemStack itemStack, Consumer<InventoryClickEvent> clickConsumer) {
        this.itemStack = itemStack;
        this.clickConsumer = clickConsumer;
    }

    public MenuItem(ItemStack itemStack) {
        this(itemStack, null);
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public Consumer<InventoryClickEvent> getClickConsumer() {
        return clickConsumer;
    }

    public void setClickConsumer(Consumer<InventoryClickEvent> clickConsumer) {
        this.clickConsumer = clickConsumer;
    }
}
