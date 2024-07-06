import org.example.*
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import kotlin.math.PI

class RayTest {

    @Test
    fun test_is_close() {
        val ray1: Ray = Ray(origin = Point(1f, 2f, 3f), dir= Vec(5f, 4f, -1f))
        val ray2: Ray = Ray(origin = Point(1f, 2f, 3f), dir= Vec(5f, 4f, -1f))
        val ray3: Ray = Ray(origin = Point(5f, 1f, 4f), dir= Vec(3f, 9f, 4f))

        assertTrue(ray1.is_close(ray2))
        assertFalse(ray1.is_close(ray3))
    }

    @Test
    fun test_at() {
        val ray:Ray=Ray(origin = Point(1f, 2f, 4f), dir= Vec(4f,2f, 1f))

        assertTrue(ray.at(0f).is_close(ray.origin))
        assertTrue(ray.at(1f).is_close(Point(5f, 4f, 5f)))
        assertTrue(ray.at(2f).is_close(Point(9f, 6f, 6f)))

    }

    @Test
    fun test_transform() {
        val ray:Ray=Ray(
            origin = Point(1f,2f,3f),
            dir = Vec(6f,5f,4f),
        )


        val transf:Transformation = translation(Vec(10f, 11f, 12f)) * rotation(u = Vec(1f, 0f,0f), theta = (PI/2).toFloat())
        val transformed:Ray=ray.transform(transf)

        assertTrue(transformed.origin.is_close(Point(11f, 8f, 14f)))
        assertTrue(transformed.dir.is_close(Vec(6f, -4f, 5f), eps = 1e-4f))

        assertTrue(transformed.is_close(transf*ray))

    }
}