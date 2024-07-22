package org.example
import kotlin.math.floor
/**
 * Class Pigment, associates a Color to a Point of coordinates (u,v) of a 2D parametric surface
 * This is an open class meant to be extended by different pigment types
 */
open class Pigment() {

    /**
     * Function to get the color at a given 2D point (u, v).
     * This method is open and can be overridden by subclasses.
     */
    open fun get_color(uv: Vec2D): Color {
        return Color()
    }

    open fun are_similar(pigment: Pigment, eps: Float=1e-5f):Boolean
    {
        return false
    }

}

/**
 * Class UniformPigment: Represents a uniform pigment that applies a consistent hue over the entire surface.
 * @color = the uniform color to be applied
 */
class UniformPigment( val color :Color): Pigment() {

    /**
     * Overrides the get_color function to return the uniform color
     */
    override fun get_color(uv: Vec2D): Color {
        return this.color
        }

    override fun are_similar(pigment: Pigment, eps:Float): Boolean {
        if (pigment !is UniformPigment)
            return false
        return pigment.color.is_close(this.color, eps=eps)
    }
}
/**
 * Class CheckeredPigment: Represents a checkered pigment that alternates between two colors depending on the position.
 *
 * Parameters:
 * @color1 = first color in the checkered pattern.
 * @color2 = second color in the checkered pattern.
 * @n_steps = number of steps to divide the surface for the checkered effect.
 */
class CheckeredPigment (val color1 : Color, val color2 : Color, val n_steps: Int=10)  : Pigment() {

    /**
     * Overrides the get_color function to return the appropriate checkered color.
     */
    override fun get_color(uv : Vec2D) : Color {
        val u: Int = floor((uv.u * n_steps)).toInt()
        val v: Int = floor((uv.v * n_steps)).toInt()
        return if ((u % 2) == (v % 2))  color1 else color2
    }

    override fun are_similar(pigment: Pigment, eps: Float): Boolean {
        if(pigment !is CheckeredPigment)
            return false
        return pigment.color1.is_close(this.color1, eps = eps) and pigment.color2.is_close(this.color2, eps=eps) and (pigment.n_steps==this.n_steps)
    }

}

/**
 * Class ImagePigment: Represents a textured pigment, where the texture is derived from a PFM image
 */
class ImagePigment(val Image : HdrImage) : Pigment() {

    /**
     * Overrides the get_color function to return the color from the image at the given coordinates.
     */
    override fun get_color(uv: Vec2D): Color {
        var col = (uv.u * Image.width).toInt()
        var row = (uv.v * Image.height).toInt()

        if (col >= Image.width) {
            col = Image.width -1
        }
        if (row >= Image.height) {
            row = Image.height -1
        }
        return Image.get_pixel(col,row)
    }

    override fun are_similar(pigment: Pigment, eps: Float): Boolean {
        if (pigment !is ImagePigment)
            return false
        if((pigment.Image.width!=this.Image.width) or (pigment.Image.height!=this.Image.height))
            return false
        var res=true

        for (i in 0 until pigment.Image.pixels.size)
        {
             res = res and pigment.Image.pixels[i].is_close(this.Image.pixels[i])
        }

        return res
    }

    operator fun times(factor:Float):ImagePigment
    {
        val new_image:HdrImage=HdrImage(this.Image.width, this.Image.height, this.Image.pixels)
        new_image.pixels.forEach {
            it.r*=factor
            it.g*=factor
            it.b*=factor
        }
        return ImagePigment(new_image)
    }

}