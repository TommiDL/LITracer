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

    val rotation_ax:String by option(
        "--rotatoin_ax", "-rotax",
        help = "Axis of rotation for the camera"
    ).choice("x", "y", "z").default("z")

    val traslation_x:Float by option(
        "--translation-x", "-trx",
        help = "Insert the desired value of translation of the camera on x-axis  [default value 0]"
    ).float().default(0f)

    val traslation_y:Float by option(
        "--translation-y", "-try",
        help = "Insert the desired value of translation of the camera on y-axis  [default value 0]"
    ).float().default(0f)

    val traslation_z:Float by option(
        "--translation-z", "-trz",
        help = "Insert the desired value of translation of the camera on z-axis  [default value 0]"
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
                "-   pathtracing -> rendering with pathtracing alg" +
                " [default value pathtracing]"

    ).choice("onoff", "flat", "pathtracing").default("pathtracing")


    // path tracer parameters
    val n_ray:Int by option(
        "--nray",
        help = "number of rays for pathtracing algorithm  [default value 10]"
    ).int().default(10)
    val max_depth:Int by option(
        "--max-depth", "-md" ,
        help = "max depth of bouncing per ray  \u0085[default value 5]"
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

    /**
     * Declare and set up the world with objects
     */
    fun declare_world():World
    {
        println("reading pfm files...")
        //val lcm:HdrImage=read_pfm_image(FileInputStream("nebula.pfm"))
        val wood:HdrImage=read_pfm_image(FileInputStream("pigment_images/wood.pfm"))

        println("Done")

        // Declare 10 Spheres objects, ray 1/10 in the vertex of a cube
        val objs:MutableList<Shape> = mutableListOf(

            Plane(
                transformation = translation(Vec(x=3f)) * rotation(Vec(y=1f), theta = PI.toFloat()/2),
                material = Material(
                    brdf = DiffusiveBRDF(CheckeredPigment(Color(r=5f), Color(g=10f))),
                    emitted_radiance = CheckeredPigment(Color(r=0.0f), Color(g=0.0f))
                )

            ),


            Plane(
                transformation = translation(Vec(z=-1f)),
                material =Material(
                    brdf = DiffusiveBRDF(
                        ImagePigment(
                            wood
                        )

                        //UniformPigment(
                          //  Color(1f,1f,1f)
                        //)
                    ),

                    emitted_radiance = ImagePigment(
                        wood
                    )
                   // ImagePigment(
                     //   read_pfm_image(FileInputStream("wood.pfm"))
                    //)
                )
            ),

            Sphere(
                transformation = translation(Vec(z=8f)) * scalar_transformation(7f),
                material =Material(
                    brdf = SpecularBRDF(
                        UniformPigment(Color(1f,1f,1f))
                    ),

                    emitted_radiance = UniformPigment(Color())
                    // ImagePigment(
                    //   read_pfm_image(FileInputStream("wood.pfm"))
                    //)
                )
            ),


            Sphere(
                transformation = translation(Vec(z=-0.5f, x=1f, y=-1.5f)) * scalar_transformation(0.5f),
                material =Material(
                    brdf = DiffusiveBRDF(
                        ImagePigment(
                            read_pfm_image(FileInputStream("pigment_images/wood.pfm"))
                        )
                    ),

                    emitted_radiance = UniformPigment(Color())
                )
            ),

            Sphere(
                transformation = translation(Vec(z=-0.5f, y=0.5f, x=1f)) * scalar_transformation(0.5f),
                material =Material(
                    brdf = DiffusiveBRDF(
                        ImagePigment(
                            read_pfm_image(FileInputStream("pigment_images/porfido.pfm"))
                        )
                    ),

                    emitted_radiance =ImagePigment(
                        read_pfm_image(FileInputStream("pigment_images/porfido.pfm"))
                    )

                )
            ),


            Sphere(
                transformation = translation(Vec(z=-0.5f,y=-0.5f, x=1f)) * scalar_transformation(0.5f),
                material = Material(
                    emitted_radiance = UniformPigment(Color(5f,5f,5f)),
                    brdf = SpecularBRDF(UniformPigment(Color(0f,0f, 0f,)))
                )
            ),





            Mesh(
                FileInputStream("mesh_obj_files/tetrahedron.obj"),
                transformation = translation(Vec(z=-1f, x=1f, y=2f)) * scalar_transformation(3f) ,
                material = Material(
                    brdf = DiffusiveBRDF(
                        ImagePigment(
                            read_pfm_image(FileInputStream("pigment_images/porfido.pfm"))
                        )
                    ),
                    emitted_radiance = UniformPigment(Color(0f,0f,0f))
                )
            ),



            Sphere(
                transformation = scalar_transformation(100f),
                material = Material(
                    brdf = SpecularBRDF(UniformPigment(Color())),
                    emitted_radiance = UniformPigment(Color())
                )
            )


        )

        return World( objs )

    }

    /**
     * Select and configure the camera based on the user's choice
     */
    private fun _camera_selection()
    {
        val translation:Transformation= translation(
            Vec(
                x = this.traslation_x,
                y = this.traslation_y,
                z = this.traslation_z
            )
        )
        val u:Vec
        if (this.rotation_ax=="x")
        {
            u=Vec(x=1f)
        }else if (this.rotation_ax=="y")
        {
            u=Vec(y=1f)
        }else if(this.rotation_ax=="z")
        {
            u=Vec(z=1f)
        }else
        {
            u=Vec(z=1f)
        }

        val rotation:Transformation = rotation(
            u = u,
            theta = rotation_angle*2* PI.toFloat()/360f
        )

        if (this.camera_ch=="perspective")
            this.camera=PerspectiveCamera(
                transformation = rotation *translation* translation(Vec(-1f, 0f, 0f)),
                aspect_ratio = this.width.toFloat()/this.height,
                distance = this.distance
            )
        else if (this.camera_ch=="orthogonal")
            this.camera=OrthogonalCamera(
                transformation = rotation * translation * translation(Vec(-1f, 0f, 0f)),
                aspect_ratio = this.width.toFloat()/this.height,
            )

    }

    /**
     * Select the renderer based on the user's choice
     */
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

    /**
     * Main function to run the demo and generate the images
     */
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
            img.write_pfm_image(FileOutputStream("images/" + "${this.pfm}.pfm"))
            println("Image saved in PFM format at PATH: ${"images/" + this.pfm}.pfm")
        }catch (e1: FileNotFoundException)
        {
            println("Impossible to write on file ${"images/" + this.pfm}.pfm")
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
                val out_stream: FileOutputStream = FileOutputStream("images/" + param.output_png_filename)
                img.write_ldr_image(stream = out_stream, format = "PNG", gamma = param.gamma)
                println("Image saved in PNG format at PATH: ${"images/" + param.output_png_filename}")

            }catch (e:Error)
            {
                println("Impossible to write on file ${"images/" + param.output_png_filename}")
                println("Error: $e")

                return
            }

        }

    }



}
