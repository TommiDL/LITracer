package org.example

/**
 * Class World: Holds a list of shapes which makes a "world"
 * Add shapes to a world using ".World.add()".
 * Call "World.ray_intersection" to check whether a light ray intersects any of the shapes in the world.
 */
class World(var list_shapes: MutableList<Shape> = mutableListOf()) {
    fun add(shape: Shape) {
        list_shapes.add(shape)
    }

    /**
     * Checks for ray intersection with shapes in the world.
     * @return The closest hit record if any intersection found, otherwise null.
     */
    fun ray_intersection(ray: Ray): HitRecord? {
        var closest: HitRecord? = null
        for (shape in list_shapes) {
            val intersection = shape.ray_intersection(ray)

            if (intersection == null) {
                continue
            }
            if (closest == null || intersection.t < closest.t) {
            closest = intersection
            }
        }
        return closest
    }
}

