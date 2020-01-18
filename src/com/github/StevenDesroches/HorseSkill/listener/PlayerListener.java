package com.github.StevenDesroches.HorseSkill.listener;

import java.io.*;
import java.util.UUID;

import com.github.StevenDesroches.HorseSkill.HorseSkill;
import com.github.StevenDesroches.HorseSkill.datatype.RpgHorse;
import com.github.StevenDesroches.HorseSkill.datatype.RpgHorseSerializationManager;
import com.github.StevenDesroches.HorseSkill.util.FileWriterUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;


public class PlayerListener implements Listener {

    private HorseSkill horseSkill;
    private File playerDir;


    public PlayerListener(HorseSkill plugin) {
        this.horseSkill = plugin;
        this.playerDir = new File(plugin.getDataFolder(), "/players/");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("HorseSkill.load")) {
            File file = new File(this.playerDir, player.getUniqueId().toString() + ".json");

            if (file.exists()) {
                RpgHorseSerializationManager rpgHorseSerializationManager = this.horseSkill.getRpgHorseSerializationManager();
                String json = FileWriterUtils.loadFile(file);
                RpgHorse rpgHorse = rpgHorseSerializationManager.deserialize(json);
                this.horseSkill.getHorseList().put(rpgHorse.getHorseUUID(), rpgHorse);
                this.horseSkill.getPlayerHorseList().put(player.getUniqueId(), rpgHorse.getHorseUUID());
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (player.hasPermission("HorseSkill.save")) {
            if (this.horseSkill.getPlayerHorseList().containsKey(player.getUniqueId())) {
                UUID horseUUID = this.horseSkill.getPlayerHorseList().get(player.getUniqueId());
                File file = new File(this.playerDir, player.getUniqueId().toString() + ".json");
                RpgHorseSerializationManager rpgHorseSerializationManager = this.horseSkill.getRpgHorseSerializationManager();
                RpgHorse rpgHorse = this.horseSkill.getHorseList().get(horseUUID);
                rpgHorse.unsummon();
                String json = rpgHorseSerializationManager.serialize(rpgHorse);
                FileWriterUtils.saveFile(file, json);
            }
        }

    }
}
