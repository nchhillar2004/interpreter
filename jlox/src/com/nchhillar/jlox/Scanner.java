package com.nchhillar.jlox;

import static com.nchhillar.jlox.TokenType.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Scanner {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    // Map of reserved words to their token types.
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

    // Main scanning loop: go through source and build token list.
    List<Token>  scanTokens() {
        while (!isAtEnd()) {
            start = current; // mark start of next token
            scanToken();
        }

        tokens.add(new Token(EOF, "", null, line)); // add EOF marker
        return tokens;
    }

    // Look at next char and figure out what kind of token it starts.
    private void scanToken() {
        char c = advance();
        switch(c) {
            // Single-char tokens.
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

            // Two-char tokens: check if next char matches.
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

            // Slash could be division or comment.
            case '/':
                if (match('/')) {
                    // Comment: skip until newline.
                    while (peek() != '\n' && !isAtEnd()) advance();
                } else {
                    addToken(SLASH);
                }
                break;

            // Whitespace: just skip it.
            case ' ':
            case '\r':
            case '\t':
                break;

            case '\n':
                line++;
                break;

            // String literal starts here.
            case '"': string(); break;

            // Special case for "or" keyword (incomplete - should use identifier()).
            case 'o':
                if (peek() == 'r') {
                    addToken(OR);
                }
                break;

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

    // Scan an identifier or keyword.
    private void identifier() {
        while (isAlphaNumeric(peek())) advance();

        String text = source.substring(start, current);
        TokenType type = keywords.get(text);
        if (type == null) type = IDENTIFIER; // not a keyword, so it's user-defined
        addToken(type);
    }

    // Scan a number (integer or decimal).
    private void number() {
        while (isDigit(peek())) advance();

        // Check for decimal point followed by digits.
        if (peek() == '.' && isDigit(peekNext())) {
            advance(); // consume the '.'
            while (isDigit(peek())) advance();
        }

        addToken(NUMBER, Double.parseDouble(source.substring(start, current)));
    }

    // Scan a string literal between quotes.
    private void string() {
        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n') line++; // strings can span lines
            advance();
        }

        if (isAtEnd()) {
            Jlox.error(line, "Unterminated string.");
            return;
        }

        advance(); // consume closing quote
        // Extract string value without the quotes.
        String value = source.substring(start + 1, current - 1);
        addToken(STRING, value);
    }

    // Helper functions.

    private boolean isAtEnd() {
        return current >= source.length();
    }

    // Move forward one char and return it.
    private char advance(){
        current++;
        return source.charAt(current - 1);
    }

    // Create token with no literal value.
    private void addToken(TokenType type) {
        addToken(type, null);
    }

    // Create token with a literal value (for numbers, strings, etc.).
    private void addToken(TokenType type, Object literal) {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }

    // Conditionally consume next char if it matches (for two-char tokens).
    private boolean match(char expected) {
        if (isAtEnd()) return false;
        if (source.charAt(current) != expected) return false;

        current++;
        return true;
    }

    // Look at current char without consuming it.
    private char peek() {
        if (isAtEnd()) return '\0';
        return source.charAt(current);
    }

    // Look ahead one char without consuming.
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
