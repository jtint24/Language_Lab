package com.example.langlab.Parser;

import com.example.langlab.ErrorManager.ErrorManager;
import com.example.langlab.ErrorManager.Error;
import com.example.langlab.Lexer.Token;
import com.example.langlab.Lexer.TokenType;
import com.example.langlab.Lexer.Tokenizer;
import com.example.langlab.Parser.EventLibrary.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

public class Parser {
    ArrayList<Token> tokens = new ArrayList<>();
    int pos = 0;
    int fuel = 256;
    ArrayList<Event> events = new ArrayList<>();
    ErrorManager errorManager;
    Tokenizer tokenizer;

     public Parser(Tokenizer tokenizer, ErrorManager errorManager) {
         this.errorManager = errorManager;
         this.tokenizer = tokenizer;
     }

     public void setSymbols(List<Token> tokens) {
         this.tokens.clear();
         this.tokens.addAll(tokens);
     }
     MarkOpened open() {
         MarkOpened mark = new MarkOpened(this.events.size());
         this.events.add(new OpenEvent(TreeKind.invalid()));
         return mark;
     }

     MarkOpened openBefore(MarkClosed closer) {
        MarkOpened opener = new MarkOpened(closer.index);
        events.add(closer.index, new OpenEvent(TreeKind.invalid()));
        return opener;
     }

      MarkClosed close(MarkOpened m, TreeKind kind) {
         this.events.set(m.index, new OpenEvent(kind));
         CloseEvent closer = new CloseEvent();
         this.events.add(closer);
         return new MarkClosed(m.index);
     }

     void advance() {
         assert !this.eof();
         this.fuel = 256;
         this.events.add(new AdvanceEvent());
         this.pos++;
     }
     boolean eof() {
         return this.pos == this.tokens.size();
     }

     TokenType nth(int lookahead) {
         if (this.fuel == 0) {
             errorManager.logError(new Error(Error.ErrorType.PARSER_ERROR, "Parser is stuck at `"+tokens.get(this.pos+lookahead).getLexeme()+"`", true));
         }

         this.fuel--;

         if (this.pos+lookahead >= this.tokens.size()) {
             errorManager.logError(new Error(Error.ErrorType.PARSER_ERROR, "Parser has reached the end of the file, but expected a continuation", true));
         }

         TokenType retTokenType = this.tokens.get(this.pos+lookahead).getTokenType();
         return retTokenType;
     }

     boolean at(TokenType kind) {
         if (this.eof()) {
             return false;
         }
         return this.nth(0) == kind;
     }

     boolean eat(TokenType kind) {
         if (!this.eof() && this.at(kind)) {
             this.advance();
             return true;
         } else {
             return false;
         }
     }

     void expect(TokenType kind) {
         if (tokens.get(pos).getTokenType() != kind) {
             errorManager.logError(new Error(Error.ErrorType.PARSER_ERROR, "Expected token of type "+kind+", got "+this.nth(0), true));
         }

         this.eat(kind);
     }

     void advanceWithError(Error error) {
         MarkOpened m_opened = this.open();
         errorManager.logError(error);
         this.advance();
         this.close(m_opened, TreeKind.invalid());
     }

     public ParseTreeNode buildTree() {
         Stack<ParseTreeNode> stack = new Stack<>();

         Iterator<Token> symbolIterator = tokens.iterator();

         assert events.get(events.size()-1) instanceof CloseEvent;

         for (Event event : events) {
             if (event instanceof OpenEvent) {
                 stack.push(new NonterminalParseTreeNode(((OpenEvent) event).kind));
             } else if (event instanceof CloseEvent) {
                 ParseTreeNode tree = stack.pop();
                 if (stack.isEmpty()) {
                     stack.push(tree);
                 } else {
                     ((NonterminalParseTreeNode) stack.peek()).addChild(tree);
                 }
             } else if (event instanceof AdvanceEvent) {
                 Token token = symbolIterator.next();
                 ((NonterminalParseTreeNode) stack.peek()).addChild(new TerminalParseTreeNode(token));
             }
         }
         assert stack.size() == 1;

         return stack.pop();
     }
}
