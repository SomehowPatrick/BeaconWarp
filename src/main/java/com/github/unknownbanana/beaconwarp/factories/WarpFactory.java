package com.github.unknownbanana.beaconwarp.factories;

import com.github.unknownbanana.beaconwarp.exception.ConfigParseException;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Factory record responsible for storing the warps
 * in memory and providing methods for accessing them
 *
 * @author UnknownBanana
 * @version 1.0
 * @since 1.0
 */

public record WarpFactory(List<WarpData> warps,
                          String warpMessage) {

    /**
     * Factory method loading the warps from a config file
     *
     * @param yamlConfiguration The config file
     * @return An instance of the WarpFactory class
     */

    public static WarpFactory loadDataFromFile(YamlConfiguration yamlConfiguration) {
        var warps = yamlConfiguration.getStringList("warps");
        var warpData = new ArrayList<WarpData>();
        AtomicReference<String> warpMessage = new AtomicReference<>("");

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
                var name = yamlConfiguration.getString("data." + warp + ".name");
                if (name == null) {
                    throw new ConfigParseException("The name for warp " + warp + " is not available!");
                }
                name = ChatColor.translateAlternateColorCodes('&', name);
                var slot = yamlConfiguration.getInt("data." + warp + ".slot");
                if (slot > 26) {
                    throw new ConfigParseException("The slot " + slot + " is not supported for an inventory of size 27 slots");
                }
                var message = yamlConfiguration.getString("config.message");
                if (message == null) {
                    throw new ConfigParseException("The message in the config is not set!");
                }
                warpMessage.set(ChatColor.translateAlternateColorCodes('&', message));
                warpData.add(new WarpData(warp, name, material, location, slot));
            } catch (ConfigParseException configParseException) {
                configParseException.printStackTrace();
            }
        });
        return new WarpFactory(warpData, warpMessage.get());
    }

    /**
     * Returns a list of all warps
     *
     * @return A list of all warps
     */

    public List<Location> getWarpLocation() {
        List<Location> locations = new ArrayList<>();
        this.warps.forEach(warp -> locations.add(warp.destination));
        return locations;
    }

    public List<WarpData> getWarpData() {
        return this.warps;
    }



    /**
     * Data class holding informations about a warp
     */

    public record WarpData(String identifier, String name, Material warpMaterial, Location destination, int slot) {
    }
}
