package io.github.keli5.tfvaults.command

import com.charleskorn.kaml.Yaml
import io.github.keli5.tfvaults.data.PersonalVault
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.io.File
import java.io.PrintWriter
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.createDirectories

val vaultFileDir: Path = Paths.get("./plugins/TFVaults/").createDirectories()

class CommandPersonalVault : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command,
                           label: String, args: Array<out String>?) : Boolean {
        if (sender !is Player) {
            sender.sendMessage("This command can only be run by a player.")
            return false
        }
        val userUUID: String = sender.playerProfile.id.toString()
        var userVault: PersonalVault

        if (File("$vaultFileDir/$userUUID.yml").exists()) {
            TODO("Load vault contents")
        } else {
            println("No vault exists for ${sender.playerProfile.name} / $userUUID. Creating one now...")

            val userVaultFile = File("$vaultFileDir/$userUUID.yml")
            userVaultFile.createNewFile() // actually create it
            userVault = PersonalVault(userUUID, emptyList())

            writeVaultToDisk(userVault)

        }
        return true
    }
}

private fun writeVaultToDisk(vault: PersonalVault) {
    val vaultFile = File("$vaultFileDir/${vault.owner}.yml")
    val encodedVault = Yaml.default.encodeToString(PersonalVault.serializer(), vault)
    PrintWriter(vaultFile).print(encodedVault)
}

private fun loadVaultFromDisk(uuid: String): PersonalVault {
    val vaultFile = File("$vaultFileDir/$uuid.yml")
    val fileContents = vaultFile.readText(Charsets.UTF_8)
    return Yaml.default.decodeFromString(PersonalVault.serializer(), fileContents)
}