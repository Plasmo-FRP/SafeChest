package org.venterok.safechest.utils

import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockBurnEvent
import org.bukkit.event.block.BlockExplodeEvent
import org.bukkit.event.entity.ExplosionPrimeEvent
import org.venterok.safechest.Safechest.Companion.formatColor
import org.venterok.safechest.objects.ConfigVal.Companion.config
import org.venterok.safechest.objects.DataHelp.Companion.cacheChest
import org.venterok.safechest.objects.DataHelp.Companion.checkFileExists
import org.venterok.safechest.objects.DataHelp.Companion.chestRemove
import org.venterok.safechest.objects.DataHelp.Companion.randomChance

class BlockBreakEvent : Listener {
    @EventHandler
    fun chestBreak( e: BlockBreakEvent) {
        if (e.block.type != Material.BARREL) return
        val coords = "${e.block.location.blockX}_${e.block.location.blockY}_${e.block.location.blockZ}"

        val pl = e.player
        if (!cacheChest.containsKey(coords) || pl.gameMode == GameMode.CREATIVE) return

        if (pl.inventory.itemInMainHand.itemMeta?.displayName == config.getString("crowbarFeature.crowbar-item-name")) {
            val messageEnabled = config.getBoolean("crowbarFeature.message-enabled")
            if (randomChance(config.getInt("crowbarFeature.chance"))) {

                val sound = config.getString("crowbarFeature.sound")
                val pitch = config.getLong("crowbarFeature.sound-pitch").toFloat()
                val volume = config.getLong("crowbarFeature.sound-volume").toFloat()

                pl.world.playSound(e.block.location, Sound.valueOf(sound!!), pitch, volume)
                if(messageEnabled) { pl.sendMessage(formatColor(config.getString("message.crowbar-success")!!)) }

                return

            } else {
                if(messageEnabled) { pl.sendMessage(formatColor(config.getString("message.crowbar-fail")!!)) }

                e.isCancelled = true
            }
        } else e.isCancelled = true

        }
    }
    @EventHandler
    fun chestBurn( e: BlockBurnEvent) {
        if (e.block.type != Material.BARREL) return
        val coords = "${e.block.location.blockX}_${e.block.location.blockY}_${e.block.location.blockZ}"
        if (!cacheChest.containsKey(coords)) return
        else cacheChest.remove(coords)
        if (!checkFileExists(coords)) return
        else e.isCancelled = true
    }
    @EventHandler
    fun chestExplode( e: BlockExplodeEvent) {
        if (e.block.type != Material.BARREL) return
        val coords = "${e.block.location.blockX}_${e.block.location.blockY}_${e.block.location.blockZ}"
        if (!cacheChest.containsKey(coords)) return
        else cacheChest.remove(coords)
        if (!checkFileExists(coords)) return
        else e.isCancelled = true
    }

