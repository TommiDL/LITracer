
import org.example.*
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.ByteOrder
import kotlin.test.assertFailsWith

class HdrImageTest {

    @Test
    fun img_creation()
    {
        val img:HdrImage=HdrImage(7,4)

        assertTrue(img.width==7)
        assertTrue(img.height==4)
    }

    @Test
    fun getPixels() {
    }


    @Test
    fun get_set_pixel() {
        val img: HdrImage = HdrImage(7,4)
        val reference_color:Color = Color(1.0f, 2.0f, 3.0f)
        img.set_pixel(3, 2, reference_color)

        assertTrue(reference_color.is_close(img.get_pixel(3, 2)))
    }

    @Test
    fun valid_coordinates() {
        val img: HdrImage = HdrImage(7,4)

        assertTrue(img.valid_coordinates(0, 0))
        assertTrue(img.valid_coordinates(6, 3))
        assertFalse(img.valid_coordinates(-1, 0))
        assertFalse(img.valid_coordinates(0, -1))
        assertFalse(img.valid_coordinates(7, 0))
        assertFalse(img.valid_coordinates(0, 4))
    }

    @Test
    fun pixel_offset() {
        val img: HdrImage = HdrImage(7,4)

        assertTrue(img.pixel_offset(3,2) == 17)
        assertTrue(img.pixel_offset(6,3) == 7*4-1)
    }

    @Test
    fun _clamp() {
        val x:Float=10.7f
        assertTrue((_clamp(x)<=1) and (_clamp(x)>=0))
    }
    @Test
    fun average_luminosity(){
        val img:HdrImage= HdrImage(2,1)
        img.set_pixel(0,0, Color(5.0f, 10.0f, 15.0f)) //lum = 10.0
        img.set_pixel(1,0, Color(500.0f, 1000.0f, 1500.0f)) //lum = 100.0

        println(img.average_luminosity(delta = 0.0))

        assertTrue(are_similar(100.0f, img.average_luminosity(delta = 0.0).toFloat()))
    }

    @Test
    fun normalize_image() {
        val img:HdrImage=HdrImage(2,1)

        img.set_pixel(0,0,Color(5.0f, 10.0f, 15.0f))
        img.set_pixel(1,0,Color(500.0f, 1000.0f, 1500.0f))

        img.normalize_image(factor = 1000.0f, luminosity = 100.0f)

        assertTrue(img.get_pixel(0,0).is_close(Color(0.5e2f, 1.0e2f, 1.5e2f)))
        assertTrue(img.get_pixel(1,0).is_close(Color(0.5e4f, 1.0e4f, 1.5e4f)))
    }

