
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.choice
import com.github.ajalt.clikt.parameters.types.float
import org.example.HdrImage
import org.example.Parameters
import org.example.read_png
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream

/**
 * Command Line Interface to convert a PNG file in PFM image
 *
 * Parameters:
 *      @pfm_output = name of the output PFM file
 *      @png_input = name of the input PNG file
 *      @a = clamp value
 *      @gamma = gamma value of the screen
 *      @denormalize = whether to process denormalization on th original image
 *      @luminosity = luminosity to process normalization
 *      @ldr_inv = LDR inversion from PNG image
 */
class png2pfm: CliktCommand(printHelpOnEmptyArgs = true, help="Conversion from a PNG image to a PFM file")
{

    val pfm_output:String by argument(
        "--output_pfm",
        help="name of the output file PFM (specify .pfm format) to create"
    )

    val a:Float by option(
        "--factor","-a",
        help="clamp value (float) [default value 1]"
    ).float().default(1f)

    val gamma:Float by option(
        "--gamma",
        help="gamma value of the screen [default value 1]"
    ).float().default(1f)

    val png_input:String by argument(
        "--input_png",
        help = "name of the input file PNG (specify .png format)"
    )

/*    val declamp:Boolean by option(
        "--declamp",
        help = "invert the clamping process in the png image"
    ).choice("true" to true, "false" to false).default(false)
*/
    val denormalize:Boolean by option(
        "--denormalize",
        help = "process denormalization on original image  [default value true]"
    ).choice("true" to true, "false" to false).default(true)

    val luminosity:Float? by option(
        "--luminosity",
        help = "luminosity to process normalization  \n" +
                "[default value null]"
    ).float()

    val ldr_inv:Boolean by option(
        "--ldr-inv",
        help = "LDR inversion from png image [default value true]"
    ).choice(
        "true" to true,
        "false" to false
    ).default(true)


    override fun run()
    {

        val parameters: Parameters = Parameters()

        //parse command line parameters
        try {
            parameters.parse_command_line(
                listOf<String>(
                    pfm_output,
                    "$a",
                    "$gamma",
                    png_input)
            )

        } catch (e:RuntimeException)
        {
            println("Error in parsing parameters:")
            println(e)
            return
        }

        val inp_stream: FileInputStream
        val img: HdrImage
        try {
             inp_stream= FileInputStream(parameters.output_png_filename)

            img= read_png(
                stream=inp_stream,
                parameters=parameters,
                luminosity = luminosity,
                declamp=false,
                denormalize = denormalize,
                ldr_inversion =ldr_inv
            )


        } catch (e2: FileNotFoundException)
        {
            println("Impossible to open file ${parameters.output_png_filename}")
            println("Error: $e2")
            return
        }


/*
        // renormalize by average luminosity
        if(luminosity!=null) {
            img.normalize_image(
                luminosity = luminosity,
                factor = parameters.factor
            )
        }

 */
        // write PFM file
        try {
            println("Saving")
            img.write_pfm_image(stream= FileOutputStream(parameters.input_pfm_file_name))

        } catch (e:Error)
        {
            println("Impossible to write file ${parameters.input_pfm_file_name}")
            println(e)
            return
        }

        println("File ${parameters.input_pfm_file_name} has been read from the disk")


    }


}
