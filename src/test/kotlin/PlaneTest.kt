

import org.example.*
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import kotlin.math.PI


class PlaneTest {

    @Test
    fun test_plane() {
        val plane: Plane = Plane()
        val ray1: Ray = Ray(
            origin = Point(0f, 0f, 1f),
            dir = Vec(0f, 0f, 1f) * (-1),
        )


        assertTrue(
            plane.ray_intersection(ray1)?.is_close(
                HitRecord(
                    world_point = Point(0f, 0f, 0f),
                    normal = Normal(0f, 0f, 1f),
                    surface_point = plane._plane_point_to_uv(Point(0f, 0f, 0f)),
                    t = 1f,
                    ray = ray1,
                    material = plane.material
                )
            ) ?: true
        )


        val ray2 : Ray = Ray(
            origin = Point(0f, 0f, 2f ),
            dir = Vec(0f, 0f, 1f )

        )

        assertFalse(plane.ray_intersection(ray2) != null)

        val ray3 : Ray = Ray(
            origin = Point(1f, 1f, 1f ),
            dir = Vec(1f, 0f, 0f )

        )
        assertFalse(plane.ray_intersection(ray3) != null)

        val ray4 : Ray = Ray(
            origin = Point(1f, 1f, 1f ),
            dir = Vec(0f, 1f, 0f )
        )
        assertFalse(plane.ray_intersection(ray4) != null)

    }

    @Test
    fun test_transformed_plane() {

        //test on a translated plane
        val plane_translated: Plane = Plane(translation(vec = Vec(0f, 0f, 1f)))

        val ray1: Ray = Ray(
            origin = Point(0f, 0f, 2f),
            dir = Vec(0f, 0f, 1f) * (-1),
        )

        assertTrue(
            plane_translated.ray_intersection(ray1)?.is_close(
                HitRecord(
                    world_point = Point(0f, 0f, 1f),
                    normal = Normal(0f, 0f, 1f),
                    surface_point = plane_translated._plane_point_to_uv(Point(0f, 0f, 1f)),
                    t = 1f,
                    ray = ray1,
                    material = plane_translated.material
                )
            ) ?: true
        )

        val ray2 : Ray = Ray(
            origin = Point(0f, 0f, 3f ),
            dir = Vec(0f, 0f, 1f )

        )

        assertFalse(plane_translated.ray_intersection(ray2) != null)

        val ray3 : Ray = Ray(
            origin = Point(2f, 2f, 2f ),
            dir = Vec(1f, 0f, 0f )

        )
        assertFalse(plane_translated.ray_intersection(ray3) != null)

        val ray4 : Ray = Ray(
            origin = Point(2f, 2f, 2f ),
            dir = Vec(0f, 1f, 0f )
        )
        assertFalse(plane_translated.ray_intersection(ray4) != null)

        //test on a rotated plane
        val plane_rotated: Plane = Plane(rotation(Vec(1f,0f,0f),  theta = ((PI)/2).toFloat()))


        val ray5 : Ray = Ray(
            origin = Point(0f, 1f, 0f ),
            dir = Vec(0f, 1f, 0f ) * (-1)
        )

        assertTrue(
            plane_rotated.ray_intersection(ray5)?.is_close(
                HitRecord(
                    world_point = Point(0f, 0f, 0f),
                    normal = Normal(0f, 1f, 0f),
                    surface_point = plane_translated._plane_point_to_uv(Point(0f, 0f, 0f)),
                    t = 1f,
                    ray = ray5,
                    material = plane_translated.material
                )
            ) ?: true
        )

        val ray6 : Ray = Ray(
            origin = Point(0f, 1f, 0f ),
            dir = Vec(0f, 1f, 0f )

        )

        assertFalse(plane_rotated.ray_intersection(ray6) != null)

        val ray7 : Ray = Ray(
            origin = Point(0f, 1f, 0f ),
            dir = Vec(1f, 0f, 0f )

        )
        assertFalse(plane_rotated.ray_intersection(ray7) != null)

        val ray8 : Ray = Ray(
            origin = Point(0f, 1f, 0f ),
            dir = Vec(0f, 0f, 1f )
        )
        assertFalse(plane_rotated.ray_intersection(ray8) != null)


    }

    @Test
    fun test_UV_Coordinates(){
        val plane:Plane = Plane()

        val ray1 = Ray(origin = Point(0f, 0f, 1f), dir = Vec(0f,0f, -1f))
        val intersection1 = plane.ray_intersection((ray1))
        val surfacePoint = intersection1?.surface_point
        assertTrue(surfacePoint?.is_close(Vec2D(0f, 0f)) ?: true)

        val ray2 = Ray(origin = Point(0.25f, 0.75f, 1f), dir = Vec(0f,0f, -1f))
        val intersection2 = plane.ray_intersection((ray2))
        val surfacePoint2 = intersection2?.surface_point
        assertTrue(surfacePoint2?.is_close(Vec2D(0.25f, 0.75f)) ?: true)

        val ray3 = Ray(origin = Point(4.25f, 7.75f, 1f), dir = Vec(0f,0f, -1f))
        val intersection3 = plane.ray_intersection((ray3))
        val surfacePoint3 = intersection3?.surface_point
        assertTrue(surfacePoint3?.is_close(Vec2D(0.25f, 0.75f)) ?: true)

    }


}