package com.nchhillar.jlox;

import java.util.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

// Main entry point for the Lox interpreter.
// Handles running files or starting an interactive REPL, then pipes code through Scanner -> Parser -> AST printer.
public class Jlox {
    private static final Interpreter interpreter = new Interpreter();
    static boolean hadError = false;
    static boolean hadRuntimeError = false;

    // Entry point: decide whether to run a file or start REPL based on command line args.
    public static void main(String[] args) throws IOException{
        if (args.length > 1) {
            System.out.println("Usage: jlox [script]");
            System.exit(64);
        } else if (args.length == 1) {
            runFile(args[0]);
        } else {
            runPrompt();
        }
    }

    // Read a file and run it once (batch mode).
    private static void runFile (String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        run(new String(bytes, Charset.defaultCharset()));

        if (hadError) System.exit(65);
        if (hadRuntimeError) System.exit(70);
    }

    // Interactive REPL: read one line, run it, print result, repeat.
    private static void runPrompt() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        for (;;) {
            System.out.print("jloxi> ");
            String line = reader.readLine();
            if (line == null) break; // EOF (Ctrl+D)
            run(line);
            hadError = false; // reset so one mistake doesn't kill the session
        }
    }

    // Core pipeline: source text -> tokens -> AST -> print it.
    private static void run(String source) {
        Scanner sc = new Scanner(source);
        List<Token> tokens = sc.scanTokens();
        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        if (hadError) return; // don't try to print broken AST
        
        Resolver resolver = new Resolver(interpreter);
        resolver.resolve(statements);
        
        if (hadError) return;

        interpreter.interpret(statements);
    }

    // Error reporting helpers.
    static void error(int line, String message) {
        report(line, "", message);
    }

    private static void report(int line, String where, String message) {
        System.err.println("[line " + line + "] Error" + where + ": " + message);
        hadError = true;
    }

    static void error(Token token, String message) {
        if (token.type == TokenType.EOF) {
            report(token.line, " at end", message);
        } else {
            report(token.line, " at '" + token.lexeme + "'", message);
        }
    }

    static void runtimeError(RuntimeError error) {
        System.err.println(error.getMessage() +
            "\n[line " + error.token.line + "]");
        hadRuntimeError = true;
    }
}
