import org.example.*
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class CubeTest
{
    @Test
    fun test_normal(){
        val cube:Cube=Cube()

        assertTrue(Normal(x=-1f).is_close(cube._cube_normal(Point(0f, 0.5f,0.5f), Vec(x=1f, 0f,0f))))

    }

    @Test
    fun test_coordinate_uv()
    {
        val cube:Cube=Cube()

        println(cube.cube_point_to_uv(Point(0f,0.5f, 0.5f)))
    }


    @Test
    fun test_fire_ray()
    {
        val cube:Cube=Cube()


        // parallel x ray
        var ray:Ray=Ray(
            origin = Point(-2f, 0f, 0f),
            dir = Vec(x=1f, 0f, 0f),
        )

        println(cube.ray_intersection(ray)?.world_point)
        println(cube.ray_intersection(ray)?.normal)
        println(cube.ray_intersection(ray)?.t)


        assertTrue(cube.ray_intersection(ray)!!.is_close(
            HitRecord(
                world_point = Point(-1f,0f,0f),
                normal = Normal(x=-1f, 0f, 0f),
                surface_point = cube.cube_point_to_uv(Point(-1f,0f, 0f)),
                material = Material(),
                ray = ray,
                t=1f
            )
        ))


        ray=Ray(
            origin = Point(-2f,0f,-0.5f),
            dir = Vec(x=1f, z=0.5f, y=0f)
        )
        println(cube.ray_intersection(ray)?.world_point)

        assertTrue(cube.ray_intersection(ray)!!.is_close(
            HitRecord(
                world_point = Point(-1f,0f,0f),
                normal = Normal(x=-1f, 0f, 0f),
                surface_point = cube.cube_point_to_uv(Point(-1f,0f, 0f)),
                material = Material(),
                ray = ray,
                t=1f
            )
        ))


        ray=Ray(
            origin = Point(-2f),
            dir = Vec(x=1f, z=2f)
        )
        assertTrue(cube.ray_intersection(ray)==null)
        ray=Ray(
            origin = Point(-2f),
            dir = Vec(x=1f, z=-2f)
        )
        assertTrue(cube.ray_intersection(ray)==null)
        ray=Ray(
            origin = Point(-2f),
            dir = Vec(x=1f, y=2f)
        )
        assertTrue(cube.ray_intersection(ray)==null)
        ray=Ray(
            origin = Point(-2f),
            dir = Vec(x=1f, y=-2f)
        )
        assertTrue(cube.ray_intersection(ray)==null)


    }
}