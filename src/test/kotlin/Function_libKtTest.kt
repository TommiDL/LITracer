

import org.example.PCG
import org.example.Vec
import org.example.are_similar
import org.example.create_onb_from_z
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class Function_libKtTest {

    @Test
    fun test_are_similar() {
        assert(are_similar(1.99999999999999f, 2f))
    }


    @Test
    fun test_onb_from_z()
    {
        val pcg: PCG = PCG()

        for(i in 0 until  1000000)
        {
            val normal=Vec(
                x=pcg.random_float(),
                y=pcg.random_float(),
                z=pcg.random_float()
            ).normalize()

            val e1:Vec= create_onb_from_z(normal)[0]
            val e2:Vec= create_onb_from_z(normal)[1]
            val e3:Vec= create_onb_from_z(normal)[2]

            assertTrue(e3.is_close(normal))

            assertTrue(are_similar(0f, e1*e2))
            assertTrue(are_similar(0f, e2*e3))
            assertTrue(are_similar(0f, e3*e1))

            assertTrue(are_similar(1f, e1.squared_norm()))
            assertTrue(are_similar(1f, e2.squared_norm()))
            assertTrue(are_similar(1f, e3.squared_norm()))

            assertTrue(e1.prod(e2).is_close(e3, eps = 1e-5f) )
            assertTrue(e2.prod(e3).is_close(e1, eps = 1e-5f) )
            assertTrue(e3.prod(e1).is_close(e2, eps = 1e-5f) )
        }

    }

}

