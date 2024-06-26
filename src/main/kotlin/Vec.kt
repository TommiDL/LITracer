package org.example

import kotlin.math.pow
import kotlin.math.sqrt

data class Vec(var x:Float=0f, var y:Float=0f, var z:Float=0f)
{
    override fun toString(): String {
        return "Vector (x=$x, y=$y, z=$z)"
    }

    /**
     * Comparison with another vector
     */
    fun is_close(vec:Vec, eps:Float=1e-15f):Boolean
    {
        return (are_similar(this.x, vec.x, eps=eps) and are_similar(this.y, vec.y, eps=eps) and  are_similar(this.z, vec.z, eps=eps))
    }

    /**
     * Sum between vectors
     */
    operator fun plus(vec:Vec):Vec
    {
        return Vec(x=this.x+vec.x, y=this.y+vec.y, z=this.z+vec.z)
    }

    /**
     * Sum between vectors
     */
    operator fun plus(pt:Point):Point
    {
        return Point(x=this.x+pt.x, y=this.y+pt.y, z=this.z+pt.z)
    }


    /**
     * Difference between vectors
     */
    operator fun minus(vec:Vec):Vec
    {
        return Vec(x=this.x-vec.x, y=this.y-vec.y, z=this.z-vec.z)
    }

    /**
     * Moltiplication by a scalar
     */
    operator fun times(factor:Float):Vec
    {
        return Vec(x=factor*this.x, y=factor*this.y, z=factor*this.z)
    }

    operator fun times(factor:Int):Vec
    {
        return Vec(x=factor*this.x, y=factor*this.y, z=factor*this.z)
    }

    operator fun div(factor:Float):Vec
    {
        return Vec(x=this.x/factor, y=this.y/factor, z=this.z/factor)
    }

    operator fun div(factor:Int):Vec
    {
        return Vec(x=this.x/factor, y=this.y/factor, z=this.z/factor)
    }

    /**
     * return the opposite vector
     */
    fun neg():Vec
    {
        return Vec(x=-this.x, y=-this.y, z=-this.z)
    }

    /**
     * return the scalar product
     */
    operator fun times(vec: Vec):Float
    {
        return (this.x*vec.x + this.y*vec.y+this.z*vec.z)
    }

    /**
     * Vector product
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
     * returns the Norm of a Vector
     */
    fun norm():Float
    {
        return sqrt(this.squared_norm())
    }
    /**
     * returns the squared Norm of a Vector
     */
    fun squared_norm():Float
    {
        return this * this
    }
    /**
     * returns the Vector normalized
     */
    fun normalize():Vec
    {
        val norm:Float=this.norm()
        return Vec(this.x/norm, this.y/norm, this.z/norm)
    }
    /**
     * conversion from Vec to Normal
     */
    fun conversion():Normal
    {
        val norm=this.norm()
        return Normal(x=this.x/norm, y=this.y/norm, z=this.z/norm)
    }
}
