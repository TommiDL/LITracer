package org.example

import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.imageio.ImageIO
import kotlin.math.log10
import kotlin.math.pow

/**
 * Clamps a floating-point value using a specific formula.
 */
fun _clamp (x: Float): Float
{
    return x / (1 + x)
}

/**
 * Reads an HDR image from a PFM file:
 * @stream = the input stream to read the PFM file from
 */
fun read_pfm_image(stream: InputStream):HdrImage
{

    val magic=_read_line(stream)
    if(magic!="PF")
    {
        throw InvalidPfmFileFormat("Invalid magic in PFM File")
    }


    val img_size:String=_read_line(stream)
    val _width=_parse_img_size(img_size)[0]
    val _height=_parse_img_size(img_size)[1]

    val endianness_line:String=_read_line(stream)
    val endiannes: ByteOrder = _parse_endianness(endianness_line)

    val img:HdrImage = HdrImage(width=_width, height=_height)

    for (i in img.height-1 downTo 0){
        for(j in 0..img.width-1){
            val r:Float= _read_float(stream, endiannes)
            val g:Float= _read_float(stream, endiannes)
            val b:Float= _read_float(stream, endiannes)

            img.set_pixel(j,i, Color(r,g,b))

        }
    }


    return img


}

/**
 * Parses the endianness from the PFM file:
 * @line = line containing the enaidnness information
 */
fun _parse_endianness(line:String):ByteOrder
{
    var value:Float=0f
    try {
        value=line.toFloat()
    }catch (e:Exception)
    {
        throw InvalidPfmFileFormat("missing endianness specification")
    }
    if(value>0)
    {
        return ByteOrder.BIG_ENDIAN
    }
    else if (value<0)
    {
        return ByteOrder.LITTLE_ENDIAN
    }
    else
    {
        throw InvalidPfmFileFormat("invalid endianness specification, it cannot be zero")
    }
}

/**
 * Parses the image size from the PFM file:
 * @str = string containing the image size
 */
fun _parse_img_size(str:String):Array<Int>
{
    val l = str.split(" ")

    if(l.size!=2)
    {
        throw InvalidPfmFileFormat("invalid image size specification")
    }

    var w:Int=0
    var h:Int=0

    w= l[0].toInt()
    h= l[1].toInt()

    if((w<0) or (h<0))
    {
        throw InvalidPfmFileFormat("invalid width/height")
    }


    return arrayOf<Int>(w, h)
}

/**
 * Reads a line from the input stream
 */
fun _read_line(stream: InputStream):String {

    val res:ByteArrayOutputStream=ByteArrayOutputStream()


    while (true)
    {
        val buff:Int=stream.read()

        if ((buff == -1) or  (buff=='\n'.code))
        {
            return res.toString("ASCII")
        }

        res.write(buff)
    }

}

/**
 * Read a 32-bit float from a binary file opened in a stream object:
 * @stream = input stream to read from
 * @endianness = the byte order
 */
fun _read_float(stream: InputStream, endiannes:ByteOrder):Float
{
    // dichiarare un array di 4 byte perchè le righe sono tutte a 32 bit
    val b:ByteArray=ByteArray(4)

    try {
        stream.read(b)
        val value:ByteBuffer=ByteBuffer.wrap(b)
        value.order(endiannes)
        return value.float

    }catch (e:Exception)
    {
        throw InvalidPfmFileFormat("impossible to read binary data from the file")
    }


}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

/**
 * De-clamps a floating-point value using a specific formula
 */
fun _declamp(y:Float):Float
{
    return y/(1-y)
}

/**
 * Inverts an LDR color with optional gamma correction
 */
fun invert_ldr(col:Color, gamma: Float=1f):Color
{
    val new_color:Color=Color(
        r = (col.r/255f).pow(gamma),
        g = (col.g/255f).pow(gamma),
        b = (col.b/255f).pow(gamma),
    )

    return new_color
}

/**
 * Reads a PNG image and processes it into an HDR image:
 * @stream = the input stream to read the PNG file from.
 * @parameters = additional parameters for processing.
 * @luminosity = optional luminosity value for processing.
 * @declamp = whether to apply de-clamping.
 * @denormalize = whether to apply denormalization.
 * @ldr_inversion = whether to apply LDR inversion.
 */
