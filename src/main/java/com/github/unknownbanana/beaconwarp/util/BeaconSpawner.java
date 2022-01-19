package com.github.unknownbanana.beaconwarp.util;

import org.bukkit.Location;
import org.bukkit.Material;

import java.util.List;

/**
 * Utility record for spawning the warp beacons
 *
 * @author UnknownBanana
 * @version 1.0
 * @since 1.0
 */

public record BeaconSpawner(List<Location> locations) {

    /**
     * Spawns the beacons
     */

    public void spawnBeacons() {
        this.locations.forEach(location -> location.getWorld().getBlockAt(location).setType(Material.BEACON));
    }
}
