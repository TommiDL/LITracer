import org.example.GrammarError
import org.example.KEYWORDS
import org.example.KeywordEnum
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.example.Token
import java.io.ByteArrayInputStream
import org.example.InputStream as InputStream
import java.io.InputStream as InputStream1

class InputStreamTest {

    @Test
    fun testInputFile() {
        val myString: String = "abc   \nd\nef"
        val stream: InputStream = InputStream(myString.byteInputStream())

        assertTrue(stream.location.line_num == 1)
        assertTrue(stream.location.col_num == 1)

        assertTrue(stream.read_char() == 'a')
        assertTrue(stream.location.line_num == 1)
        assertTrue(stream.location.col_num == 2)

        stream.unread_char('A')
        assertTrue(stream.location.line_num == 1)
        assertTrue(stream.location.col_num == 1)

        assertTrue(stream.read_char() == 'A')
        assertTrue(stream.location.line_num == 1)
        assertTrue(stream.location.col_num == 2)

        assertTrue(stream.read_char() == 'b')
        assertTrue(stream.location.line_num == 1)
        assertTrue(stream.location.col_num == 3)

        assertTrue(stream.read_char() == 'c')
        assertTrue(stream.location.line_num == 1)
        assertTrue(stream.location.col_num == 4)

        stream.skip_whitespaces_and_comments()

        assertTrue(stream.read_char() == 'd')
        assertTrue(stream.location.line_num == 2)
        assertTrue(stream.location.col_num == 2)

        assertTrue(stream.read_char() == '\n')
        assertTrue(stream.location.line_num == 3)
        assertTrue(stream.location.col_num == 1)

        assertTrue(stream.read_char() == 'e')
        assertTrue(stream.location.line_num == 3)
        assertTrue(stream.location.col_num == 2)

        assertTrue(stream.read_char() == 'f')
        assertTrue(stream.location.line_num == 3)
        assertTrue(stream.location.col_num == 3)

        assertTrue(stream.read_char() == '\u0000')
    }

    @Test
    fun test_lexer() {
        val myString: String = """
       # This is a comment
       # This is another comment
        new material sky_material(
            diffuse(image("my file.pfm")),
            <5.0, 500.0, 300.0>
        ) # Comment at the end of the line
    """
        val stream: InputStream = InputStream(myString.byteInputStream())

        stream.read_token()?.let { _assert_is_keyword(it, KeywordEnum.NEW) }
        stream.read_token()?.let { _assert_is_keyword(it, KeywordEnum.MATERIAL) }
        stream.read_token()?.let { _assert_is_identifier(it, "sky_material") }
        stream.read_token()?.let { _assert_is_symbol(it, "(") }
        stream.read_token()?.let { _assert_is_keyword(it, KeywordEnum.DIFFUSE) }
        stream.read_token()?.let { _assert_is_symbol(it, "(") }
        stream.read_token()?.let { _assert_is_keyword(it, KeywordEnum.IMAGE) }
        stream.read_token()?.let { _assert_is_symbol(it, "(") }
        stream.read_token()?.let { _assert_is_string(it, "my file.pfm") }
        stream.read_token()?.let { _assert_is_symbol(it, ")") }
    }

    private fun _assert_is_string(token: Token, string: String) {
        if(token !is Token.StringToken) {
            throw AssertionError("Token '$token' is not a StringToken")
        }
        if(token.string != string){
            throw AssertionError("expecting symbol '$string' instead of '$token'")
        }
    }

    private fun _assert_is_symbol(token: Token, symbol: String) {
        if(token !is Token.SymbolToken) {
            throw AssertionError("Token '$token' is not a SymbolToken")
        }
        if(token.symbol != symbol){        
            throw AssertionError("expecting symbol '$symbol' instead of '$token'")
        }
    }

    private fun _assert_is_identifier(token: Token, identifier: String) {
        if(token !is Token.IdentifierToken){
            throw AssertionError("Token '$token' is not an IdentifierToken")
        }
        if(token.identifier != identifier) {
            throw AssertionError("expecting identifier '$identifier' instead of '$token'")
        }
    }

    private fun _assert_is_keyword(token: Token, keyword: KeywordEnum) {
        if (token !is Token.KeywordToken) {
            throw AssertionError("Token '$token' is not a KeywordToken")
        }
        if (token.keyword != keyword) {
            throw AssertionError("Token '${token.keyword}' is not equal to keyword '$keyword'")
        }
    }
}