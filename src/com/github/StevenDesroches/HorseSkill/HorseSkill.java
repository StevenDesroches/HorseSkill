package com.github.StevenDesroches.HorseSkill;

import com.github.StevenDesroches.HorseSkill.commands.HorseCommand;
import com.github.StevenDesroches.HorseSkill.datatype.RpgHorse;
import com.github.StevenDesroches.HorseSkill.datatype.RpgHorseSerializationManager;
import com.github.StevenDesroches.HorseSkill.listener.EntityListener;
import com.github.StevenDesroches.HorseSkill.listener.PlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HorseSkill extends JavaPlugin {
    public static HorseSkill instance;
    private Map<UUID, RpgHorse> horseList;
    private Map<UUID, UUID> playerHorseList;
    private RpgHorseSerializationManager rpgHorseSerializationManager;

    /*public HorseSkill getInstance() {
        return this.instance;
    }*/

    public Map<UUID, RpgHorse> getHorseList() {
        return this.horseList;
    }

    public Map<UUID, UUID> getPlayerHorseList() {
        return this.playerHorseList;
    }

    public RpgHorseSerializationManager getRpgHorseSerializationManager() {
        return this.rpgHorseSerializationManager;
    }

    public void onEnable() {
        instance = this;
        horseList = new HashMap<>();
        playerHorseList = new HashMap<>();
        rpgHorseSerializationManager = new RpgHorseSerializationManager();

        this.getServer().getPluginManager().registerEvents(new EntityListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerListener(instance), this);
        this.getCommand("horse").setExecutor(new HorseCommand(instance));
        Bukkit.getConsoleSender().sendMessage("[HorseSkill] onEnable");
    }

    public void onDisable() {
    }
}
