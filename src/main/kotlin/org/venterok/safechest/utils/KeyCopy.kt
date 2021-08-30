package org.venterok.safechest.utils

import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.HumanEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.inventory.PrepareSmithingEvent
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.SmithingInventory
import org.bukkit.inventory.meta.ItemMeta
import org.venterok.safechest.Safechest.Companion.formatColor
import org.venterok.safechest.objects.ConfigVal


class KeyCopy : Listener {
    @EventHandler
    fun onCraft(e: PrepareSmithingEvent) {

        val items: Array<ItemStack> = e.inventory.contents

        val viewers: List<HumanEntity> = e.viewers
        val keyOriginal: ItemStack = items[0] // Оригинал
        val keyToCopy: ItemStack = items[1] // Копирка

        if (keyOriginal == null || keyToCopy == null) {
            e.result = ItemStack(Material.AIR)
            viewers.forEach { humanEntity -> (humanEntity as Player).updateInventory() }
            return
        }

        val keysNames = ConfigVal.config.getStringList("itemOptions.key-items-names")
        if (!keysNames.contains(keyOriginal.itemMeta?.displayName) || !keysNames.contains(keyToCopy.itemMeta?.displayName)) return
        if (!keyOriginal.itemMeta?.hasLore()!! || keyToCopy.itemMeta?.hasLore()!!) return


        val resultItem = ItemStack(Material.WARPED_FUNGUS_ON_A_STICK)
        resultItem.itemMeta = keyOriginal.itemMeta
        val meta: ItemMeta? = resultItem.itemMeta
        val creatorLore = formatColor(ConfigVal.config.getString("itemOptions.copy-lore")!!.replace("{player}", e.viewers[0].name))
        meta!!.lore = (listOf(formatColor(keyOriginal.itemMeta?.lore?.get(0)!!), "", creatorLore))
        meta.addEnchant(Enchantment.LOYALTY, 1, true)
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        resultItem.itemMeta = meta

        e.result = resultItem

        viewers.forEach { humanEntity -> (humanEntity as Player).updateInventory() }
    }
    @EventHandler
    fun inventoryClick(e : InventoryClickEvent) {
        if (e.slotType != InventoryType.SlotType.RESULT || e.inventory.type != InventoryType.SMITHING) return

        val pl = e.whoClicked as Player
        val inv = e.inventory as SmithingInventory
        if (e.isShiftClick) {
            e.isCancelled = true
            return
        }
        else pl.setItemOnCursor(e.currentItem)

        e.currentItem = ItemStack(Material.AIR)
        inv.setItem(1, ItemStack(Material.AIR))

        pl.updateInventory()
    }
}