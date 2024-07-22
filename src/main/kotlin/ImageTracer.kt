package org.example

/**
 * Interface between camera and screen
 * Arguments: image = the image to be created and camera = the observer
 */
class ImageTracer(val image:HdrImage, val camera: Camera)
{
    /**
     * Fires a ray in a given pixel, specifying the target's position in the pixel
     * @col = column of the pixel
     * @row = row of the pixel
     * @u_pixel = horizontal position within the pixel
     * @v_pixel = vertical position within the pixel
     */
    fun fire_ray(col:Int, row:Int, u_pixel:Float=0.5f, v_pixel:Float=0.5f):Ray
    {
        val u:Float=(col+u_pixel)/(this.image.width)
        val v:Float= 1f - (row+v_pixel)/(this.image.height)


        return this.camera.fire_ray(u=u, v=v)
    }

    /**
     * Fires a 'samples' number of rays using stratified sampling in every pixel of the image with a given function for rendering the ray
     * @func = function that takes a Ray and returns a Color
     */
    fun fire_all_ray(func:(Ray)->Color, pcg: PCG=PCG(), samples:Int=1)
    {
        val side:Float=1f/samples

        var counter:Int=0
        val size:Int=this.image.height*this.image.width

        println("Firing all rays")
        print("["+" ".repeat(this.image.height/10)+"]")
        print("\r[")
        for(row in 0 until this.image.height)
        {
            for(col in  0 until this.image.width)
            {
                var color:Color=Color()

                //if samples=1 use default values
                if (samples==1)
                {
                    val ray:Ray=this.fire_ray(col, row)
                    color=func(ray)
                    this.image.set_pixel(col, row, color)
                    counter+=1
                    continue
                }


                for (i in 0 until  samples)
                {
                    val ray:Ray=this.fire_ray(col, row, u_pixel = i*side+pcg.random_float(), v_pixel = i*side+pcg.random_float())

                    color+=func(ray)

                }
                color/=samples

                this.image.set_pixel(col, row, color)

                counter+=1
            }
            //print("\r["+"#".repeat(1+row/10)+" ".repeat(this.image.height/10 - (row/10))+"]\r")
            print("\r["+"#".repeat(row*20/this.image.height)+" ".repeat((this.image.height-row)*20/this.image.height)+"] [${100*counter.toFloat()/size} %]      ")

        }
        //print("]")
        println()
    }
}