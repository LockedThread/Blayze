package dev.lockedthread.blayze.blayzecore.items;

import dev.lockedthread.blayze.blayzecore.events.EventFilters;
import dev.lockedthread.blayze.blayzecore.events.EventPost;
import dev.lockedthread.blayze.blayzecore.units.Unit;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class UnitCustomItem extends Unit {

    @Override
    public void setup() {
        /*CommandPost.of()
                .builder()
                .assertPermission("gsg.customitem")
                .handler(c -> {
                    if (c.getRawArgs().length == 0) {
                        c.reply("", "&d/customitem list", "&d/customitem give [player] [item] {amount} {PRESET-NBT...}", "&d/customitem info [item]", "&d/customitem migrate [customitem] [migration-type] [arguments] - Ask locked for help!", "\n");
                    } else if (c.getRawArgs().length == 1) {
                        if (c.getRawArg(0).equalsIgnoreCase("list")) {
                            c.reply("", "&dCustomItems: &f" + Joiner.on(", ").skipNulls().join(CustomItem.getCustomItemMap().keySet()), "");
                        } else {
                            c.reply("&cInvalid argument");
                        }
                    } else if (c.getRawArgs().length == 2) {
                        if (c.getRawArg(0).equalsIgnoreCase("info")) {
                            CustomItem customItem = c.getArg(1).forceParse(CustomItem.class);
                            c.reply("",
                                    "&dName: &f" + customItem.getName(),
                                    "&dBlockBreakEvent: &f" + (customItem.getBreakEventConsumer() != null),
                                    "&dBlockPlaceEvent: &f" + (customItem.getPlaceEventConsumer() != null),
                                    "&dPlayerInteractEvent: &f" + (customItem.getInteractEventConsumer() != null),
                                    "");
                        } else {
                            c.reply("&cInvalid argument");
                        }
                    } else if (c.getRawArgs().length >= 3) {
                        if (c.getRawArg(0).equalsIgnoreCase("give")) {
                            Player target = c.getArg(1).forceParse(Player.class);
                            CustomItem customItem = c.getArg(2).forceParse(CustomItem.class);
                            int amount = c.getArg(3).parse(int.class).orElse(1);

                            Map<String, Object> nbt = null;
                            if (c.getRawArgs().length > 4) {
                                String[] strings = Arrays.copyOfRange(c.getRawArgs(), 4, c.getRawArgs().length);
                                if (strings.length % 2 == 0) {
                                    nbt = new HashMap<>();
                                    String key = null;
                                    for (int i = 0; i < strings.length; i++) {
                                        String value = strings[i];
                                        if (i % 2 != 0) {
                                            if (Utils.isBoolean(value)) {
                                                nbt.put(key, Boolean.parseBoolean(value));
                                            } else if (Utils.isDouble(value)) {
                                                nbt.put(key, Double.parseDouble(value));
                                            } else if (Utils.isInteger(value)) {
                                                nbt.put(key, Integer.parseInt(value));
                                            } else {
                                                nbt.put(key, value);
                                            }
                                        } else {
                                            key = value;
                                        }
                                    }
                                } else {
                                    c.reply("&cUnable to set NBT, the can't have a null value for " + strings[strings.length - 1]);
                                    return;
                                }
                            }


                            ItemStack itemStack;
                            if (nbt != null) {
                                itemStack = customItem.getItemEdit().getEditedItemStack(nbt);
                            } else {
                                try {
                                    itemStack = customItem.getItemStack();
                                } catch (NullPointerException ex) {
                                    c.reply("&cThis item requires meta data");
                                    return;
                                }
                            }

                            for (int i = 0; i < amount; i++) {
                                target.getInventory().addItem(itemStack);
                            }
                            c.reply("&eYou have given " + target.getName() + " " + amount + " " + customItem.getName() + "s");
                        }
                    } else {
                        c.reply("&cInvalid argument");
                    }
                }).post(BLAYZE_CORE, "customitem", "customitems");*/

        EventPost.of(BlockBreakEvent.class, EventPriority.HIGH)
                .filter(EventFilters.getIgnoreCancelled())
                .filter(EventFilters.getIgnoreHandNull())
                .handle(event -> {
                    final CustomItem customItem = CustomItem.findCustomItem(event.getPlayer().getItemInHand());
                    if (customItem != null && customItem.getBreakEventConsumer() != null) {
                        customItem.getBreakEventConsumer().accept(event);
                    }
                }).post(BLAYZE_CORE);

        EventPost.of(BlockPlaceEvent.class, EventPriority.HIGH)
                .filter(EventFilters.getIgnoreCancelled())
                .filter(EventFilters.getIgnoreHandNull())
                .handle(event -> {
                    final CustomItem customItem = CustomItem.findCustomItem(event.getItemInHand());
                    if (customItem != null && customItem.getPlaceEventConsumer() != null) {
                        customItem.getPlaceEventConsumer().accept(event);
                    }
                }).post(BLAYZE_CORE);

        EventPost.of(PlayerInteractEvent.class, EventPriority.HIGHEST)
                .filter(EventFilters.getIgnoreHandNull())
                .handle(event -> {
                    final CustomItem customItem = CustomItem.findCustomItem(event.getItem());
                    if (customItem != null && customItem.getInteractEventConsumer() != null) {
                        customItem.getInteractEventConsumer().accept(event);
                    }
                }).post(BLAYZE_CORE);
    }
}
