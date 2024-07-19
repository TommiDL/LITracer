package org.example
/**
 * An error found by the lexer/parser while reading a scene file
 *
 *     The fields of this type are the following:
 *
 *     - `file_name`: the name of the file, or the empty string if there is no real file
 *     - `line_num`: the line number where the error was discovered (starting from 1)
 *     - `col_num`: the column number where the error was discovered (starting from 1)
 *     - `message`: a user-friendly error message
 */
data class GrammarError (val location: SourceLocation, override val message: String ) : Exception("Grammar error at $location: $message\n\n")
{}
