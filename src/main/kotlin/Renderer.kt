package org.example

/**
 * A class implementing a solver of the renderer equation to inherit
 * @world = world in which the rendering is performed.
 * @background_color = background color to use when no objects are hit by a ray
 */
open class Renderer(open val world: World, open val background_color:Color=Color(0f,0f,0f))
{
    /**
     * Estimates the radiance along a @ray
     */
    open operator fun invoke(ray: Ray):Color
    {
        throw NotImplementedError("Renderer.radiance is an abstract Method")
    }
}

/**
 * An on/off renderer
 *
 *This renderer is mostly useful for debugging purposes, as it is really fast, but it produces simple images
 */
class OnOffRenderer(
    override val world: World,
    override val background_color: Color=Color(0f,0f,0f),
    val color:Color=Color(255f,255f, 255f)
):Renderer(world)
{

    /**
     * Estimates the radiance along a @ray
     */
    override operator fun invoke(ray: Ray):Color
    {
        return if (this.world.ray_intersection(ray=ray)!=null)  this.color else this.background_color
    }
}


/**
 * A «flat» renderer
 *
 *     This renderer estimates the solution of the rendering equation by neglecting any contribution of the light.
 *     It just uses the pigment of each surface to determine the final radiance.
 */
class FlatRenderer(
    override val world: World,
    override val background_color: Color=Color(0f,0f,0f)
):Renderer(world)
{

    /**
     * Estimates the radiance along a ray
     */
    override operator fun invoke(ray: Ray): Color {

        val hit=this.world.ray_intersection(ray)

        if(hit==null)
            return this.background_color

        val material:Material=hit.material

        return material.brdf.pigment.get_color(hit.surface_point) +
                material.emitted_radiance.get_color(hit.surface_point)
    }
}
