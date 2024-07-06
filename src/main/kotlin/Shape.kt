package org.example

/**
 * A base class representing a geometric shape in the scene.
 *
 * @transformation = transformation applied to the shape (e.g., translation, rotation, scaling).
 * @material = material properties of the shape (e.g., color, texture, reflectivity).
 */
open class Shape(
    open val transformation: Transformation=Transformation(),
    open val material: Material=Material(),
)
{

    /**
     * Determines whether a given ray intersects with the shape.
     *
     * @ray = ray to test for intersection.
     * @return: A HitRecord containing the details of the intersection, or null if there is no intersection.
     */
    open fun ray_intersection(ray: Ray): HitRecord?
    {
        return null
    }

}