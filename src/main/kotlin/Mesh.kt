package org.example

import java.io.InputStream


class indexes(val i1:Int, val i2:Int, val i3:Int)


//tutta la mesh viene trasformata


/**
 * Mesh of triangles to represent a complex shape
 *
 * Parameters:
 *      - Vertexes of triangles
 *      - Normals of the shape for each vertex
 *      - 3 Indexes of the vertexes to define a triangle
 */
class Mesh :Shape{
    val vertexes: Array<Point>
    val triangles:Array<indexes>
    //normali smooth nella mesh

    val normals:Array<Normal>

    override val material:Material

    val aabb:AxisAlignedBoundingBox

    override val transformation:Transformation
    /**
     * Constructor from arrays
     */
    constructor(
        vertexes: Array<Point>,
        triangles:Array<indexes>,
        normals:Array<Normal>,
        transformation: Transformation=Transformation(),
        material: Material=Material()
    )
    {
        this.vertexes = vertexes
        this.triangles = triangles
        this.normals=normals

        this.transformation=transformation
        this.material=material

        aabb=AxisAlignedBoundingBox(this)

    }

    /**
     * Constructor Mesh of triangle from a obj file
     */
    constructor(
        stream: InputStream,
        transformation:Transformation=Transformation(),
        material: Material=Material()
    )
    {

        println("In constructor from obj file")
        this.transformation = transformation
        this.material=material

        var vertexes= arrayOf<Point>()
        var normals= arrayOf<Normal>()
        var triangles= arrayOf<indexes>()

        var lines:List<String> = stream.bufferedReader().readLines()

        lines.forEach{line:String->
            //println(line)
            val split=line.split(" ")

            if((line.contains("#")) or (line == " ")) {
                return@forEach
            }

            if((split[0]=="v"))
            {
                vertexes+=Point(
                    x=split[1].toFloat(),
                    y=split[2].toFloat(),
                    z=split[3].toFloat(),
                )
            }

            if ((split[0]=="vn"))
            {
                normals+=Normal(
                    x=split[1].toFloat(),
                    y=split[2].toFloat(),
                    z=split[3].toFloat(),
                )
            }

            if(split[0]=="f")
            {
                triangles+=indexes(
                    i1=split[1].split("//")[0].toInt(),
                    i2=split[2].split("//")[0].toInt(),
                    i3=split[3].split("//")[0].toInt(),
                )
            }


        }


        this.vertexes=vertexes
        this.normals=normals
        this.triangles=triangles

        if(vertexes.size<3) throw Error("Not enough vertexes in obj file")
        //if(normals.size<3) throw Error("Not enough normals in obj file")
        if(triangles.isEmpty()) throw Error("Not enough triangles in obj file")

        aabb=AxisAlignedBoundingBox(this)

    }


    /**
     * Get a triangle from the mesh specifing the indexes of the vertexes
     */
    fun get_triangle(indexes: indexes):Triangle
    {
        
        return Triangle(
            A=this.vertexes[indexes.i1-1],
            B=this.vertexes[indexes.i2-1],
            C=this.vertexes[indexes.i3-1],
            material=material
            )
    }
    /**
     * Get a triangle from the mesh specifing the indexes of the vertexes
     */
    fun get_triangle(i1: Int, i2: Int, i3: Int):Triangle
    {

        return Triangle(
            A=this.vertexes[i1],
            B=this.vertexes[i2],
            C=this.vertexes[i3],
            material=material
        )
    }

    operator fun invoke(indexes: indexes):Triangle
    {
        return Triangle(
            A=this.vertexes[indexes.i1-1],
            B=this.vertexes[indexes.i2-1],
            C=this.vertexes[indexes.i3-1],
            material=material
        )
    }


    override fun ray_intersection(ray:Ray):HitRecord?
    {
        val inv_ray:Ray = ray.transform(this.transformation.inverse())

        if (aabb.ray_intersection(inv_ray)==false) return null

        var closest:HitRecord?=null

        this.triangles.forEach {
            val intersection:HitRecord?=this.get_triangle(it).ray_intersection(inv_ray)
            if (intersection==null)
            {
                return@forEach
            }

            if( (closest==null) || (intersection.t < closest!!.t) )
            {
                closest=intersection
            }
        }

        return closest
    }

    fun get_center():Point
    {
        var x_mean:Float=0f
        var y_mean:Float=0f
        var z_mean:Float=0f

        this.vertexes.forEach {
            x_mean+=it.x
            y_mean+=it.y
            z_mean+=it.z
        }

        return Point(
            x = x_mean/this.vertexes.size,
            y = y_mean/this.vertexes.size,
            z = z_mean/this.vertexes.size,
        )

    }


}
