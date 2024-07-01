package org.example

import kotlin.math.*

/**
 * A class representing a spherical shape.
 *
 * @transformation = transformation applied to the sphere (e.g., translation, rotation, scaling).
 * @material = material properties of the sphere (e.g., color, texture, reflectivity).
 */
class Sphere(
    override val transformation:Transformation=Transformation(),
    override val material: Material=Material()
):Shape() {

    /**
     * Determines whether a given ray intersects with the sphere
     */
    override fun ray_intersection(ray: Ray): HitRecord? {

        val inv_ray:Ray = ray.transform(this.transformation.inverse())

        val delta_4:Float= (inv_ray.origin.conversion() * inv_ray.dir).pow(2) - inv_ray.dir.squared_norm()*(inv_ray.origin.conversion().squared_norm()-1)

        if(delta_4<=0)
            return null

        val t1:Float = (-(inv_ray.origin.conversion()*inv_ray.dir) - sqrt(delta_4))/inv_ray.dir.squared_norm()
        val t2:Float = (-(inv_ray.origin.conversion()*inv_ray.dir) + sqrt(delta_4))/inv_ray.dir.squared_norm()

        val first_hit_t:Float

        if( (t1>inv_ray.tmin) and (t1<inv_ray.tmax) )
            first_hit_t = t1
        else if ( (t2>inv_ray.tmin) and (t2<inv_ray.tmax) )
            first_hit_t = t2
        else return null

        val hit_point:Point=inv_ray.at(first_hit_t)

        return HitRecord(
            world_point = this.transformation * hit_point,
            normal = this.transformation*_sphere_normal(hit_point, ray.dir),
            surface_point = _sphere_point_to_uv(hit_point),
            t=first_hit_t,
            ray = ray,
            material = this.material
        )

    }

    /**
     * Computes the normal vector at a given point on the sphere
     */
    fun _sphere_normal(point:Point, ray_dir:Vec):Normal
    {
        val res:Normal=Normal(x = point.x, y = point.y, z = point.z)

        return if (point.conversion() * ray_dir < 0f) res else res*(-1)
    }

    /**
     * Converts a point on the sphere to UV coordinates
     */
    fun _sphere_point_to_uv(point:Point):Vec2D
    {
        val u = atan2(point.y, point.x) / (2f* PI.toFloat())

        return Vec2D(u= if(u>=0f) u else u + 1f, v = acos(point.z)/PI.toFloat())

    }

}