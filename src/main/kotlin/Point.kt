package org.example

data class Point(var x:Float=0f, var y:Float=0f, var z:Float=0f){
    /**
     * this function converts to a string the values of the Point class
     */
    override fun toString():String{
        return "Point (x$x, y$y, z$z)"
    }
    /**
     * check if two Points are similar within a certain precision
     */
    fun is_close(a:Point):Boolean{
        return (are_similar(this.x, a.x, eps = 1e-5f) and are_similar(this.y, a.y, eps = 1e-5f) and are_similar(this.z, a.z, eps = 1e-5f))
    }


    /**
     * Sum between a Point and a Vector, returns a Point
     */
    fun plus_Point_Vec (a:Vec):Point{
        return Point(x=this.x + a.x, y=this.y + a.y, z=this.z + a.z)

    }

    /**
     * Difference between Points
     */
    operator fun minus(a:Point):Point
    {
        return Point(x=this.x - a.x, y=this.y - a.y, z=this.z - a.z)
    }

    /**
     * Difference between Point and a Vec, return a Point
     */
    fun minus_Point_Vec(a:Vec):Point
    {
        return Point(x=this.x - a.x, y=this.y - a.y, z=this.z - a.z)
    }

    /**
     * conversion from Point to Vec
     */
    fun conversion():Vec
    {
    return Vec(x = this.x, y=this.y, z = this.z)
    }
}
