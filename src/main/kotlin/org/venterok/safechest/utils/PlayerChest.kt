package org.venterok.safechest.utils

import org.bukkit.Location
import org.bukkit.entity.Player

class PlayerChest(val pl: Player, val coords: Location, val id: Int) {

    fun getPlayer(): Player {
        return pl
    }
    fun getLocation(): Location {
        return coords
    }
    fun getChestId(): Int {
        return id
    }
    fun save() {

    }

}
