grammar GraphTransformer;

@header {
   package dae.fxcreator.io.codegen.parser;
} 
		
transform: template*;

template: TEMPLATE ID LBRACE code* RBRACE;
code: CODE ID (LBRACKET property RBRACKET)? (writeToBuffer)? LBRACE ( methodCall| expression | forLoop | ifStatement)* RBRACE;
methodCall: property LPARENS (parameter (COMMA parameter) *)? RPARENS SEMICOLUMN ;
expression: ID? write+ SEMICOLUMN  ;
setting : ID LBRACKET property RBRACKET;
writeToBuffer: PIPE ID;
property : ID DOT ID;
write: WRITE  (value |  property | setting);
value: INT | FLOAT | BOOLEAN | STRING;

forLoop: FOR LPARENS ID COLON property RPARENS LBRACE (expression|methodCall)* RBRACE;

booleanComparisonOp: LE | LEQ | MO | MOQ | EQUALS | NEQUALS; 
booleanBinaryOp : AND | OR | XOR;
booleanValue: BOOLEAN | property;
booleanExpr: booleanValue | (booleanValue booleanBinaryOp booleanValue) | NOT booleanExpr;

ifStatement: IF LPARENS booleanExpr RPARENS LBRACE expression* RBRACE elseStatement?; 
elseStatement: ELSE LBRACE expression* RBRACE;


parameter: ID | value | property;

BACKSLASH: '\\';
ASSIGN: '=';
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
COLON: ':';
TEMPLATE: 'template';
CODE: 'code';
FOR: 'for';
IF: 'if';
ELSE: 'else';
WRITE: '<<';
LE: '<';
LEQ: '<=';
MO: '>';
MOQ: '>=';
EQUALS: '==';
NEQUALS: '!=';
AND: '&&';
OR: '||';
XOR: '^';
NOT: '!';


INT: [+-]?[0-9]+;
FLOAT: [+-]?[0-9]* DOT [0-9]+;
STRING : '"' (ESC | ~["\\])* '"' ;
fragment ESC : '\\' (["\\/bfnrt] | UNICODE) ;
fragment UNICODE : 'u' HEX HEX HEX HEX ;
fragment HEX : [0-9a-fA-F] ;
BOOLEAN: 'true' | 'false';
ID : ([a-z]|[A-Z])+[a-zA-Z0-9]* ;
LINES : [ \t\r\n] -> skip ; // newlines