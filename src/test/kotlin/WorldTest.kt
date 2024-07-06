
import org.example.*
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse


class WorldTest {
    val vec_x = Vec(1f,0f,0f) //versore_x (1,0,0)
    @Test
    fun test_ray_intersection() {
        val world = World()
        val sphere1: Sphere = Sphere(transformation = translation(vec_x * 2))
        val sphere2: Sphere = Sphere(transformation = translation(vec_x * 8))
        world.add(sphere1)
        world.add(sphere2)

        val intersection1 = world.ray_intersection(ray = Ray( Point(0f,0f,0f), dir = vec_x))
        assertTrue(intersection1 != null)

        assertTrue(intersection1!!.world_point.is_close(Point(1f,0f,0f)))

        val intersection2 = world.ray_intersection(ray = Ray(Point(0f,0f,0f), dir = vec_x.neg()))
        assertFalse(intersection2 != null)
    }


}