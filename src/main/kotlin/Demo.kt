package org.example

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.choice
import com.github.ajalt.clikt.parameters.types.float
import com.github.ajalt.clikt.parameters.types.int
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import kotlin.math.PI


val WHITE:Color=Color(255f, 255f, 255f)
val BLACK:Color=Color(0f,0f,0f)




/**
 * Command Line Interface to produce an image of 10 spheres
 * Parameters:
 *      @ rotation angle
 *      @ camera type
 */
class Demo : CliktCommand(printHelpOnEmptyArgs = true,help="Create a png demo image and a pfm file")
{
    val camera_ch:String by option(
        "--camera", "-cam",
        help = "Camera type for the scene rendering: \n" +
                "\tperspective or orthogonal  \n" +
                "\t[default value perspective]"
    ).choice("perspective", "orthogonal").default("perspective")


    lateinit var camera:Camera

    val rotation_angle:Float by option(
        "--angle", "--rotation-angle", "-rot",
        help="Camera rotation angle  [default value 0]"
    ).float().default(0f)

    val traslation_x:Float by option(
        "--traslation-x", "-trx",
        help = "Insert the desired value of traslation of the camera on x-axis  [default value 0]"
    ).float().default(0f)

    val traslation_y:Float by option(
        "--traslation-y", "-try",
        help = "Insert the desired value of traslation of the camera on y-axis  [default value 0]"
    ).float().default(0f)

    val traslation_z:Float by option(
        "--traslation-z", "-trz",
        help = "Insert the desired value of traslation of the camera on z-axis  [default value 0]"
    ).float().default(0f)


    val distance:Float by option(
        "--dist",
        help = "distance of the camera from the screen  \n" +
                "[default value 1]"
    ).float().default(1f)


    val alg:String by option(
        "--algorithm", "-alg",
        help = "Select rendering algorithm type:\u0085" +
                "-   onoff -> rendering in black&white format\u0085" +
                "-   flat -> rendering in colored format\u0085" +
                "-   pathtracing -> rendering with pathtracing alg\u0085" +
                " [default value pathtracing]"

    ).choice("onoff", "flat", "pathtracing").default("pathtracing")


    // pathtracer parameters
    val n_ray:Int by option(
        "--nray",
        help = "number of rays for pathtracing algorithm  [default value 10]"
    ).int().default(10)
    val max_depth:Int by option(
        "--max-depth", "-md" ,
        help = "max depth of bouncing per ray  [default value 5]"
    ).int().default(5)
    val russian_roulette:Int by option(
        "--russian-roul", "-rr",
        help = "depth to start suppressing the ray bouncing probability  [default value 3]"
    ).int().default(3)
    val bck_col:Color by option(
        "--bck-col",
        help = "Background Color  [default value black]"
    ).choice("white" to WHITE, "black" to BLACK).default(BLACK)


    //image parameters
    val width by option(
        "--width", "-w",
        help="Width of the PNG image  [default value 480]"
    ).int().default(480)

    val height by option(
        "--height", "-he",
        help="Height of the PNG image [default value 480]"
    ).int().default(480)

    val pfm:String by option(
        "--pfm-output", "-pfm",
        help="Name of the pfm output file  \n" +
                "[default value output.pfm]"
    ).default("output")
    val png:String? by option(
        "--png-output", "-png",
        help = "Path of the png output file  \n" +
                "[default value null]"
    )




