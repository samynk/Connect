grammar GraphTransformer;

@header {
   package dae.fxcreator.io.codegen.parser;
} 
		
transform: template*;

template: TEMPLATE ID LBRACE code* RBRACE;
code: CODE ID (LBRACKET property RBRACKET)? (writeToBuffer)? LBRACE expression* RBRACE;
expression: ID? write+ SEMICOLUMN  ;
setting : ID LBRACKET property RBRACKET;
writeToBuffer: PIPE ID;
property : ID DOT ID;
write: WRITE  (value |  property | setting);
value: INT | FLOAT | BOOLEAN | STRING;

BACKSLASH: '\\';
EQUALS: '=';
PLUS: '+';
MINUS: '-';
DIVIDEBY: '/';
TIMES: '*';
MODULO: '%';
LBRACE: '{';
RBRACE: '}';
LPARENS: '(';
RPARENS: ')';
LBRACKET: '[';
RBRACKET: ']';
PIPE: '->';
DOT: '.';
COMMA: ',';
SEMICOLUMN:';';
DEGREE: '°';
TEMPLATE: 'template';
CODE: 'code';
WRITE: '<<';

INT: [+-]?[0-9]+;
FLOAT: [+-]?[0-9]* DOT [0-9]+;
STRING: ["]~('"')*["];
BOOLEAN: 'true' | 'false';
ID : ([a-z]|[A-Z])+[a-zA-Z0-9]* ;
LINES : [ \t\r\n] -> skip ; // newlines