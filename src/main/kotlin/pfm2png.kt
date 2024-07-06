
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.float
import org.example.HdrImage
import org.example.Parameters
import org.example.read_pfm_image
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream

/**
 * Command Line Interface to convert a PFM file into a PNG image
 *
 * Parameters:
 *      @ pfm_input = input PFM file
 *      @ a = clamp value
 *      @ gamma = gamma value of the screen
 *      @ png output = output name of the png file
 *
 */
class pfm2png: CliktCommand(printHelpOnEmptyArgs = true, help="Conversion from a PFM file to a PNG image")
{
    // Input PFM file name
   val pfm_input:String by argument(
        "--input_pfm",
        help="name of the input file PFM (specify .pfm format) to load"
    )

    // Clamp value with default of 1
    val a:Float by option(
        "--factor", "-a",
        help="clamp value (float)  [default value 1]"
    ).float().default(1f)

    // Gamma value with default of 1
    val gamma:Float by option(
        "--gamma",
        help="gamma value of the screen  [default value 1]"
    ).float().default(1f)

    // Output PNG file name
    val png_output:String by argument(
        "--output_png",
        help = "name of the output file PNG (specify .png format)"
    )

    // Optional luminosity normalization
    val luminosity:Float? by option(
        "--luminosity", "-lum",
        help = "Luminosity to normalize image  \n" +
                "[default value null]"
    ).float()


    /**
     * Main function to run the command.
     * It reads the PFM file, processes the image, and writes the output to a PNG file.
     */
    override fun run()
    {

        val parameters: Parameters = Parameters()

        try {

            parameters.parse_command_line(
                listOf<String>(
                    pfm_input,
                    "$a",
                    "$gamma",
                    png_output)
            )
        } catch (e:RuntimeException)
        {
            println("Error:$e")
            return
        }


        try {
            val inp_stream: FileInputStream = FileInputStream(parameters.input_pfm_file_name)
            val img: HdrImage = read_pfm_image(stream=inp_stream)
            println("File ${parameters.input_pfm_file_name} has been read from the disk")

            img.normalize_image(factor = parameters.factor)
            img.clamp_image()

            try {
                val out_stream: FileOutputStream = FileOutputStream(parameters.output_png_filename)
                img.write_ldr_image(stream = out_stream, format = "PNG", gamma = parameters.gamma)

                println("${parameters.output_png_filename} has been written to disk")

            }catch (e1: FileNotFoundException)
            {
                println("Impossible to write on file ${parameters.output_png_filename}")
                println("Error: $e1")

                return
            }

        } catch (e2: FileNotFoundException)
        {
            println("Impossible to open file ${parameters.input_pfm_file_name}")
            println("Error: $e2")
            return
        }



    }


}

