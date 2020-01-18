package com.github.StevenDesroches.HorseSkill.datatype;

import net.minecraft.server.v1_12_R1.EntityHorse;
import net.minecraft.server.v1_12_R1.PathfinderGoalFloat;
import net.minecraft.server.v1_12_R1.World;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class rpgHorse {

    private UUID horseUUID;
    private double originalSpeed;
    private double originalArmor;
    private double originalToughness;
    private double originalHealth;
    private double originalKnockbackResist;
    private double originalJump;
    private Horse.Style originalStyle;
    private Horse.Color originalColor;

    private UUID playerUUID;

    private double updatedSpeed;
    private double updatedArmor;
    private double updatedToughness;
    private double updatedHealth;
    private double updatedKnockbackResist;
    private double updatedJump;


    public rpgHorse(UUID horseUUID, UUID playerUUID) {
        this.horseUUID = horseUUID;
        this.playerUUID = playerUUID;

        Entity entity = Bukkit.getEntity(horseUUID);
        if (entity instanceof Horse) {
            Horse horse = (Horse) entity;
            originalSpeed = horse.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue();
            originalArmor = horse.getAttribute(Attribute.GENERIC_ARMOR).getBaseValue();
            originalToughness = horse.getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS).getBaseValue();
            originalHealth = horse.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
            originalKnockbackResist = horse.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).getBaseValue();
            originalJump = horse.getJumpStrength();
            originalStyle = horse.getStyle();
            originalColor = horse.getColor();

            createUpdatedStats();
            updateHorseStats(horse);
        }
    }


    private void createUpdatedStats() {
        updatedSpeed = this.originalSpeed * 1.5;
        //if (this.originalArmor != 0) {
        //    updatedArmor = this.originalArmor * 1.5;
        //} else {
            updatedArmor = 15;
        //}

        //if (this.originalToughness != 0) {
        //    updatedToughness = this.originalToughness * 1.5;
        //} else {
            updatedToughness = 15;
        //}
        updatedHealth = this.originalHealth * 2;
        updatedKnockbackResist = this.originalKnockbackResist + 1;
        updatedJump = this.originalJump * 1.5;
    }

    private void updateHorseStats(Horse horse) {
        Player player = (Player) Bukkit.getEntity(this.playerUUID);

        horse.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(this.updatedSpeed);
        horse.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(this.updatedArmor);
        horse.getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS).setBaseValue(this.updatedToughness);
        horse.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(this.updatedHealth);
        horse.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(this.updatedKnockbackResist);
        horse.setJumpStrength(this.updatedJump);
        horse.setStyle(this.originalStyle);
        horse.setColor(this.originalColor);
        horse.setTamed(true);
        horse.setOwner(player);
        horse.setAdult();
        horse.setBreed(false);

        if (player.getInventory().contains(Material.SADDLE)) {
            player.getInventory().remove(Material.SADDLE);
            horse.getInventory().setSaddle(new ItemStack(Material.SADDLE));
        }
    }

    public void summonHorse() {
        Entity entityHorse = Bukkit.getEntity(this.horseUUID);
        Player player = (Player) Bukkit.getEntity(this.playerUUID);
        if (entityHorse instanceof Horse) {
            Horse horse = (Horse) entityHorse;

            if ((horse).getInventory().getSaddle() != null) {
                player.getInventory().addItem(new ItemStack(Material.SADDLE));
                horse.getInventory().setSaddle(null);

            }
            horse.remove();
            horse.setHealth(0);

            horse = (Horse) player.getWorld().spawnEntity(player.getLocation(), EntityType.HORSE);

            this.horseUUID = horse.getUniqueId();

            updateHorseStats(horse);

        }
    }

    public void unsummon() {

    }

    public void unclaim() {
        Entity entity = Bukkit.getEntity(this.horseUUID);
        if (entity instanceof Horse) {
            ((Horse) entity).setHealth(0);
            if (((Horse) entity).getInventory().getSaddle() != null) {
                Player player = (Player) Bukkit.getEntity(this.playerUUID);
                player.getInventory().addItem(new ItemStack(Material.SADDLE));
            }
        }


        this.horseUUID = null;
        this.originalToughness = 0;
        this.originalStyle = null;
        this.playerUUID = null;
        this.originalArmor = 0;
        this.originalColor = null;
        this.originalHealth = 0;
        this.originalJump = 0;
        this.originalKnockbackResist = 0;
        this.originalSpeed = 0;
        this.updatedArmor = 0;
        this.updatedHealth = 0;
        this.updatedJump = 0;
        this.updatedKnockbackResist = 0;
        this.updatedSpeed = 0;
        this.updatedToughness = 0;

        System.gc();
    }

    public boolean tryRiding(UUID playerUUID) {
        boolean state = true;
        if (!playerUUID.equals(this.playerUUID)) {
            state = false;
        }
        return state;
    }


}
