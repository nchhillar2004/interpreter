package com.nchhillar.jlox;

import java.util.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

/* Lox scripting language */
public class Jlox {
    static boolean hadError = false;

    /* ----- Main function ----- */
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

    // If path given, jlox reads the file and executes it
    private static void runFile (String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        run(new String(bytes, Charset.defaultCharset()));

        if (hadError) System.exit(65);
    }

    // Else, run interactively, enter and execute code one line at a time
    private static void runPrompt() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        // Infinitely run jlox interactive prompt
        for (;;) {
            System.out.print("jloxi> ");
            String line = reader.readLine();
            if (line == null) break;
            run(line);
            hadError = false; // if user makes mistake, it shouldn't kill their entire session
        }
    }

    // Both file runner and the prompt are wrappers around this core 'run' function
    private static void run(String source) {
        Scanner sc = new Scanner(source);
        List<Token> tokens = sc.scanTokens();
        Parser parser = new Parser(tokens);
        Expr expression = parser.parse();

        // Stop if there was a syntax error.
        if (hadError) return;

        System.out.println(new AstPrinter().print(expression));
    }

    /* ----- Error handling ----- */
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
}
