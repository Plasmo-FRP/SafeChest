package org.venterok.safechest.utils

import org.bukkit.Location
import org.bukkit.configuration.file.YamlConfiguration
import org.venterok.safechest.SafeChest
import java.io.File
import java.util.*

class DataHelp {
    companion object {

        val cacheChest = hashMapOf<Location, PlayerChest>()

        val path = SafeChest.inst!!.dataFolder
        fun checkFileExists(name: String,): Boolean {
            val file = File("$path/data.yml")
            val data = YamlConfiguration.loadConfiguration(file)
            val list = data.getConfigurationSection("")!!.getKeys(false)
            return list.contains(name)
        }
        fun chestInfoGet(): YamlConfiguration {
            val file = File("$path/data.yml")
            return YamlConfiguration.loadConfiguration(file)
        }
    }
}