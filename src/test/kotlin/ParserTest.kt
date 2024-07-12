
import org.example.*
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream
import java.io.FileInputStream
import java.io.FileNotFoundException
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class ParserTest
{
    val parse=Parser()
    @Test
    fun test_expect_symbol()
    {

        var str:String=""
        for(k in listOf('(', ',', ')', '{', '}', '[', ']', ';', ':', '!', '?', '*', '/'))
        {
            str+=k+"\n"
        }

        str+=">\n"
        str+="\"string\"\n"

        val line: ByteArrayInputStream = ByteArrayInputStream(str.toByteArray())


        val stream: InputStream=InputStream(line)

        for(k in listOf('(', ',', ')', '{', '}', '[', ']', ';', ':', '!', '?', '*', '/'))
            parse.expect_symbol(stream, k.toString())

        assertFailsWith<GrammarError> {
            parse.expect_symbol(stream, ">")
        }
        assertFailsWith<GrammarError> {
            parse.expect_symbol(stream, ">")
        }
    }


    @Test
    fun test_expect_number() {
        val line: ByteArrayInputStream = ByteArrayInputStream(
            "1\t\t\t 2.0  \n 3  m  ".toByteArray()
        )
        val stream = InputStream(line)

        assertTrue(are_similar(parse.expect_number(stream, Scene()), 1f))
        assertTrue(are_similar(parse.expect_number(stream, Scene()), 2f))
        assertTrue(are_similar(parse.expect_number(stream, Scene()), 3f))

        assertFailsWith<GrammarError> {
            parse.expect_number(stream, Scene())
        }
    }
    @Test
    fun test_expect_keywords()
    {
        val line: ByteArrayInputStream = ByteArrayInputStream(
            "new \t\t\timage    trial".toByteArray()
        )

        val stream=InputStream(line)

        assertTrue(parse.expect_keywords(stream, listOf(KeywordEnum.NEW, KeywordEnum.IMAGE))==KeywordEnum.NEW)
        assertTrue(parse.expect_keywords(stream, listOf(KeywordEnum.NEW, KeywordEnum.IMAGE))==KeywordEnum.IMAGE)


        assertFailsWith<GrammarError> {
            parse.expect_keywords(stream, listOf(KeywordEnum.NEW, KeywordEnum.IMAGE))
        }

    }
    @Test
    fun test_expect_string()
    {
        val line: ByteArrayInputStream = ByteArrayInputStream(
            "\"string\" >".toByteArray()
        )

        val stream=InputStream(line)

        assertTrue(
            parse.expect_string(stream)=="string"
        )

        assertFailsWith<GrammarError> {
            parse.expect_string(stream)
        }
    }


    @Test
    fun test_expect_identifier()
    {
        val line: ByteArrayInputStream = ByteArrayInputStream(
            "trial \t\t\timage    test".toByteArray()
        )

        val stream=InputStream(line)

        assertTrue(parse.expect_identifier(stream)=="trial")
        assertFailsWith<GrammarError> { (parse.expect_identifier(stream)=="image") }
        assertTrue(parse.expect_identifier(stream)=="test")
    }

    // test parse
    @Test
    fun test_parse_color()
    {
        val line: ByteArrayInputStream = ByteArrayInputStream(
            " <0.7, 0.5, 1>\n\n\t\t<0.3, 0.18, 0.1><0.1, 0.2, 0.5>\t\t\t\t\n<0, 0, 0> > <>".toByteArray()
        )

        val stream=InputStream(line)

        val scene=Scene()
        assertTrue(parse.parse_color(stream, scene = scene).is_close(Color(0.7f, 0.5f, 1f)) )
        assertTrue(parse.parse_color(stream, scene = scene).is_close(Color(0.3f, 0.18f, 0.1f)) )
        assertTrue(parse.parse_color(stream, scene = scene).is_close(Color(0.1f, 0.2f, 0.5f)) )
        assertTrue(parse.parse_color(stream, scene = scene).is_close(Color(0.0f, 0.0f, 0f)) )

        assertFailsWith<GrammarError> { parse.parse_color(stream, scene = scene) }
        assertFailsWith<GrammarError> { parse.parse_color(stream, scene = scene) }

    }

    @Test
    fun test_parse_vector()
    {
        val line: ByteArrayInputStream = ByteArrayInputStream(
            "[14.6, 5.1, 0]\t\n\n\n\n[8.0, 340.6, 0.7]\n[,,,]".toByteArray()
        )

        val stream=InputStream(line)


        val scene=Scene()
        assertTrue(parse.parse_vector(stream, scene = scene).is_close(Vec(14.6f, 5.1f, 0f)) )
        assertTrue(parse.parse_vector(stream, scene = scene).is_close(Vec(8.0f, 340.6f, 0.7f)) )

        assertFailsWith<GrammarError> { parse.parse_vector(stream, scene = scene) }
        assertFailsWith<GrammarError> { parse.parse_vector(stream, scene = scene) }

    }
    @Test
    fun test_parse_pigment()
    {
        val line: ByteArrayInputStream = ByteArrayInputStream(
            ("uniform(<0.1, 2, 6.4>)\n" +
                    "checkered(<7.07, 9.34, 0.04>, <0.0, 11.3, 5>, 4)\n" +
                    "image(\"memorial.pfm\")\n\n" +
                    //"uniform(<,,,>)\n" +
                    //"checkered()\n" +
                    //"image(<0,0,0>)\n" +
                    "image(\"sky.pfm\")").toByteArray()
        )

        val stream=InputStream(line)

        val scene:Scene=Scene()

        assertTrue(parse.parse_pigment(stream, scene =scene).are_similar(UniformPigment(Color(0.1f, 2f, 6.4f))))
        assertTrue(parse.parse_pigment(stream, scene =scene).are_similar(CheckeredPigment(
            Color(7.07f, 9.34f, 0.04f),
            Color(0.0f, 11.3f, 5f),
            4)))
        assertTrue(parse.parse_pigment(stream, scene =scene).are_similar(ImagePigment(read_pfm_image(FileInputStream("memorial.pfm")))))

        //assertFailsWith<GrammarError> { parse.parse_pigment(stream, scene =scene) }
        //assertFailsWith<GrammarError> { parse.parse_pigment(stream, scene =scene) }
        //assertFailsWith<GrammarError> { parse.parse_pigment(stream, scene =scene) }
        assertFailsWith<FileNotFoundException> { parse.parse_pigment(stream, scene) }

    }


    @Test
    fun test_parse_brdf()
    {
        val line: ByteArrayInputStream = ByteArrayInputStream(
        ("    diffuse(image(\"memorial.pfm\"))" +
                "    specular(uniform(<0.5, 0.5, 0.5>))" +
                "    diffuse(checkered(<0.3, 0.5, 0.1>,\n" +
                "                      <0.1, 0.2, 0.5>, 4))" +
                "<0,0,0>").toByteArray()
    )

        val stream=InputStream(line)

        val scene:Scene=Scene()

        var brdf:BRDF=parse.parse_brdf(stream, scene)

        assertTrue(brdf is DiffusiveBRDF)
        assertTrue(brdf.pigment.are_similar(ImagePigment(read_pfm_image(FileInputStream("memorial.pfm")))))

        brdf=parse.parse_brdf(stream, scene)

        assertTrue(brdf is SpecularBRDF)
        assertTrue(brdf.pigment.are_similar(UniformPigment(Color(0.5f, 0.5f, 0.5f))))

        brdf=parse.parse_brdf(stream, scene)

        assertTrue(brdf is DiffusiveBRDF)
        assertTrue(brdf.pigment.are_similar(
            CheckeredPigment(
                Color(0.3f, 0.5f, 0.1f),
                Color(0.1f, 0.2f, 0.5f),
                4
            )
        ))

        assertFailsWith<GrammarError> { parse.parse_brdf(stream, scene) }

    }
    @Test
    fun test_parse_material()
    {
        val line: ByteArrayInputStream = ByteArrayInputStream(
            (" material trial( specular(uniform(<0.5, 0.5, 0.5>)),\n" +
                    "    uniform(<0, 0, 0>) ) " +
                    "material trial( specular(uniform(<0.5, 0.5, 0.5>)),\\n\" +\n" +
                    "                    \"    uniform(<0, 0, 0>) ) ").toByteArray()
        )

        val stream=InputStream(line)

        val scene:Scene=Scene()

        val material:Pair<String, Material> = parse.parse_material(stream, scene)

        assertTrue(material.first=="trial")

        assertTrue(material.second.brdf is SpecularBRDF)

        assertTrue(material.second.brdf.pigment.are_similar(UniformPigment(Color(0.5f, 0.5f, 0.5f))))

        assertTrue(material.second.emitted_radiance.are_similar(UniformPigment(Color(0f,0f,0f))))

        //test error repetition
        assertFailsWith<GrammarError> { parse.parse_material(stream, scene) }

    }
    @Test
    fun test_parse_transformation()
    {
        val line: ByteArrayInputStream = ByteArrayInputStream(
            (
                    "  translation([0, 0, 100]) * rotation_y(7) \n" +
                    "identity \n" +
                    "rotation_z(30) * translation([-4, 0, 1])\n" +
                    " scaling([5,5,5]) \n" +
                    "" +
                    "translation([1, 0,0]) *").toByteArray()
        )

        val stream=InputStream(line)

        val scene:Scene=Scene()

        var transformation:Transformation = parse.parse_transformation(stream, scene)

        assertTrue(transformation.is_consistent())
        assertTrue(
            are_matr_close(
                transformation.matrix,
                (translation(Vec(0f, 0f, 100f)) * rotation(Vec(x=0f, y=1f, z=0f), theta = 7f) ).matrix
            )
        )

        //identity
        transformation = parse.parse_transformation(stream, scene)
        assertTrue(transformation.is_consistent())
        assertTrue(
            are_matr_close(
                transformation.matrix,
                (Transformation()).matrix
            )
        )

        transformation = parse.parse_transformation(stream, scene)
        assertTrue(transformation.is_consistent())
        assertTrue(
            are_matr_close(
                transformation.matrix,
                ( rotation(u=Vec(z=1f), 30f) * translation(Vec(-4f, 0f, 1f) )).matrix
            )
        )


        transformation = parse.parse_transformation(stream, scene)
        assertTrue(transformation.is_consistent())
        assertTrue(
            are_matr_close(
                transformation.matrix,
                ( scalar_transformation(5f)).matrix
            )
        )


        assertFailsWith<GrammarError> { parse.parse_transformation(stream, scene) }


    }
    @Test
    fun test_parse_sphere()
    {
        val line: ByteArrayInputStream = ByteArrayInputStream(
            ("  material sphere_material(\n" +
                    "    specular(uniform(<0.5, 0.5, 0.5>)),\n" +
                    "    uniform(<0, 0, 0>)\n" +
                    ")" +
                    "  sphere(sphere_material, translation([0, 0, 1]))  ").toByteArray()
        )

        val stream=InputStream(line)

        val scene:Scene=Scene()

        val material=parse.parse_material(stream, scene)

        scene.materials[material.first]=material.second

        println(scene.materials)
        val sphere:Sphere=parse.parse_sphere(stream, scene)

        assertTrue(sphere.material.brdf is SpecularBRDF)
        assertTrue(sphere.material.brdf.pigment.are_similar(UniformPigment(Color(0.5f,0.5f, 0.5f))))
        assertTrue(sphere.material.emitted_radiance.are_similar(UniformPigment(Color(0f,0f,0f))))
        assertTrue(
            are_matr_close(
                sphere.transformation.matrix,
                translation(Vec(0f,0f,1f)).matrix
            )
        )


    }

    @Test
    fun test_parse_plane()
    {
        val line: ByteArrayInputStream = ByteArrayInputStream(
            ("  \n" +
                    "material ground_material(\n" +
                    "    diffuse(checkered(<0.3, 0.5, 0.1>,\n" +
                    "                      <0.1, 0.2, 0.5>, 4)),\n" +
                    "    uniform(<0, 0, 0>)\n" +
                    ")" +
                    "" +
                    "  plane (ground_material, identity)  ").toByteArray()
        )

        val stream=InputStream(line)

        val scene:Scene=Scene()


        val material=parse.parse_material(stream, scene)

        scene.materials[material.first]=material.second

        println(scene.materials)
        val plane:Plane=parse.parse_plane(stream, scene)

        assertTrue(plane.material.brdf is DiffusiveBRDF)
        assertTrue(plane.material.brdf.pigment.are_similar(
            CheckeredPigment(
                Color(0.3f,0.5f, 0.1f),
                Color(0.1f, 0.2f, 0.5f),
                4
            )
        ))
        assertTrue(plane.material.emitted_radiance.are_similar(UniformPigment(Color(0f,0f,0f))))
        assertTrue(
            are_matr_close(
                plane.transformation.matrix,
                Transformation().matrix
            )
        )


    }

    @Test
    fun test_parse_camera()
    {
        val line: ByteArrayInputStream = ByteArrayInputStream(
            ("  camera(perspective, rotation_z(30) * translation([-4, 0, 1]), 1.0, 1.0) " +
                    "camera(orthogonal, rotation_y(0) * translation([5, 6, -7]), 1.0) " +
                    " camera(perspective, rotation_z(30) * translation([-4, 0, 1]), 1.0, 1.0)").toByteArray()
        )

        val stream=InputStream(line)

        val scene:Scene=Scene()

        var camera:Camera=parse.parse_camera(stream, scene)

        assertTrue(camera is PerspectiveCamera)
        assertTrue(are_similar(camera.distance, 1f))
        assertTrue(are_similar(camera.aspect_ratio, 1f))
        assertTrue(
            are_matr_close(
                camera.transformation.matrix,
                (rotation(u=Vec(z=1f, x=0f, y=0f),30f) * translation(Vec(-4f, 0f, 1f))).matrix
            )
        )

        camera=parse.parse_camera(stream, scene)
        assertTrue(camera is OrthogonalCamera)
        assertTrue(are_similar(camera.aspect_ratio, 1f))
        assertTrue(
            are_matr_close(
                camera.transformation.matrix,
                (rotation(u=Vec(z=0f, x=0f, y=1f),0f) * translation(Vec(5f, 6f, -7f))).matrix
            )
        )


    }

    @Test
    fun test_parse_scene()
    {
        val line: ByteArrayInputStream = ByteArrayInputStream(
            ("   # Declare a floating-point variable named \"clock\"\n" +
                    "float clock(150)\n" +
                    "\n" +
                    "# Declare a few new materials. Each of them includes a BRDF and a pigment\n" +
                    "\n" +
                    "# We can split a definition over multiple lines and indent them as we like\n" +
                    "material sky_material(\n" +
                    "    diffuse(image(\"sky-dome.pfm\")),\n" +
                    "    uniform(<0.7, 0.5, 1>)\n" +
                    ")\n" +
                    "\n" +
                    "material ground_material(\n" +
                    "    diffuse(checkered(<0.3, 0.5, 0.1>,\n" +
                    "                      <0.1, 0.2, 0.5>, 4)),\n" +
                    "    uniform(<0, 0, 0>)\n" +
                    ")\n" +
                    "\n" +
                    "material sphere_material(\n" +
                    "    specular(uniform(<0.5, 0.5, 0.5>)),\n" +
                    "    uniform(<0, 0, 0>)\n" +
                    ")\n" +
                    "\n" +
                    "# Define a few shapes\n" +
                    "sphere(sphere_material, translation([0, 0, 1]))\n" +
                    "\n" +
                    "# The language is flexible enough to permit spaces before \"(\"\n" +
                    "plane (ground_material, identity)\n" +
                    "\n" +
                    "# Here we use the \"clock\" variable! Note that vectors are notated using\n" +
                    "# square brackets ([]) instead of angular brackets (<>) like colors, and\n" +
                    "# that we can compose transformations through the \"*\" operator\n" +
                    "plane(sky_material, translation([0, 0, 100]) * rotation_y(clock))\n" +
                    "\n" +
                    "# Define a camera\n" +
                    "camera(perspective, rotation_z(30) * translation([-4, 0, 1]), 1.0, 1.0) ").toByteArray()
        )

        val stream=InputStream(line)

        val scene:Scene=parse.parse_scene(stream)

    }



}