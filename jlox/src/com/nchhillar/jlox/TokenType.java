package com.nchhillar.jlox;

// All possible token types in Lox.
enum TokenType {
    // Single-character tokens.
    LEFT_PAREN, RIGHT_PAREN, LEFT_BRACE, RIGHT_BRACE,
    COMMA, DOT, MINUS, PLUS, SEMICOLON, SLASH, STAR,

    // One or two character tokens.
    BANG, BANG_EQUAL,
    EQUAL, EQUAL_EQUAL,
    GREATER, GREATER_EQUAL,
    LESS, LESS_EQUAL,

    // Literals (values in the code).
    IDENTIFIER, STRING, NUMBER,

    // Keywords (reserved words).
    AND, OR, IF, ELSE, FOR, WHILE, NIL, TRUE, FALSE,
    PRINT, FUN, RETURN, SUPER, CLASS, THIS, VAR,

    EOF // end of file marker
}