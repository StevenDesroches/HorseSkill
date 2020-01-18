package com.github.StevenDesroches.HorseSkill;

import com.github.StevenDesroches.HorseSkill.commands.HorseCommand;
import com.github.StevenDesroches.HorseSkill.datatype.rpgHorse;
import com.github.StevenDesroches.HorseSkill.listener.EntityListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HorseSkill extends JavaPlugin {
    private static HorseSkill instance;
    private static Map<UUID, rpgHorse> horseList;
    private static Map<UUID, UUID> playerHorseList;

    public static HorseSkill getInstance() {
        return instance;
    }

    public static Map<UUID, rpgHorse> getHorseList() {
        return horseList;
    }

    public static Map<UUID, UUID> getPlayerHorseList() {
        return playerHorseList;
    }

    public void onEnable() {
        instance = this;
        horseList = new HashMap<>();
        playerHorseList = new HashMap<>();
        this.getServer().getPluginManager().registerEvents(new EntityListener(), this);
        this.getCommand("horse").setExecutor(new HorseCommand());
        Bukkit.getConsoleSender().sendMessage("[HorseSkill] onEnable");
    }

    public void onDisable() {
    }
}
