import java.io.File
import java.lang.RuntimeException
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    if (args.size < 2) {
        System.err.println("Usage: ./your_program.sh tokenize <filename>")
        exitProcess(1)
    }

    val command = args[0]
    val filename = args[1]

    if (command != "tokenize") {
        System.err.println("Unknown command: ${command}")
        exitProcess(1)
    }

    val fileContents = File(filename).readText()
    val scanner = Scanner(fileContents)
    scanner.process()

    scanner.tokens.forEach {
        println(it)
    }
}

class Scanner(private val code: String) {
    var tokens = listOf<Token>()

    fun process() {
        tokens = scanTokens(code)
    }

    private fun scanTokens(code: String): List<Token> {
        var t = mutableListOf<Token>()
        code.toCharArray().forEach {
            val tokenType = TokenType.from(it.toString())
            t += Token(tokenType, it.toString())
        }
        t += Token(TokenType.EOF, "")
        return t
    }
}

data class Token(val type: TokenType, val lexeme: String, val value: String? = null) {
    override fun toString(): String {
        return "$type $lexeme $value"
    }
}

enum class TokenType(val lexeme: String? = null) {
    LEFT_PAREN("("),
    RIGHT_PAREN(")"),
    LEFT_BRACE("{"),
    RIGHT_BRACE("}"),
    SEMICOLON(";"),
    COMMA(","),
    PLUS("+"),
    MINUS("-"),
    STAR("*"),
    BANG_EQUAL("!="),
    EQUAL_EQUAL("=="),
    LESS_EQUAL("<="),
    GREATER_EQUAL(">="),
    LESS("<"),
    GREATER(">"),
    SLASH("/"),
    DOT("."),
    EOF("");

    companion object {
        fun from(value: String): TokenType {
            return when (value) {
                LEFT_PAREN.lexeme -> LEFT_PAREN
                RIGHT_PAREN.lexeme -> RIGHT_PAREN
                LEFT_BRACE.lexeme -> LEFT_BRACE
                RIGHT_BRACE.lexeme -> RIGHT_BRACE
                SEMICOLON.lexeme -> SEMICOLON
                COMMA.lexeme -> COMMA
                PLUS.lexeme -> PLUS
                MINUS.lexeme -> MINUS
                STAR.lexeme -> STAR
                BANG_EQUAL.lexeme -> BANG_EQUAL
                EQUAL_EQUAL.lexeme -> EQUAL_EQUAL
                LESS_EQUAL.lexeme -> LESS_EQUAL
                GREATER_EQUAL.lexeme -> GREATER_EQUAL
                LESS.lexeme -> LESS
                GREATER.lexeme -> GREATER
                SLASH.lexeme -> SLASH
                DOT.lexeme -> DOT
                else -> throw RuntimeException("Unknown token: $value")
            }
        }
    }
}
