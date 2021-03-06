package de.tammo.kommand.help

import de.tammo.kommand.annotation.Model
import de.tammo.kommand.annotation.Parameter
import de.tammo.kommand.annotation.Route
import de.tammo.kommand.executor.CommandExecutor
import de.tammo.kommand.result.CommandResult

/**
 * Command for printing help for other commands.
 *
 * @author Tammo0987
 * @since 1.0
 */
@Model("help", "Show help for all commands")
class HelpCommand(private val info: (String) -> Unit, private val warn: (String) -> Unit) {

    /**
     * Prints help fo all commands.
     */
    @Route(description = "Prints help for all commands")
    fun defaultRoute(): CommandResult {
        this.info.invoke("<-- Commands -->")
        this.info.invoke("")
        CommandExecutor.commands.forEach { this.info.invoke("${it.label.capitalize()} -> ${it.description}") }
        return CommandResult.SUCCESS
    }

    /**
     * Prints help for a specific [command].
     *
     * @param [command] Print help on this command.
     */
    @Route("command", "Get help for a specific command")
    @Parameter("command", String::class, "Get help for this command")
    fun helpForCommand(command: String): CommandResult {
        val commandModel = CommandExecutor.commandModel(command) ?: return CommandResult.FAILED

        if (commandModel.subModels.isEmpty() && commandModel.routes.isEmpty()) {
            this.warn.invoke("There are no possible sub commands or routes!")
            return CommandResult.SUCCESS
        }

        if (commandModel.subModels.isNotEmpty()) {
            this.info.invoke("<-- Sub Commands -->")
            this.info.invoke("")
            commandModel.subModels.forEach { this.info.invoke("${it.label.capitalize()} -> ${it.description}") }
        }

        if (commandModel.routes.isNotEmpty()) {
            this.info.invoke("<-- Routes -->")
            this.info.invoke("")
            commandModel.routes.forEach {
                val name = if (it.name.isEmpty() || it.name.isBlank()) "Default" else it.name.capitalize()
                this.info.invoke("$name -> ${it.description}")
            }

        }
        return CommandResult.SUCCESS
    }

}