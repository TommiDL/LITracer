
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.multiple
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import org.example.Color
import org.example.HdrImage
import org.example.InvalidPfmFileFormat
import org.example.read_pfm_image
import java.io.FileInputStream
import java.io.FileOutputStream

class ImageMerge:CliktCommand(printHelpOnEmptyArgs = true,help="Merge PFM file to create a less noisy image")
{
    val args by argument("pfm_files", help = "PFM files to merge").multiple()

    val output_pfm by option(
        "--output_pfm", "-pfm",
        help = "Path to save the result in a PFM file (specify .pfm) [default merge.pfm]"
    ).default("merge.pfm")

    val output_png:String? by option(
        "--output_png", "-png",
        help = "Path to save the result in a PNG file with default sRGB conversion values (specify .png) [default null]"
    )

    override fun run() {
        var files:Array<HdrImage> = arrayOf<HdrImage>()

        args.forEach {
            files += read_pfm_image(FileInputStream(it))
        }

        val w=files[0].width
        val h=files[0].height

        //check files dimensions are equals
        for (i in files)
        {
            if ((i.width!=w) or (i.height!=h))
            {
                throw InvalidPfmFileFormat("Error: To proceed with merging files have to be of same size")
            }
        }

        val res:HdrImage=HdrImage(width = w, height = h)

        for(col in 0 until w)
        {
            for (row in 0 until h)
            {
                var color:Color=Color()
                for (file in files)
                {
                    color+=file.get_pixel(col, row)
                }

                color/=files.size

                res.set_pixel(col, row, color)
            }
        }

        res.write_pfm_image(FileOutputStream(output_pfm))

        if(output_png!=null)
        {
            res.write_ldr_image(FileOutputStream(output_png), format = "PNG")
        }


    }
}