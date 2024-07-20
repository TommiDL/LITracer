package org.example

/**
 * Interface representing an Axis-Aligned-Bounding-Box
 * Provides methods to create the bounding box and verify intersections with a ray
 */

// fare come classe
class AxisAlignedBoundingBox
{
    var xmin:Float
    var xmax:Float

    var ymin:Float
    var ymax:Float

    var zmin:Float
    var zmax:Float

    /**
     * Constructor to initialize the bounding box with specified min and max values for each axis.
     */
    constructor(
        xmin:Float,
        xmax:Float,
        ymin:Float,
        ymax:Float,
        zmin:Float,
        zmax:Float,
    )
    {
        this.xmin=xmin
        this.xmax=xmax

        this.ymin=ymin
        this.ymax=ymax

        this.zmin=zmin
        this.zmax=zmax
    }

    /**
     * Constructor to initialize the bounding box based on a mesh
     * The bounding box will be calculated to encompass all vertices of the mesh
     */
    constructor(mesh: Mesh)
    {
        this.xmax = mesh.vertexes.maxBy { it.x }.x
        this.xmin = mesh.vertexes.minBy { it.x }.x

        this.ymax = mesh.vertexes.maxBy { it.y }.y
        this.ymin = mesh.vertexes.minBy { it.y }.y

        this.zmax = mesh.vertexes.maxBy { it.z }.z
        this.zmin = mesh.vertexes.minBy { it.z }.z

    }

    /**
     * Calculate the Axis-Aligned-Bounding-Box based on the vertices of a Shape.
     * @vertices = list of vertices of the shape.
     * @return a pair of Points representing the minimum and maximum points of the bounding box
     *
     * to override in child class
     *
     */
    fun calculateBoundingBox(vertices: Array<Point>): Pair<Point, Point>
    {
        this.xmax = vertices.maxBy { it.x }.x
        this.xmin = vertices.minBy { it.x }.x

        this.ymax = vertices.maxBy { it.y }.y
        this.ymin = vertices.minBy { it.y }.y

        this.zmax = vertices.maxBy { it.z }.z
        this.zmin = vertices.minBy { it.z }.z

        return Pair(Point(x=xmin, y=ymin, z=zmin), Point(x=xmax, y=ymax, z=zmax))
    }

    /**
     * Calculate the Axis-Aligned-Bounding-Box based on the vertices of a Shape.
     * @mesh = mesh of triangles.
     * @return A pair of Points representing the minimum and maximum points of the bounding box
     *
     * to override in child class
     *
     */
    fun calculateBoundingBox(mesh: Mesh): Pair<Point, Point>
    {
        this.xmax = mesh.vertexes.maxBy { it.x }.x
        this.xmin = mesh.vertexes.minBy { it.x }.x

        this.ymax = mesh.vertexes.maxBy { it.y }.y
        this.ymin = mesh.vertexes.minBy { it.y }.y

        this.zmax = mesh.vertexes.maxBy { it.z }.z
        this.zmin = mesh.vertexes.minBy { it.z }.z

        return Pair(Point(x=xmin, y=ymin, z=zmin), Point(x=xmax, y=ymax, z=zmax))
    }


    /**
     * Verify if there is an intersection between a Ray and the Axis-Altigned-Bounding-Box
     */
    //to review for rays perpendicular to a face
    fun ray_intersection(ray: Ray): Boolean
    {

        // x intersection
        val txmin =(xmin-ray.origin.x)/ray.dir.x
        val txmax = (xmax-ray.origin.x)/ray.dir.x

        // y intersection
        val tymin = (ymin-ray.origin.y)/ray.dir.y
        val tymax = (ymax-ray.origin.y)/ray.dir.y

        // x intersection
        val tzmin = (zmin-ray.origin.z)/ray.dir.z
        val tzmax = (zmax-ray.origin.z)/ray.dir.z


        var xintersection:Boolean=true
        var yintersection:Boolean=true
        var zintersection:Boolean=true

        if ((txmin.isInfinite()) and (txmax.isInfinite()))
        {
            xintersection=false
        }

        if ((tymin.isInfinite()) and (tymax.isInfinite()))
        {
            yintersection=false
        }

        if ((tzmin.isInfinite()) and (tzmax.isInfinite()))
        {
            zintersection=false
        }


        return (

                (
                        (if (xintersection and yintersection) interval_intersection(txmin, txmax, tymin, tymax) else true) and
                        (if (xintersection and zintersection) interval_intersection(txmin, txmax, tzmin, tzmax) else true) and
                        (if (yintersection and zintersection) interval_intersection(tymin, tymax, tzmin, tzmax) else true)
                ) and (xintersection or yintersection or zintersection) //no intersection at all
                )
    }


    /**
     * Function to execute a cut of the AABB
     */
    fun cut()
    {
        println("TO DO")
    }


}

