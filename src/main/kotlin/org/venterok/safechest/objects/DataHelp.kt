package org.venterok.safechest.objects

import org.bukkit.Location
import org.bukkit.configuration.file.YamlConfiguration
import org.venterok.safechest.SafeChest
import org.venterok.safechest.utils.PlayerChest
import java.io.File

class DataHelp {
    companion object {

        var cacheChest = hashMapOf<String, PlayerChest>()

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
        fun chestFileSet(coords: String, id: Int, pl: String, kc: Boolean) {
            val file = File("$path/data.yml")
            val data = YamlConfiguration.loadConfiguration(file)
            //TODO Set info to data.yml
        }
        fun chestRemove(name: String,){
            val file = File("$path/data.yml")
            val data = YamlConfiguration.loadConfiguration(file)
            val list = data.getConfigurationSection("")!!.getKeys(false)
            //TODO Remove info from data.yml
        }
    }
}