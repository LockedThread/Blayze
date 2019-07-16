package dev.lockedthread.blayze.blayzecore.items;

import dev.lockedthread.blayze.blayzecore.BlayzeCore;
import dev.lockedthread.blayze.blayzecore.module.Module;
import dev.lockedthread.blayze.blayzecore.utils.NBTItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class CustomItem {

    private static final Map<String, CustomItem> CUSTOM_ITEM_MAP = new HashMap<>();

    private final String moduleName;
    private final String name;
    private ItemStack itemStack;
    private Consumer<PlayerInteractEvent> interactEventConsumer;
    private Consumer<BlockBreakEvent> breakEventConsumer;
    private Consumer<BlockPlaceEvent> placeEventConsumer;
    private ItemEdit itemEdit;

    public CustomItem(Module module, ItemStackBuilder itemStackBuilder, String name) {
        this(module, new NBTItem(itemStackBuilder.build()).set(name, true).buildItemStack(), name);
        CUSTOM_ITEM_MAP.put(name, this);
    }

    public CustomItem(Module module, ItemStack itemStack, String name) {
        this.moduleName = module.getName();
        this.name = name;
        this.itemStack = itemStack;
        CUSTOM_ITEM_MAP.put(name, this);
    }

    @NotNull
    public static CustomItem of(Module module, ConfigurationSection section, String name) {
        return of(module, ItemStackBuilder.of(section), name);
    }

    @NotNull
    public static CustomItem of(Module module, ConfigurationSection configurationSection) {
        return of(module, configurationSection, configurationSection.getName());
    }

    @NotNull
    public static CustomItem of(Module module, ItemStackBuilder itemStackBuilder, String name) {
        return new CustomItem(module, new NBTItem(itemStackBuilder.build()).set(name, true).buildItemStack(), name);
    }

    @NotNull
    public static Map<String, CustomItem> getCustomItemMap() {
        return CUSTOM_ITEM_MAP;
    }

    // TODO: Optimize this somehow
    public static CustomItem findCustomItem(ItemStack itemStack) {
        if (itemStack == null || itemStack.getAmount() == 0 || itemStack.getType() == Material.AIR) return null;
        if (BlayzeCore.getInstance().getConfig().getBoolean("items-check-nbt")) {
            return new NBTItem(itemStack).getKeys()
                    .stream()
                    .map(CUSTOM_ITEM_MAP::get)
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElse(null);
        }
        return getCustomItemMap()
                .values()
                .stream()
                .filter(customItem -> customItem.getItemStack().getType() == itemStack.getType())
                .filter(customItem -> customItem.getItemStack().hasItemMeta() && itemStack.hasItemMeta() && customItem.getItemStack().getItemMeta().hasDisplayName() && itemStack.getItemMeta().hasDisplayName())
                .filter(customItem -> customItem.getItemStack().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', itemStack.getItemMeta().getDisplayName())))
                .findFirst()
                .orElse(null);
    }

    @Nullable
    public static CustomItem getCustomItem(String name) {
        return CUSTOM_ITEM_MAP.get(name);
    }

    @NotNull
    public ItemStack getItemStack() {
        return itemEdit != null ? itemEdit.getEditedItemStack() : itemStack;
    }

    @NotNull
    public ItemStack getOriginalItemStack() {
        return itemStack;
    }

    @NotNull
    public String getName() {
        return name;
    }

    @Nullable
    public Consumer<PlayerInteractEvent> getInteractEventConsumer() {
        return interactEventConsumer;
    }

    @NotNull
    public CustomItem setInteractEventConsumer(Consumer<PlayerInteractEvent> interactEventConsumer) {
        this.interactEventConsumer = interactEventConsumer;
        return this;
    }

    @Nullable
    public Consumer<BlockBreakEvent> getBreakEventConsumer() {
        return breakEventConsumer;
    }

    @NotNull
    public CustomItem setBreakEventConsumer(Consumer<BlockBreakEvent> breakEventConsumer) {
        this.breakEventConsumer = breakEventConsumer;
        return this;
    }

    @Nullable
    public Consumer<BlockPlaceEvent> getPlaceEventConsumer() {
        return placeEventConsumer;
    }

    @NotNull
    public CustomItem setPlaceEventConsumer(Consumer<BlockPlaceEvent> placeEventConsumer) {
        this.placeEventConsumer = placeEventConsumer;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CustomItem that = (CustomItem) o;

        return Objects.equals(moduleName, that.moduleName) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        int result = moduleName != null ? moduleName.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (itemStack != null ? itemStack.hashCode() : 0);
        result = 31 * result + (interactEventConsumer != null ? interactEventConsumer.hashCode() : 0);
        result = 31 * result + (breakEventConsumer != null ? breakEventConsumer.hashCode() : 0);
        result = 31 * result + (placeEventConsumer != null ? placeEventConsumer.hashCode() : 0);
        result = 31 * result + (itemEdit != null ? itemEdit.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CustomItem{" +
                "name='" + name + '\'' +
                ", itemStack=" + itemStack +
                ", interactEventConsumer=" + interactEventConsumer +
                ", breakEventConsumer=" + breakEventConsumer +
                ", placeEventConsumer=" + placeEventConsumer +
                '}';
    }

    @Nullable
    public ItemEdit getItemEdit() {
        return itemEdit;
    }

    public void setItemEdit(ItemEdit itemEdit) {
        this.itemEdit = itemEdit;
    }

    @NotNull
    public String getModuleName() {
        return moduleName;
    }

    public interface ItemEdit {

        ItemStack getEditedItemStack();

        default <T> ItemStack getEditedItemStack(Map<String, T> map) {
            return null;
        }
    }
}
