package org.venterok.safechest

import net.md_5.bungee.api.ChatColor
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import org.venterok.safechest.utils.InteractionEvent
import org.venterok.safechest.utils.BlockBreakEvent
import java.io.File
import java.util.regex.Matcher
import java.util.regex.Pattern

class SafeChest : JavaPlugin() {
    override fun onEnable() {
        inst = this

        configFile = setUpConfig()
        Bukkit.getPluginManager().registerEvents(InteractionEvent(), this)
        Bukkit.getPluginManager().registerEvents(BlockBreakEvent(), this)
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
        var inst: SafeChest? = null
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