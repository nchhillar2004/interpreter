package com.nchhillar.jlox;

import java.util.List;

// Abstract syntax tree (AST) node types.
// Each expression type is a subclass that represents a different kind of expression in Lox.
abstract class Expr {
    // Visitor pattern interface for traversing the AST.
    interface Visitor<R> {
        R visitBinaryExpr(Binary expr);
        R visitGroupingExpr(Grouping expr);
        R visitLiteralExpr(Literal expr);
        R visitUnaryExpr(Unary expr);
    }
    
    // Binary expression: left operator right (e.g., 1 + 2, x == y).
    static class Binary extends Expr {
        Binary(Expr left, Token operator, Expr right) {
            this.left = left;
            this.operator = operator;
            this.right = right;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitBinaryExpr(this);
        }

        final Expr left;
        final Token operator;
        final Expr right;
    }
    
    // Grouping: (expression) - parentheses for precedence.
    static class Grouping extends Expr {
        Grouping(Expr expression) {
            this.expression = expression;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitGroupingExpr(this);
        }

        final Expr expression;
    }
    
    // Literal value: a number, string, boolean, or nil.
    static class Literal extends Expr {
        Literal(Object value) {
            this.value = value;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitLiteralExpr(this);
        }

        final Object value;
    }
    
    // Unary expression: operator right (e.g., !true, -5).
    static class Unary extends Expr {
        Unary(Token operator, Expr right) {
            this.operator = operator;
            this.right = right;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitUnaryExpr(this);
        }

        final Token operator;
        final Expr right;
    }

    // Visitor pattern: each node accepts a visitor to traverse it.
    abstract <R> R accept(Visitor<R> visitor);
}
