package org.example

import com.github.ajalt.clikt.core.CliktCommand


/**
 * Selection class:
 *  Used to create a command that can have subcommands.
 *  Inherits from CliktCommand and prints help when no arguments are provided
 */
class Selection:CliktCommand(printHelpOnEmptyArgs = true, name="selection") {
    override fun run()=Unit
}

