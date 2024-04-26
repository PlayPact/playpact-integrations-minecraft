package net.playpact.minecraft;

import org.bukkit.plugin.java.JavaPlugin;

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
public class PlayPactPlugin extends JavaPlugin {

    PlayPactClient playPactClient;

    @Override
    public void onEnable(){
        getLogger().info("PlayPact enabled.");
        playPactClient = new PlayPactClient(getLogger());
        if(playPactClient.checkPlayPactConnection()){
            getLogger().info("PlayPact verified.");
        } else {
            getLogger().info("PlayPact not reached. Disabling PlayPact..");
            return;
        }

        getServer().getPluginManager().registerEvents(new PlayPactEventListener(this, playPactClient), this);

    }

    @Override
    public void onDisable(){
        getLogger().info("PlayPact disabled.");
    }
}
