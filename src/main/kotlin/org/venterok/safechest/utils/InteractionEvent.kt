package org.venterok.safechest.utils

import org.bukkit.Material
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.venterok.safechest.utils.DataHelp.Companion.cacheChest
import org.venterok.safechest.utils.DataHelp.Companion.checkFileExists
import org.venterok.safechest.utils.DataHelp.Companion.chestInfoGet
import java.util.*

class InteractionEvent : Listener{
    fun checkChestData( e: PlayerInteractEvent) {
        if (e.clickedBlock!!.type != Material.CHEST) return
        val coords = "${e.clickedBlock!!.location.blockX}_${e.clickedBlock!!.location.blockY}_${e.clickedBlock!!.location.blockZ}"
        if (!checkFileExists(coords)) {

        }
        else if (!cacheChest.contains(coords)) {
            val id = chestInfoGet().getInt("$coords.id")
            val location = e.clickedBlock!!.location
            val player = e.player


            cacheChest[location] = PlayerChest(player, location, id)
        }
    }
}