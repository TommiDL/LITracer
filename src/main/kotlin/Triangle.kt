package org.example

/**
 * Definition of a triangle through 3 points
 */
data class Triangle(
    val A: Point = Point(1f, 0f, 0f),
    val B: Point = Point(0f, 1f, 0f),
    val C:Point = Point(0f, 0f, 1f),
    override val material: Material=Material()
):Shape(material=material) {

    /**
     * Returns a point inside the triangle given the barycentric coordinates (@beta & @gamma)
     */
    fun get_point(beta:Float, gamma:Float):Point?{
        if((0f <= beta) && (beta <= 1f) && (0f <= gamma) && (gamma <= 1f))
        {
            return (this.A + (this.B - this.A) * beta + (this.C - this.A) * gamma)
        }
        else
            return null
    }

    /**
     * Operator which turns instances of the class into callable objects
     */
    operator fun invoke(beta:Float, gamma: Float):Point{
        if((0f <= beta) && (beta <= 1f) && (0f <= gamma) && (gamma <= 1f)) {
            return (this.A + (this.B - this.A) * beta + (this.C - this.A) * gamma)
        }
        else {
            throw IllegalArgumentException("Point is outside the triangle! beta and gamma must be between 0 and 1")
        }
    }

    /**
     * Determinant of a 3x3 matrix
     * @a = element at (0, 0)
     * @b = element at (0, 1)
     * @c = element at (0, 2)
     * @d = element at (1, 0)
     * @e = element at (1, 1)
     * @f = element at (1, 2)
     * @g = element at (2, 0)
     * @h = element at (2, 1)
     * @i = element at (2, 2)
     */
    fun det(
        a:Float, b:Float, c:Float,
        d:Float, e:Float, f:Float,
        g:Float, h:Float, i:Float
    ):Float
    {
        val res:Float = a * (( e * i) - ( h * f )) - b * ( (d*i) - (f*g) ) + c * (( d*h ) - ( e*g ))

        return res
    }

    /**
     * Checks for ray intersection with the triangle
     * @return The hit record if there is an intersection, otherwise null
     */
    override fun ray_intersection(ray: Ray): HitRecord? {


        val M:Float = this.det(
            a=this.B.x-this.A.x, b=this.C.x-this.A.x, c=ray.dir.x,
            d=this.B.y-this.A.y, e=this.C.y-this.A.y, f=ray.dir.y,
            g=this.B.z-this.A.z, h=this.C.z-this.A.z, i=ray.dir.z
        )

        if (are_similar(M, 0f))
            return null

        val beta:Float = this.det(
            a=ray.origin.x-this.A.x, b=this.C.x-this.A.x, c=ray.dir.x,
            d=ray.origin.y-this.A.y, e=this.C.y-this.A.y, f=ray.dir.y,
            g=ray.origin.z-this.A.z, h=this.C.z-this.A.z, i=ray.dir.z
        ) /M

        val gamma:Float = this.det(
            a=this.B.x-this.A.x, b=ray.origin.x-this.A.x, c=ray.dir.x,
            d=this.B.y-this.A.y, e=ray.origin.y-this.A.y, f=ray.dir.y,
            g=this.B.z-this.A.z, h=ray.origin.z-this.A.z, i=ray.dir.z
        )/M

        val t:Float = -this.det(
            a=this.B.x-this.A.x, b=this.C.x-this.A.x, c=ray.origin.x-this.A.x,
            d=this.B.y-this.A.y, e=this.C.y-this.A.y, f=ray.origin.y-this.A.y,
            g=this.B.z-this.A.z, h=this.C.z-this.A.z, i=ray.origin.z-this.A.z
        )/M

        if (t > ray.tmax || t < ray.tmin )
            return null

        if (beta<0f || beta>1f )
            return null

        if (gamma<0f || gamma>1f)
            return null


        if(gamma+beta>1)
            return null

        val hit_point:Point? = this.get_point(beta, gamma)


        if (hit_point!=null)
            return HitRecord(
                world_point = hit_point,
                normal = _triangle_normal(hit_point, ray.dir),
                surface_point = Vec2D(beta, gamma),
                t = t,
                ray = ray,
                material = this.material
            )
        else
            return null
    }

    /**
     * Calculates the normal to a point into the triangle
     */
    fun _triangle_normal(point:Point, ray_dir:Vec):Normal
    {
        val diff1:Vec = this.B - this.A
        val diff2:Vec = this.C - this.A

        val norm:Vec = Vec(diff1.x, diff1.y, diff1.z).prod(diff2)

        return if (point.conversion() * ray_dir < 0f) norm.conversion() else norm.conversion()*(-1)
    }

}