package com.github.unknownbanana.beaconwarp;

import com.github.unknownbanana.beaconwarp.factories.PlayerFactory;
import com.github.unknownbanana.beaconwarp.listener.BeaconListener;
import com.github.unknownbanana.beaconwarp.util.BeaconSpawner;
import com.github.unknownbanana.beaconwarp.factories.WarpFactory;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main entrypoint of the plugin
 *
 * @author UnknownBanana
 * @version 1.0
 * @see JavaPlugin
 * @since 1.0
 */

public class BeaconWarp extends JavaPlugin {
    private WarpFactory warpFactory;
    private PlayerFactory playerFactory;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        this.warpFactory = WarpFactory.loadDataFromFile((YamlConfiguration) getConfig());
        this.playerFactory = new PlayerFactory();
        new BeaconSpawner(this.warpFactory.getWarpLocation()).spawnBeacons();
        Bukkit.getPluginManager().registerEvents(new BeaconListener(this), this);
    }

    public WarpFactory getWarpFactory() {
        return this.warpFactory;
    }

    public PlayerFactory getPlayerFactory() {
        return this.playerFactory;
    }
}