    @Test
    fun clamp_image() {
        val img:HdrImage=HdrImage(2,1)

        img.set_pixel(0,0, Color(0.5e1f, 1.0e1f, 1.5e1f))
        img.set_pixel(1,0, Color(0.5e3f, 1.0e3f, 1.5e3f))

        img.clamp_image()

        for( pix in img.pixels)
        {
            assertTrue((pix.r>=0) and (pix.r<=1))
            assertTrue((pix.g>=0) and (pix.g<=1))
            assertTrue((pix.b>=0) and (pix.b<=1))
        }

    }
    @Test
    fun test_read_pfm_image() {

        // tutti i byte con bit + sign = 1 perchè usa byte con sgn [-128, +127 = 7f]
        val LE_REFERENCE_BYTES:ByteArray = byteArrayOf(
            0x50, 0x46, 0x0a, 0x33, 0x20, 0x32, 0x0a, 0x2d, 0x31, 0x2e, 0x30, 0x0a,
            0x00, 0x00, 0xc8.toByte(), 0x42, 0x00, 0x00, 0x48, 0x43, 0x00, 0x00, 0x96.toByte(), 0x43,
            0x00, 0x00, 0xc8.toByte(), 0x43, 0x00, 0x00, 0xfa.toByte(), 0x43, 0x00, 0x00, 0x16, 0x44,
            0x00, 0x00, 0x2f, 0x44, 0x00, 0x00, 0x48, 0x44, 0x00, 0x00, 0x61, 0x44,
            0x00, 0x00, 0x20, 0x41, 0x00, 0x00, 0xa0.toByte(), 0x41, 0x00, 0x00, 0xf0.toByte(), 0x41,
            0x00, 0x00, 0x20, 0x42, 0x00, 0x00, 0x48, 0x42, 0x00, 0x00, 0x70, 0x42,
            0x00, 0x00, 0x8c.toByte(), 0x42, 0x00, 0x00, 0xa0.toByte(), 0x42, 0x00, 0x00, 0xb4.toByte(), 0x42,
        )

        val BE_REFERENCE_BYTES:ByteArray= byteArrayOf(
            0x50, 0x46, 0x0a, 0x33, 0x20, 0x32, 0x0a, 0x31, 0x2e, 0x30, 0x0a, 0x42,
            0xc8.toByte(), 0x00, 0x00, 0x43, 0x48, 0x00, 0x00, 0x43, 0x96.toByte(), 0x00, 0x00, 0x43,
            0xc8.toByte(), 0x00, 0x00, 0x43, 0xfa.toByte(), 0x00, 0x00, 0x44, 0x16, 0x00, 0x00, 0x44,
            0x2f, 0x00, 0x00, 0x44, 0x48, 0x00, 0x00, 0x44, 0x61, 0x00, 0x00, 0x41,
            0x20, 0x00, 0x00, 0x41, 0xa0.toByte(), 0x00, 0x00, 0x41, 0xf0.toByte(), 0x00, 0x00, 0x42,
            0x20, 0x00, 0x00, 0x42, 0x48, 0x00, 0x00, 0x42, 0x70, 0x00, 0x00, 0x42,
            0x8c.toByte(), 0x00, 0x00, 0x42, 0xa0.toByte(), 0x00, 0x00, 0x42, 0xb4.toByte(), 0x00, 0x00,
        )

        for(reference_bytes in listOf(LE_REFERENCE_BYTES, BE_REFERENCE_BYTES))
        {
            val img:HdrImage= read_pfm_image(ByteArrayInputStream(reference_bytes))

            assertTrue(img.width==3)
            assertTrue(img.height==2)

            println(img.get_pixel(0, 0))

            assertTrue(img.get_pixel(0, 0).is_close(Color(1.0e1f, 2.0e1f, 3.0e1f)))
            assertTrue(img.get_pixel(1, 0).is_close(Color(4.0e1f, 5.0e1f, 6.0e1f)))
            assertTrue(img.get_pixel(2, 0).is_close(Color(7.0e1f, 8.0e1f, 9.0e1f)))
            assertTrue(img.get_pixel(0, 1).is_close(Color(1.0e2f, 2.0e2f, 3.0e2f)))
            assertTrue(img.get_pixel(0, 0).is_close(Color(1.0e1f, 2.0e1f, 3.0e1f)))
            assertTrue(img.get_pixel(1, 1).is_close(Color(4.0e2f, 5.0e2f, 6.0e2f)))
            assertTrue(img.get_pixel(2, 1).is_close(Color(7.0e2f, 8.0e2f, 9.0e2f)))

        }

    }


    @Test
    fun test_read_pfm_image_wrong() {
        val buf:ByteArrayInputStream= ByteArrayInputStream("PF\\n3 2\\n-1.0\\nstop".toByteArray())

        assertFailsWith<InvalidPfmFileFormat> { read_pfm_image(buf) }
    }


    @Test
    fun test_parse_endianness() {
        assertTrue(org.example._parse_endianness("1.0") == ByteOrder.BIG_ENDIAN)
        assertTrue(org.example._parse_endianness("-1.0") == ByteOrder.LITTLE_ENDIAN)

        assertFailsWith<InvalidPfmFileFormat> { _parse_endianness(line = "0.0") }
        assertFailsWith<InvalidPfmFileFormat> { _parse_endianness(line = "abc") }


    }

    @Test
    fun test_parse_img_size() {
        assertTrue(_parse_img_size("3 2").contentEquals((arrayOf <Int>(3,2))))

        assertFailsWith<InvalidPfmFileFormat> { _parse_img_size("-1 3") }
        assertFailsWith<InvalidPfmFileFormat> { _parse_img_size("3 2 1") }
        assertFailsWith<InvalidPfmFileFormat> { _parse_img_size("2") }

    }

    @Test
    fun test_read_line() {
        val line:ByteArrayInputStream=ByteArrayInputStream("Hello\nworld".toByteArray())

        assertTrue(_read_line(line)=="Hello")
        assertTrue(_read_line(line)=="world")
        assertTrue(_read_line(line)=="")


    }


