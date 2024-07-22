import org.example.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test


class TokenTest {

    // Definiamo una posizione di esempio per i test
    private val location = SourceLocation("example.txt", 1, 1)

    @Test
    fun testKeywordToken() {
        val token = Token.KeywordToken(location, KeywordEnum.NEW)
        assertIsKeyword(token, KeywordEnum.NEW)
    }

    @Test
    fun testIdentifierToken() {
        val token = Token.IdentifierToken(location, "identifier")
        assertIsIdentifier(token, "identifier")
    }

    @Test
    fun testSymbolToken() {
        val token = Token.SymbolToken(location, "symbol")
        assertIsSymbol(token, "symbol")
    }

    @Test
    fun testLiteralNumberToken() {
        val token = Token.LiteralNumberToken(location, 123.45f)
        assertIsNumber(token, 123.45f)
    }

    @Test
    fun testStringToken() {
        val token = Token.StringToken(location, "string")
        assertIsString(token, "string")
    }
    @Test

    fun testStopToken() {
        val token = Token.StopToken(location)
        assertIsStopToken(token)
    }

    // Funzioni di asserzione per ciascun tipo di token

    private fun assertIsKeyword(token: Token, keyword: KeywordEnum) {
        assertTrue(token is Token.KeywordToken, "Il token non è di tipo KeywordToken")
        if (token is Token.KeywordToken) {
            assertEquals(keyword, token.keyword, "Il token '$token' non è uguale alla parola chiave '$keyword'")
        }
    }

    private fun assertIsIdentifier(token: Token, identifier: String) {
        assertTrue(token is Token.IdentifierToken, "Il token non è di tipo IdentifierToken")
        if (token is Token.IdentifierToken) {
            assertEquals(identifier, token.identifier, "Ci si aspettava l'identificatore '$identifier' invece di '$token'")
        }
    }

    private fun assertIsSymbol(token: Token, symbol: String) {
        assertTrue(token is Token.SymbolToken, "Il token non è di tipo SymbolToken")
        if (token is Token.SymbolToken) {
            assertEquals(symbol, token.symbol, "Ci si aspettava il simbolo '$symbol' invece di '$token'")
        }
    }

    private fun assertIsNumber(token: Token, number: Float) {
        assertTrue(token is Token.LiteralNumberToken, "Il token non è di tipo LiteralNumberToken")
        if (token is Token.LiteralNumberToken) {
            assertEquals(number, token.value, "Il token '$token' non è uguale al numero '$number'")
        }
    }

    private fun assertIsString(token: Token, s: String) {
        assertTrue(token is Token.StringToken, "Il token non è di tipo StringToken")
        if (token is Token.StringToken) {
            assertEquals(s, token.string, "Il token '$token' non è uguale alla stringa '$s'")
        }
    }

    private fun assertIsStopToken(token: Token) {
        assertTrue(token is Token.StopToken, "Il token non è di tipo StopToken")
        if (token is Token.StopToken) {
            assertEquals("StopToken", token.toString(), "Il token non è correttamente implementato come StopToken")
        }
    }

}
