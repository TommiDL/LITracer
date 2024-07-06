package org.example

import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Base class for Bidirectional Reflectance Distribution Function (BRDF).
 * Represents how light is reflected at an opaque surface
 */
open class BRDF(val pigment: Pigment = UniformPigment(Color(255f, 255f, 255f)))
{
    /**
     * Function that returns a color corresponding to the BRDF integrated in the r/g/b band
     */
    open fun eval(normal: Normal, in_dir: Vec, out_dir: Vec, uv: Vec2D):Color
    {
        return Color(0f,0f,0f)
    }

    /**
     * Overloaded operator to evaluate the BRDF
     */
    operator fun invoke(normal: Normal, in_dir: Vec, out_dir: Vec, uv: Vec2D):Color
    {
        return Color(0f,0f,0f)
    }

    /**
     * Abstract method to compute the scattered ray
     * Throws NotImplementedError if not overridden
     */
    open fun scatter_ray(
        pcg:PCG,
        incoming_dir:Vec,
        interaction_point:Point,
        normal: Normal,
        depth:Int
    ):Ray
    {
        throw NotImplementedError("BRDF.scatter_ray is an abstract Method")
    }

}

/**
 * Diffusive BRDF with uniform diffusion of wavelength, representing uniform reflection
 *  parameter pigment: the pigment of the surface
 *  parameter reflectance: the reflectance coefficient
 */
class DiffusiveBRDF(pigment: Pigment, val reflectance:Float=1f):BRDF(pigment)
{
    /**
     * Evaluates the diffusive BRDF
     */
    override fun eval(normal: Normal, in_dir: Vec, out_dir: Vec, uv: Vec2D):Color
    {
        return  this.pigment.get_color(uv) * (this.reflectance/ PI.toFloat())
    }

    /**
     * Computes the scattered ray for the diffusive BRDF
     */
    override fun scatter_ray(
        pcg: PCG,
        incoming_dir: Vec,
        interaction_point: Point,
        normal: Normal,
        depth: Int
    ):Ray {

        // the last vectorf is equal to the normal
        val e:Array<Vec> = create_onb_from_z(normal)

        // bound from 0 to 1
        val cos_theta_sq:Float=pcg.random_float()


        val cos_theta:Float= sqrt(cos_theta_sq)
        val sin_theta:Float= sqrt(1f-cos_theta_sq)

        val phi:Float= 2f*PI.toFloat()*pcg.random_float()

        return Ray(
            origin = interaction_point,
            dir= e[0]* cos(phi) * cos_theta + e[1] * sin(phi) * cos_theta +e[2]*sin_theta,
            tmin = 1e-3f,
            tmax = Float.POSITIVE_INFINITY,
            depth = depth
        )
    }
}

/**
 * Specular BRDF representing perfect mirror-like reflection.
 *  parameter pigment: the pigment of the surface
 */
class SpecularBRDF(pigment: Pigment):BRDF(pigment)
{
    /**
     * Computes the scattered ray for the specular BRDF
     */
    override fun scatter_ray(pcg: PCG, incoming_dir: Vec, interaction_point: Point, normal: Normal, depth: Int): Ray {
        val ray_dir:Vec=Vec(
            x=incoming_dir.x,
            y=incoming_dir.y,
            z=incoming_dir.z
        ).normalize()

        val _norm=normal.toVec().normalize()

        return Ray(
            origin = interaction_point,
            dir = ray_dir-_norm * 2 *(_norm*ray_dir),
            tmin = 1e-3f,
            depth = depth
        )
    }
}