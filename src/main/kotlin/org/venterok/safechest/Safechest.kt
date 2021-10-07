package org.venterok.safechest

import net.md_5.bungee.api.ChatColor
import org.bukkit.Bukkit
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import org.venterok.safechest.objects.DataHelp
import org.venterok.safechest.utils.InteractionEvent
import org.venterok.safechest.utils.BlockBreakEvent
import org.venterok.safechest.utils.KeyCopy
import java.io.File
import java.util.regex.Matcher
import java.util.regex.Pattern

class Safechest : JavaPlugin() {
    override fun onEnable() {
        inst = this

        configFile = setUpConfig()
        Bukkit.getPluginManager().registerEvents(InteractionEvent(), this)
        Bukkit.getPluginManager().registerEvents(BlockBreakEvent(), this)
        Bukkit.getPluginManager().registerEvents(KeyCopy(), this)

        val path = inst!!.dataFolder
        val file = File("$path/data.yml")
        val data = YamlConfiguration.loadConfiguration(file)
        data.set("created", true)
        data.save(file)
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
    companion object {
        var configFile : File? = null
        private val pattern: Pattern = Pattern.compile("#[a-fA-F0-9]{6}")
        fun formatColor(msg: String): String {
            var msg = msg
            var matcher: Matcher = pattern.matcher(msg)
            while (matcher.find()) {
                val color = msg.substring(matcher.start(), matcher.end())
                msg = msg.replace(color, ChatColor.of(color).toString() + "")
                matcher = pattern.matcher(msg)
            }
            return ChatColor.translateAlternateColorCodes('&', msg)
        }
        fun unformatColor(msg: String): String {
            return ChatColor.stripColor(msg);
        }
        var inst: Safechest? = null
    }   
    private fun setUpConfig(): File {
        val config = File(dataFolder.toString() + File.separator + "config.yml")
        if (!config.exists()) {
            logger.info("Creating config file...")
            getConfig().options().copyDefaults(true)
            saveDefaultConfig()
        }
        return config
    }
}