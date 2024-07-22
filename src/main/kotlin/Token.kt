package org.example


// USING sealed class as "Union type" for tokens  (classi sigillate)
sealed class Token(var location: SourceLocation) {
    //  IdentifierToken
    /**
     *   Token containing an identifier
     */
    class IdentifierToken(location: SourceLocation, var identifier: String) : Token (location){
        override fun toString(): String {
            return identifier
        }
    }

    class KeywordToken(location: SourceLocation, val keyword: KeywordEnum): Token(location){
        override fun toString(): String {
            return keyword.toString()
        }
    }
    //  StringToken
    /**
     *   Token containing a literal string
     */
    class StringToken(location: SourceLocation, val string: String) : Token(location) {
        override fun toString(): String {
            return string
        }
    }

    //  LiteralNumberToken
    /**
     *   Token containing a literal number
     */

    class LiteralNumberToken(location: SourceLocation, val value: Float) : Token(location) {
        override fun toString(): String {
            return value.toString()
        }
    }

    // SymbolToken
    /**
     *   Token containing a symbol e.g. variable name
     */

     class SymbolToken(location: SourceLocation, val symbol: String) : Token(location) {
        override fun toString(): String {
            return symbol
        }
    }

    //Stoptoken
    /**
     * Token signalling the end of a file
     *
     * */
     class StopToken(location: SourceLocation) : Token(location){
         override fun toString(): String {
             return "StopToken"
        }
    }

}











