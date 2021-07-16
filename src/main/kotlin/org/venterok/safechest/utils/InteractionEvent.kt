package org.venterok.safechest.utils

import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.meta.ItemMeta
import org.venterok.safechest.Safechest.Companion.formatColor
import org.venterok.safechest.objects.ConfigVal.Companion.config
import org.venterok.safechest.objects.DataHelp.Companion.cacheChest
import org.venterok.safechest.objects.DataHelp.Companion.checkFileExists
import org.venterok.safechest.objects.DataHelp.Companion.chestFileSet
import org.venterok.safechest.objects.DataHelp.Companion.chestInfoGet
import org.venterok.safechest.objects.DataHelp.Companion.chestKeyInfoSet


class InteractionEvent : Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    fun mainEvent( e: PlayerInteractEvent ) {
        //MainEvent
        if (e.clickedBlock?.type != Material.BARREL) return
        if (e.action != Action.RIGHT_CLICK_BLOCK) return

        val coords = "${e.clickedBlock!!.location.blockX}_${e.clickedBlock!!.location.blockY}_${e.clickedBlock!!.location.blockZ}"
        val pl = e.player
        val handItem = pl.inventory.itemInMainHand

        //lockRegister
        if (handItem.itemMeta?.displayName == config.getString("itemOptions.lock-item-name") && !checkFileExists(coords)) {

            pl.inventory.removeItem(handItem)
            pl.updateInventory()

            val newID = "${System.currentTimeMillis()}"

            chestFileSet(coords, newID, pl.name, false)
            cacheChest[coords] = PlayerChest(e.clickedBlock!!.location, newID, false)

            pl.sendMessage(formatColor(config.getString("message.lock-setup")!!.replace("{id}", newID.toString())))

            e.isCancelled = true
            return
        }

        if (!checkFileExists(coords)) return

        if (!cacheChest.containsKey(coords)) {
            val id = chestInfoGet().getString("$coords.id")!!
            val status = chestInfoGet().getBoolean("$coords.key-created-before")
            cacheChest[coords] = PlayerChest(e.clickedBlock!!.location, id, status)
        }

        //key-Already-Bound
        if (handItem.itemMeta?.displayName == config.getString("itemOptions.key-item-name") && handItem.itemMeta?.hasLore() == false && cacheChest[coords]?.kc == true) {
            pl.sendMessage(formatColor(config.getString("message.key-already-bound")!!))
            e.isCancelled = true
            return
        }
        //lock-Already-Bound
        if (handItem.itemMeta?.displayName == config.getString("itemOptions.lock-item-name") && cacheChest[coords]?.id != null) {
            pl.sendMessage(formatColor(config.getString("message.lock-already-bound")!!))
            e.isCancelled = true
            return
        }

        //keyRegister
        if (handItem.itemMeta?.displayName == config.getString("itemOptions.key-item-name") && handItem.itemMeta?.hasLore() == false && cacheChest[coords]?.kc == false) {
            val meta: ItemMeta? = handItem.itemMeta
            //TODO PlaceHolderApi Support
            val nickname = formatColor(config.getString("itemOptions.creator-line")!!).replace("{player}", pl.name)
            meta!!.lore = (listOf(cacheChest[coords]!!.id, nickname))
            meta.addEnchant(Enchantment.LOYALTY, 1, true)
            handItem.itemMeta = meta

            pl.updateInventory()

            cacheChest[coords]!!.kc = true
            chestKeyInfoSet(coords, true, pl.name)

            pl.sendMessage(formatColor(config.getString("message.key-setup")!!.replace("{id}", cacheChest[coords]!!.id)))

            e.isCancelled = true
            return
        }

        //if item lore contains ID, the chest will be open
        if (handItem.itemMeta?.lore?.contains(cacheChest[coords]) == false) {
            return
        }
        else {
            pl.sendMessage(formatColor(config.getString("message.chest-closed")!!))
            e.isCancelled = true
        }
    }
}