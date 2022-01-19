package com.github.unknownbanana.beaconwarp.warp;

import com.github.unknownbanana.beaconwarp.exception.ConfigParseException;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

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
                var material = yamlConfiguration.getItemStack("data." + warp + ".material");
                if (material == null) {
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

    public record WarpData(String name, ItemStack warpMaterial, Location destination) {
    }
}
