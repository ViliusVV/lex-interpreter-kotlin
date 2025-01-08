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

    val fileContents = File(filename).readLines()
    val scanner = Scanner(fileContents)
    scanner.process()

    scanner.tokens.forEach {
        println(it)
    }

    exitProcess(if (scanner.hasErrored) 65 else 0)
}

class Scanner(private val code: List<String>) {
    var tokens = mutableListOf<Token>()
    var hasErrored = false

    fun process() {
        code.forEachIndexed { index, line ->
            tokens += scanTokens(line, index + 1)
        }
        tokens += Token(TokenType.EOF, "")
    }

    private fun scanTokens(line: String, lineNumber: Int): List<Token> {
        var t = mutableListOf<Token>()

        line.toCharArray().forEach {
            try {
                val tokenType = TokenType.from(it.toString())
                t += Token(tokenType, it.toString())
            } catch (e: UnknownTokenException) {
                System.err.println("[line $lineNumber] Error: Unexpected character: ${e.token}")
                hasErrored = true
            }
        }

        return t
    }
}

