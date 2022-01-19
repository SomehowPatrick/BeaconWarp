package com.github.unknownbanana.beaconwarp.listener;

import com.github.unknownbanana.beaconwarp.BeaconWarp;
import com.github.unknownbanana.beaconwarp.factories.WarpFactory;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public record BeaconListener(BeaconWarp beaconWarp) implements Listener {
    public static final TextComponent INVENTORY_TITLE = Component.text("Test").color(TextColor.color(100000));

    @EventHandler
    public void onBeaconOpen(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) {
            return;
        }
        if (event.getClickedBlock().getType() != Material.BEACON) {
            return;
        }
        AtomicBoolean location = new AtomicBoolean(false);
        this.beaconWarp.getWarpFactory().getWarpLocation().forEach(warp -> {
            if (warp.equals(event.getClickedBlock().getLocation())) {
                location.set(true);
            }
        });
        if (!location.get()) {
            return;
        }
        Inventory inventory = Bukkit.createInventory(null, InventoryType.CHEST, INVENTORY_TITLE);
        var warpData = this.beaconWarp.getWarpFactory().getWarpData();
        warpData.forEach(warp -> {
            if (!warp.destination().equals(event.getClickedBlock().getLocation())) {
                inventory.addItem(createWarp(warp));
            }
        });
        event.setCancelled(true);
        event.getPlayer().openInventory(inventory);
    }

    @EventHandler
    public void onClick(InventoryClickEvent inventoryClickEvent) {
        if (!(inventoryClickEvent.getWhoClicked() instanceof Player player)) {
            return;
        }
        if (inventoryClickEvent.getClickedInventory() == null) {
            return;
        }
        if (!inventoryClickEvent.getView().title().equals(INVENTORY_TITLE)) {
            return;
        }
        if (inventoryClickEvent.getCurrentItem() == null) {
            return;
        }
        inventoryClickEvent.setCancelled(true);
        var item = inventoryClickEvent.getCurrentItem();
        if (!item.getItemMeta().hasDisplayName()) {
            return;
        }
        var data = this.beaconWarp.getWarpFactory().getWarpData();
        AtomicReference<Location> warp = new AtomicReference<>(null);
        data.forEach(entry -> {
            if (entry.name().equals(item.getItemMeta().getDisplayName())) {
                warp.set(entry.destination());
            }
        });
        if (warp.get() == null) {
            return;
        }
        player.closeInventory();
        player.teleport(warp.get().clone().add(0, 1, 0));
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent playerJoinEvent) {
        this.beaconWarp.getPlayerFactory().playerJoin(playerJoinEvent.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent playerQuitEvent) {
        try {
            this.beaconWarp.getPlayerFactory().playerQuit(playerQuitEvent.getPlayer());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ItemStack createWarp(WarpFactory.WarpData warpData) {
        var itemStack = new ItemStack(warpData.warpMaterial());
        var itemMeta = itemStack.getItemMeta();
        itemMeta.displayName(Component.text(warpData.name()));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

}
