package org.example

import kotlin.math.pow
import kotlin.math.sqrt

/**
 * A class representing a 3D vector
 */
data class Vec(var x:Float=0f, var y:Float=0f, var z:Float=0f)
{
    /**
     * Returns a string representation of the vector
     */
    override fun toString(): String {
        return "Vector (x=$x, y=$y, z=$z)"
    }

    /**
     * Compares this vector with another vector within a certain precision (@eps)
     */
    fun is_close(vec:Vec, eps:Float=1e-15f):Boolean
    {
        return (are_similar(this.x, vec.x, eps=eps) and are_similar(this.y, vec.y, eps=eps) and  are_similar(this.z, vec.z, eps=eps))
    }

    /**
     * Adds this vector to another vector
     */
    operator fun plus(vec:Vec):Vec
    {
        return Vec(x=this.x+vec.x, y=this.y+vec.y, z=this.z+vec.z)
    }

    /**
     * Adds this vector to a point
     */
    operator fun plus(pt:Point):Point
    {
        return Point(x=this.x+pt.x, y=this.y+pt.y, z=this.z+pt.z)
    }


    /**
     * Subtracts another vector from this vector
     */
    operator fun minus(vec:Vec):Vec
    {
        return Vec(x=this.x-vec.x, y=this.y-vec.y, z=this.z-vec.z)
    }

    /**
     * Multiplies this vector by a scalar
     */
    operator fun times(factor:Float):Vec
    {
        return Vec(x=factor*this.x, y=factor*this.y, z=factor*this.z)
    }

    /**
     * Multiplies this vector by an integer scalar
     */
    operator fun times(factor:Int):Vec
    {
        return Vec(x=factor*this.x, y=factor*this.y, z=factor*this.z)
    }

    /**
     * Divides this vector by a scalar
     */
    operator fun div(factor:Float):Vec
    {
        return Vec(x=this.x/factor, y=this.y/factor, z=this.z/factor)
    }

    /**
     * Divides this vector by an integer scalar
     */
    operator fun div(factor:Int):Vec
    {
        return Vec(x=this.x/factor, y=this.y/factor, z=this.z/factor)
    }

    /**
     * Returns the opposite of this vector
     */
    fun neg():Vec
    {
        return Vec(x=-this.x, y=-this.y, z=-this.z)
    }

    /**
     * Returns the dot product of this vector and another vector
     */
    operator fun times(vec: Vec):Float
    {
        return (this.x*vec.x + this.y*vec.y+this.z*vec.z)
    }

    /**
     * Returns the cross product of this vector and another vector
     */
    fun prod(vec:Vec):Vec
    {
        return Vec(
            x=this.y * vec.z - this.z * vec.y,
            y=this.z * vec.x - this.x * vec.z,
            z=this.x * vec.y - this.y * vec.x,
        )
    }
    /**
     * Returns the norm (length) of the vector
     */
    fun norm():Float
    {
        return sqrt(this.squared_norm())
    }
    /**
     * Returns the squared norm of the vector
     */
    fun squared_norm():Float
    {
        return this * this
    }
    /**
     * Returns the normalized version of this vector
     */
    fun normalize():Vec
    {
        val norm:Float=this.norm()
        return Vec(this.x/norm, this.y/norm, this.z/norm)
    }
    /**
     * Converts this vector to a normal vector
     */
    fun conversion():Normal
    {
        val norm=this.norm()
        return Normal(x=this.x/norm, y=this.y/norm, z=this.z/norm)
    }
}
