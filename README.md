# Crafting Interpreters Learnings
Lox scripting language interpreters.

## High-Level Architecture

### 1. Tree-walk Interpreter (jlox – Java)
`Lexing -> Parsing -> AST -> Interpreting`
+ Variables & Scope
+ Control Flow
+ Functions & Closures
+ Classes

### 2. Bytecode VM (clox – C)
`Compiler -> Bytecode -> Virtual Machine (VM)`
+ Memory & Garbage Collection
+ Optimizations

## Reference
[Crafting Interpreters](https://craftinginterpreters.com/) by Robert Nystrom
