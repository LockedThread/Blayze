package dev.lockedthread.blayze.blayzecore.menus;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public abstract class Menu implements InventoryHolder {

    private final Map<Integer, MenuItem> menuItemMap;
    private final Inventory inventory;

    public Menu(String name, int slots) {
        if (slots == 5) {
            this.inventory = Bukkit.createInventory(this, InventoryType.HOPPER, ChatColor.translateAlternateColorCodes('&', name));
        } else if (slots > 54) {
            throw new RuntimeException("Unable to create Menu with name \"" + name + "\" because " + slots + " is greater than 54");
        } else {
            this.inventory = Bukkit.createInventory(this, slots, ChatColor.translateAlternateColorCodes('&', name));
        }
        this.menuItemMap = new HashMap<>();
    }

    @Nullable
    public MenuItem getMenuItem(int index) {
        return menuItemMap.get(index);
    }

    @Nullable
    public Map.Entry<Integer, MenuItem> getMenuItemWhere(Predicate<MenuItem> predicate) {
        for (Map.Entry<Integer, MenuItem> entry : menuItemMap.entrySet()) {
            if (predicate.test(entry.getValue())) {
                return entry;
            }
        }
        return null;
    }

    public void setMenuItem(int index, MenuItem menuItem) {
        int slots = getSlots();
        if (index >= slots) {
            throw new RuntimeException("Unable to add MenuItem at index " + index + " because the size of the Menu is " + slots);
        }
        menuItemMap.put(index, menuItem);
        inventory.setItem(index, menuItem.getItemStack());
    }

    public void addMenuItem(MenuItem menuItem) {
        setMenuItem(getInventory().firstEmpty(), menuItem);
    }

    public int getSlots() {
        return inventory.getSize();
    }

    @Override
    @NotNull
    public Inventory getInventory() {
        return inventory;
    }
}
