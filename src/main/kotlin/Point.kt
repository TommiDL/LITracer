package org.example

data class Point(var x:Float=0f, var y:Float=0f, var z:Float=0f){
    /**
     * Converts the point instance to a string representation
     */
    override fun toString():String{
        return "Point (x=$x, y=$y, z=$z)"
    }
    /**
     * Check if this point is similar to another point within a certain precision (@eps)
     */
    fun is_close(a:Point, eps:Float = 1e-5f):Boolean{
        return (are_similar(this.x, a.x, eps = eps) and are_similar(this.y, a.y, eps=eps) and are_similar(this.z, a.z, eps = eps))
    }


    /**
     * Adds a Vector to this Point, returning a new Point
     */
    operator fun plus (a:Vec):Point{
        return Point(x=this.x + a.x, y=this.y + a.y, z=this.z + a.z)

    }

    /**
     * Subtracts another Point to this Point, returning a Vector
     */
    operator fun minus(a:Point):Vec
    {
        return Vec(x=this.x - a.x, y=this.y - a.y, z=this.z - a.z)
    }

    /**
     * Subtracts a Vector from this Point, returning a new Point
     */
    operator fun minus(a:Vec):Point
    {
        return Point(x=this.x - a.x, y=this.y - a.y, z=this.z - a.z)
    }
    /**
     * Converts this Point to a Vector
     */
    fun conversion():Vec
    {
    return Vec(x = this.x, y=this.y, z = this.z)
    }
}
