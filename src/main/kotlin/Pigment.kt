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

}