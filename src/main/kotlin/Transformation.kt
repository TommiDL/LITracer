package org.example

import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin

val ID:FloatArray = floatArrayOf(
    1f,0f,0f,0f,
    0f,1f,0f,0f,
    0f,0f,1f,0f,
    0f,0f,0f,1f
)

/**
 * A class representing a 4x4 homogeneous transformation matrix
 * @matrix = elements of the matrix stored in a row-major order
 * @width = width of the matrix, default is 4
 * @height = height of the matrix, default is 4
 */
data class HomMatrix(var matrix:FloatArray,
                     var width:Int=4,
                     var height:Int=4,
                    )
{

    init {

        require(matrix.size==this.width*this.height) { "A homogeneous matrix must be 4×4" }
    }

    /**
     * Default constructor initializing the matrix to the identity matrix
     */
    constructor():this(ID.copyOf())
    /*constructor():this(
        floatArrayOf(
            1.0f, 0.0f, 0.0f, 0.0f,
            0.0f, 1.0f, 0.0f, 0.0f,
            0.0f, 0.0f, 1.0f, 0.0f,
            0.0f, 0.0f, 0.0f, 1.0f
        )
    )*/

    override fun toString(): String {
        var str:String=""

        for(i in 0 until this.width)
        {
            for(j in 0 until this.height)
            {
                str+=this[i,j].toString()+"\t"
            }
            str+="\n"
        }
        return str
    }

    /**
     * Accesses the element at position (x, y) in the matrix.
     */
    operator fun get(x:Int, y:Int):Float
    {
        return matrix.get(x*this.width+y)
    }

    /**
     * Sets the element at position (x, y) in the matrix.
     */
    operator fun set(x:Int, y:Int, float: Float)
    {
        matrix.set(x * this.width + y, float)
    }

    /**
     * Set the matrix to the specified array of elements
     *
     * the order is given by concatenate rows
     *
     * @array = new elements of the matrix
     */
    fun set(array:FloatArray)
    {
        if (array.size==matrix.size)
        {
            matrix = array.copyOf()
        }
        else
        {
            throw ArrayIndexOutOfBoundsException("The size of the array is not compatible with the matrix dimension\n${array.toString()} ")
        }
    }

    /**
     * Multiplies this matrix by another matrix
     */
    operator fun times(m:HomMatrix):HomMatrix
    {
        val result:HomMatrix = HomMatrix( FloatArray(m.width*this.height){0f} )

        for(i in 0 until this.height)
        {
            for(j in 0 until m.width)
            {
                for (k in 0 until this.width)
                {
                    result[i,j]+=this[i,k]*m[k,j]
                }
            }
        }
        return result
    }

    /**
     * Scalar product (Multiplies this matrix by a scalar value)
     */
    operator fun times(factor:Float):HomMatrix
    {
        val res:HomMatrix=HomMatrix()
        for(i in 0 until this.matrix.size)
        {
            res.matrix[i]=factor*this.matrix[i]
        }
        return res
    }


    /**
     * Product matrix-Vec->Vec (Multiplies this matrix by a vector)
     */
    operator fun times(vec: Vec): Vec
    {
        val vx:Float=matrix[0]              *vec.x  +matrix[1]              *vec.y  +matrix[2]              *vec.z
        val vy:Float=matrix[this.width+0]   *vec.x  +matrix[this.width+1]   *vec.y  +matrix[this.width+2]   *vec.z
        val vz:Float=matrix[2*this.width+0] *vec.x  +matrix[2*this.width+1] *vec.y  +matrix[2*this.width+2] *vec.z

        //val alpha:Float=matrix[3*this.width+0]*vec.x+matrix[3*this.width+1]*vec.y+matrix[3*this.width+2]*vec.z

    //    if(are_similar(alpha,0f))
    //    {
            return Vec(x=vx,y=vy,z=vz)
    //    }
    }
    /**
     * Product matrix-Point->Point (Multiplies this matrix by a point)
     */
    operator fun times(p:Point):Point
    {
        val vx:Float=matrix[0]*p.x+matrix[1]*p.y+matrix[2]*p.z+matrix[3]
        val vy:Float=matrix[this.width+0]*p.x+matrix[this.width+1]*p.y+matrix[this.width+2]*p.z+matrix[this.width+3]
        val vz:Float=matrix[2*this.width+0]*p.x+matrix[2*this.width+1]*p.y+matrix[2*this.width+2]*p.z+matrix[2*this.width+3]
        val alpha:Float=matrix[3*this.width+0]*p.x+matrix[3*this.width+1]*p.y+matrix[3*this.width+2]*p.z+matrix[3*this.width+3]

        if(are_similar( alpha,1f))
        {
            return Point(x=vx,y=vy,z=vz)
        }
        else
        {
            return Point(x=vx/alpha, y=vy/alpha, z=vz/alpha)
        }
    }

    /**
     * Product matrix-Normal->Normal (Multiplies this matrix by a normal vector)
     */
    operator fun times(normal: Normal):Normal
    {
        val vx:Float=matrix[0]*normal.x + matrix[this.width+0]*normal.y +matrix[2*this.width+0]*normal.z
        val vy:Float=matrix[1]*normal.x + matrix[this.width+1]*normal.y +matrix[2*this.width+1]*normal.z
        val vz:Float=matrix[2]*normal.x + matrix[this.width+2]*normal.y +matrix[2*this.width+2]*normal.z

        return Normal(x=vx,y=vy,z=vz)
    }

    /**
     * Calculates the determinant of this matrix
     */
    fun det():Float
    {
        require(this.width == this.height) { "A squared matrix must have width = height" }

        if (this.width == 1) return this[0,0]
        if (this.width == 2) return (this[0,0] * this[1,1] - this[0,1] * this[1,0])

        var det = 0f
        for (c in 0 until this.width) {
            var incValue = 1f
            var decValue = 1f

            for (r in 0 until this.height) {
                incValue *= this[r, (c+r) % this.width]
                decValue *= this[this.height-r-1, (c+r) % this.width]
            }
            det += (incValue - decValue)
        }
            return det
    }

}


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

