package org.example

import kotlin.Float.Companion.POSITIVE_INFINITY

/**
 * Ray class for the simulation of a light ray
 *
 * Parameters:
 *          @origin = origin point of the ray
 *          @dir = direction vector of the ray
 *          @tmin = minimum value of the ray parameter t
 *          @tmax = maximum value of the ray parameter t
 *          @depth = depth of the ray, typically used for recursive ray tracing
 */
data class Ray(
    val origin:Point=Point(),
    val dir:Vec=Vec(),
    val tmin:Float=1e-5f,
    val tmax:Float=POSITIVE_INFINITY,
    val depth:Int=0,
    )
{
    /**
     * Checks if this Ray is similar to another Ray within a certain precision
     */
    fun is_close(other:Ray, eps:Float=1e-5f):Boolean
    {
        return (
                this.origin.is_close(other.origin, eps=eps) and
                this.dir.is_close(other.dir, eps = eps)
                )
    }

    /**
     * Returns the point along the ray at the specified parameter t (length)
     */
    fun at(t:Float):Point
    {
        return this.origin + this.dir * t
    }

    /**
     * Return the ray evolved with the transformation given
     * @transformation = transformation to apply to the ray
     */
    fun transform(transformation: Transformation):Ray
    {
        return Ray(
            origin=transformation*this.origin,
            dir=transformation*this.dir,
            tmin=this.tmin,
            tmax = tmax,
            depth=this.depth,
        )
    }
}