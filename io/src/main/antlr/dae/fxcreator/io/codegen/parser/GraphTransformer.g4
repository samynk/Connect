grammar GraphTransformer;

@header {
   package dae.fxcreator.io.codegen.parser;
} 
		
transform: map? template*;

map: MAP LBRACE mapping* RBRACE;
mapping: key=ID PIPE (ID (DOT ID)*) SEMICOLUMN;

template: TEMPLATE ID LBRACE code* RBRACE;
code: CODE codeID=ID setting?  (writeToBuffer)? 
    LBRACE 
        statements
    RBRACE;

statements: ((objectChain SEMICOLUMN )
        | writeBuffer 
        | forLoop 
        | ifStatement)*;

object: PORT? identifier = ID ( parameterList | setting )?;
parameterList: LPARENS (parameter (COMMA parameter) *) RPARENS;
setting: LBRACKET group=ID DOT key=ID RBRACKET;
parameter: value | objectChain;
objectChain: object ( DOT object)*;

writeBuffer: buffer=ID? write+ SEMICOLUMN  ;
writeToBuffer: PIPE ID;
write: WRITE  (value |  objectChain );

value: INT | FLOAT | BOOLEAN | STRING;

// control structures
forLoop: FOR LPARENS var=ID COLON objectChain RPARENS 
    LBRACE 
       statements
    RBRACE;
ifStatement: IF LPARENS booleanExpr RPARENS LBRACE statements RBRACE elseStatement?; 
elseStatement: ELSE LBRACE statements RBRACE;

booleanComparisonOp: LE | LEQ | MO | MOQ | EQUALS | NEQUALS; 
booleanBinaryOp : AND | OR | XOR;
// objectChain has to evaluate to boolean
booleanValue: BOOLEAN | objectChain;
booleanExpr: booleanValue | (booleanValue booleanBinaryOp booleanValue) | NOT booleanExpr;

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
MAP : 'map';
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
PORT: '$';


INT: [+-]?[0-9]+;
FLOAT: [+-]?[0-9]* DOT [0-9]+;
STRING : '"' (ESC | ~["\\])* '"' ;
fragment ESC : '\\' (["\\/bfnrt] | UNICODE) ;
fragment UNICODE : 'u' HEX HEX HEX HEX ;
fragment HEX : [0-9a-fA-F] ;
BOOLEAN: 'true' | 'false';
ID : ([a-z]|[A-Z])+[a-zA-Z0-9]* ;

LINES : [ \t\r\n] -> skip ; // newlines