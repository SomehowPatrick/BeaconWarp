package com.github.unknownbanana.beaconwarp;

import com.github.unknownbanana.beaconwarp.listener.BeaconListener;
import com.github.unknownbanana.beaconwarp.util.BeaconSpawner;
import com.github.unknownbanana.beaconwarp.warp.WarpFactory;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class BeaconWarp extends JavaPlugin {
    private WarpFactory warpFactory;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        this.warpFactory = new WarpFactory().loadDataFromFile((YamlConfiguration) getConfig());
        new BeaconSpawner(this.warpFactory.getWarpLocation()).spawnBeacons();
        Bukkit.getPluginManager().registerEvents(new BeaconListener(this), this);
    }

    public WarpFactory getWarpFactory() {
        return this.warpFactory;
    }
}
