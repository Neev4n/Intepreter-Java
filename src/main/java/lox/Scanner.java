package lox;

import java.util.ArrayList;
import java.util.List;

import static lox.TokenType.*;

public class Scanner {

    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    private int start = 0;
    private int current = 0;
    private int line = 1;

    public Scanner(String source) {

        this.source = source;

    }

    List<Token> scanTokens(){

        while (!isAtEnd()){

            start = current;
            scanToken();

        }

        tokens.add(new Token(EOF, "", null, line));
        return tokens;

    }

    private boolean isAtEnd(){
        return current >= source.length();
    }

    private void scanToken(){

        char c = advance();

        switch (c) {
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

            case '!': addToken(match('=') ? BANG_EQUAL : BANG); break;
            case '=': addToken(match('=') ? EQUAL_EQUAL : EQUAL); break;
            case '>': addToken(match('=') ? GREATER_EQUAL : GREATER); break;
            case '<': addToken(match('=') ? LESS_EQUAL : LESS); break;

            case '"': string(); break;

            case '/':
                if (match('/')){
                    while(peek() != '\n' && !isAtEnd()) advance();
                } else {
                    addToken(SLASH);
                }
                break;

            // no need to add tokens for redundant characters
            case ' ':
            case '\r':
            case '\t':
                // Ignore whitespace.
                break;

            case '\n':
                line++;
                break;

            default:
                Lox.error(line, "Unexpected character.");
                break;
        }
    }

    private char advance(){
        return source.charAt(current++);    // get current character and advance current pointer
    }

    private void addToken(TokenType type) {
        addToken(type, null);   // add token with no literal object
    }

    private void addToken(TokenType type, Object literal){
        String text = source.substring(start, current); // get lexeme
        tokens.add(new Token(type, text, literal, line));   // add token
    }

    private boolean match(char expected){
        if (isAtEnd()) return false;    // if final character return false
        if (source.charAt(current) != expected) return false;   // if next character is not the expected on return false

        current++;  // advance current pointer and return true
        return true;
    }

    private char peek(){
        if (isAtEnd()) return '\0'; // if we are at end then return null character
        return source.charAt(current);  // return the character at the current pointer
    }

    private void string(){

        // keep consuming characters until closing quote (check if we are at end every time)
        while (peek() != '"' && !isAtEnd()){
            if (peek() == '\n'); line++;    // if we encounter a newline then increment line
            advance();  // move to next character
        }

        advance();

        if (isAtEnd()){
            Lox.error(line, "Unterminated string.");
            return;
        }

        String value = source.substring(start+1, current-1);
        addToken(STRING, value);
    }
}
