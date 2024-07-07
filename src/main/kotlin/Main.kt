package org.example

import com.github.ajalt.clikt.core.subcommands
import pfm2png
import png2pfm

/**
 * Main function to run the application.
 * It initializes the Selection command and adds subcommands for Demo, pfm2png, and png2pfm.
 *
 * TIP: To run the code, press the designated run shortcut or click the execute icon in the gutter.
 *
 * @argv = the command-line arguments
 */
fun main(argv:Array<String>) = Selection().subcommands(Demo(), pfm2png(), png2pfm()).main(argv)
