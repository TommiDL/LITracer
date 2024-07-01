import org.example.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class PigmentTest {
    @Test
    fun UniformPigmentTest() {
        val color : Color = Color(1f , 2f , 3f)
        val pigment : Pigment = UniformPigment(color = color)

        assert(pigment.get_color(Vec2D(0f, 0f)).is_close(color))
        assert(pigment.get_color(Vec2D(1f, 0f)).is_close(color))
        assert(pigment.get_color(Vec2D(0f, 1f)).is_close(color))
        assert(pigment.get_color(Vec2D(1f, 1f)).is_close(color))
    }
    @Test
    fun CheckeredPigmentTest() {
        val color1 : Color = Color(1f, 2f, 3f)
        val color2 : Color = Color(10f, 20f, 30f)
        val pigment : Pigment = CheckeredPigment(color1 = color1, color2 = color2, n_steps = 2)

        assert(pigment.get_color(Vec2D(0.25f, 0.25f)).is_close(color1))
        assert(pigment.get_color(Vec2D(0.75f, 0.25f)).is_close(color2))
        assert(pigment.get_color(Vec2D(0.25f, 0.75f)).is_close(color2))
        assert(pigment.get_color(Vec2D(0.75f, 0.75f)).is_close(color1))
    }
    @Test
    fun ImagePigmentTest(){
        val image = HdrImage(width=2, height=2)
        image.set_pixel(0, 0, Color(1f, 2f, 3f))
        image.set_pixel(1, 0, Color(2f, 3f, 1f))
        image.set_pixel(0, 1, Color(2f, 1f, 3f))
        image.set_pixel(1, 1, Color(3f, 2f, 1f))

        val pigment = ImagePigment(image)

        assert(pigment.get_color(Vec2D(0f, 0f)) == (Color(1f, 2f, 3f)))
        assert(pigment.get_color(Vec2D(1f, 0f)) == (Color(2f, 3f, 1f)))
        assert(pigment.get_color(Vec2D(0f, 1f)) == (Color(2f, 1f, 3f)))
        assert(pigment.get_color(Vec2D(1f, 1f)) == (Color(3f, 2f, 1f)))


    }

}