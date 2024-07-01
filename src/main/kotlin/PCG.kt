package org.example

/**
 * PCG (Permuted Congruential Generator) class which simulates a random number generator:
 * takes two 64-bit unsigned integers as input (state & inc)
 *
 * Properties:
 *          @state = current state of the generator
 *          @inc = increment value used in the generator
 */
data class PCG(var state: ULong = 0u, var inc: ULong = 0u) {

    /**
     * Initialization block to set up the initial state and increment.
     * It discards the first few random numbers to ensure the generator is properly initialized.
     */
    init {
        this.state = 0u
        this.inc = ((54u shl 1) or 1u).toULong() // shl: Shifts this value left by the bitCount(=1) number of bits
        this.random()   // Throw a random number and discard it
        this.state += 42u
        this.random()   // Throw a random number and discard it
    }

    /**
     * Generates a random unsigned integer using the PCG algorithm:
     * uses unsigned long variables
     * returns UInt
     */
    fun random(): UInt {
        val oldState = this.state
        this.state = (oldState * 6364136223846793005u + this.inc)
        val xorShifted = (((oldState shr 18) xor oldState) shr 27).toUInt() // shr: Shifts this value right by the bitCount(=27) number of bits
        val rot = (oldState shr 59).toUInt() // Calculate the rotation value

        // Rotate the bits of xorShifted and return the result
        return (xorShifted shr rot.toInt()) or (xorShifted shl ((-rot.toInt()) and 31))
    }

    /**
     * Generates a random floating-point number between 0 and 1
     */
    fun random_float():Float
    {
        return this.random().toFloat()/0xffffffff
    }

}
