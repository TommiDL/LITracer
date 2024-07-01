package org.example

//data class  --> override operatori
/**
 * Data class representing a color in RGB floating-point notation
 * @r = red component
 * @g = green component
 * @b = blue component
 */
data class Color(var r:Float = 0f, var g:Float = 0f, var b:Float = 0f)
{
    /** Overrides the equality operator (==) to compare two Color objects
     */
    override fun equals(other: Any?): Boolean {
        if (other is Color) {
            return ((this.r == other.r) and (this.g == other.g) and (this.b == other.b))

        }

        return false
    }

    /**
     * Sum every component of the RGB format of 2 Colors
     */
    operator fun plus(col:Color):Color
    {
        return Color(this.r + col.r, this.g + col.g, this.b + col.b)
    }

    /**
     * Minus: (R1,G1, B1) - (R2,G2,B2) = (R1-R2, G1-G2, B1-B2)
     */
    operator fun minus(col: Color):Color
    {
        return Color(r=this.r-col.r, g=this.g-col.g, b=this.b-col.b)
    }

    /**
     * Scalar(floating-point)*Color: multiply every component of the RGB format by the value passed by
     */
    operator fun times(factor:Float):Color
    {
        return Color(r = factor * this.r, g = factor * this.g, b = factor * this.b)
    }

    /**
     * Scalar(integer)*Color: multiply every component of the RGB format by the value passed by
     */
    operator fun times(factor:Int):Color
    {
        return Color(r = factor * this.r, g = factor * this.g, b = factor * this.b)
    }

    /**
     * Color*Color (tensor product):  return the color given by the product component by component
     * (R1,G1,B1)*(R2,G2,B2)=(R1*R2, G1*G2, B1*B2)
     */
    operator fun times(factor: Color):Color
    {
        return Color(r=factor.r*this.r, g=factor.g*this.g, b=factor.b*this.b,)
    }

    /**
     * Color/Scalar(Floating-point): Divide every component of the RGB format by the value passed by
     */
    operator fun div(factor: Float):Color
    {
        return Color(r=this.r/factor, g=this.g/factor, b=this.b/factor)
    }

    /**
     * Color/Scalar(Integer): Divide every component of the RGB format by the value passed by
     */
    operator fun div(factor: Int):Color
    {
        return Color(r=this.r/factor, g=this.g/factor, b=this.b/factor)
    }

    /*    operator fun div(factor: Color):Color
    {
        /**
         * Color/Color:  return the color given by the division component by component:
         *      (R1,G1,B1)/(R2,G2,B2)=(R1/R2, G1/G2, B1/B2)
         */
        if (factor.r or)

    }
*/

    /**
     * Returns a string representation of the Color in RGB notation
     */
    override fun toString(): String
    {
        return "Color(r=$r, g=$g, b=$b)"
    }

    /**
     * Compares two colors within a certain tolerance for the floating-point components
     * @color = the color to compare with
     * @eps = the tolerance value
     */
    fun is_close(color:Color, eps:Float=1e-5f):Boolean
    {
        if(are_similar(this.r, color.r, eps) and are_similar(this.g, color.g, eps) and are_similar(this.b, color.b, eps))
        {
            return true
        }

        return false
        
    }

    /**
     * Returns the image luminosity in a double variable.  
     * No input, just uses r,g,b of type color
     * Luminosity is calculated as the average of the maximum and minimum RGB components
     */
    fun luminosity(): Double
    {
        return (maxOf(this.r, this.g, this.b) + minOf(this.r, this.g, this.b)) * 0.5
    }

}