/**
 * A class representing a transformation consisting of a matrix and its inverse
 */
data class Transformation(var matrix:HomMatrix= HomMatrix(ID.copyOf()), var invmatrix:HomMatrix=HomMatrix(ID.copyOf()))
{
    override fun toString(): String {
        val str:String="Matrix:\n${this.matrix.toString()}\nInverse Matrix:\n${this.invmatrix.toString()}"

        return str
    }

    /**
     * Checks if the transformation matrix and its inverse are consistent
     */
    fun is_consistent():Boolean
    {
        val prod:HomMatrix = this.matrix*this.invmatrix
        return are_matr_close(prod, HomMatrix(ID))
    }

    /**
     * Multiplies this transformation by another transformation
     */
    operator fun times(other:Transformation):Transformation
    {
        return Transformation(
            matrix=this.matrix*other.matrix,
            invmatrix=other.invmatrix*this.invmatrix
        )
    }

    /**
     * Transforms a point using this transformation
     */
    operator fun times(p: Point):Point
    {
        return this.matrix*p
    }

    /**
     * Transforms a vector using this transformation
     */
    operator fun times(vec: Vec):Vec
    {
        return this.matrix*vec
    }

    /**
     * Transforms a normal vector using this transformation
     */
    operator fun times(normal: Normal):Normal
    {
        return this.invmatrix*normal
    }

    /**
     * Transforms a ray using this transformation
     */
    operator fun times(ray:Ray):Ray
    {
        return Ray(
            origin = this * ray.origin,
            dir = this * ray.dir,
            tmin = ray.tmin,
            tmax = ray.tmax,
            depth = ray.depth,
        )
    }

