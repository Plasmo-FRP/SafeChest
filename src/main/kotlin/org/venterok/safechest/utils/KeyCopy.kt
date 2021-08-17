package org.venterok.safechest.utils

import com.ruverq.mauris.items.ItemsLoader
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.PrepareAnvilEvent
import org.bukkit.event.inventory.PrepareItemCraftEvent
import org.bukkit.event.inventory.PrepareSmithingEvent
import org.bukkit.inventory.ItemStack
import org.venterok.safechest.objects.ConfigVal

class KeyCopy : Listener {
    @EventHandler
    fun onCraft(e: PrepareSmithingEvent) {

        val items: Array<ItemStack> = e.inventory.contents

        val keyOriginal: ItemStack = items[0] // Оригинал
        val keyToCopy: ItemStack = items[1] // Копирка

        println("1 $keyOriginal")
        println("1 $keyToCopy")

        val keysNames = ConfigVal.config.getStringList("itemOptions.key-items-names")
        if (keysNames.contains(keyOriginal.itemMeta?.displayName) || keysNames.contains(keyToCopy.itemMeta?.displayName)) return
        if (keyOriginal.itemMeta?.hasLore()!! || !keyToCopy.itemMeta?.hasLore()!!) return

        e.inventory.result = ItemsLoader.getMaurisItem("golden_key").asItemStack

    }
}