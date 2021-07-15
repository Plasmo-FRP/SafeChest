package org.venterok.safechest.utils

import org.bukkit.Location
import org.bukkit.entity.Player

class PlayerChest(val loc: Location, val id: Int, var kc: Boolean) {

    fun getLocation(): Location {
        return loc
    }
    fun getChestId(): Int {
        return id
    }
    fun getKeyCreated(): Boolean {
        return kc
    }

}
