/**
 * Student name:
 * Student ID:
 */
grammar MC;

@lexer::header{
    package mc.parser;
}

@lexer::members{
@Override
public Token emit() {
    switch (getType()) {
    case ILLEGAL_ESCAPE:
         Token result = super.emit();
         String s = super.emit().getText();
         boolean boo = false;
         for (int i = 0 ; i < s.length()-1; i++)
         {
            char nc = s.charAt(i+1);
            if ((s.charAt(i) == '\\') && (nc != 'b')&& (nc != 'f')&& (nc != 'r')&& (nc != 'n')
            && (nc != 't')&& (nc != '\'')&& (nc != '\"')&& (nc != '\\') )
            {
                boo = true;
                throw new IllegalEscape(s.substring(0, i+2));
            }
            if (boo == true ) break;
         }
    case UNCLOSE_STRING:       
        result = super.emit();
        String t = super.emit().getText();
        if (t.charAt(t.length() - 1) == '\n' )
        throw new UncloseString(t.substring(0,t.length() - 1) );
        else
        throw new UncloseString( t.substring(0,t.length()) );
    case ERROR_CHAR:
        result = super.emit();
        throw new ErrorToken(result.getText()); 
    default:
        return super.emit();
    }   
}
}
@parser::header{
    package mc.parser;
}

options{
    language=Java;
}

program : (decla)*;

decla : vardecla | funcdecla;

vardecla : pritype subvar (COMA subvar)* SEMI;

subvar : ID | ID LSB INT RSB;

funcdecla : functype ID LB paralistdecla RB blocksta;

paralistdecla : (paradecla (COMA paradecla)*)?;

paradecla : pritype (ID | ID LSB RSB);

blocksta : LP (vardecla)*(sta)* RP;

sta : ifsta | dowhilesta |  forsta | breaksta | contista | returnsta | expsta | blocksta;

ifsta : IF LB exp RB sta (ELSE sta)?;

dowhilesta : DO (sta)+ WHILE  exp SEMI;

forsta : FOR '(' exp SEMI exp SEMI exp ')' sta;

breaksta : BREAK SEMI;

contista : CONTINUE SEMI;

returnsta : RETURN exp? SEMI;

expsta : exp SEMI;

exp : exp1 ASSIGN exp | exp1;

exp1 : exp1 OR exp2 | exp2;

exp2 : exp2 AND exp3 | exp3;

exp3 : exp4 (EQUAL | NOTEQUAL) exp4 | exp4;

exp4 : exp5 (LT | LTE | GT | GTE) exp5 | exp5;

exp5 : exp5 (ADD | SUB) exp6 | exp6;

exp6 : exp6 (DIV | MUL | MOD) exp7 | exp7;

exp7 :  (NOT | SUB) exp7 | exp8;

exp8 : exp9 LSB exp RSB | exp9;

exp9 : LB exp RB | operand;

operand : literal | ID | funcall  ;

funcall : ID LB listexp RB;

listexp : (exp (COMA exp)*)?;

literal : INT | FLOAT | STRING | BOOLEAN;

functype : pritype | VOIDTYPE | arrpointertype;

arrpointertype : pritype LSB RSB;

pritype : INTTYPE | FLOATTYPE | BOOLTYPE | STRINGTYPE ;



// LITERALS
INT: [0-9]+;

fragment EOFFLOAT : ([e|E]'-'?[0-9]+);

fragment FLOATWITHOUTE : [0-9]*'.'[0-9]+ | [0-9]+'.'[0-9]*;

FLOAT: FLOATWITHOUTE | INT EOFFLOAT | FLOATWITHOUTE EOFFLOAT;

BOOLEAN : 'true' | 'false';

fragment STRING_UNIT : ((~('\\'))? ('\\\\')* '\\\"'| ~[\n\"] );

ILLEGAL_ESCAPE: '\"' STRING_UNIT* ( (~'\\') ('\\\\')* '\\' ~[bfrnt\'\"\\] ) .* ('\n'|EOF)?  {setText(getText().substring(1, getText().length()));};

STRING : '\"'    STRING_UNIT*  '\"' {setText(getText().substring(1, getText().length()-1));};

UNCLOSE_STRING: '\"' STRING_UNIT* ('\n'|EOF)    {setText(getText().substring(1, getText().length()));};



//KEYWORD

STRINGTYPE : 'string';
INTTYPE : 'int';
VOIDTYPE : 'void' ;
FLOATTYPE: 'float';
BOOLTYPE: 'boolean';
IF : 'if';
ELSE : 'else';
DO : 'do';
WHILE : 'while';
FOR : 'for';
BREAK : 'break';
CONTINUE : 'continue';
RETURN : 'return';


// OTHER

ID : [a-zA-Z_][a-zA-Z0-9_]*;
WS : [ \t\r\n\f]+ -> skip ; // skip spaces, tabs, newlines
CONMMENTLINE : '//'(.)*?('\n'|EOF) -> skip ;
COMMENTBLOCK : '/*'.*?'*/' -> skip;


//DAU CAU

LB: '(' ;
RB: ')' ;
LP: '{';
RP: '}';
LSB : '[' ;
RSB : ']';
SEMI: ';' ;
COMA : ',';

// DAU PHPE TOAN

ADD: '+';
MUL: '*';
NOT: '!';
OR: '||';
NOTEQUAL: '!=';
LT: '<';
LTE: '<=';
ASSIGN: '=';
SUB: '-';
DIV: '/';
MOD: '%';
AND: '&&';
EQUAL: '==';
GT: '>';
GTE: '>=';


//   ERROR CHAR


ERROR_CHAR: . ;



