package org.venterok.safechest.objects

import org.bukkit.configuration.file.YamlConfiguration
import org.venterok.safechest.Safechest
import org.venterok.safechest.utils.PlayerChest
import java.io.File
import kotlin.random.Random

class DataHelp {
    companion object {

        var cacheChest = hashMapOf<String, PlayerChest>()

        val path = Safechest.inst!!.dataFolder
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
        fun chestKeyInfoSet(coords: String, nw: Boolean, nn: String) {
            val file = File("$path/data.yml")
            val data = YamlConfiguration.loadConfiguration(file)

            data.set("$coords.key-created-before", nw)
            data.set("$coords.key-creator", nn)
            data.save(file)
        }
        fun chestFileSet(coords: String, id: String, pl: String, kc: Boolean) {
            val file = File("$path/data.yml")
            val data = YamlConfiguration.loadConfiguration(file)

            data.set("$coords.id", id)
            data.set("$coords.creator", pl)
            data.set("$coords.key-created-before", kc)
            data.set("$coords.key-creator", "Nothing")
            data.save(file)
        }
        fun chestRemove(name: String,){
            val file = File("$path/data.yml")
            val data = YamlConfiguration.loadConfiguration(file)

            data.set(name, null)
            data.save(file)
        }
        fun randomChance(chance: Int): Boolean {
            var trueOrFalse = false
            if (chance != 100) {
                val chanceRand = Random.nextInt(1, 100)
                if (chanceRand < chance) {
                    trueOrFalse = true
                }
            } else trueOrFalse = true
            return trueOrFalse
        }
    }
}