fun read_png(
    stream: InputStream,
    parameters: Parameters= Parameters(),
    luminosity: Float?=null,
    declamp:Boolean=false,
    denormalize:Boolean=false,
    ldr_inversion:Boolean=false
):HdrImage
{

    //read the image
    val png=ImageIO.read(stream)
    val size:Int=png.width*png.height

    val img:HdrImage=HdrImage(width = png.width, height = png.height)

    var counter:Int=0

    println("Reading Image")
    // read raw image
    for(col in 0 until png.width)
    {
        for (row in png.height-1 downTo 0 )
        {
            val png_color=png.getRGB(col, row)

            val blue: Float = (png_color and 0xff).toFloat()
            val green: Float = ((png_color and 0xff00) shr 8).toFloat()
            val red: Float = ((png_color and 0xff0000) shr 16).toFloat()

            img.set_pixel(col, row, Color(r = red, g = green, b = blue))

            counter+=1

        }
        print("\r["+"#".repeat(col*20/png.width)+" ".repeat((png.width-col)*20/png.width)+"]  [${100*counter.toFloat()/size}%]  ")
    }
    println()

    counter=0
    img.pixels.forEach {
        var color=it


        //invert ldr
        if(ldr_inversion)
            color=invert_ldr(color, gamma = parameters.gamma)

        if(declamp)
        {
            color.r = _declamp(color.r)
            color.g = _declamp(color.g)
            color.b = _declamp(color.b)
        }

        it.r=color.r
        it.g=color.g
        it.b=color.b

        counter+=1

        print("\r["+"#".repeat(counter*20/size)+" ".repeat((size-counter)*20/size)+"]  [${100*counter.toFloat()/size}%]  ")

    }


    println()

    println("final size of the array: ${img.pixels.size}")


    if (denormalize)
    {
        println("de-normalizing in progress")
        img._denormalize_image(
            factor = parameters.factor,
            luminosity = luminosity
        )

        println()
        println("done")
    }

    return img

}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

/**
 * Represents an HDR image (matrix) with dimensions (width, height) and a pixel array of colors in RGB format
 */
class HdrImage(val width:Int = 0, val height:Int=0)
{
    var pixels = Array<Color>(size = width * height) {Color(0f,0f,0f)}

    constructor(width: Int, height: Int, pixels:Array<Color>):this(width = width, height=height)
    {
        this.pixels=pixels.clone()
    }

    /**
     * Return the `Color` value for a pixel in the image
     *
     * The pixel in the top-left corner has coordinates (0, 0).
     */
    fun get_pixel(x:Int, y:Int):Color
    {

        assert(this.valid_coordinates(x,y))
        return this.pixels[this.pixel_offset(x,y)]
    }


    /**
     * Sets a new color for a pixel in the image
     * The pixel in the top-left corner has coordinates (0, 0)
     */
    fun set_pixel(x:Int, y:Int, new_color:Color)
    {

        assert(this.valid_coordinates(x,y))
        this.pixels[this.pixel_offset(x,y)]=new_color
    }


    /**
     * Checks if the coordinates are within the image boundaries
     */
    fun valid_coordinates(x:Int, y:Int):Boolean
    {
        return ((x>=0) and (x<this.width) and (y>=0) and (y<this.height))
    }

    /**
     * Returns the index of the pixel in the array
     */
    fun pixel_offset(x:Int, y:Int):Int
    {
        return y*this.width+x
    }

    /**
     * Computes the average of the image using a logarithmic average
     * @delta = a small value to avoid log(0)
     */
    fun average_luminosity (delta: Double = 1e-10): Double
    {
        var cumsum:Double = 0.0

        for (pix in this.pixels) {
            cumsum += log10(delta + pix.luminosity())
        }

        return 10.0.pow(cumsum / this.pixels.size)
    }

