
import org.example.*
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class SphereTest
{
    @Test

    fun test_sphere() {
        val sphere: Sphere = Sphere()

        var ray: Ray = Ray(
            origin = Point(0f, 0f, 2f),
            dir = Vec(0f, 0f, 1f) * (-1),
        )

        assertTrue(
            sphere.ray_intersection(ray)?.is_close(
                HitRecord(
                    world_point = Point(0f, 0f, 1f),
                    normal = Normal(0f, 0f, 1f),
                    surface_point = sphere._sphere_point_to_uv(Point(0f, 0f, 1f)), //da cambiare
                    t = 1f,
                    ray = ray,
                    material = sphere.material
                )
            ) ?: false
        )

        ray = Ray(
            origin = Point(3f, 0f, 0f),
            dir = Vec(1f, 0f, 0f) * (-1),
        )

        assertTrue(
            sphere.ray_intersection(ray)?.is_close(
                HitRecord(
                    world_point = Point(1f, 0f, 0f),
                    normal = Normal(1f, 0f, 0f),
                    surface_point = Vec2D(u = 0f, v = 0.5f), //da cambiare
                    t = 2f, // da cambiare
                    ray = ray,
                    material = sphere.material
                )
            ) ?: false
        )


        ray = Ray(
            origin = Point(0f, 0f, 0f),
            dir = Vec(1f, 0f, 0f),
        )

        assertTrue(
            sphere.ray_intersection(ray)?.is_close(
                HitRecord(
                    world_point = Point(1f, 0f, 0f),
                    normal = Normal(1f, 0f, 0f) * (-1),
                    surface_point = Vec2D(u = 0f, v = 0.5f),
                    t = 1f,
                    ray = ray,
                    material = sphere.material
                )
            ) ?: false
        )

    }
        @Test
        fun test_transformed_sphere()
        {
            val sphere:Sphere=Sphere(translation(vec = Vec(10f, 0f,0f)))

            var ray:Ray=Ray(
                origin = Point(10f, 0f, 2f),
                dir = Vec(0f,0f,-1f),
            )

            ray=Ray(
                origin = Point(13f, 0f, 0f),
                dir = Vec(-1f,0f,0f),
            )


            assertTrue(
                sphere.ray_intersection(ray)?.is_close(
                    HitRecord(
                        world_point = Point(11f, 0f, 0f),
                        normal = Normal(1f, 0f,0f),
                        t = 2f,
                        surface_point = Vec2D(0f, 0.5f),
                        ray = ray,
                        material = sphere.material
                    )
                )?:false
            )


            assertTrue(
                Sphere().ray_intersection(
                    Ray(
                        origin = Point(0f, 0f, 2f),
                        dir = Vec(0f,0f,-1f),
                    )
                )!=null
            )

            assertFalse(
                Sphere().ray_intersection(
                    Ray(
                        origin = Point(-10f, 0f, 0f),
                        dir = Vec(0f,0f,-1f),
                    )
                )!=null
            )


        }

}
