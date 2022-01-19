package com.github.unknownbanana.beaconwarp.factories;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Factory class responsible for loading player data in and from memory
 * for quicker access to the data
 *
 * @author UnknownBanana
 * @version 1.0
 * @since 1.0
 */
public class PlayerFactory {
    private final Map<Player, List<String>> playerData;

    private final File file;
    private final YamlConfiguration yamlConfiguration;

    public PlayerFactory() {
        this.playerData = new HashMap<>();
        this.file = new File("plugins//BeaconWarp//data.yml");
        this.yamlConfiguration = YamlConfiguration.loadConfiguration(this.file);
    }

    /**
     * Loads data into memory
     *
     * @param player The player to load data of
     */

    public void playerJoin(Player player) {
        var list = this.yamlConfiguration.getStringList("data." + player.getUniqueId());
        if (!list.isEmpty()) {
            this.playerData.put(player, list);
            return;
        }
        this.playerData.put(player, new ArrayList<>());
    }

    /**
     * Removes data from memory and saves it to the data file
     *
     * @param player The player to remove data of
     * @throws IOException Because of {@link YamlConfiguration#save(File)}
     */

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

    /**
     * Checks if a player has a warp with a specific identifier discovered
     *
     * @param player The player to check
     * @param name   The identifier to use
     * @return If the warp has been discovered by the player
     */

    public boolean hasWarpDiscovered(Player player, String name) {
        return this.playerData.get(player).contains(name);
    }

    /**
     * Returns a list of all warps a player has discovered
     *
     * @param player The player to use
     * @return A list off all warps a player has discovered
     */

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