    /**
     * Normalizes the image based on the average luminosity:
     * @factor = normalization factor
     * @luminosity = optional luminosity value for normalization
     */
    fun normalize_image (factor: Float, luminosity: Float? = null)
    {
        val lum = luminosity ?: this.average_luminosity(delta = 1e-10)
        for(i in this.pixels.indices)
            this.pixels[i] = (this.pixels[i] * (factor / lum.toFloat()))
    }

    /**
     * De-normalizes the image based on the average luminosity:
     * @factor = normalization factor
     * @luminosity = optional luminosity value for normalization
     */
    fun _denormalize_image(factor: Float, luminosity: Float? = null)
    {
        val lum = luminosity ?: this.average_luminosity(delta = 1e-10)
        for(i in this.pixels.indices)
            this.pixels[i] = (this.pixels[i] * (lum.toFloat()/factor))

    }


    /**
     * Correction for light sources through "_clamp" function
     * declared outside the class HdrImage
     */
    fun clamp_image ()
    {
        for(i in this.pixels.indices) {
            this.pixels[i].r = _clamp(this.pixels[i].r)
            this.pixels[i].g = _clamp(this.pixels[i].g)
            this.pixels[i].b = _clamp(this.pixels[i].b)
        }
    }

    /**
     * Writes a float value to a stream with the specified byte order(endianness)
     */
    fun _writeFloatToStream(stream: OutputStream, value: Float, order: ByteOrder) {
        val bytes = ByteBuffer.allocate(4).putFloat(value).array() // Big endian

        if (order == ByteOrder.LITTLE_ENDIAN) {
            bytes.reverse()
        }

        stream.write(bytes)
    }

    /**
     * Writes the HDR image to a PFM file
     *
     * the color matrix is written with (0,0) in the bottom left corner
     */
    fun write_pfm_image(stream: OutputStream, endiannes:ByteOrder = ByteOrder.LITTLE_ENDIAN) {

        var counter:Int=0

        println("Writing pfm image")
        print("[")

        val endiannes_str:String = if (endiannes == ByteOrder.LITTLE_ENDIAN) "-1.0" else "1.0"

        //setting the header that must be printed on the top of the file
        val header:String = "PF\n${this.width} ${this.height}\n$endiannes_str\n"
        //converting the string type to tne ASCII
        try {
            stream.write(header.toByteArray())
        }catch (e:IOException){
            println("Stream Write Error:$e")
        }
        // the (0,0) pixel starts from the bottom left
        for (i in this.height-1 downTo 0){
            for(j in 0..this.width-1){
                val color:Color = this.get_pixel(j,i)
                _writeFloatToStream(stream, color.r, endiannes)
                _writeFloatToStream(stream, color.g, endiannes)
                _writeFloatToStream(stream, color.b, endiannes)

                counter+=1

            }
            print("\r["+"#".repeat((this.height-i)*20/this.height)+" ".repeat((i)*20/this.height)+"]  [ ${100*counter.toFloat()/this.pixels.size}% ]   ")
        }
        println()
    }

    /**
     * Writes the HRD image to a LDR image file from a Stream of Output in the specified format:
     * @stream = output stream
     * @format = image format (e.g. "png")
     * @gamma = gamma correction value
     */
    fun write_ldr_image(stream: OutputStream, format:String, gamma:Float=1.0f)
    {
        var counter:Int=0

        println("Writing LDR image in format $format")
        print("[")
        val img:BufferedImage = BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB)

        for (y in 0 until this.height) {
            for (x in 0 until this.width) {
                // 0xFF0000 corresponds to sRGB(255, 0, 0):
                val color:Color=this.pixels[pixel_offset(x,y)]


                val conv:Int = (255*(color.r.pow(1/gamma))).toInt() * 65536 + (255*(color.g.pow(1/gamma))).toInt() * 256 + (255*(color.b.pow(1/gamma))).toInt()
                img.setRGB(x, y, conv)

                counter+=1
            }
            print("\r["+"#".repeat((y)*20/this.height)+" ".repeat((this.height-y)*20/this.height)+"]  [${100*counter.toFloat()/this.pixels.size} %]    ")
        }
        ImageIO.write(img, format, stream)
        println()
    }




}







