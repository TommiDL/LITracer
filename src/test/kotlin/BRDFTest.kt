import org.example.*
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertTrue

class BRDFTest
{
    @Test
    fun test_DiffusiveBrdf()
    {
        val red_unif_brdf:DiffusiveBRDF=DiffusiveBRDF(
            pigment = UniformPigment(
                Color(r=20f)
            )
        )

        val pcg:PCG=PCG()

        for(trial in 0 until 100)
        {
            val rayx: Ray = red_unif_brdf.scatter_ray(
                pcg = pcg,
                incoming_dir = Vec(x = -1f),
                interaction_point = Point(),
                normal = Normal(1f, 0f, 0f),
                depth = 2
            )

            assertTrue(
                abs(rayx.dir.x) <= 1f
            )

            assertTrue(
                abs(rayx.dir.y) <= 1f
            )

            assertTrue(
                abs(rayx.dir.z) <= 1f
            )


            val rayy: Ray = red_unif_brdf.scatter_ray(
                pcg = pcg,
                incoming_dir = Vec(y = -1f),
                interaction_point = Point(),
                normal = Normal(0f, 1f, 0f),
                depth = 2
            )

            assertTrue(
                abs(rayy.dir.x) <= 1f
            )

            assertTrue(
                abs(rayy.dir.y) <= 1f
            )

            assertTrue(
                abs(rayy.dir.z) <= 1f
            )

            val rayz: Ray = red_unif_brdf.scatter_ray(
                pcg = pcg,
                incoming_dir = Vec(z = -1f),
                interaction_point = Point(),
                normal = Normal(0f, 0f, 1f),
                depth = 2
            )

            assertTrue(
                abs(rayz.dir.x) <= 1f
            )

            assertTrue(
                abs(rayz.dir.y) <= 1f
            )

            assertTrue(
                abs(rayz.dir.z) <= 1f
            )


        }
    }


    @Test
    fun test_SpecularBrdf()
    {
        val brdf:SpecularBRDF=SpecularBRDF(
            UniformPigment(Color())
        )

        val pcg=PCG()

        // x-y
        var ray:Ray=brdf.scatter_ray(
            pcg=pcg,
            interaction_point = Point(0f,0f,0f),
            incoming_dir = Vec(x=-1f, y=1f),
            normal = Normal(x=1f,0f,0f),
            depth = 2
        )

        assertTrue(ray.dir.is_close(Vec(x=1f, y=1f).normalize()))

        ray=brdf.scatter_ray(
            pcg=pcg,
            interaction_point = Point(0f,0f,0f),
            incoming_dir = Vec(x=1f, y=1f),
            normal = Normal(x=1f,0f,0f),
            depth = 2
        )

        assertTrue(ray.dir.is_close(Vec(x=-1f, y=1f).normalize()))


        //yz
        ray=brdf.scatter_ray(
            pcg=pcg,
            interaction_point = Point(0f,0f,0f),
            incoming_dir = Vec(y=1f, z=1f),
            normal = Normal(y=1f),
            depth = 2
        )

        assertTrue(ray.dir.is_close(Vec(y=-1f, z=1f).normalize()))

        ray=brdf.scatter_ray(
            pcg=pcg,
            interaction_point = Point(0f,0f,0f),
            incoming_dir = Vec(y=-1f, z=1f),
            normal = Normal(y=1f),
            depth = 2
        )

        assertTrue(ray.dir.is_close(Vec(y=1f, z=1f).normalize()))

        ray=brdf.scatter_ray(
            pcg=pcg,
            interaction_point = Point(0f,0f,0f),
            incoming_dir = Vec(y=1f, z=1f),
            normal = Normal(z=1f),
            depth = 2
        )

        assertTrue(ray.dir.is_close(Vec(y=1f, z=-1f).normalize()))

        ray=brdf.scatter_ray(
            pcg=pcg,
            interaction_point = Point(0f,0f,0f),
            incoming_dir = Vec(y=1f, z=-1f),
            normal = Normal(z=1f),
            depth = 2
        )

        assertTrue(ray.dir.is_close(Vec(y=1f, z=1f).normalize()))

        //zx
        ray=brdf.scatter_ray(
            pcg=pcg,
            interaction_point = Point(0f,0f,0f),
            incoming_dir = Vec(x=1f, z=1f),
            normal = Normal(x=1f),
            depth = 2
        )

        assertTrue(ray.dir.is_close(Vec(x=-1f, z=1f).normalize()))

        ray=brdf.scatter_ray(
            pcg=pcg,
            interaction_point = Point(0f,0f,0f),
            incoming_dir = Vec(x=-1f, z=1f),
            normal = Normal(x=1f),
            depth = 2
        )

        assertTrue(ray.dir.is_close(Vec(x=1f, z=1f).normalize()))

        ray=brdf.scatter_ray(
            pcg=pcg,
            interaction_point = Point(0f,0f,0f),
            incoming_dir = Vec(x=1f, z=1f),
            normal = Normal(z=1f),
            depth = 2
        )

        assertTrue(ray.dir.is_close(Vec(x=1f, z=-1f).normalize()))

        ray=brdf.scatter_ray(
            pcg=pcg,
            interaction_point = Point(0f,0f,0f),
            incoming_dir = Vec(x=1f, z=-1f),
            normal = Normal(z=1f),
            depth = 2
        )

        assertTrue(ray.dir.is_close(Vec(x=1f, z=1f).normalize()))


    }
}