    /**
     * Returns the inverse of this transformation
     */
    fun inverse():Transformation
    {
        return Transformation(matrix=this.invmatrix, invmatrix=this.matrix)
    }
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


/**
 * Returns a Translation trasformation generated by the input vector
 */
fun translation(vec:Vec=Vec()):Transformation
{
    return Transformation(
        matrix = HomMatrix(floatArrayOf(
            1f, 0f, 0f, vec.x,
            0f, 1f, 0f, vec.y,
            0f, 0f, 1f, vec.z,
            0f, 0f, 0f, 1f   ,
            )),
        invmatrix = HomMatrix(floatArrayOf(
            1f, 0f, 0f, -vec.x,
            0f, 1f, 0f, -vec.y,
            0f, 0f, 1f, -vec.z,
            0f, 0f, 0f, 1f   ,
            ))
    )
}

/**
 * Returns the array of elements for a rotation HomoMatrix
 * with the given axis (@u) and angle (@theta)
 */
fun _rotation_matrix(u:Vec=Vec(), theta: Float=0f):FloatArray
{
    val v:Vec=u.normalize()
    return floatArrayOf(
            cos(theta) + v.x.pow(2)*(1- cos(theta)),     v.x*v.y*(1- cos(theta)) - v.z*sin(theta),      v.x*v.z*(1- cos(theta)) + v.y* sin(theta),      0f,
            v.y*v.x*(1 - cos(theta)) + v.z* sin(theta),     cos(theta) + ( v.y.pow(2)*(1- cos(theta)) ),    v.y*v.z*(1- cos(theta)) - v.x* sin(theta),      0f,
            v.z*v.x*(1 - cos(theta)) - v.y*sin(theta) ,     v.z*v.y*(1- cos(theta)) +v.x* sin(theta),      cos(theta) +(v.z.pow(2)*(1- cos(theta))),      0f,
            0f                                        ,     0f                                     ,        0f                                      ,      1f,
    )
}

/**
 * Returns a Rotation transformation around a given axis by a specified angle in radians.
 *
 * @u = vec representing the axis of rotation. Default is a zero vector.
 * @theta = angle of rotation in radians. Default is 0.
 *
 * @return A transformation representing the rotation.
 * @example
 * // Example usage:
 * // Create a transformation that rotates by π radians (180 degrees) around the z-axis
 * val rotation_z: Transformation = rotation(Vec(0f, 0f, 1f), theta = (PI).toFloat())
 */
fun rotation(u:Vec=Vec(), theta:Float=0f):Transformation
{
    return Transformation(
        matrix = HomMatrix( _rotation_matrix(u, theta)),
        invmatrix = HomMatrix( _rotation_matrix(u, -theta))
    )
}

/**
 * Returns the array of elements for a scaling homogeneous matrix with the given scale factors.
 *
 * @sx = scaling factor along the x-axis.
 * @sy = scaling factor along the y-axis.
 * @sz = scaling factor along the z-axis.
 * @return The array of elements for the scaling matrix.
 */
fun _scalar_tranformation_array(sx:Float=1f, sy:Float=1f, sz:Float=1f):FloatArray
{
    return floatArrayOf(
        sx, 0f, 0f, 0f,
        0f, sy, 0f, 0f,
        0f, 0f, sz, 0f,
        0f, 0f, 0f, 1f,
    )
}

/**
 * Returns a scaling transformation with the given scale factors along each axis
 */
fun scalar_transformation(sx:Float=1f, sy:Float=1f, sz:Float=1f):Transformation
{
    return Transformation(
        matrix = HomMatrix(_scalar_tranformation_array(sx, sy, sz)),
        invmatrix = HomMatrix(_scalar_tranformation_array(1/sx, 1/sy, 1/sz))
    )
}

/**
 * Returns a uniform scaling transformation with the given scale factor (@s)
 */
fun scalar_transformation(s:Float=1f):Transformation
{
    return Transformation(
        matrix = HomMatrix(_scalar_tranformation_array(s, s, s)),
        invmatrix = HomMatrix(_scalar_tranformation_array(1/s, 1/s, 1/s))
    )
}
