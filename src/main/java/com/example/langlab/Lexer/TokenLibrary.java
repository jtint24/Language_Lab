package com.example.langlab.Lexer;

import javafx.scene.paint.Color;
import java.util.HashSet;

public class TokenLibrary {
    public static TokenType[] getTokens() {
        return new TokenType[]{
            whitespace,
                FLOAT_TOKEN_TYPE,
                INT_TOKEN_TYPE,
                stringLiteral,
                identifier,
                var,
                ret,
                plus,
                div,
                mult,
                sub,
                equals,
                lParen,
                rParen,
                lBrace,
                rBrace,
                lBracket,
                rBracket,
                comma,
                arrow
        };
    }
    


    public static final TokenType whitespace = new TokenType(
            "whitespace",
            (String lexeme) -> {
                return allIn(lexeme, " \t\n");
            },
            (String lexeme) -> {
                return allIn(lexeme, " \t\n");
            },
            Color.LIGHTGRAY
    );

    public static final TokenType lParen = fromString("(", Color.rgb(200, 100, 30)
    );
    public static final TokenType rParen = fromString(")", Color.rgb(200, 100, 30)
    );

    public static final TokenType lBrace = fromString("{", Color.rgb(200, 100, 30)
    );
    public static final TokenType rBrace = fromString("}", Color.rgb(200, 100, 30)
    );
    public static final TokenType lBracket = fromString("[", Color.rgb(200, 100, 30)
    );
    public static final TokenType rBracket = fromString("]", Color.rgb(200, 100, 30)
    );
    public static final TokenType arrow = fromString("->", Color.rgb(200, 100, 30)
    );

    public static final TokenType comma = fromString(",", Color.rgb(200, 100, 30)
    );

    public static final TokenType stringLiteral = new TokenType(
            "String",
            (String lexeme) -> {
                if (!lexeme.startsWith("\"") || !lexeme.endsWith("\"") || lexeme.length() < 2) {
                    return false;
                }
                if (lexeme.equals("\"\"")) {
                    return true;
                }
                return !lexeme.substring(1, lexeme.length() - 1).contains("\"");
            },
            (String lexeme) -> {
                if (!lexeme.startsWith("\"")) {
                    return false;
                }
                if (lexeme.length() > 2 && lexeme.substring(1, lexeme.length()-1).contains("\"")) {
                    return false;
                }
                return true;
            },
            Color.rgb(10,180,10)
    );


    public static final TokenType identifier = new TokenType(
            "identifier",
            (String lexeme) -> {
                    if (inKeywords(lexeme)) {
                        return false;
                    }
                    if (lexeme.length() == 0) {
                        return true;
                    }
                    return  (Character.isLetter(lexeme.charAt(0)) || lexeme.charAt(0) == '_') && (allIn(lexeme.toLowerCase(), "1234567890qwertyuiopasdfghjklzxcvbnm_"));
                },
            (String lexeme) -> {
                if (lexeme.length() == 0) {
                    return true;
                }
                return  (Character.isLetter(lexeme.charAt(0)) || lexeme.charAt(0) == '_') && (allIn(lexeme.toLowerCase(), "1234567890qwertyuiopasdfghjklzxcvbnm_"));
            },
            Color.rgb(10, 120, 200)
    );

    public static final TokenType div = fromString("/", Color.rgb(200, 100, 30)).toBinder(new BindingPowers(PrecedenceLevel.MULTIPLICATION, Associativity.LEFT));
    public static final TokenType mult = fromString("*", Color.rgb(200, 100, 30)).toBinder(new BindingPowers(PrecedenceLevel.MULTIPLICATION, Associativity.LEFT));
    public static final TokenType sub = fromString("-", Color.rgb(200, 100, 30)).toBinder(new BindingPowers(PrecedenceLevel.ADDITION, Associativity.LEFT));

    public static final TokenType plus = new TokenType(
            "+",
            (String lexeme) -> {
                return lexeme.equals("+");
            },
            (String lexeme) -> {
                return "+".contains(lexeme);
            },
            Color.rgb(200, 100, 30)
    ).toBinder(new BindingPowers(PrecedenceLevel.ADDITION, Associativity.LEFT));

    private static final TokenType FLOAT_TOKEN_TYPE = new TokenType(
            "float",
            (String lexeme) -> {
                return allIn(lexeme, "1234567890.") && 1 == countOf(lexeme, '.');
            },
            (String lexeme) -> {
                return allIn(lexeme, "1234567890.") && 2 > countOf(lexeme, '.');
            },
            Color.rgb(180, 90, 230)
    );

    public static final TokenType INT_TOKEN_TYPE = new TokenType(
            "int",
            (String lexeme) -> {
                if (lexeme.length() == 0) {
                    return false;
                }
                return allIn(lexeme, "1234567890");
            },
            (String lexeme) -> {
                if (lexeme.length() == 0) {
                    return false;
                }
                return allIn(lexeme, "1234567890");
            },
            Color.rgb(180, 90, 230)
    );

    public static final TokenType epsilon = new TokenType(
            "epsilon",
            (String lexeme) -> {
                return lexeme.length() == 0;
            },
            (String lexeme) -> {
                return lexeme.length() == 0;
            },
            Color.BLACK
    );

    public static final TokenType eof = new TokenType(
            "eof",
            (String lexeme) -> {
                return false;
            },
            (String lexeme) -> {
                return false;
            },
            Color.BLACK
    );

    public static final TokenType var = fromString("var", Color.rgb(200, 180, 50));
    public static final TokenType ret = fromString("return", Color.rgb(200, 180, 50));
    public static final TokenType equals = fromString("=", Color.rgb(200, 100, 30));


    private static boolean allIn(String lexeme, String validChars) {
        for (char lexemeChar : lexeme.toCharArray()) {
            if (!validChars.contains("" + lexemeChar)) {
                return false;
            }
        }

        return true;
    }

    private static int countOf(String lexeme, char c) {
        int count = 0;
        for (char lexemeChar : lexeme.toCharArray()) {
            if (lexemeChar == c) {
                count ++;
            }
        }
        return count;
    }

    private static boolean inKeywords(String lexeme) {
        HashSet<String> keywords = new HashSet<>() {{
                add("var");
                add("match");
                add("if");
                add("else");
                add("return");
        }};

        return keywords.contains(lexeme);

    }

    private static TokenType fromString(String target, Color main ) {
        return new TokenType(
                target,
                target::equals,
                target::startsWith,
                main
        );
    }



    public static TokenType getEpsilon() {
        return epsilon;
    }

    public static TokenType getEOF() {
        return eof;
    }
}
