package net.playpact.minecraft;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Set;
import java.util.UUID;

/**
 * Copyright 2024 (C) CUTE.DEV
 *
 * @author: Lukas Klepper
 * <p>
 * -----------------------------------------------------------------------------
 * Revision History
 * -----------------------------------------------------------------------------
 * VERSION     AUTHOR/      DESCRIPTION OF CHANGE
 * OLD/NEW     DATE
 * -----------------------------------------------------------------------------
 * NO RC   | Lukas Klepper | Initial Create.
 * | 11.02.2024    |
 * ---------|---------------|---------------------------------------------------
 */
public class PlayPactEventListener implements Listener {

    public JavaPlugin plugin;
    public PlayPactClient playpact;

    public PlayPactEventListener(JavaPlugin _plugin, PlayPactClient _playpact){
        this.plugin = _plugin;
        this.playpact = _playpact;
    }

    @EventHandler
    public void onPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        plugin.getLogger().info("Checking PlayPact stats for " + event.getName());

        if(isPlayerOperator(event.getUniqueId()) || playpact.isPlayerAllowedToJoin(event.getUniqueId(), this.getServerIdentifier())){
            plugin.getLogger().info("Allowing " + event.getName() + " to enter.");
            event.allow();
        } else {
            plugin.getLogger().info("Disallowing " + event.getName() + " to enter.");
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "PlayPact challenge failed.");
        }
    }

    @EventHandler
    public void onPlayerJoinedServer(PlayerJoinEvent event) {
        playpact.playerJoinedServer(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerQuitServer(PlayerQuitEvent event) {
        playpact.playerLeftServer(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerRemoved(PlayerKickEvent event){
        if(event.getPlayer().isBanned()){
            onPlayerBanned(event);
        } else {
            onPlayerKicked(event);
        }
    }

    private void onPlayerKicked(PlayerKickEvent event){
        playpact.playerWasKicked(event.getPlayer().getUniqueId(), event.getReason());
    }

    private void onPlayerBanned(PlayerKickEvent event){
        playpact.playerWasBanned(event.getPlayer().getUniqueId(), event.getReason());
    }

    private Boolean isPlayerOperator(UUID playerId) {
        Set<OfflinePlayer> operators = plugin.getServer().getOperators();
        return operators.stream().
                filter(offlinePlayer -> offlinePlayer.getUniqueId()
                        .equals(playerId))
                .count() == 1;
    }

    private String getServerIdentifier(){
        return "xyz";
    }
}
