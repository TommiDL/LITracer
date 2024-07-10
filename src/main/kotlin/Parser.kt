
import org.example.*

/**
 * The Parser class check the correct behavior of the grammar in the scene definition file
 */
class Parser {


    /**
     * Read a token from `stream` and check that it matches `symbol`
     */
    fun expect_symbol(stream: InputStream, symbol: String) {
        val token: Token? = stream.read_token()
        if (token !is Token.SymbolToken)
        {
            throw GrammarError(token!!.location, "got $token instead of $symbol")
        }
        if ((token as Token.SymbolToken).symbol != symbol)
        {
            throw GrammarError(token.location, "got $token instead of $symbol")
        }
    }

    /**
     * Read a token from `stream` and check that it is either a literal number or a variable.
     */
    //default scene = empty
    fun expect_number(stream: InputStream, scene: Scene): Float {
        //read token
        val token: Token? = stream.read_token()

        //check if number
        if (token is Token.LiteralNumberToken) {
            return token.value
        }

        //check if is an existing var
        else if (token is Token.IdentifierToken) {
            val variable_name = token.identifier

            if (variable_name !in scene.float_variables) {
                throw GrammarError(token.location, "Unknown variable $token")
            }

            return scene.float_variables[variable_name]!!
        }

        //not number or var
        throw GrammarError(token!!.location, "got $token instead of a number")
    }


    fun expect_keywords(stream: InputStream, keywords: List<KeywordEnum>): KeywordEnum
    {
        val token:Token?=stream.read_token()

        if (token is Token.KeywordToken)
        {
            if (token.keyword !in keywords)
                throw GrammarError(token.location, "got ${token.keyword} instead of expected keywords: (${keywords.forEach{it.toString()+", "} })")

            else return token.keyword
        }

        throw GrammarError(token!!.location, "got $token instead of expected keywords: (${keywords.forEach{it.toString()+", "} })")

    }

    fun expect_string(stream: InputStream): String
    {
        val token:Token?= stream.read_token()

        if(token !is Token.StringToken)
        {
            throw GrammarError(token!!.location, "got $token instead of String")
        }

        return token.string
    }

    fun expect_identifier(stream: InputStream): String
    {
        val token:Token?=stream.read_token()
        if (token !is Token.IdentifierToken)
            throw GrammarError(token!!.location, "got $token instead of identifier")

        return token.identifier
    }


    fun parse_color(stream: InputStream, scene: Scene): Color {
        expect_symbol(stream, "<")
        val red: Float = expect_number(stream, scene)
        expect_symbol(stream, ",")
        val green: Float = expect_number(stream, scene)
        expect_symbol(stream, ",")
        val blue: Float = expect_number(stream, scene)
        expect_symbol(stream, ">")


        return Color(r = red, g = green, b = blue)
    }

    fun parse_vector(stream: InputStream, scene: Scene): Vec
    {
        expect_symbol(stream, "[")
        val x:Float=expect_number(stream, scene)
        expect_symbol(stream, ",")
        val y:Float=expect_number(stream, scene)
        expect_symbol(stream, ",")
        val z:Float=expect_number(stream, scene)
        expect_symbol(stream, "]")

        return Vec(x=x, y=y, z=z)
    }
/*
    fun parse_pigment(stream: InputStream, scene: Scene): Pigment
    {
    }

    fun parse_brdf(stream: InputStream, scene: Scene): BRDF
    {
    }

    fun parse_material(stream: InputStream, scene: Scene): Map<String, Material>
    {
    }

    fun parse_transformation(stream: InputStream, scene: Scene)
    {
    }

    fun parse_sphere(stream: InputStream, scene: Scene): Sphere
    {
    }

    fun parse_plane(stream: InputStream, scene: Scene) : Plane
    {
    }

    fun parse_camera(stream: InputStream, scene:Scene) : Camera
    {
    }

    fun parse_scene(s: InputStream) : Scene
    {
    }
*/
}