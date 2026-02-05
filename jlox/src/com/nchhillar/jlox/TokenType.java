package com.nchhillar.jlox;

enum TokenType {
    // Single char tokens.
    LEFT_PAREN, RIGHT_PAREN, LEFT_BRACE, RIGHT_BRACE,
    COMMA, DOT, MINUS, PLUS, SEMICOLON, SLASH, STAR,

    // One or two chat tokens.
    BANG, BANG_EQUAL,
    EQUAL, EQUAL_EQUAL,
    GREATER, GREATER_EQUAL,
    LESS, LESS_EQUAL,

    // Literals.
    IDENTIFIER, STRING, NUMBER,

    // Keywords.
    AND, OR, IF, ELSE, FOR, WHILE, FUN, NIL, CLASS,
    PRINT, RETURN, SUPER, THIS, TRUE, FALSE, VAR,

    EOF
}
