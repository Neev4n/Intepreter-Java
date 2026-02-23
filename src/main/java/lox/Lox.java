package lox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Lox {

    private static final Interpreter interpreter = new Interpreter();
    static boolean hadError = false;
    static boolean hadRuntimeError = false;

    public static void main(String[] args) throws IOException {

        // more than one argument not allowed
        if (args.length > 1) {
            System.out.println("Usage: jlox [script]");
            System.exit(64);

        // if single argument then must be file path
        } else if (args.length == 1) {
            runFile(args[0]);

        // if no arguments then interactive cli
        } else {
            runPrompt();
        }

    }

    private static void runFile(String path) throws IOException{

        byte[] bytes = Files.readAllBytes(Paths.get(path)); // read all bytes from path
        run(new String(bytes, Charset.defaultCharset()));   // create new string from bytes (convert bytes using default character set) then run
        if (hadError) System.exit(65);
        if (hadRuntimeError) System.exit(70);
    }

    private static void runPrompt() throws IOException{

        InputStreamReader input = new InputStreamReader(System.in); // get input stream from terminal
        BufferedReader reader = new BufferedReader(input);  // create buffered reader from input

        for (;;) {
            System.out.print("> "); // display prompt
            String line = reader.readLine();    // read line
            if (line == null) break;    // if user ctrl+d then line is null so break
            run(line);  // if line is valid then interpret line
            hadError = false;
        }

    }

    private static void run(String source){

        Scanner scanner = new Scanner(source);  // create scanner from source string
        List<Token> tokens = scanner.scanTokens();   // get tokens from scanner

        Parser parser = new Parser(tokens);
        Expr expression = parser.parse();

        // Stop if there was a syntax error.
        if (hadError) return;

        interpreter.interpret(expression);

    }

    public static void runParse(String source) {
        Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scanTokens();

        Parser parser = new Parser(tokens);
        Expr expression = parser.parse();

        if (hadError) System.exit(65);

        System.out.println(new AstPrinter().print(expression));
    }

    public static void runEvaluate(String source) {
        Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scanTokens();

        Parser parser = new Parser(tokens);
        Expr expression = parser.parse();

        if (hadError) System.exit(65);

        interpreter.interpret(expression);
    }

    public static void runTokenize(String source) {
        Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scanTokens();

        for (Token token : tokens) {
            System.out.println(token.type + " " + token.lexeme + " " + token.literal);
        }

        // If scanning had an error, exit 65 (even if you printed some tokens).
        if (hadError) System.exit(65);
    }

    // runtime errors act differently to normal errors (runtime errors do not crash the repl)
    static void runtimeError(RuntimeError error) {
        System.err.println(error.getMessage() +
                "\n[line " + error.token.line + "]");
        hadRuntimeError = true;
    }

    // let other systems report errors separately to the implementations of those systems
    static void error(int line, String message){
        report(line, "", message);
    }

    static void error(Token token, String message) {
        if (token.type == TokenType.EOF) {
            report(token.line, " at end", message);
        } else {
            report(token.line, " at '" + token.lexeme + "'", message);
        }
    }

    private static void report(int line, String where, String message){

        System.err.println(
                "[line " + line + "] Error" + where + ": " + message);
        hadError = true;

    }
}
