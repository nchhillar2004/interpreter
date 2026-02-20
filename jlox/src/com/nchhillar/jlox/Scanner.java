package com.nchhillar.jlox;

import static com.nchhillar.jlox.TokenType.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Scanner {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    private static final Map<String, TokenType> keywords;

    static {
        keywords = new HashMap<>();
        keywords.put("and",    AND);
        keywords.put("class",  CLASS);
        keywords.put("else",   ELSE);
        keywords.put("false",  FALSE);
        keywords.put("for",    FOR);
        keywords.put("fun",    FUN);
        keywords.put("if",     IF);
        keywords.put("nil",    NIL);
        keywords.put("or",     OR);
        keywords.put("print",  PRINT);
        keywords.put("return", RETURN);
        keywords.put("super",  SUPER);
        keywords.put("this",   THIS);
        keywords.put("true",   TRUE);
        keywords.put("var",    VAR);
        keywords.put("while",  WHILE);
    }

    // while scanner is in the source code, track:
    private int start = 0; // points to the first character in the lexeme currently beign scanned
    private int current = 0; // character currently being considered
    private int line = 1; // tracks what source line 'current' is on

    Scanner(String source) {
        this.source = source;
    }

    // Scan tokens, add tokens, until runs out of characters
    List<Token>  scanTokens() {
        // loop inside the source code, scan
        while (!isAtEnd()) {
            // We are at the beginning of the next lexeme.
            start = current;
            scanToken(); // each turn of loop we scan single token
        }

        // append one final "end of line" token
        tokens.add(new Token(EOF, "", null, line));
        return tokens;
    }

    // Recognizing Lexemes
    private void scanToken() {
        char c = advance();
        switch(c) {
            // single character lexemes
            case '(': addToken(LEFT_PAREN); break;
            case ')': addToken(RIGHT_PAREN); break;
            case '{': addToken(LEFT_BRACE); break;
            case '}': addToken(RIGHT_BRACE); break;
            case ',': addToken(COMMA); break;
            case '.': addToken(DOT); break;
            case '-': addToken(MINUS); break;
            case '+': addToken(PLUS); break;
            case ';': addToken(SEMICOLON); break;
            case '*': addToken(STAR); break;

            case '!':
                addToken(match('=') ? BANG_EQUAL : BANG);
                break;
            case '=':
                addToken(match('=') ? EQUAL_EQUAL : EQUAL);
                break;
            case '<':
                addToken(match('=') ? LESS_EQUAL : LESS);
                break;
            case '>':
                addToken(match('=') ? GREATER_EQUAL : GREATER);
                break;

            // longer lexemes
            case '/':
                if (match('/')) {
                    // A comment goes until the end of the line.
                    while (peek() != '\n' && !isAtEnd()) advance();
                } else {
                    addToken(SLASH);
                }
                break;

            // newlines and whitespaces
            case ' ':
            case '\r':
            case '\t':
                break; // Ignore whitespace.

            case '\n':
                line++;
                break;

            // String literals
            case '"': string(); break;

            // reserved words and identifiers
            case 'o':
                if (peek() == 'r') {
                    addToken(OR);
                }
                break;

            // Error if unexpected char
            default:
                if (isDigit(c)) {
                    number();
                } else if (isAlpha(c)) {
                    identifier();
                } else {
                    Jlox.error(line, "Unexpected character.");
                }   
                break;
        }
    }

    private void identifier() {
        while (isAlphaNumeric(peek())) advance();

        String text = source.substring(start, current);
        TokenType type = keywords.get(text);
        if (type == null) type = IDENTIFIER;
        addToken(type);
    }

    private void number() {
        while (isDigit(peek())) advance();

        // Look for a fractional part.
        if (peek() == '.' && isDigit(peekNext())) {
            // Consume the "."
            advance();

            while (isDigit(peek())) advance();
        }

        addToken(NUMBER, Double.parseDouble(source.substring(start, current)));
    }

    private void string() {
        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n') line++;
            advance();
        }

        if (isAtEnd()) {
            Jlox.error(line, "Unterminated string.");
            return;
        }

        // The closing ".
        advance();
        // Trin the surrounding quotes.
        String value = source.substring(start + 1, current - 1);
        addToken(STRING, value);
    }

    /* ----- Helper functions ----- */

    // tells if we've consumed all the characters
    private boolean isAtEnd() {
        return current >= source.length();
    }

    // consumes the next character in the source file and returns it
    private char advance(){
        current++;
        return source.charAt(current - 1);
    }

    // grab the text of current lexeme and creates a new token for it
    private void addToken(TokenType type) {
        addToken(type, null);
    }

    // overload to handle literal values too
    private void addToken(TokenType type, Object literal) {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }

    private boolean match(char expected) {
        if (isAtEnd()) return false;
        if (source.charAt(current) != expected) return false;

        current++;
        return true;
    }

    private char peek() {
        if (isAtEnd()) return '\0';
        return source.charAt(current);
    }

    private char peekNext() {
        if (current + 1 >= source.length()) return '\0';
        return source.charAt(current + 1);
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') ||
                (c >= 'A' && c <= 'Z') ||
                c == '_';
    }

    private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }
}
