package lox;

enum TokenType {

    // single-character tokens
    LEFT_PAREN, RIGHT_PAREN, LEFT_BRACE, RIGHT_BRACE,
    COMMA, DOT, MINUS, PLUS, SEMICOLON, SLASH, STAR,

    // one or more character tokens
    BANG, BANG_EQUAL,
    EQUAL, EQUAL_EQUAL,
    GREATER, GREATER_EQUAL,
    LESS, LESS_EQUAL,

    // literal
    IDENTIFIER, STRING, NUMBER,

    // keywords
    AND, OR, IF, ELSE, TRUE, FALSE, VAR, FOR, WHILE,
    NIL, PRINT, RETURN, SUPER, THIS, CLASS, FUN,

    EOF
}
