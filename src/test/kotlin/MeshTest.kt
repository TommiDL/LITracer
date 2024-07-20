
import org.example.*
import org.junit.jupiter.api.Test
import java.io.FileInputStream
import java.io.FileOutputStream
import kotlin.math.PI
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MeshTest
{
    @Test
    fun constructorTest()
    {
        try {
            val mesh:Mesh = Mesh(stream = FileInputStream("mesh_obj_files/tetrahedron.obj"))

            assertTrue(mesh.triangles.size==4)
            assertTrue(mesh.vertexes.size==4)

            assertTrue(mesh.vertexes[0].is_close(Point(1f,1f,1f)))
            assertTrue(mesh.vertexes[1].is_close(Point(2f,1f,1f)))
            assertTrue(mesh.vertexes[2].is_close(Point(1f,2f,1f)))
            assertTrue(mesh.vertexes[3].is_close(Point(1f,1f,2f)))

            assertEquals(mesh.triangles[0], indexes(1,3,2))
            assertEquals(mesh.triangles[1], indexes(1,4,3))
            assertEquals(mesh.triangles[2], indexes(1,2,4))
            assertEquals(mesh.triangles[3], indexes(2,3,4))

        } catch (e:Error)
        {
            println(e)
        }



    }

    @Test
    fun getTriangleTest()
    {

    }

    @Test
    fun rayintersectionTest()
    {
        try {

/*            val triangle:Triangle=Triangle(
                A=Point(0f, 0f, 0f),
                B=Point(0f,0.5f, 1f),
                C=Point(0f,0f,1f),
            )
*/
            val mesh1:Mesh=Mesh(
                stream = FileInputStream("mesh_obj_files/tetrahedron.obj"),
                        //transformation = scalar_transformation(2f,2f,2f) *
                        translation(Vec(-1.25f,-1.25f, -1.25f))
            )

            println(mesh1.get_center())

            val mesh2:Mesh=Mesh(
                stream = FileInputStream("mesh_obj_files/humanoid_tri.obj"),
                transformation =
                    translation(Vec(z=-1f))*
                    rotation(Vec(z=1f), theta = PI.toFloat()/4) *
                        scalar_transformation(sx=0.07f,sy=0.07f, sz=0.07f)

            )

            println(mesh2.get_center())

            val world:World=World(
                mutableListOf<Shape>(
                    //mesh1,
                    mesh2,
                    //triangle
                )
            )

            val camera:Camera=OrthogonalCamera(
                transformation = rotation(Vec(z=1f), theta = 7*PI.toFloat()/8)


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
                val filename="meshtest.png"
                //save image in PNG file
                img.normalize_image(1f)
                img.clamp_image()
                val out_stream: FileOutputStream = FileOutputStream(filename)
                img.write_ldr_image(stream = out_stream, format = "PNG")
                println("Image saved in PNG format at PATH: ${filename}")

            }catch (e:Error)
            {
                println("Impossible to write on file meshtest.png")
                println("Error: $e")

                return
            }

        } catch (e:Error)
        {
            println(e)
        }
    }




}