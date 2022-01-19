package com.github.unknownbanana.beaconwarp.warp;

import com.github.unknownbanana.beaconwarp.exception.ConfigParseException;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class WarpFactory {
    private final List<WarpData> warps;

    public WarpFactory() {
        this.warps = new ArrayList<>();
    }

    public WarpFactory loadDataFromFile(YamlConfiguration yamlConfiguration) {
        var warps = yamlConfiguration.getStringList("warps");

        warps.forEach(warp -> {
            try {
                var material = Material.valueOf(yamlConfiguration.getString("data." + warp + ".material"));
                if (material == Material.AIR) {
                    throw new ConfigParseException("The material for warp " + warp + " is not available!");
                }
                var location = yamlConfiguration.getLocation("data." + warp + ".location");
                if (location == null) {
                    throw new ConfigParseException("The location for warp " + warp + " is not available!");
                }
                this.warps.add(new WarpData(warp, material, location));
            } catch (ConfigParseException configParseException) {
                configParseException.printStackTrace();
            }
        });

        return this;
    }

    public WarpData getWarpByName(String name) {
        AtomicReference<WarpData> warpData = new AtomicReference<>(null);
        this.warps.forEach(warp -> {
            if (warp.name.equalsIgnoreCase(name)) {
                warpData.set(warp);
            }
        });
        return warpData.get();
    }

    public List<Location> getWarpLocation() {
        List<Location> locations = new ArrayList<>();
        this.warps.forEach(warp -> locations.add(warp.destination));
        return locations;
    }

    public record WarpData(String name, Material warpMaterial, Location destination) {
    }
}
