package io.github.keli5.tfvaults

import io.github.keli5.tfvaults.command.CommandPersonalVault
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.java.JavaPlugin

val nameToCommandExecutor = mapOf(
    "personalvault" to CommandPersonalVault(),
)

class TFVaults : JavaPlugin(), Listener {
    override fun onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this)

        for ((name, executor) in nameToCommandExecutor) {
            getCommand(name)!!.setExecutor(executor)
            print("Registering command $name to ${executor.toString()}\n")
        }
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        // pass
    }
}