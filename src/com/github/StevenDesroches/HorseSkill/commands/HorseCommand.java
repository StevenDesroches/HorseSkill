package com.github.StevenDesroches.HorseSkill.commands;

import com.github.StevenDesroches.HorseSkill.HorseSkill;
import com.github.StevenDesroches.HorseSkill.datatype.RpgHorse;
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
    private HorseSkill horseSkill;

    public HorseCommand(HorseSkill plugin) {
        this.horseSkill = plugin;
    }

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
                        if (horseSkill.getPlayerHorseList().containsKey(player.getUniqueId())) {
                            player.sendMessage("You Own A Horse");
                        } else {
                            Horse horse = (Horse) vehicle;
                            horseSkill.getPlayerHorseList().put(player.getUniqueId(), horse.getUniqueId());
                            horseSkill.getHorseList().put(horse.getUniqueId(), new RpgHorse(horse.getUniqueId(), player.getUniqueId()));
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
                if (horseSkill.getPlayerHorseList().containsKey(player.getUniqueId())) {
                    UUID horseUUID = horseSkill.getPlayerHorseList().get(player.getUniqueId());
                    horseSkill.getHorseList().get(horseUUID).unclaim();
                    horseSkill.getHorseList().remove(horseUUID);
                    horseSkill.getPlayerHorseList().remove(player.getUniqueId());
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
                if (horseSkill.getPlayerHorseList().containsKey(player.getUniqueId())) {
                    if (this.cdMap.containsKey(player.getUniqueId())) {
                        if ((System.currentTimeMillis() - this.cdMap.get(player.getUniqueId())) > 300000) {
                            summonHorseMethod(player);
                        } else {
                            long milliseconds = (300000 + (this.cdMap.get(player.getUniqueId()) - System.currentTimeMillis()));
                            player.sendMessage("Command On Cooldown");
                            player.sendMessage("Cooldown : " + (milliseconds / 1000) + " Seconds");
                        }
                    } else {
                        summonHorseMethod(player);
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

    private void summonHorseMethod(Player player) {
        UUID horseUUID = horseSkill.getPlayerHorseList().get(player.getUniqueId());
        horseSkill.getHorseList().get(horseUUID).summonHorse();
        RpgHorse rpgHorse = horseSkill.getHorseList().get(horseUUID);

        horseSkill.getHorseList().remove(horseUUID);
        horseSkill.getHorseList().put(rpgHorse.getHorseUUID(), rpgHorse);
        horseSkill.getPlayerHorseList().put(player.getUniqueId(), rpgHorse.getHorseUUID());

        player.sendMessage("Success!");
        this.cdMap.put(player.getUniqueId(), System.currentTimeMillis());
    }
}