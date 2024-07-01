
import org.example.*
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class pathtracerTest
{
    @Test
    fun testFurnace()
    {


        val pcg:PCG=PCG()

        //run the furnace test several times using random values of L_e and p_d

        for(i in 0 until 5)
        {
            println()
            val emitted_radiance= pcg.random_float()
            println(emitted_radiance)
            val reflectance:Float= pcg.random_float()*0.9f //avoid numbers too close to 1
            println(reflectance)
            println()
            val world:World=World()

            val enclousure_material=Material(
                brdf = DiffusiveBRDF(
                    pigment = UniformPigment(Color(1f,1f,1f) * reflectance)
                ),
                emitted_radiance=UniformPigment(Color(1f,1f,1f)*emitted_radiance)
            )


            world.add(Sphere(material = enclousure_material))

            val tracer:pathtracer=pathtracer(
                world = world,
                n_ray = 1,
                max_depth = 100,
                russian_roulette_limit = 105,
                pcg = pcg
            )

            val ray:Ray=Ray(
                origin = Point(0f,0f,0f),
                dir = Vec(x=1f)
            )
            val color:Color=tracer(ray)

            val expected=emitted_radiance/(1f-reflectance)

            println(expected)
            println(color)
            assertTrue(are_similar(expected, color.r, eps = 1e-3f))
            assertTrue(are_similar(expected, color.g, eps = 1e-3f))
            assertTrue(are_similar(expected, color.b, eps = 1e-3f))


        }
    }
}
