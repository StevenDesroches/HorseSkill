package com.github.StevenDesroches.HorseSkill.commands;

import com.github.StevenDesroches.HorseSkill.HorseSkill;
import com.github.StevenDesroches.HorseSkill.datatype.rpgHorse;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HorseCommand implements CommandExecutor {

    private Map<UUID, Long> cdMap = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        boolean commandState = false;

        if (args.length != 0) {
            switch (args[0].toLowerCase()) {
                case "claim":
                    commandState = claim(commandSender);
                    break;

                case "unclaim":
                    commandState = unclaim(commandSender);
                    break;

                case "summon":
                    commandState = summon(commandSender);
                    break;
            }
        }

        return commandState;
    }

    public boolean claim(CommandSender commandSender) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;

            if (player.hasPermission("HorseSkill.commands.claim")) {
                Entity vehicle = player.getVehicle();
                if (vehicle != null) {
                    if (vehicle instanceof Horse) {
                        if (HorseSkill.getPlayerHorseList().containsKey(player.getUniqueId())) {
                            player.sendMessage("You Own A Horse");
                        } else {
                            Horse horse = (Horse) vehicle;
                            HorseSkill.getPlayerHorseList().put(player.getUniqueId(), horse.getUniqueId());
                            HorseSkill.getHorseList().put(horse.getUniqueId(), new rpgHorse(horse.getUniqueId(), player.getUniqueId()));
                            player.sendMessage("Success!");
                        }
                    } else {
                        player.sendMessage("Vehicule Is Not A Horse");
                    }
                } else {
                    player.sendMessage("No Vehicule");
                }
            } else {
                player.sendMessage("No Permission");
            }
        } else {
            commandSender.sendMessage("Console can't use this command");
        }

        return true;
    }

    public boolean unclaim(CommandSender commandSender) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;

            if (player.hasPermission("HorseSkill.commands.unclaim")) {
                if (HorseSkill.getPlayerHorseList().containsKey(player.getUniqueId())) {
                    UUID horseUUID = HorseSkill.getPlayerHorseList().get(player.getUniqueId());
                    HorseSkill.getHorseList().get(horseUUID).unclaim();
                    HorseSkill.getHorseList().remove(horseUUID);
                    HorseSkill.getPlayerHorseList().remove(player.getUniqueId());
                    player.sendMessage("Success!");
                } else {
                    player.sendMessage("You Don't Own Any Horses");
                }
            } else {
                player.sendMessage("No Permission");
            }
        } else {
            commandSender.sendMessage("Console can't use this command");
        }

        return true;
    }

    public boolean summon(CommandSender commandSender) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;

            if (player.hasPermission("HorseSkill.commands.summon")) {
                if (HorseSkill.getPlayerHorseList().containsKey(player.getUniqueId())) {
                    if (this.cdMap.containsKey(player.getUniqueId())) {
                        if ((System.currentTimeMillis() - this.cdMap.get(player.getUniqueId())) > 1800000) {
                            UUID horseUUID = HorseSkill.getPlayerHorseList().get(player.getUniqueId());
                            HorseSkill.getHorseList().get(horseUUID).summonHorse();
                            player.sendMessage("Success!");
                            this.cdMap.put(player.getUniqueId(), System.currentTimeMillis());
                        } else {
                            long milliseconds = (System.currentTimeMillis() - this.cdMap.get(player.getUniqueId()));
                            player.sendMessage("Command On Cooldown");
                            player.sendMessage("Cooldown : " + (milliseconds / 1000) + " seconds");
                        }
                    } else {
                        UUID horseUUID = HorseSkill.getPlayerHorseList().get(player.getUniqueId());
                        HorseSkill.getHorseList().get(horseUUID).summonHorse();
                        player.sendMessage("Success!");
                        this.cdMap.put(player.getUniqueId(), System.currentTimeMillis());
                    }
                } else {
                    player.sendMessage("You Don't Own A Horse");
                }
            } else {
                player.sendMessage("No Permission");
            }
        } else {
            commandSender.sendMessage("Console can't use this command");
        }
        return true;
    }
}