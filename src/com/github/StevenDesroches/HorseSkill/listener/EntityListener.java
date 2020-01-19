package com.github.StevenDesroches.HorseSkill.listener;

import java.util.List;
import java.util.UUID;

import com.github.StevenDesroches.HorseSkill.HorseSkill;
import com.gmail.nossr50.api.ExperienceAPI;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class EntityListener implements Listener {

    private HorseSkill horseSkill;

    public EntityListener(HorseSkill horseSkill) {
        this.horseSkill = horseSkill;
    }

    @EventHandler
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        Entity target = event.getEntity();
        //if (target instanceof RpgHorse) {
        if (this.horseSkill.getHorseList().containsKey(target.getUniqueId())) {
            List<Entity> passengers = target.getPassengers();
            if (!passengers.isEmpty()) {
                Entity firstPassenger = passengers.get(0);
                if (firstPassenger.getUniqueId() == damager.getUniqueId()) {
                    event.setCancelled(true);
                }
            }
        }
        if (damager instanceof Player) {
            if (damager.getVehicle() != null) {
                if (damager.hasPermission("HorseSkill.commands.claim")) {
                    Entity vehicule = damager.getVehicle();
                    Player player = (Player) damager;
                    if (vehicule instanceof Horse) {
                        ExperienceAPI.addMultipliedXP(player, "TAMING", (int) (event.getDamage() * 100), "PVE");
                    }
                }
            }
        }
    }

    @EventHandler
    public void onEntityDeathEvent(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        if (this.horseSkill.getHorseList().containsKey(entity.getUniqueId())) {
            event.getDrops().clear();
        }
    }

}
