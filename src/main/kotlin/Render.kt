
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.choice
import com.github.ajalt.clikt.parameters.types.int
import org.example.*
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream

class Render:CliktCommand(printHelpOnEmptyArgs = true,help="Render an image from a scene defined in input file")
{
    lateinit var scene:Scene

    val file_name:String by argument(
        "--filename",
        help="File path in which the scene is defined"
    )

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
        help = "max depth of bouncing per ray  \u0085[default value 3]"
    ).int().default(3)
    val russian_roulette:Int by option(
        "--russian-roul", "-rr",
        help = "depth to start suppressing the ray bouncing probability  [default value 3]"
    ).int().default(3)
    val bck_col: Color by option(
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
    fun declare_world():Scene
    {
        val stream:InputStream=InputStream(
            stream = FileInputStream(file_name),
            file_name = file_name
        )

        val parser:Parser=Parser()

        val _scene:Scene

        try {
            _scene=parser.parse_scene(stream)

        } catch (e:Error)
        {
            println(e)
            throw e
        }

        return _scene
    }


    /**
     * Select the renderer based on the user's choice
     */
    private fun _renderer_selection(world: World): Renderer
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

        this.scene=declare_world()

        if(this.scene.camera==null)
        {
            this.scene.camera=PerspectiveCamera()
        }

        val img: HdrImage = HdrImage(width = this.width, height=this.height)

        val tracer: ImageTracer = ImageTracer(camera= this.scene.camera!!, image = img)

        val renderer: Renderer


        try {
            renderer=_renderer_selection(scene.world)
        }catch (e:FileNotFoundException)
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

            val param: Parameters = Parameters(output_png_filename = "${this.png}.png")

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

