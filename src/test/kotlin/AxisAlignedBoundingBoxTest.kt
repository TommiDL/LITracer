import org.example.*
import java.io.FileInputStream
import kotlin.test.Test
import kotlin.test.assertTrue

class AxisAlignedBoundingBoxTest
{
    val mesh:Mesh=Mesh(FileInputStream("mesh_obj_files/tetrahedron.obj"))
    @Test
    fun constructorTest()
    {

        mesh.vertexes.forEach {
            println(it)
        }

        val box: AxisAlignedBoundingBox =AxisAlignedBoundingBox(this.mesh)

        assertTrue(box.xmin==-0.25f)
        assertTrue(box.xmax==0.75f)

        assertTrue(box.ymin==-0.25f)
        assertTrue(box.ymax==0.75f)

        assertTrue(box.zmin==-0.25f)
        assertTrue(box.zmax==0.75f)



    }

    @Test
    fun ray_intersectionTest()
    {
        val box:AxisAlignedBoundingBox=AxisAlignedBoundingBox(this.mesh)

        val ray1:Ray= Ray(
            origin = Point(0f, 0f, 0f),
            dir = Vec(x=1.5f, y=1.5f, 1.5f),
        )

        assertTrue(box.ray_intersection(ray1))

        val ray2:Ray= Ray(
            origin = Point(0f, 1.5f, 1.5f),
            dir = Vec(x=1f),
        )
        val ray3:Ray= Ray(
            origin = Point(1.5f, 0f, 1.5f),
            dir = Vec(y=1f),
        )
        val ray4:Ray= Ray(
            origin = Point(1.5f, 1.5f, 0f),
            dir = Vec(z=1f),
        )


        assertTrue(box.ray_intersection(ray2))
        assertTrue(box.ray_intersection(ray3))
        assertTrue(box.ray_intersection(ray4))


    }
}