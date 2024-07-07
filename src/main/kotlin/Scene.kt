
import org.example.Camera
import org.example.Material
import org.example.World

class Scene (
    val materials: Map<String, Material> = mapOf<String, Material>(),
    val world: World=World(),
    val camera: Camera?=null,
    val float_variables:Map<String, Float> = mapOf<String, Float>(),
    val overridden_variables:Set<String> = setOf<String>()
)
{

}