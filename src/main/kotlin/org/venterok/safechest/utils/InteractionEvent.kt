package org.venterok.safechest.utils

import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.meta.ItemMeta
import org.venterok.safechest.Safechest.Companion.formatColor
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
        if (e.clickedBlock?.type != Material.BARREL) return
        val coords = "${e.clickedBlock!!.location.blockX}_${e.clickedBlock!!.location.blockY}_${e.clickedBlock!!.location.blockZ}"
        if (cacheChest.containsKey(coords)) return
        if (!checkFileExists(coords)) return

        val id = chestInfoGet().getString("$coords.id")!!
        val status = chestInfoGet().getBoolean("$coords.key-created-before")
        cacheChest[coords] = PlayerChest(e.clickedBlock!!.location, id, status)

        if (!cacheChest[coords]!!.kc) return

        val pl = e.player
        val handItem = pl.inventory.itemInMainHand
        println(handItem.itemMeta?.displayName)

        if (handItem.itemMeta?.lore?.contains(cacheChest[coords]!!.id) == true) return
        if (handItem.itemMeta?.displayName == config.getString("itemOptions.lock-item-name")) {
            pl.sendMessage(formatColor(config.getString("message.lock-already-bound")!!))
            e.isCancelled = true
            return
        }
        pl.sendMessage(formatColor(config.getString("message.chest-closed")!!))
        e.isCancelled = true
    }
    @EventHandler(priority = EventPriority.NORMAL)
    fun lockChest( e: PlayerInteractEvent ) {
        if (e.clickedBlock?.type != Material.BARREL) return
        val coords = "${e.clickedBlock!!.location.blockX}_${e.clickedBlock!!.location.blockY}_${e.clickedBlock!!.location.blockZ}"
        if (cacheChest.contains(coords)) return
        val pl = e.player

        val handItem = pl.inventory.itemInMainHand
        if (handItem.itemMeta?.displayName != config.getString("itemOptions.lock-item-name")) return

        pl.inventory.removeItem(handItem)
        pl.updateInventory()

        val newID = "${System.currentTimeMillis()}"

        chestFileSet(coords, newID, pl.name, false)
        cacheChest[coords] = PlayerChest(e.clickedBlock!!.location, newID, false)

        pl.sendMessage(formatColor(config.getString("message.lock-setup")!!.replace("{id}", newID.toString())))

        e.isCancelled = true
    }
    @EventHandler(priority = EventPriority.HIGH)
    fun keyRegister( e: PlayerInteractEvent ) {
        if (e.clickedBlock?.type != Material.BARREL) return
        val coords = "${e.clickedBlock!!.location.blockX}_${e.clickedBlock!!.location.blockY}_${e.clickedBlock!!.location.blockZ}"
        if (!cacheChest.contains(coords)) return

        val pl = e.player
        val handItem = pl.inventory.itemInMainHand
        println(handItem.itemMeta?.displayName)
        if (cacheChest[coords]!!.kc) return

        if (handItem.itemMeta?.displayName == config.getString("itemOptions.key-item-name") && handItem.itemMeta?.lore == null) {

            val meta: ItemMeta? = handItem.itemMeta
            if (meta != null) {
                meta.lore?.add(cacheChest[coords]!!.id.toString())
                meta.lore?.add(formatColor(config.getString("itemOptions.creator-line")!!.replace("{player}", "$pl")))
                meta.addEnchant(Enchantment.LOYALTY, 1, true)
                handItem.itemMeta = meta
            }

            pl.updateInventory()

            cacheChest[coords]!!.kc = true
            chestInfoGet().set("$coords.key-created-before", true)

            pl.sendMessage(formatColor(config.getString("message.key-setup")!!.replace("{id}", cacheChest[coords]!!.id)))

            e.isCancelled = true
        }
        else {
            e.isCancelled = true
            return
        }
    }
}