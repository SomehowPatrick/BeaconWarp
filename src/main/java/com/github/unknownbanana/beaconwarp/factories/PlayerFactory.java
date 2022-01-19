package com.github.unknownbanana.beaconwarp.factories;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerFactory {
    private final Map<Player, List<String>> playerData;

    private final File file;
    private final YamlConfiguration yamlConfiguration;

    public PlayerFactory() {
        this.playerData = new HashMap<>();
        this.file = new File("plugins//BeaconWarp//data.yml");
        this.yamlConfiguration = YamlConfiguration.loadConfiguration(this.file);
    }

    public void playerJoin(Player player) {
        var list = this.yamlConfiguration.getStringList("data." + player.getUniqueId());
        if (!list.isEmpty()) {
            this.playerData.put(player, list);
            return;
        }
        this.playerData.put(player, new ArrayList<>());
    }

    public void playerQuit(Player player) throws IOException {
        this.yamlConfiguration.set("data." + player.getUniqueId(), this.playerData.get(player));
        this.yamlConfiguration.save(this.file);
        this.playerData.remove(player);
    }

    public void discoverWarp(Player player, String name) {
        var data = this.playerData.get(player);
        data.add(name);
        this.playerData.put(player, data);
    }

    public boolean hasWarpDiscovered(Player player, String name) {
        return this.playerData.get(player).contains(name);
    }

    public List<String> getWarps(Player player) {
        if (this.playerData.containsKey(player)) {
            return this.playerData.get(player);
        }
        if (this.yamlConfiguration.contains("data." + player.getUniqueId())) {
            return this.yamlConfiguration.getStringList("data." + player.getUniqueId());
        }
        return new ArrayList<>();
    }

}
