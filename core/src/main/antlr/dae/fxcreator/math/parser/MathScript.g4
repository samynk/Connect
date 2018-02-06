grammar MathScript;

@header {
    package dae.fxcreator.io.math.parser;
}

script: expression*;

expression: ( assignment | formula );
assignment: ID EQUALS formula SEMICOLUMN;

call: ID DOT ID LPARENS value (COMMA value)* RPARENS SEMICOLUMN;

method: ID LPARENS value (COMMA value)* RPARENS;
formula:  
    value                   // literal value
 | (PLUS|MINUS) formula     // unary operator             
 | LPARENS formula RPARENS  // expression in brackets 
 | op1=formula op=(TIMES | CROSS | DIVIDEBY | MODULO)   op2=formula         // precedence 1 for x,/ and %
 | op1=formula op=(PLUS | MINUS)                op2=formula        // precedence 2 for +,-
 | method                   // method call
 ;                  
 
value:      primitive | fvec |  ID ;
primitive:  INT | FLOAT | BOOLEAN | STRING;
fvec :      fvec2 | fvec3 | fvec4;
fvec2:      LBRACKET x=FLOAT COMMA y=FLOAT RBRACKET;
fvec3:      LBRACKET x=FLOAT COMMA y=FLOAT COMMA z=FLOAT RBRACKET;
fvec4:      LBRACKET x=FLOAT COMMA y=FLOAT COMMA z=FLOAT COMMA w=FLOAT RBRACKET;

BACKSLASH: '\\';
EQUALS: '=';
PLUS: '+';
MINUS: '-';
DIVIDEBY: '/';
TIMES: '*';
CROSS: '\u00c4' | 'cross';
DOT : '\u00b7' | 'dot';
MODULO: '%';
LBRACE: '{';
RBRACE: '}';
LPARENS: '(';
RPARENS: ')';
LBRACKET: '[';
RBRACKET: ']';
COMMA: ',';
SEMICOLUMN:';';
DEGREE: '°';

INT: [+-]?[0-9]+[\u00B0]?;
FLOAT: [+-]?[0-9]* DOT [0-9]+[\u00B0]?;
STRING: ["]~('"')*["];
BOOLEAN: 'true' | 'false';
ID : ([a-z]|[A-Z])+[a-zA-Z0-9]* ;
LINES : [ \t\r\n] -> skip ; // newlines