    fun declare_world():World
    {


        println("reading pfm files...")
        val lcm:HdrImage=read_pfm_image(FileInputStream("conversion.pfm"))


        println("Done")


        // Declare 10 Spheres objs, ray 1/10 in the vertex of a cube
        val objs:MutableList<Shape> = mutableListOf(

/*            Sphere(
                transformation = traslation(Vec(z=-0.5f)) * scalar_transformation(0.3f),
                material = Material(
                    emitted_radiance = UniformPigment(Color(b=20f))
                )
            ),
*/
            Plane(
                transformation = traslation(Vec(z=-1f)),
                material = Material(
                    brdf = DiffusiveBRDF(CheckeredPigment(Color(r=5f), Color(g=10f))),
                    emitted_radiance = CheckeredPigment(Color(r=0.01f), Color(g=0.01f))
                )
            ),




            Sphere(
                transformation = traslation(Vec(x=0f, y=1f, z=1f)) * scalar_transformation(0.3f),
                material = Material(
                    emitted_radiance = UniformPigment(Color(10f,10f,10f)),
                    brdf = SpecularBRDF(UniformPigment(Color(0f,0f, 0f,)))
                )
            ),



            Sphere(
                transformation = traslation(Vec(x=1.5f, y=-1.5f, z=0f)) * scalar_transformation(0.7f),
                material = Material(
                    emitted_radiance = UniformPigment(Color()),
                    brdf = SpecularBRDF(UniformPigment(Color(5f,5f,5f)))
                )
            ),




            Mesh(
                FileInputStream("tetrahedron.obj"),
                transformation = traslation(Vec(z=1f, x=-1f)),
                material = Material(
                    brdf = DiffusiveBRDF(
                        UniformPigment(
                            Color(r=10f)
                        )
                    ),
                    emitted_radiance = UniformPigment(Color(0f,0f,0f))
                )
            ),

            Mesh(
                FileInputStream("shuttle.obj"),
                transformation = traslation(Vec(z=0f, y=1f, x=3f)) * scalar_transformation(0.2f) * rotation(Vec(y=1f), theta = PI.toFloat()/8) * rotation(Vec(z=1f), PI.toFloat()/2),
                material = Material(
                    brdf = DiffusiveBRDF(
                        UniformPigment(
                            Color(r=10f)
                        )
                    ),
                    emitted_radiance = UniformPigment(Color(0f,0f,0f))
                )
            ),


/*            Mesh(
                FileInputStream("shuttle.obj"),
                transformation = traslation(Vec(z=1f, y=0f, x=3f)) * scalar_transformation(0.3f) * rotation(Vec(y=1f), theta = -PI.toFloat()/8),
                material = Material(
                    brdf = DiffusiveBRDF(
                        UniformPigment(
                            Color(b=10f)
                        )
                    ),
                    emitted_radiance = UniformPigment(Color(0f,0f,0.7f))
                )
            ),


 */

            Sphere(
                transformation = traslation(Vec(x=3f)) * scalar_transformation(100f),
                material = Material(
                    brdf= DiffusiveBRDF(
                        UniformPigment(Color())
                    ),
                    emitted_radiance = ImagePigment(
                        lcm
                    )

                )
            )



        )

        return World( objs )

    }

    private fun _camera_selection()
    {
        if (this.camera_ch=="perspective")
            this.camera=PerspectiveCamera(
                transformation = rotation(u=Vec(z=1f), rotation_angle*2* PI.toFloat()/360f) * traslation(Vec(-1f, 0f, 0f))
                    * traslation(Vec(
                    x= this.traslation_x,
                    y= this.traslation_y,
                    z= this.traslation_z,
                    )),
                aspect_ratio = this.width.toFloat()/this.height,
                distance = this.distance
            )
        else if (this.camera_ch=="orthogonal")
            this.camera=OrthogonalCamera(
                transformation = rotation(u = Vec(z=1f), rotation_angle*2* PI.toFloat()/360f)* traslation(Vec(-1f, 0f, 0f))                    * traslation(Vec(
                    x= this.traslation_x,
                    y= this.traslation_y,
                    z= this.traslation_z,
                )),
                aspect_ratio = this.width.toFloat()/this.height,
            )

    }

    private fun _renderer_selection(world: World):Renderer
    {
        if(alg=="onoff")
        {
            return OnOffRenderer(world)
        }
        else if(alg=="flat")
        {
            return FlatRenderer(world)
        }
        else if(alg=="pathtracing")
        {
            return pathtracer(
                world = world,
                n_ray = this.n_ray,
                max_depth = this.max_depth,
                russian_roulette_limit = this.russian_roulette,
                background_color = this.bck_col,
            )
        }
        else //da capire se tenerla
        {
            throw ExceptionInInitializerError("render not initialized")
        }
    }

    override fun run()
    {
        this._camera_selection()

        val world:World=declare_world()

        val img:HdrImage=HdrImage(width = this.width, height=this.height)

        val tracer:ImageTracer=ImageTracer(camera= camera, image = img)

        val renderer:Renderer


        try {
            renderer=_renderer_selection(world)
        }catch (e:Error)
        {
            println("An error occurred in renderer algorithm definition")
            println(e)
            return
        }



        tracer.fire_all_ray(){ray: Ray ->  renderer(ray)}


        try {
            // Save image in PFM file
            img.write_pfm_image(FileOutputStream("${this.pfm}.pfm"))
            println("Image saved in PFM format at PATH: ${this.pfm}.pfm")
        }catch (e1: FileNotFoundException)
        {
            println("Impossible to write on file ${this.pfm}.pfm")
            println("Error: $e1")
            return
        }



        if (png!=null)
        {

            val param:Parameters=Parameters(output_png_filename = "${this.png}.png")

            img.normalize_image(factor = param.factor)
            img.clamp_image()

            try {
                //save image in PNG file
                val out_stream: FileOutputStream = FileOutputStream(param.output_png_filename)
                img.write_ldr_image(stream = out_stream, format = "PNG", gamma = param.gamma)
                println("Image saved in PNG format at PATH: ${param.output_png_filename}")

            }catch (e:Error)
            {
                println("Impossible to write on file ${param.output_png_filename}")
                println("Error: $e")

                return
            }

        }

    }



}
