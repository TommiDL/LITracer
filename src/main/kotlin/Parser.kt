
import org.example.*
import java.io.FileInputStream
import java.io.FileNotFoundException

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
    //change to unit type (float int double)
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
            if (token.keyword !in keywords) {
                var list_str: String = ""
                keywords.forEach { list_str += it.toString() + ", " }

                throw GrammarError(token.location, "got ${token.keyword} instead of expected keywords: (${list_str})")
            }
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

    fun parse_pigment(stream: InputStream, scene: Scene): Pigment
    {
        val keyword:KeywordEnum=expect_keywords(stream, listOf(KeywordEnum.UNIFORM, KeywordEnum.CHECKERED, KeywordEnum.IMAGE))

        when(keyword){
            KeywordEnum.UNIFORM-> {
                expect_symbol(stream, "(")
                val col:Color=parse_color(stream, scene)
                expect_symbol(stream, ")")

                return UniformPigment(col)
            }//
            KeywordEnum.CHECKERED-> {
                expect_symbol(stream, "(")
                val col1:Color=parse_color(stream, scene)
                expect_symbol(stream, ",")
                val col2:Color=parse_color(stream, scene)
                expect_symbol(stream, ",")
                val num:Float=expect_number(stream, scene)
                expect_symbol(stream, ")")

                return CheckeredPigment(col1, col2, num.toInt())
            }//

            KeywordEnum.IMAGE-> {
                expect_symbol(stream, "(")
                val name:String=expect_string(stream)
                expect_symbol(stream, ")")

                val image:HdrImage
                try {
                    image=read_pfm_image(FileInputStream(name))

                }catch (e:FileNotFoundException)
                {
                    throw e
                }
                return ImagePigment(image)
            }//

            else->throw GrammarError(stream.location, "Expected  Pigment's Keyword Token but $keyword Token gived instead")

        }

    }

    fun parse_brdf(stream: InputStream, scene: Scene): BRDF
    {
        try {
            val keyword:KeywordEnum=expect_keywords(stream, listOf(KeywordEnum.DIFFUSE, KeywordEnum.SPECULAR))

            when(keyword)
            {
                KeywordEnum.DIFFUSE->{
                    expect_symbol(stream, "(")
                    val pigment:Pigment=parse_pigment(stream, scene)
                    expect_symbol(stream, ")")

                    return DiffusiveBRDF(pigment)
                }
                KeywordEnum.SPECULAR->{
                    expect_symbol(stream, "(")
                    val pigment:Pigment=parse_pigment(stream, scene)
                    expect_symbol(stream, ")")

                    return SpecularBRDF(pigment)
                }
                else->{
                    throw throw GrammarError(stream.location, "Expected  BRDF's Keyword Token but $keyword Token gived instead")
                }
            }
        }catch (e:FileNotFoundException)
        {
            throw e
        }

    }

    fun parse_material(stream: InputStream, scene: Scene): Pair<String, Material>
    {
        try {
            val keyword:KeywordEnum=expect_keywords(stream, listOf(KeywordEnum.MATERIAL))

            val identifier:String=expect_identifier(stream)

            expect_symbol(stream, "(")
            val brdf:BRDF=parse_brdf(stream, scene)
            expect_symbol(stream, ",")
            val emitted_rad=parse_pigment(stream, scene)
            expect_symbol(stream, ")")

            return Pair<String, Material>(identifier, Material(brdf=brdf, emitted_radiance = emitted_rad))

        }catch (e: FileNotFoundException)
        {
            throw e
        }
    }

    fun parse_transformation(stream: InputStream, scene: Scene): Transformation
    {
        var res:Transformation=Transformation()

        while(true)
        {
            val transformation_kw:KeywordEnum=expect_keywords(
                stream,
                listOf(
                    KeywordEnum.IDENTITY,
                    KeywordEnum.TRANSLATION,
                    KeywordEnum.ROTATION_X,
                    KeywordEnum.ROTATION_Y,
                    KeywordEnum.ROTATION_Z,
                    KeywordEnum.SCALING
                )
            )


            if(transformation_kw == KeywordEnum.IDENTITY)
            {
                //do nothing
            }
            else if(transformation_kw == KeywordEnum.TRANSLATION )
            {
                expect_symbol(stream, "(")
                res*= translation(parse_vector(stream, scene))
                expect_symbol(stream, ")")
            }
            else if(transformation_kw == KeywordEnum.ROTATION_X )
            {
                expect_symbol(stream, "(")
                res*= rotation(u=Vec(x=1f, y=0f, z=0f), theta = expect_number(stream, scene))
                expect_symbol(stream, ")")

            }
            else if(transformation_kw == KeywordEnum.ROTATION_Y )
            {
                expect_symbol(stream, "(")
                res*= rotation(u=Vec(x=0f, y=1f, z=0f), theta = expect_number(stream, scene))
                expect_symbol(stream, ")")
            }
            else if(transformation_kw == KeywordEnum.ROTATION_Z )
            {
                expect_symbol(stream, "(")
                res*= rotation(u=Vec(x=0f, y=0f, z=1f), theta = expect_number(stream, scene))
                expect_symbol(stream, ")")
            }
            else if(transformation_kw == KeywordEnum.SCALING )
            {
                expect_symbol(stream, "(")
                res*= scalar_transformation(parse_vector(stream, scene))
                expect_symbol(stream, ")")
            }

            // We must peek the next token to check if there is another transformation that is being
            // chained or if the sequence ends. Thus, this is a LL(1) parser.

            val next_kw=stream.read_token()
            if(next_kw !is Token.SymbolToken)
            {
                stream.unread_token(next_kw!!)
                break
            }
            else if(next_kw.symbol!="*")
            {
                stream.unread_token(next_kw)
                break
            }

        }
        return res
    }

    fun parse_sphere(stream: InputStream, scene: Scene): Sphere
    {
        val keywords:KeywordEnum=expect_keywords(stream, listOf(KeywordEnum.SPHERE))

        expect_symbol(stream, "(")
        val material_name:String=expect_identifier(stream)

        if(material_name !in scene.materials.keys){
            throw GrammarError(stream.location, "got unknown material with name $material_name")
        }

        expect_symbol(stream, ",")
        val transformation:Transformation=parse_transformation(stream, scene)
        expect_symbol(stream, ")")

        return Sphere(transformation=transformation, material = scene.materials[material_name]!!)


    }

    fun parse_plane(stream: InputStream, scene: Scene) : Plane
    {
        val keywords:KeywordEnum=expect_keywords(stream, listOf(KeywordEnum.PLANE))

        expect_symbol(stream, "(")
        val material_name:String=expect_identifier(stream)

        if(material_name !in scene.materials.keys){
            throw GrammarError(stream.location, "got unknown material with name $material_name")
        }

        expect_symbol(stream, ",")
        val transformation:Transformation=parse_transformation(stream, scene)
        expect_symbol(stream, ")")

        return Plane(transformation=transformation, material = scene.materials[material_name]!!)

    }

    fun parse_camera(stream: InputStream, scene:Scene) : Camera
    {

        val keywords:KeywordEnum=expect_keywords(stream, listOf(KeywordEnum.CAMERA))

        expect_symbol(stream, "(")
        val camera_type:KeywordEnum=expect_keywords(stream, listOf(KeywordEnum.PERSPECTIVE, KeywordEnum.ORTHOGONAL))


        expect_symbol(stream, ",")
        val transformation:Transformation=parse_transformation(stream, scene)
        expect_symbol(stream, ",")


        when(camera_type)
        {
            KeywordEnum.ORTHOGONAL->{
                val aspect_ratio:Float=expect_number(stream, scene)
                expect_symbol(stream, ")")
                return OrthogonalCamera(transformation = transformation, aspect_ratio = aspect_ratio)
            }
            KeywordEnum.PERSPECTIVE->{
                val aspect_ratio:Float=expect_number(stream, scene)
                expect_symbol(stream, ",")
                val dist:Float=expect_number(stream, scene)
                expect_symbol(stream, ")")
                return PerspectiveCamera(transformation = transformation, aspect_ratio = aspect_ratio, distance = dist)
            }
            else-> throw GrammarError(stream.location, "got $camera_type instead of expected Camera type keyword")
        }
    }

    /**
     * Read a scene description from a stream and return a :class:`.Scene` object
     */
    fun parse_scene(stream: InputStream, variables:Map<String, Float> = mapOf<String, Float>() ) : Scene
    {
        val scene:Scene=Scene(float_variables = variables.toMutableMap(), overridden_variables = variables.keys.toSet())

        while(true)
        {
            val what:Token?=stream.read_token()

            if(what is Token.StopToken)
            {
                break
            }

            if(what !is Token.KeywordToken)
            {
                throw GrammarError(what!!.location, "Expected a Keyword instead of $what")
            }

            if(what.keyword==KeywordEnum.FLOAT)
            {
                val variable_name:String=expect_identifier(stream)

                // saved for the error message
                val variable_loc:SourceLocation=stream.location

                expect_symbol(stream, "(")
                val variable_value:Float=expect_number(stream, scene)
                expect_symbol(stream, ")")

                if((variable_name in scene.float_variables) and (variable_name !in scene.overridden_variables))
                {
                    throw GrammarError(variable_loc, "variable $variable_name cannot be redefined")
                }

                if(variable_name !in scene.overridden_variables) {
                    scene.float_variables[variable_name] = variable_value
                }

            }

            else if(what.keyword == KeywordEnum.SPHERE)
            {
                stream.unread_token(what)
                scene.world.add(parse_sphere(stream, scene))
            }

            else if(what.keyword == KeywordEnum.PLANE)
            {
                stream.unread_token(what)
                scene.world.add(parse_plane(stream, scene))
            }

            else if(what.keyword == KeywordEnum.CAMERA)
            {
                stream.unread_token(what)
                if (scene.camera!=null)
                {
                    throw GrammarError(what.location, "You cannot define more than one camera")
                }
                scene.camera=parse_camera(stream, scene)
            }

            else if (what.keyword==KeywordEnum.MATERIAL)
            {
                try {
                    stream.unread_token(what)
                    val mat:Pair<String, Material> = parse_material(stream, scene)
                    scene.materials[mat.first]=mat.second

                }catch (e:FileNotFoundException)
                {
                    throw e
                }
            }

        }
        return scene

    }

}