package org.venterok.safechest.utils

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.venterok.safechest.objects.DataHelp
import org.venterok.safechest.objects.DataHelp.Companion.cacheChest
import org.venterok.safechest.objects.DataHelp.Companion.checkFileExists
import org.venterok.safechest.objects.DataHelp.Companion.chestRemove

class blockBreakEvent : Listener {
    @EventHandler
    fun chestDataDelete( e: BlockBreakEvent) {
        if (e.block.type != Material.BARREL) return
        val coords = "${e.block.location.blockX}_${e.block.location.blockY}_${e.block.location.blockZ}"
        if (!cacheChest.containsKey(coords)) return
        else cacheChest.remove(coords)
        if (!checkFileExists(coords)) return
        else chestRemove(coords)

    }
}