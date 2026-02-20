package com.nchhillar.jlox;

// Represents a single token from the source code.
// A token is a meaningful unit like a keyword, operator, identifier, or literal value.
class Token{
    final TokenType type;    // what kind of token (e.g., NUMBER, PLUS, IDENTIFIER)
    final String lexeme;     // raw text from source (e.g., "123", "+", "myVar")
    final Object literal;    // parsed value for literals (e.g., 123.0 for number, "hello" for string)
    final int line;          // line number in source (for error messages)

    Token(TokenType type, String lexeme, Object literal, int line) {
        this.type = type;
        this.lexeme = lexeme;
        this.literal = literal;
        this.line = line;
    }

    public String toString() {
        return type + " " + lexeme + " " + literal;
    }
}
