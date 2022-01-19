package com.github.unknownbanana.beaconwarp;

import com.github.unknownbanana.beaconwarp.util.BeaconSpawner;
import com.github.unknownbanana.beaconwarp.warp.WarpFactory;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class BeaconWarp extends JavaPlugin {
    private WarpFactory warpFactory;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        this.warpFactory = new WarpFactory().loadDataFromFile((YamlConfiguration) getConfig());
        new BeaconSpawner(this.warpFactory.getWarpLocation()).spawnBeacons();
    }

    public WarpFactory getWarpFactory() {
        return this.warpFactory;
    }
}
