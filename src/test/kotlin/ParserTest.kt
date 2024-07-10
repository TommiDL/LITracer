
import org.example.GrammarError
import org.example.InputStream
import org.example.KeywordEnum
import org.example.are_similar
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class ParserTest
{
    val parse=Parser()
    @Test
    fun test_expect_symbol()
    {

        var str:String=""
        for(k in listOf('(', ',', ')', '{', '}', '[', ']', ';', ':', '!', '?', '*', '/'))
        {
            str+=k+"\n"
        }

        str+=">\n"
        str+="\"string\"\n"

        val line: ByteArrayInputStream = ByteArrayInputStream(str.toByteArray())


        val stream: InputStream=InputStream(line)

        for(k in listOf('(', ',', ')', '{', '}', '[', ']', ';', ':', '!', '?', '*', '/'))
            parse.expect_symbol(stream, k.toString())

        assertFailsWith<GrammarError> {
            parse.expect_symbol(stream, ">")
        }
        assertFailsWith<GrammarError> {
            parse.expect_symbol(stream, ">")
        }
    }


    @Test
    fun test_expect_number() {
        val line: ByteArrayInputStream = ByteArrayInputStream(
            "1\t\t\t 2.0  \n 3  m  ".toByteArray()
        )
        val stream = InputStream(line)

        assertTrue(are_similar(parse.expect_number(stream, Scene()), 1f))
        assertTrue(are_similar(parse.expect_number(stream, Scene()), 2f))
        assertTrue(are_similar(parse.expect_number(stream, Scene()), 3f))

        assertFailsWith<GrammarError> {
            parse.expect_number(stream, Scene())
        }
    }
    @Test
    fun test_expect_keywords()
    {
        val line: ByteArrayInputStream = ByteArrayInputStream(
            "new \t\t\timage    trial".toByteArray()
        )

        val stream=InputStream(line)

        assertTrue(parse.expect_keywords(stream, listOf(KeywordEnum.NEW, KeywordEnum.IMAGE))==KeywordEnum.NEW)
        assertTrue(parse.expect_keywords(stream, listOf(KeywordEnum.NEW, KeywordEnum.IMAGE))==KeywordEnum.IMAGE)


        assertFailsWith<GrammarError> {
            parse.expect_keywords(stream, listOf(KeywordEnum.NEW, KeywordEnum.IMAGE))
        }

    }
    @Test
    fun test_expect_string()
    {
        val line: ByteArrayInputStream = ByteArrayInputStream(
            "\"string\" >".toByteArray()
        )

        val stream=InputStream(line)

        assertTrue(
            parse.expect_string(stream)=="string"
        )

        assertFailsWith<GrammarError> {
            parse.expect_string(stream)
        }
    }


    @Test
    fun test_expect_identifier()
    {
        val line: ByteArrayInputStream = ByteArrayInputStream(
            "float \t\t\timage    trial".toByteArray()
        )

        val stream=InputStream(line)

        parse.expect_identifier(stream)
    }
}