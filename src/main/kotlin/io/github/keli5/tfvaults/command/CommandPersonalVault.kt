package io.github.keli5.tfvaults.command

import com.charleskorn.kaml.EmptyYamlDocumentException
import com.charleskorn.kaml.Yaml
import io.github.keli5.tfvaults.data.PersonalVault
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
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
        val userVaultPath = File("$vaultFileDir/$userUUID.yml")
        val userVault: PersonalVault
        sender.sendMessage(sender.inventory.itemInMainHand.toString())

        if (File("$vaultFileDir/$userUUID.yml").exists()) {
            userVault = loadVault(userUUID);
            sender.sendMessage(ItemStack.deserializeBytes(userVault.items[0]).toString())
        } else {
            println("No vault exists for ${sender.playerProfile.name} / $userUUID. Creating one now...")
            userVaultPath.createNewFile() // actually create it
            userVault = PersonalVault(userUUID, listOf(sender.inventory.itemInMainHand.serializeAsBytes()))
            writeVaultToDisk(userVault)
        }
        return true
    }
}

private fun writeVaultToDisk(vault: PersonalVault) {
    val vaultFile = File("$vaultFileDir/${vault.owner}.yml")
    val encodedVault = Yaml.default.encodeToString(PersonalVault.serializer(), vault)
    vaultFile.writeText(encodedVault)
}

private fun loadVault(uuid: String): PersonalVault {
    val vaultFile = File("$vaultFileDir/$uuid.yml")
    val fileContents = vaultFile.readText(Charsets.UTF_8)
    return try {
        Yaml.default.decodeFromString(PersonalVault.serializer(), fileContents)
    } catch (err: EmptyYamlDocumentException) {
        val newVault = PersonalVault(uuid, emptyList())
        writeVaultToDisk(newVault)
        newVault
    }
}