
import org.example.*
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import java.io.FileOutputStream
import kotlin.test.Test

class TriangleTest {

    @Test
    fun test_get_point() {

        val beta:Float = 0.5f
        val gamma:Float = 0.5f
        val triangle:Triangle = Triangle()
        val point:Point =  Point(0f, 0.5f, 0.5f)
        val point1:Point =  Point(1f, 1f, 1f)

        assertTrue(triangle.get_point(beta, gamma)!!.is_close(point))
        assertFalse(triangle.get_point(beta, gamma)!!.is_close(point1))

        assertTrue(triangle(beta, gamma).is_close(point))
        assertFalse(triangle(beta, gamma).is_close(point1))


    }

    @Test
    fun testDet() {

        val a:Triangle = Triangle()
        val b:Triangle = Triangle()

        assertTrue(are_similar(a.det(1f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 1f), 1f))
        assertFalse(are_similar(b.det(1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f), 1f))
        assertTrue(are_similar(b.det(1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f), 0f))

    }

    @Test
    fun testRayIntersection(){



        val tr_x:Triangle = Triangle(
            Point(x=1f, y= -1f, z= -1f),
            Point(x=1f, y=  1f, z= -1f),
            Point(x=1f, y=  0f, z= 1f )
        )
        val ray_x=Ray(
            origin = Point(0f,0f,0f),
            dir = Vec(x=1f)
        )

        assertTrue(
            tr_x.ray_intersection(ray_x)!!.is_close(
                HitRecord(
                    world_point = Point(1f, 0f, 0f),
                    normal = ((tr_x.B-tr_x.A).prod(tr_x.C-tr_x.A)*(-1f)).conversion(),
                    surface_point = Vec2D(u=0.25f, 0.5f),
                    t= 1f,
                    ray=ray_x,
                    material = tr_x.material
                )
            )
        )

        val ray_x2:Ray=Ray(
            origin = Point(0f, y=0.51f, 0.51f),
            dir = Vec(x=1f)
        )

        assertTrue(
            tr_x.ray_intersection(ray_x2)==null
        )


        val tr_y:Triangle = Triangle(
            Point(x= -1f,y= 1f, z=-1f),
            Point(x= 1f, y= 1f, z=-1f),
            Point(x= 0f, y= 1f, z=1f )
        )

        val ray_y=Ray(
            origin = Point(0f,0f,0f),
            dir = Vec(y=1f)
        )

        assertTrue(
            tr_y.ray_intersection(ray_y)!!.is_close(
                HitRecord(
                    world_point = Point(0f, 1f, 0f),
                    normal = ((tr_y.B-tr_y.A).prod(tr_y.C-tr_y.A)*(-1f)).conversion(),
                    surface_point = Vec2D(u=0.25f, 0.5f),
                    t= 1f,
                    ray=ray_y,
                    material = tr_y.material
                )
            )
        )

        val ray_y2:Ray=Ray(
            origin = Point(0.51f, y=0f, 0.51f),
            dir = Vec(y=1f)
        )

        assertTrue(
            tr_y.ray_intersection(ray_y2)==null
        )



        val tr_z:Triangle = Triangle(
            Point(x=-1f, y=-1f, z=1f),
            Point(x= 1f, y=-1f, z=1f),
            Point(x= 0f, y=1f , z=1f)
        )

        val ray_z=Ray(
            origin = Point(0f,0f,0f),
            dir = Vec(z=1f)
        )

        assertTrue(
            tr_z.ray_intersection(ray_z)!!.is_close(
                HitRecord(
                    world_point = Point(0f, 0f, 1f),
                    normal = ((tr_z.B-tr_z.A).prod(tr_z.C-tr_z.A)*(-1f)).conversion(),
                    surface_point = Vec2D(u=0.25f, 0.5f),
                    t= 1f,
                    ray=ray_z,
                    material = tr_z.material
                )
            )
        )

        val ray_z2:Ray=Ray(
            origin = Point(0.51f, y=0.51f, 0f),
            dir = Vec(z=1f)
        )

        assertTrue(
            tr_z.ray_intersection(ray_z2)==null
        )




    }

    @Test
    fun printTriangle()
    {
        val world:World=World(
            mutableListOf<Shape>(
                Triangle(
                    Point(0f,0f,0f),
                    Point(0f, 1f,0f),
                    Point(0f, 0f, 1f)
                )
            )
        )

            val camera:Camera=OrthogonalCamera(
                //transformation = rotation(Vec(z=1f), theta = PI.toFloat()/4)
            )
            /*val camera:Camera=PerspectiveCamera(
                aspect_ratio = 0.5f,
                distance = 50f,
                //transformation = rotation(u=Vec(x =1f), theta = PI.toFloat()/4)
            )*/

            val img:HdrImage=HdrImage(width = 960, height=960)

            val tracer:ImageTracer=ImageTracer(camera= camera, image = img)

            val WHITE:Color=Color(255f, 255f, 255f)
    //            Color(r= Random.nextFloat(), g= Random.nextFloat(), b= Random.nextFloat())
            val BLACK:Color=Color()

            tracer.fire_all_ray(func={ ray: Ray ->
                val hit=world.ray_intersection(ray)!=null

                if (hit) WHITE
                else BLACK
            })

            try {
                val filename="triangle.png"
                //save image in PNG file
                img.normalize_image(1f)
                img.clamp_image()
                val out_stream: FileOutputStream = FileOutputStream(filename)
                img.write_ldr_image(stream = out_stream, format = "PNG")
                println("Image saved in PNG format at PATH: ${filename}")

            }catch (e:Error)
            {
                println("Impossible to write on file triangle.png")
                println("Error: $e")

                return
            }

    }

}