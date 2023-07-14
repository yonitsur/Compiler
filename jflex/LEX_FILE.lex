/***************************/
/* FILE NAME: LEX_FILE.lex */
/***************************/

/***************************/
/* AUTHOR: OREN ISH SHALOM */
/***************************/

/*************/
/* USER CODE */
/*************/
   
import java_cup.runtime.*;

/******************************/
/* DOLAR DOLAR - DON'T TOUCH! */
/******************************/
      
%%
   
/************************************/
/* OPTIONS AND DECLARATIONS SECTION */
/************************************/
   
/*****************************************************/ 
/* Lexer is the name of the class JFlex will create. */
/* The code will be written to the file Lexer.java.  */
/*****************************************************/ 
%class Lexer

/********************************************************************/
/* The current line number can be accessed with the variable yyline */
/* and the current column number with the variable yycolumn.        */
/********************************************************************/
%line
%column
    
/*******************************************************************************/
/* Note that this has to be the EXACT smae name of the class the CUP generates */
/*******************************************************************************/
%cupsym TokenNames

/******************************************************************/
/* CUP compatibility mode interfaces with a CUP generated parser. */
/******************************************************************/
%cup
   
/****************/
/* DECLARATIONS */
/****************/
/*****************************************************************************/   
/* Code between %{ and %}, both of which must be at the beginning of a line, */
/* will be copied letter to letter into the Lexer class code.                */
/* Here you declare member variables and functions that are used inside the  */
/* scanner actions.                                                          */  
/*****************************************************************************/   
%{
	/*********************************************************************************/
	/* Create a new java_cup.runtime.Symbol with information about the current token */
	/*********************************************************************************/
	private Symbol symbol(int type)               {return new Symbol(type, yyline, yycolumn);}
	private Symbol symbol(int type, Object value) {return new Symbol(type, yyline, yycolumn, value);}

	/*******************************************/
	/* Enable line number extraction from main */
	/*******************************************/
	public int getLine()    { return yyline + 1; }
	public int getCharPos() { return yycolumn;   } 
%}

/***********************/
/* MACRO DECALARATIONS */
/***********************/
LineTerminator	= \r|\n|\r\n
WhiteSpace		= {LineTerminator} | [ \t\f]
INTEGER			= 0 | [1-9][0-9]*
LEADING_ZERO	= 0[0-9]+ 

LEGAL_COMMENT_CHARS = [a-zA-Z] | [0-9] | "(" | ")" | "[" | "]" | "{" | "}" | "?" | "!" | "+" | "-" | "." | ";"| {WhiteSpace}
ID				= [a-zA-Z]([a-zA-Z] | [0-9])* 
STRING          = \"[a-zA-Z]*\"

LEGAL_COMMENT_CHARS2 = [a-zA-Z] | [0-9] | [ \t] | "(" | ")" | "[" | "]" | "{" | "}" | "?" | "*" | "/" | "!" | "+" | "-" | "." | ";" 
TYPE1_COMMENT   =  "//" {LEGAL_COMMENT_CHARS2}* {LineTerminator}+
T1_ERR = "//" (({LEGAL_COMMENT_CHARS2})*[^a-zA-Z0-9\t()\[\]{}?*/!+-.;\n]+)+ {LineTerminator}

TYPE2_COMMENT   = "/*" 
					(
					(({LEGAL_COMMENT_CHARS} |"/") | (\*+{LEGAL_COMMENT_CHARS}))* 
					| ( {LEGAL_COMMENT_CHARS} |"*")* 
					)
					"*/"
T2_ERR 			= "/*"
					(
					(({LEGAL_COMMENT_CHARS} |"/") | (\*+{LEGAL_COMMENT_CHARS}))* 
					| ( {LEGAL_COMMENT_CHARS} |"*")* 
					)
   
/******************************/
/* DOLAR DOLAR - DON'T TOUCH! */
/******************************/

%%

/************************************************************/
/* LEXER matches regular expressions to actions (Java code) */
/************************************************************/
   
/**************************************************************/
/* YYINITIAL is the state at which the lexer begins scanning. */
/* So these regular expressions will only be matched if the   */
/* scanner is in the start state YYINITIAL.                   */
/**************************************************************/

<YYINITIAL> {
{TYPE1_COMMENT}		{ /* just skip what was found, do nothing */ }
{TYPE2_COMMENT}		{ /* just skip what was found, do nothing */ }
{T1_ERR} { return symbol(TokenNames.error) ;} 
{T2_ERR} { return symbol(TokenNames.error) ;} 
{WhiteSpace}		{ /* just skip what was found, do nothing */ }

//{LineTerminator}	{ /* just skip what was found, do nothing */ }

{LEADING_ZERO}		{ return symbol(TokenNames.error);}

"if"				{ return symbol(TokenNames.IF);}
"array"             { return symbol(TokenNames.ARRAY);}
"class"             { return symbol(TokenNames.CLASS);}
"nil"               { return symbol(TokenNames.NIL);}
"extends"           { return symbol(TokenNames.EXTENDS);}
"return"            { return symbol(TokenNames.RETURN);}
"while"             { return symbol(TokenNames.WHILE);}
"new"               { return symbol(TokenNames.NEW);}
//"void"        		{ return symbol(TokenNames.TYPE_VOID);}
//"int"          		{ return symbol(TokenNames.TYPE_INT);}
//"string"       		{ return symbol(TokenNames.TYPE_STRING);}
"="					{ return symbol(TokenNames.EQ);}
"<"					{ return symbol(TokenNames.LT);}
">"					{ return symbol(TokenNames.GT);}
"."					{ return symbol(TokenNames.DOT);}
"+"					{ return symbol(TokenNames.PLUS);}
"-"					{ return symbol(TokenNames.MINUS);}
"*"					{ return symbol(TokenNames.TIMES);}
"/"					{ return symbol(TokenNames.DIVIDE);}
":="				{ return symbol(TokenNames.ASSIGN);}
"("					{ return symbol(TokenNames.LPAREN);}
")"					{ return symbol(TokenNames.RPAREN);}
"["					{ return symbol(TokenNames.LBRACK);}
"]"					{ return symbol(TokenNames.RBRACK);}
"{"					{ return symbol(TokenNames.LBRACE);}
"}"					{ return symbol(TokenNames.RBRACE);}
","					{ return symbol(TokenNames.COMMA);}
";"					{ return symbol(TokenNames.SEMICOLON);}
{ID}				{ return symbol(TokenNames.ID, new String(yytext()));}
{INTEGER}			{ 
						if((Integer.parseInt(yytext()) > 32767)||(Integer.parseInt(yytext()) < 0)){
							return symbol(TokenNames.error);
						}
                        return symbol(TokenNames.INT, new Integer(yytext()));
                    } 
{STRING}			{ return symbol(TokenNames.STRING, new String(yytext()));}


<<EOF>>				{ return symbol(TokenNames.EOF);}
}
