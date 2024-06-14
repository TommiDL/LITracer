package org.example

import com.github.ajalt.clikt.core.subcommands
import pfm2png
import png2pfm


//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main(argv:Array<String>) = Selection().subcommands(Demo(), pfm2png(), png2pfm()).main(argv)
