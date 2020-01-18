package com.github.StevenDesroches.HorseSkill.listener;

import java.util.List;

import com.github.StevenDesroches.HorseSkill.HorseSkill;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityListener implements Listener {

    @EventHandler
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        Entity target = event.getEntity();
        //if (target instanceof rpgHorse) {
        if (HorseSkill.getHorseList().containsKey(target.getUniqueId())) {
            List<Entity> passengers = target.getPassengers();
            if (!passengers.isEmpty()) {
                Entity firstPassenger = passengers.get(0);
                if (firstPassenger.getUniqueId() == damager.getUniqueId()) {
                    System.out.println("Attacking own horse");
                    event.setCancelled(true);
                }
            }
        }
    }
}
