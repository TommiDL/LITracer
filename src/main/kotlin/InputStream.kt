package org.example

/**
 * Class InputStream: uses a stream for reading from a file
 * This class implements a wrapper around a stream, with the following additional capabilities:
 *     - It tracks the line number and column number;
 *     - It permits to "un-read" characters and tokens.
 */
class InputStream {

    private var stream: java.io.InputStream
    private var file_name: String
    private var tabulations: Int

    constructor(stream: java.io.InputStream, file_name: String = "", tabulations: Int = 8) {
        this.stream = stream
        this.file_name = file_name
        this.tabulations = tabulations
        this.location = SourceLocation(file_name = file_name, line_num = 1, col_num = 1)
        this.saved_location = this.location
        this.saved_char = '\u0000'
    }

    var location: SourceLocation
    var saved_char: Char
    var saved_location: SourceLocation
    var saved_token:Token? = null

    /**
     *         """Read a new character from the stream with a check on the end of the stream (byte == -1)"""
     */
    fun read_char(): Char {

        val ch: Char

        if (saved_char != '\u0000') {
            ch = saved_char
            saved_char = '\u0000'
        } else {
            val byte = this.stream.read()
            ch = if (byte == -1) '\u0000' else byte.toChar()
        }

        saved_location = location.copy()
        update_pos(ch)
        return ch

    }

    /**
     *         """Push a character back to the stream"""
     */
    fun unread_char(ch: Char) {
        require(this.saved_char == '\u0000') // NB: catch the exception when you use it
        this.saved_char = ch
        this.location = this.saved_location.copy()
    }

    /**
     *         """Keep reading characters until a non-whitespace/non-comment character is found"""
     */
    fun skip_whitespaces_and_comments(){
        val WHITESPACE = listOf(' ', '\t', '\n', '\r')
        var ch = this.read_char()

        while (ch in WHITESPACE || ch == '#') {
            if (ch == '#') {
                //It's a comment! Keep reading until the end of the line (include the case "", the end-of-file)
                while (this.read_char() !in listOf('\r', '\n', '\u0000')){
                    //do nothing
                    continue
                }
            }
            ch = this.read_char()
            if (ch == '\u0000') return
        }

        // Put the non-whitespace character back
        this.unread_char(ch)
    }

    /**
     *         """Update `location` after having read `ch` from the stream"""
     */
    private fun update_pos(ch: Char?) {
        when (ch) {
            '\u0000' -> {
                return
            }
            '\n' -> {
                this.location.line_num += 1
                this.location.col_num = 1
            }
            '\t' -> {
                this.location.col_num += this.tabulations
            }
            else -> {
                this.location.col_num += 1
            }
        }
    }

    /**
     *         """Read a token from the stream"""
     */
    fun read_token():Token? {
        val SYMBOLS = listOf('(', ',', ')', '{', '}', '[', ']', ';', ':', '!', '?', '*', '/')
        var result:Token?

        if(this.saved_token != null){
            result = this.saved_token
            this.saved_token = null
            return result
        }

        this.skip_whitespaces_and_comments()

        //At this point we're sure that ch does *not* contain a whitespace character
        val ch:Char = this.read_char()
        if(ch == '\u0000'){
            return Token.StopToken(location=this.location)
        }

        //At this point we must check what kind of token begins with the "ch" character (which has been
        //put back in the stream with self.unread_char). First, we save the position in the stream
        val token_location:SourceLocation = this.location.copy()

        if(ch in SYMBOLS) {
            //One-character symbol, like '(' or ','
            return Token.SymbolToken(token_location, ch.toString())
        } else if(ch =='"'){
            //A literal string (used for file names)
            return this._parse_string_token(token_location=token_location)
        } else if(ch.isDigit() || ch in listOf('+', '-', '.')){
            //A floating-point number
            return this._parse_float_token(first_char=ch.toString(), token_location=token_location)
        } else if(ch.isLetter() || ch == '_'){
            //Since it begins with an alphabetic character, it must either be a keyword or an identifier
            return this._parse_keyword_or_identifier_token(first_char=ch.toString(), token_location=token_location)
        } else{
            //We got some weird character, like '@` or `&`
            throw GrammarError(this.location, message = "Invalid character $ch")
        }
    }

    /**
     *        """Make as if `token` were never read from `input_file`"""
     */
    fun unread_token(token:Token){
        require(this.saved_token != null)
        this.saved_token = token
    }

    private fun _parse_string_token(token_location: SourceLocation): Token.StringToken {

        var token:String = ""
        while(true) {
            val ch = this.read_char()

            if (ch == '"') break

            if (ch == '\u0000') throw GrammarError(token_location, "unterminated string")

            token += ch.toString()
        }

        return Token.StringToken(token_location, token)
    }

    private fun _parse_float_token(first_char: String, token_location: SourceLocation): Token.LiteralNumberToken {

        var token: String = first_char
        while (true) {
            val ch = this.read_char()

            if (!(ch.isDigit() || ch == '.' || ch in listOf('e', 'E'))) {
                this.unread_char(ch)
                break
            }

            token += ch
        }

        val value:Float

        try {
            value = token.toFloat()
        } catch (e: NumberFormatException) {
            throw GrammarError(token_location, "'$token' is an invalid floating-point number")
        }

        return Token.LiteralNumberToken(token_location, value)

    }

    private fun _parse_keyword_or_identifier_token(first_char: String, token_location: SourceLocation): Token{

            var token = first_char
            while (true) {
                val ch = this.read_char()
                //Note that here we do not call "isLetter" but "isLetterOrDigit": digits are ok after the first character
                if (!(ch.isLetterOrDigit() || ch == '_')) {
                    this.unread_char(ch)
                    break
                }
                token += ch
            }

            try {
                // If it is a keyword, it must be listed in the KEYWORDS map
                return Token.KeywordToken(token_location, KEYWORDS[token] ?: throw NoSuchElementException())
            } catch (e: NoSuchElementException) {
                // If we got NoSuchElementException, it is not a keyword, and thus it must be an identifier
                return Token.IdentifierToken(token_location, token)
            }
    }

}