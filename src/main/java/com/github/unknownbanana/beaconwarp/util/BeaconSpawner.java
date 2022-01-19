package com.github.unknownbanana.beaconwarp.util;

import org.bukkit.Location;
import org.bukkit.Material;

import java.util.List;

public record BeaconSpawner(List<Location> locations) {

    public void spawnBeacons() {
        this.locations.forEach(location -> location.getWorld().getBlockAt(location).setType(Material.BEACON));
    }
}
