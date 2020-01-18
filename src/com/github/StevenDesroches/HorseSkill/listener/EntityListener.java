package com.github.StevenDesroches.HorseSkill.listener;

import java.util.List;

import com.github.StevenDesroches.HorseSkill.HorseSkill;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class EntityListener implements Listener {

    @EventHandler
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        Entity target = event.getEntity();
        //if (target instanceof RpgHorse) {
        if (HorseSkill.instance.getHorseList().containsKey(target.getUniqueId())) {
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

    @EventHandler
    public void onEntityDeathEvent(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        System.out.println("allez :(");
        //if (entity instanceof Horse) {
            if (HorseSkill.instance.getHorseList().containsKey(entity.getUniqueId())) {
                event.getDrops().clear();
                System.out.println("test");
            }
        //}
    }
}
