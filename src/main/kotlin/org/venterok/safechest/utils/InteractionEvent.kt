package org.venterok.safechest.utils

import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.venterok.safechest.objects.ConfigVal.Companion.config
import org.venterok.safechest.objects.DataHelp.Companion.cacheChest
import org.venterok.safechest.objects.DataHelp.Companion.checkFileExists
import org.venterok.safechest.objects.DataHelp.Companion.chestFileSet
import org.venterok.safechest.objects.DataHelp.Companion.chestInfoGet
import java.time.LocalDateTime
import kotlin.random.Random

class InteractionEvent : Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    fun dataAddAndCheck( e: PlayerInteractEvent ) {
        if (e.clickedBlock!!.type != Material.BARREL) return
        val coords = "${e.clickedBlock!!.location.blockX}_${e.clickedBlock!!.location.blockY}_${e.clickedBlock!!.location.blockZ}"
        if (cacheChest.containsKey(coords)) return
        if (checkFileExists(coords)) return

        val id = chestInfoGet().getInt("$coords.id")
        val status = chestInfoGet().getBoolean("$coords.key-created-before")
        cacheChest[coords] = PlayerChest(e.clickedBlock!!.location, id, status)

    }
    @EventHandler
    fun lockChest( e: PlayerInteractEvent ) {
        if (e.clickedBlock!!.type != Material.BARREL) return
        val coords = "${e.clickedBlock!!.location.blockX}_${e.clickedBlock!!.location.blockY}_${e.clickedBlock!!.location.blockZ}"
        if (!cacheChest.contains(coords)) return
        val pl = e.player
        val handItem = pl.inventory.itemInMainHand
        if (handItem.itemMeta?.displayName != config.getString("itemOptions.lock-item-name")) return

        pl.inventory.removeItem(handItem)
        pl.updateInventory()

        val newID = "${LocalDateTime.now().nano}${Random.nextInt(1, 100000)}".toInt()

        chestFileSet(coords, newID, pl.name, false)
        cacheChest[coords] = PlayerChest(e.clickedBlock!!.location, newID, false)

        pl.sendMessage(config.getString("message.lock-setup")!!.replace("{id}", newID.toString()))

        e.isCancelled = true
    }
    @EventHandler
    fun keyRegister( e: PlayerInteractEvent ) {
        if (e.clickedBlock!!.type != Material.BARREL) return
        val coords = "${e.clickedBlock!!.location.blockX}_${e.clickedBlock!!.location.blockY}_${e.clickedBlock!!.location.blockZ}"
        if (!cacheChest.contains(coords)) return

        val pl = e.player
        val handItem = pl.inventory.itemInMainHand
        if (!cacheChest[coords]!!.kc) return
        if (handItem.itemMeta?.displayName == config.getString("itemOptions.key-item-name") && handItem.itemMeta?.lore == null) return

        handItem.itemMeta!!.lore!!.add(cacheChest[coords]!!.id.toString())
        handItem.itemMeta!!.addEnchant(Enchantment.LOYALTY, 1, true)
        cacheChest[coords]!!.kc = true
        chestInfoGet().set("$coords.key-created-before", true)

        pl.sendMessage(config.getString("message.key-setup")!!.replace("{id}", cacheChest[coords]!!.id.toString()))

        e.isCancelled = true
    }
}