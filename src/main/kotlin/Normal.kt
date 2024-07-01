package org.example

import kotlin.math.sqrt

/**
 * Data class which represents a 3D normal vector
 */
data class Normal(var x:Float=0f, var y:Float=0f, var z:Float=0f)
    {
    override fun toString(): String {
        return "Normal (x=$x, y=$y, z=$z)"
    }

    /**
     * Check if two normals are close within a given epsilon
     */
    fun is_close(normal: Normal, eps:Float=1e-15f):Boolean
    {
        return (are_similar(this.x, normal.x, eps=eps) and are_similar(this.y, normal.y, eps=eps) and  are_similar(this.z, normal.z, eps=eps))
    }

    /**
     * Return the opposite normal
     */
    fun neg():Normal
    {
        return Normal(x=-this.x, y=-this.y, z=-this.z)
    }

    /**
     * Scale the normal by a float factor
     */
    operator fun times(factor:Float):Normal
    {
        return Normal(x=factor * this.x, y=factor * this.y, z=factor * this.z)
    }

   /**
    * Divide the normal by a float factor
    */
    operator fun div(factor:Float):Normal
    {
        return Normal(x=this.x / factor, y=this.y / factor, z=this.z / factor)
    }

    /**
     * Scale the normal by an integer factor
     */
    operator fun times(factor:Int):Normal
    {
        return Normal(x=factor * this.x, y=factor * this.y, z=factor * this.z)
    }

    /**
     * Divide the normal by an integer factor
     */
    operator fun div(factor:Int):Normal
    {
        return Normal(x=this.x / factor, y=this.y / factor, z=this.z / factor)
    }


    /**
     * Compute the dot product with another normal
     */
    operator fun times(vec: Normal):Float
    {
        return (this.x * vec.x + this.y * vec.y + this.z * vec.z)
    }

    /**
     * Compute the cross product with a vector
     */
    operator fun times(vec: Vec):Normal
    {
        return Normal(
            x=this.y * vec.z - this.z * vec.y,
            y=this.z * vec.x - this.x * vec.z,
            z=this.x * vec.y - this.y * vec.x,
        )
    }

    /**
     * Compute the cross product with another normal
     */
    fun prod(normal: Normal):Normal
    {
        return Normal(
            x=this.y * normal.z - this.z * normal.y,
            y=this.z * normal.x - this.x * normal.z,
            z=this.x * normal.y - this.y * normal.x,
        )
    }

    /**
     * Compute the norm (magnitude) of the normal
     */
    fun norm():Float
    {
        return sqrt(this.squared_norm())
    }

    /**
     * Compute the squared norm (magnitude) of the normal
     */
    fun squared_norm():Float
    {
        return this * this
    }

    /**
     * Normalize the normal to a unit vector
     */
    fun normalize():Normal
    {
        return (this / (this.norm()))
    }

    /**
     * Convert the normal to a vector
     */
    fun toVec():Vec
    {
        return Vec(
            x=this.x,
            y=this.y,
            z=this.z
        )
    }

}
