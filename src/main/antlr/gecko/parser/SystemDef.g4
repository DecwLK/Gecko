grammar SystemDef;

model: include* defines? variants* globalCode=CODE? (contract|system)* EOF;

include: INCLUDE STRING SEMI?;

variants: VARIANTS v+=ident (COMMA? v+=ident)* SEMI?;

contract: automata;

automata: CONTRACT name=ident LBRACE
    io*
    history*
    prepost*
    transition*
    use_contracts*
    RBRACE;

/*
invariant: CONTRACT name=ident LBRACE
  io*
  history*
  prepost
  use_contracts*
 RBRACE;
*/

vvguard: '#[' (vvexpr (COMMA? vvexpr)*)?  ']';
vvexpr: vv ('..' vv)?;
vv: version | ident;
version: VERSION;

prepost: CONTRACT name=ident ASSIGN pre=expr STRONG_ARROW post=expr;

transition: vvguard? from=ident ARROW to=ident DOUBLE_COLUMN
    (contr=ident| pre=expr STRONG_ARROW post=expr);


system: REACTOR name=ident LBRACE
        io*
        use_contracts*
        connection*
        reaction?
        RBRACE;

connection: from=ioport ARROW
             (LPAREN (to+=ioport)+ RPAREN
             | to+=ioport)
             ;
ioport: (inst=ident '.')? port=ident;


use_contracts: CONTRACT use_contract (COMMA use_contract)*;
use_contract: ident ('[' (subst (COMMA subst)*)? ']')?;
subst: local=ident BARROW from=ioport;

defines: DEFINES LBRACE variable+ RBRACE;

io: type=(INPUT|OUTPUT|STATE) variable (COMMA variable)*;
history: HISTORY n=ident LPAREN INT RPAREN;

variable: n+=ident (COMMA n+=ident)* COLON t=ident (':=' init=expr)?;
reaction: CODE;

ident: Ident | HISTORY | DEFINES | VARIANTS | INCLUDE ;

exprEOF: expr EOF;

expr:
     unaryop=(NOT|MINUS) expr
    | expr op=DIV expr
    | expr op=MOD expr
    | expr op=STAR expr
    | expr op=PLUS expr
    | expr op=MINUS expr
    | expr op=SHIFTL expr
    | expr op=SHIFTR expr
    | expr op=(EQ | NEQ | LT | GT | LTE | GTE) expr
    | expr op=AND expr
    | expr op=(OR | XOR | XNOR) expr
    | expr QUESTION_MARK expr COLON expr
    | expr op=IMP expr
    | terminalAtom
    ;


terminalAtom
    : LPAREN expr RPAREN                                              # paren
    | name=ident LPAREN expr ( COMMA  expr)* RPAREN                   # functionExpr
    | casesExpr                                                       # casesExprAtom
    | value=ident varprefix*                                          # variablewithprefix
    | value=INT                                                       # integerLiteral
    | value=FLOAT                                                     # floatLiteral
    | value=('TRUE'|'true')                                           # trueExpr
    | value=('FALSE'|'false')                                         # falseExpr
    | value=WORD_LITERAL                                              # wordValue
    ;

varprefix : DOT dotted=ident            #fieldaccess
          | LBRACKET index=expr RBRACKET #arrayaccess
          ;

casesExpr  : CASE (branches+=caseBranch)+ ESAC;
caseBranch : cond=expr COLON val=expr SEMI;


// Lexer
SEMI:';';
NOT: '!';
MINUS:'-';
PLUS:'+';
CASE:'case';
ESAC:'esac';
EQ:'=';
NEQ:'!=';
LT:'<';
GTE:'>=';
LTE:'<=';
GT:'>';
SHIFTL: '<<';
DIV:'/';
MOD:'%';
STAR:'*';
SHIFTR: '>>';
IMP:'=>';
OR:'|';
XOR:'xor';
XNOR:'xnor';
AND:'&';
QUESTION_MARK : '?' ;
DOT:'.';
LPAREN:'(';
RPAREN:')';
CODE: '{=' .*? '=}';
RBRACE: '}';
LBRACE: '{';
LBRACKET:'[';
RBRACKET:']';


WS: [ \n\f\r\t]+ -> skip;
COMMENT: '//' (~[\n\r]+) -> skip;
MCOMMENT: '/*' .*? '*/' -> skip;
STRING: '"' .*? '"';
REACTOR: 'reactor';
CONTRACT: 'contract';
DEFINES : 'defines' ;
HISTORY : 'history' ;
INPUT: 'input';
OUTPUT: 'output';
STATE: 'state';
ARROW: '->';
BARROW: '<-';
COLON: ':';
COMMA: ',';
ASSIGN : ':=' ;
STRONG_ARROW : '==>' ;
DOUBLE_COLUMN : '::' ;
VARIANTS: 'variants';
INCLUDE: 'include';


WORD_LITERAL:
    '0' ('u' | 's')? ('b' | 'B' | 'o' | 'O' | '_' | 'd' | 'D' | 'h' | 'H') INT? '_' ('a'..'f' | 'A.' . 'F' | INT)*;

INT: [0-9]+;
FLOAT: INT '.' INT;
Ident: [a-zA-Z_][a-zA-Z_0-9]*;
VERSION: 'v' INT ('.' INT)*;

ERROR: .;
