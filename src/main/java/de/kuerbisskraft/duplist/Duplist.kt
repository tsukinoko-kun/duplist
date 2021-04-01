package de.kuerbisskraft.duplist

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.dsl.module

@KoinApiExtension
class Duplist : JavaPlugin(), Listener, CommandExecutor, KoinComponent {
    private val cmdInterpreter: CmdInterpreter

    init {
        registerModules()
        cmdInterpreter = inject<CmdInterpreter>().value
    }

//    override fun onEnable() {
//
//    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (command.name == "duplist") {
            return cmdInterpreter.onCommand(sender, args)
        }
        return false
    }

    private fun registerModules() {
        val plugin = this

        val koinModules = module {
            single<Plugin> { plugin }
//            single { Bukkit.getLogger() }
            single { Texts() }
            single { PermissionManager(get()) }
            single { DataManager(get()) }
            single { CmdInterpreter(get(), get(), get()) }
        }

        startKoin {
            modules(koinModules)
        }
    }
}
