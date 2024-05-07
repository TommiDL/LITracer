package org.example

class World {
    var list_shapes: MutableList<Shape> = mutableListOf()
    fun add(shape: Shape) {
        list_shapes.add(shape)
    }

    fun rayIntersection(ray: Ray): HitRecord? {
        var closest: HitRecord? = null
        for (shape in list_shapes) {
            val intersection = shape.ray_intersection(ray)

            if (intersection == null) {
                continue
            }

            closest = intersection
        }
        return closest
    }
}