    @Test
    fun test_pfm_from_png()
    {
        val factor:Float=0.2f
        val gamma:Float=1f

        val parameters=Parameters(
            factor = factor,
            gamma = gamma,
        )

        //read pfm and create a png
        val image:HdrImage= read_pfm_image(FileInputStream("memorial.pfm"))
        val lum:Float=image.average_luminosity().toFloat()
        image.normalize_image(factor=factor, luminosity = lum)
        image.clamp_image()

        image.write_ldr_image(
            stream = FileOutputStream("memorial.png"),
            format = "png",
            gamma = gamma,
        )


        println("doing conversion")
        //read png and create pfm
        val new_img=read_png(
            stream = FileInputStream("memorial.png"),
            parameters=parameters,
            luminosity = lum
        )

        //new_img!!.normalize_image(factor=factor)
        println("conversion done")

        assertTrue(new_img!=null)

        assertTrue(new_img?.width==image.width)
        assertTrue(new_img?.height==image.height)

        val pcg=PCG()

        println("\nAverage luminosity")
        println("\treal image "+image.average_luminosity())
        new_img!!.normalize_image(factor=factor)    //normalize new image
        println("\tconverted  " + new_img?.average_luminosity())


        assertTrue(are_similar(image.average_luminosity(), new_img!!.average_luminosity(), eps = 0.5f))

   /*     println("random testing started")
        if(new_img!=null)
        {
            for (trial in 0 until 100)
            {
                println("trial $trial")
                val col: Int = round(pcg.random_float() * new_img.width).toInt()
                val row: Int = round(pcg.random_float() * new_img.height).toInt()

//                println("\t$col $row")
//                println("\treal image ${image.get_pixel(col, row)}")
//                println("\tconverted  ${new_img.get_pixel(col, row)}")

                /*
                println("\t\tR real/converted ${image.get_pixel(col, row).r/new_img.get_pixel(col, row).r}")
                println("\t\tG real/converted ${image.get_pixel(col, row).g/new_img.get_pixel(col, row).g}")
                println("\t\tB real/converted ${image.get_pixel(col, row).b/new_img.get_pixel(col, row).b}")
*/

//                println("\t\tR/B real      ${image.get_pixel(col, row).r/image.get_pixel(col, row).b}")
 //               println("\t\tR/B converted ${new_img.get_pixel(col, row).r/new_img.get_pixel(col, row).b}")

                println("\t\tfraction ${
                    new_img.get_pixel(col, row).luminosity()/image.get_pixel(col, row).luminosity()
                }")

/*
                println("\t\tR-B real      ${(image.get_pixel(col, row).r-image.get_pixel(col, row).b)/(image.get_pixel(col, row).r+image.get_pixel(col, row).b)}")
                println("\t\tR-B converted ${(new_img.get_pixel(col, row).r-new_img.get_pixel(col, row).b)/(new_img.get_pixel(col, row).r+new_img.get_pixel(col, row).b)}")
*/
//                println("\t\tR-B real      ${(new_img.get_pixel(col, row).r-new_img.get_pixel(col, row).b)/(image.get_pixel(col, row).r-image.get_pixel(col, row).b)}")


                assertTrue(
                    are_similar(
                        (image.get_pixel(col, row).r-image.get_pixel(col, row).b)/(image.get_pixel(col, row).r+image.get_pixel(col, row).b),
                        (new_img.get_pixel(col, row).r-new_img.get_pixel(col, row).b)/(new_img.get_pixel(col, row).r+new_img.get_pixel(col, row).b),
                        eps=1f
                    )
                )

                assertTrue(
                    are_similar(
                        (image.get_pixel(col, row).r-image.get_pixel(col, row).g)/(image.get_pixel(col, row).r+image.get_pixel(col, row).g),
                        (new_img.get_pixel(col, row).r-new_img.get_pixel(col, row).g)/(new_img.get_pixel(col, row).r+new_img.get_pixel(col, row).g),
                        eps=1f
                    )
                )

                assertTrue(
                    are_similar(
                        (image.get_pixel(col, row).g-image.get_pixel(col, row).b)/(image.get_pixel(col, row).g+image.get_pixel(col, row).b),
                        (new_img.get_pixel(col, row).g-new_img.get_pixel(col, row).b)/(new_img.get_pixel(col, row).g+new_img.get_pixel(col, row).b),
                        eps=1f
                    )
                )

            }
        }
*/



    }
}