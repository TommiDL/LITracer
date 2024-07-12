
import org.example.Camera
import org.example.Material
import org.example.World

class Scene (
    val materials: MutableMap<String, Material> = mutableMapOf<String, Material>(),
    val world: World=World(),
    var camera: Camera?=null,
    val float_variables:MutableMap<String, Float> = mutableMapOf<String, Float>(),
    val overridden_variables:Set<String> = setOf<String>()
)
{

}