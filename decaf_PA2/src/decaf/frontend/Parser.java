//### This file created by BYACC 1.8(/Java extension  1.13)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//###           14 Sep 06  -- Keltin Leung-- ReduceListener support, eliminate underflow report in error recovery
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";






//#line 11 "Parser.y"
package decaf.frontend;

import decaf.tree.Tree;
import decaf.tree.Tree.*;
import decaf.error.*;
import java.util.*;
//#line 25 "Parser.java"
interface ReduceListener {
  public boolean onReduce(String rule);
}




public class Parser
             extends BaseParser
             implements ReduceListener
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

ReduceListener reduceListener = null;
void yyclearin ()       {yychar = (-1);}
void yyerrok ()         {yyerrflag=0;}
void addReduceListener(ReduceListener l) {
  reduceListener = l;}


//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//## **user defined:SemValue
String   yytext;//user variable to return contextual strings
SemValue yyval; //used to return semantic vals from action routines
SemValue yylval;//the 'lval' (result) I got from yylex()
SemValue valstk[] = new SemValue[YYSTACKSIZE];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
final void val_init()
{
  yyval=new SemValue();
  yylval=new SemValue();
  valptr=-1;
}
final void val_push(SemValue val)
{
  try {
    valptr++;
    valstk[valptr]=val;
  }
  catch (ArrayIndexOutOfBoundsException e) {
    int oldsize = valstk.length;
    int newsize = oldsize*2;
    SemValue[] newstack = new SemValue[newsize];
    System.arraycopy(valstk,0,newstack,0,oldsize);
    valstk = newstack;
    valstk[valptr]=val;
  }
}
final SemValue val_pop()
{
  return valstk[valptr--];
}
final void val_drop(int cnt)
{
  valptr -= cnt;
}
final SemValue val_peek(int relative)
{
  return valstk[valptr-relative];
}
//#### end semantic value section ####
public final static short VOID=257;
public final static short BOOL=258;
public final static short INT=259;
public final static short STRING=260;
public final static short CLASS=261;
public final static short DOUBLE=262;
public final static short NULL=263;
public final static short EXTENDS=264;
public final static short THIS=265;
public final static short WHILE=266;
public final static short FOR=267;
public final static short IF=268;
public final static short ELSE=269;
public final static short RETURN=270;
public final static short BREAK=271;
public final static short NEW=272;
public final static short PRINT=273;
public final static short READ_INTEGER=274;
public final static short READ_LINE=275;
public final static short LITERAL=276;
public final static short IDENTIFIER=277;
public final static short AND=278;
public final static short OR=279;
public final static short STATIC=280;
public final static short REPEAT=281;
public final static short UNTIL=282;
public final static short LESS_EQUAL=283;
public final static short GREATER_EQUAL=284;
public final static short EQUAL=285;
public final static short NOT_EQUAL=286;
public final static short UMINUS=287;
public final static short EMPTY=288;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    1,    1,    3,    4,    5,    5,    5,    5,    5,
    5,    5,    2,    6,    6,    7,    7,    7,    9,    9,
   10,   10,    8,    8,   11,   12,   12,   13,   13,   13,
   13,   13,   13,   13,   13,   13,   13,   14,   14,   14,
   25,   25,   22,   22,   24,   23,   23,   23,   23,   23,
   23,   23,   23,   23,   23,   23,   23,   23,   23,   23,
   23,   23,   23,   23,   23,   23,   23,   23,   23,   23,
   27,   27,   26,   26,   28,   28,   16,   18,   21,   15,
   29,   29,   19,   19,   20,   17,
};
final static short yylen[] = {                            2,
    1,    2,    1,    2,    2,    1,    1,    1,    1,    1,
    2,    3,    6,    2,    0,    2,    2,    0,    1,    0,
    3,    1,    7,    6,    3,    2,    0,    1,    2,    1,
    1,    1,    1,    2,    2,    2,    1,    3,    1,    0,
    2,    0,    2,    4,    5,    1,    1,    1,    3,    3,
    3,    3,    3,    3,    3,    3,    3,    3,    3,    3,
    3,    3,    2,    2,    3,    3,    1,    4,    5,    5,
    1,    1,    1,    0,    3,    1,    5,    9,    1,    6,
    2,    0,    2,    1,    4,    6,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    3,    0,    2,    0,    0,   14,   18,
    0,    8,    9,    6,   10,    0,    7,    0,   13,   16,
    0,    0,   17,   11,    0,    4,    0,    0,    0,    0,
   12,    0,   22,    0,    0,    0,    0,    5,    0,    0,
    0,   27,   24,   21,   23,    0,   72,   67,    0,    0,
    0,    0,   79,    0,    0,    0,    0,   71,    0,    0,
    0,    0,   25,   28,   37,   26,    0,   30,   31,   32,
   33,    0,    0,    0,    0,    0,    0,    0,   48,    0,
    0,    0,   46,    0,   47,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   29,   34,   35,   36,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   41,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   65,   66,    0,    0,   62,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   68,    0,    0,
   85,    0,    0,   44,    0,    0,   77,    0,    0,   69,
    0,    0,   70,   45,    0,    0,   80,   86,    0,   81,
    0,   78,
};
final static short yydgoto[] = {                          2,
    3,    4,   64,   21,   34,    8,   11,   23,   35,   36,
   65,   46,   66,   67,   68,   69,   70,   71,   72,   73,
   74,   83,   76,   85,   78,  155,   79,  123,  167,
};
final static short yysindex[] = {                      -253,
 -266,    0, -253,    0, -236,    0, -241,  -85,    0,    0,
  523,    0,    0,    0,    0, -230,    0,    6,    0,    0,
  -10,  -88,    0,    0,  -86,    0,   27,  -23,   41,    6,
    0,    6,    0,  -71,   44,   40,   45,    0,  -36,    6,
  -36,    0,    0,    0,    0,  326,    0,    0,   58,   60,
   61,   -3,    0,  431,   70,   77,   79,    0,  355,   -3,
   -3,  601,    0,    0,    0,    0,   64,    0,    0,    0,
    0,   66,   67,   68,   59,  438,    0, -148,    0,   -3,
   -3,   -3,    0,  438,    0,   96,   46,   -3,  105,  113,
 -124,  -25,  -25, -116,  106,    0,    0,    0,    0,   -3,
   -3,   -3,   -3,   -3,   -3,   -3,   -3,   -3,   -3,   -3,
   -3,   -3,   -3,    0,   -3,  123,  132,  108,  262,  124,
  639,  438,  -19,    0,    0,  130,  131,    0,  438,  491,
  470,  640,  640,  512,  512,  163,  163,  -25,  -25,  -25,
  640,  640,  359,   -3,  355,   -3,  355,    0,  383,   -3,
    0,   -3,   -3,    0,  135,  127,    0,  156,  -89,    0,
  438,  417,    0,    0,   -3,  355,    0,    0,  141,    0,
  355,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,  183,    0,   65,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  126,    0,    0,  146,
    0,  146,    0,    0,    0,  154,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  -58,    0,    0,    0,    0,
    0,  -57,    0,    0,    0,    0,    0,    0,  -58,  -81,
  -81,  -81,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  459,    0,   -2,    0,    0,  -81,
  -58,  -81,    0,  145,    0,    0,    0,  -81,    0,    0,
    0,    9,   35,    0,    0,    0,    0,    0,    0,  -81,
  -81,  -81,  -81,  -81,  -81,  -81,  -81,  -81,  -81,  -81,
  -81,  -81,  -81,    0,  -81,  -28,    0,    0,    0,    0,
  -81,   18,    0,    0,    0,    0,    0,    0,  -35,   91,
  285,   52,  284,  565,  606,  427,  611,   62,   71,   97,
  450,  483,    0,  -31,  -58,  -81,  -58,    0,    0,  -81,
    0,  -81,  -81,    0,    0,  166,    0,    0,  -33,    0,
   20,    0,    0,    0,  -18,  -58,    0,    0,    0,    0,
  -58,    0,
};
final static short yygindex[] = {                         0,
    0,  205,  200,   43,   37,    0,    0,    0,  180,    0,
  -12,    0,   15,  -77,    0,    0,    0,    0,    0,    0,
    0,  594,  818,  668,    0,    0,    0,   69,    0,
};
final static int YYTABLESIZE=971;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         82,
   40,   84,   28,  118,   28,   38,   82,    1,   43,   74,
    5,   82,   43,   43,   43,   43,   43,   43,   43,   28,
  114,  151,   40,   38,  150,   82,   43,    7,   45,   61,
   43,   43,   43,   43,   47,    9,   62,   10,   39,   47,
   47,   60,   47,   47,   47,   63,   24,   22,   26,   63,
   63,   63,   63,   63,   25,   63,   39,   47,   76,   47,
   75,   76,   43,   75,   43,  115,   30,   63,   63,   31,
   63,   64,   33,   91,   33,   64,   64,   64,   64,   64,
   32,   64,   44,   40,   39,   41,   42,  169,   47,   82,
   87,   82,   58,   64,   64,   58,   64,   80,   51,   81,
   82,   63,   51,   51,   51,   51,   51,   52,   51,   88,
   58,   52,   52,   52,   52,   52,   89,   52,   90,  100,
   51,   51,   96,   51,   97,   98,   99,   64,  116,   52,
   52,   60,   52,   53,   60,  120,  121,   53,   53,   53,
   53,   53,  111,   53,   58,  124,  128,  109,  107,   60,
  108,  114,  110,  125,   51,   53,   53,  126,   53,  157,
  127,  159,  144,   52,  148,  113,  146,  112,  111,  152,
  150,  153,  145,  109,  107,  164,  108,  114,  110,  166,
  170,  171,    1,   60,    5,  172,   20,   15,   27,   53,
   29,  113,  111,  112,   19,   42,  115,  109,  107,  111,
  108,  114,  110,   83,  109,   38,   73,    6,  114,  110,
   20,   37,  156,    0,  165,  113,    0,  112,   42,   42,
    0,    0,  115,   82,   82,   82,   82,   82,   82,   82,
    0,   82,   82,   82,   82,    0,   82,   82,   82,   82,
   82,   82,   82,   82,    0,   42,  115,   82,   82,   43,
   43,    0,    0,  115,   43,   43,   43,   43,   42,   47,
    0,   48,   12,   13,   14,   15,   16,   17,   54,    0,
   56,   57,   58,    0,    0,   47,   47,    0,    0,    0,
   47,   47,   47,   47,    0,    0,   63,   63,    0,    0,
    0,   63,   63,   63,   63,    0,    0,    0,  111,    0,
    0,    0,  147,  109,  107,    0,  108,  114,  110,    0,
    0,    0,   64,   64,    0,    0,    0,   64,   64,   64,
   64,  113,    0,  112,   59,   61,    0,   59,   61,   58,
   58,    0,    0,    0,    0,    0,   58,   58,    0,   51,
   51,    0,   59,   61,   51,   51,   51,   51,   52,   52,
    0,    0,  115,   52,   52,   52,   52,    0,   61,    0,
    0,    0,    0,    0,    0,   62,    0,    0,   60,   60,
   60,    0,    0,    0,   53,   53,   59,   61,    0,   53,
   53,   53,   53,  101,  102,    0,    0,   61,  103,  104,
  105,  106,    0,    0,   62,  111,    0,    0,    0,   60,
  109,  107,    0,  108,  114,  110,    0,    0,    0,  101,
  102,    0,    0,    0,  103,  104,  105,  106,  113,  111,
  112,    0,    0,    0,  109,  107,    0,  108,  114,  110,
    0,    0,    0,  101,  102,    0,    0,    0,  103,  104,
  105,  106,  113,    0,  112,    0,    0,    0,   42,  115,
   63,  154,    0,  111,    0,    0,    0,  168,  109,  107,
    0,  108,  114,  110,    0,    0,    0,   49,    0,   49,
   49,   49,    0,  115,  111,  160,  113,   42,  112,  109,
  107,    0,  108,  114,  110,   49,   49,    0,   49,    0,
   57,    0,    0,   57,    0,   46,    0,  113,    0,  112,
   46,   46,    0,   46,   46,   46,  111,  115,   57,    0,
    0,  109,  107,    0,  108,  114,  110,    0,   46,   49,
   46,    0,    0,   56,    0,    0,   56,  111,  115,  113,
    0,  112,  109,  107,    0,  108,  114,  110,    0,  101,
  102,   56,   57,    0,  103,  104,  105,  106,  111,   46,
  113,    0,  112,  109,  107,    0,  108,  114,  110,    0,
  115,   59,   59,   61,    0,    0,    0,    0,   59,   59,
    0,  113,    0,  112,    0,   56,    0,    0,    0,    0,
    0,  115,   12,   13,   14,   15,   16,   17,   47,    0,
   48,   49,   50,   51,    0,   52,   53,   54,   55,   56,
   57,   58,  115,    0,    0,   54,   59,    0,   54,    0,
    0,   12,   13,   14,   15,   16,   17,   47,    0,   48,
   49,   50,   51,   54,   52,   53,   54,   55,   56,   57,
   58,    0,    0,   61,    0,   59,  101,  102,    0,   75,
   62,  103,  104,  105,  106,   60,   55,   19,    0,   55,
    0,   50,   75,   50,   50,   50,    0,   54,    0,    0,
  101,  102,    0,    0,   55,  103,  104,  105,  106,   50,
   50,   61,   50,    0,   75,    0,  111,    0,   62,    0,
    0,  109,  107,   60,  108,  114,  110,   12,   13,   14,
   15,   16,   17,    0,  101,  102,    0,    0,   55,  103,
  104,  105,  106,   50,   49,   49,    0,   86,    0,   49,
   49,   49,   49,   77,    0,  101,  102,    0,    0,    0,
  103,  104,  105,  106,    0,    0,   77,   57,   57,    0,
  115,   31,    0,    0,   57,   57,   46,   46,   75,    0,
   75,   46,   46,   46,   46,    0,    0,  101,   77,    0,
    0,    0,  103,  104,  105,  106,    0,    0,   75,   75,
   56,   56,    0,    0,   75,    0,    0,   56,   56,    0,
    0,    0,    0,  103,  104,  105,  106,    0,    0,   12,
   13,   14,   15,   16,   17,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  103,  104,    0,    0,    0,    0,
    0,    0,   18,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   77,    0,   77,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   77,   77,    0,    0,    0,    0,   77,    0,
    0,    0,   54,   54,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   94,    0,   47,    0,   48,    0,    0,    0,   84,
    0,    0,   54,    0,   56,   57,   58,   92,   93,   95,
    0,    0,    0,   55,   55,    0,    0,    0,   50,   50,
    0,    0,    0,   50,   50,   50,   50,  117,    0,  119,
    0,   47,    0,   48,    0,  122,    0,    0,    0,    0,
   54,    0,   56,   57,   58,    0,    0,  129,  130,  131,
  132,  133,  134,  135,  136,  137,  138,  139,  140,  141,
  142,    0,  143,    0,    0,    0,    0,    0,  149,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  122,    0,  158,    0,    0,    0,  161,    0,  162,
  163,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         33,
   59,   59,   91,   81,   91,   41,   40,  261,   37,   41,
  277,   45,   41,   42,   43,   44,   45,   46,   47,   91,
   46,   41,   41,   59,   44,   59,   39,  264,   41,   33,
   59,   60,   61,   62,   37,  277,   40,  123,   41,   42,
   43,   45,   45,   46,   47,   37,  277,   11,   59,   41,
   42,   43,   44,   45,   18,   47,   59,   60,   41,   62,
   41,   44,   91,   44,   93,   91,   40,   59,   60,   93,
   62,   37,   30,   59,   32,   41,   42,   43,   44,   45,
   40,   47,   40,   44,   41,   41,  123,  165,   91,  123,
   54,  125,   41,   59,   60,   44,   62,   40,   37,   40,
   40,   93,   41,   42,   43,   44,   45,   37,   47,   40,
   59,   41,   42,   43,   44,   45,   40,   47,   40,   61,
   59,   60,   59,   62,   59,   59,   59,   93,  277,   59,
   60,   41,   62,   37,   44,   40,   91,   41,   42,   43,
   44,   45,   37,   47,   93,   41,   41,   42,   43,   59,
   45,   46,   47,   41,   93,   59,   60,  282,   62,  145,
  277,  147,   40,   93,   41,   60,   59,   62,   37,   40,
   44,   41,   41,   42,   43,   41,   45,   46,   47,  269,
  166,   41,    0,   93,   59,  171,   41,  123,  277,   93,
  277,   60,   37,   62,   41,  277,   91,   42,   43,   37,
   45,   46,   47,   59,   42,  277,   41,    3,   46,   47,
   11,   32,  144,   -1,   59,   60,   -1,   62,  277,  277,
   -1,   -1,   91,  257,  258,  259,  260,  261,  262,  263,
   -1,  265,  266,  267,  268,   -1,  270,  271,  272,  273,
  274,  275,  276,  277,   -1,  277,   91,  281,  282,  278,
  279,   -1,   -1,   91,  283,  284,  285,  286,  277,  263,
   -1,  265,  257,  258,  259,  260,  261,  262,  272,   -1,
  274,  275,  276,   -1,   -1,  278,  279,   -1,   -1,   -1,
  283,  284,  285,  286,   -1,   -1,  278,  279,   -1,   -1,
   -1,  283,  284,  285,  286,   -1,   -1,   -1,   37,   -1,
   -1,   -1,   41,   42,   43,   -1,   45,   46,   47,   -1,
   -1,   -1,  278,  279,   -1,   -1,   -1,  283,  284,  285,
  286,   60,   -1,   62,   41,   41,   -1,   44,   44,  278,
  279,   -1,   -1,   -1,   -1,   -1,  285,  286,   -1,  278,
  279,   -1,   59,   59,  283,  284,  285,  286,  278,  279,
   -1,   -1,   91,  283,  284,  285,  286,   -1,   33,   -1,
   -1,   -1,   -1,   -1,   -1,   40,   -1,   -1,  278,  279,
   45,   -1,   -1,   -1,  278,  279,   93,   93,   -1,  283,
  284,  285,  286,  278,  279,   -1,   -1,   33,  283,  284,
  285,  286,   -1,   -1,   40,   37,   -1,   -1,   -1,   45,
   42,   43,   -1,   45,   46,   47,   -1,   -1,   -1,  278,
  279,   -1,   -1,   -1,  283,  284,  285,  286,   60,   37,
   62,   -1,   -1,   -1,   42,   43,   -1,   45,   46,   47,
   -1,   -1,   -1,  278,  279,   -1,   -1,   -1,  283,  284,
  285,  286,   60,   -1,   62,   -1,   -1,   -1,  123,   91,
  125,   93,   -1,   37,   -1,   -1,   -1,   41,   42,   43,
   -1,   45,   46,   47,   -1,   -1,   -1,   41,   -1,   43,
   44,   45,   -1,   91,   37,   93,   60,  123,   62,   42,
   43,   -1,   45,   46,   47,   59,   60,   -1,   62,   -1,
   41,   -1,   -1,   44,   -1,   37,   -1,   60,   -1,   62,
   42,   43,   -1,   45,   46,   47,   37,   91,   59,   -1,
   -1,   42,   43,   -1,   45,   46,   47,   -1,   60,   93,
   62,   -1,   -1,   41,   -1,   -1,   44,   37,   91,   60,
   -1,   62,   42,   43,   -1,   45,   46,   47,   -1,  278,
  279,   59,   93,   -1,  283,  284,  285,  286,   37,   91,
   60,   -1,   62,   42,   43,   -1,   45,   46,   47,   -1,
   91,  278,  279,  279,   -1,   -1,   -1,   -1,  285,  286,
   -1,   60,   -1,   62,   -1,   93,   -1,   -1,   -1,   -1,
   -1,   91,  257,  258,  259,  260,  261,  262,  263,   -1,
  265,  266,  267,  268,   -1,  270,  271,  272,  273,  274,
  275,  276,   91,   -1,   -1,   41,  281,   -1,   44,   -1,
   -1,  257,  258,  259,  260,  261,  262,  263,   -1,  265,
  266,  267,  268,   59,  270,  271,  272,  273,  274,  275,
  276,   -1,   -1,   33,   -1,  281,  278,  279,   -1,   46,
   40,  283,  284,  285,  286,   45,   41,  125,   -1,   44,
   -1,   41,   59,   43,   44,   45,   -1,   93,   -1,   -1,
  278,  279,   -1,   -1,   59,  283,  284,  285,  286,   59,
   60,   33,   62,   -1,   81,   -1,   37,   -1,   40,   -1,
   -1,   42,   43,   45,   45,   46,   47,  257,  258,  259,
  260,  261,  262,   -1,  278,  279,   -1,   -1,   93,  283,
  284,  285,  286,   93,  278,  279,   -1,  277,   -1,  283,
  284,  285,  286,   46,   -1,  278,  279,   -1,   -1,   -1,
  283,  284,  285,  286,   -1,   -1,   59,  278,  279,   -1,
   91,   93,   -1,   -1,  285,  286,  278,  279,  145,   -1,
  147,  283,  284,  285,  286,   -1,   -1,  278,   81,   -1,
   -1,   -1,  283,  284,  285,  286,   -1,   -1,  165,  166,
  278,  279,   -1,   -1,  171,   -1,   -1,  285,  286,   -1,
   -1,   -1,   -1,  283,  284,  285,  286,   -1,   -1,  257,
  258,  259,  260,  261,  262,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  283,  284,   -1,   -1,   -1,   -1,
   -1,   -1,  280,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  145,   -1,  147,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  165,  166,   -1,   -1,   -1,   -1,  171,   -1,
   -1,   -1,  278,  279,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  261,   -1,  263,   -1,  265,   -1,   -1,   -1,   52,
   -1,   -1,  272,   -1,  274,  275,  276,   60,   61,   62,
   -1,   -1,   -1,  278,  279,   -1,   -1,   -1,  278,  279,
   -1,   -1,   -1,  283,  284,  285,  286,   80,   -1,   82,
   -1,  263,   -1,  265,   -1,   88,   -1,   -1,   -1,   -1,
  272,   -1,  274,  275,  276,   -1,   -1,  100,  101,  102,
  103,  104,  105,  106,  107,  108,  109,  110,  111,  112,
  113,   -1,  115,   -1,   -1,   -1,   -1,   -1,  121,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  144,   -1,  146,   -1,   -1,   -1,  150,   -1,  152,
  153,
};
}
final static short YYFINAL=2;
final static short YYMAXTOKEN=288;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,"'!'",null,null,null,"'%'",null,null,"'('","')'","'*'","'+'",
"','","'-'","'.'","'/'",null,null,null,null,null,null,null,null,null,null,null,
"';'","'<'","'='","'>'",null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,"'['",null,"']'",null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,"'{'",null,"'}'",null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,"VOID","BOOL","INT","STRING",
"CLASS","DOUBLE","NULL","EXTENDS","THIS","WHILE","FOR","IF","ELSE","RETURN",
"BREAK","NEW","PRINT","READ_INTEGER","READ_LINE","LITERAL","IDENTIFIER","AND",
"OR","STATIC","REPEAT","UNTIL","LESS_EQUAL","GREATER_EQUAL","EQUAL","NOT_EQUAL",
"UMINUS","EMPTY",
};
final static String yyrule[] = {
"$accept : Program",
"Program : ClassList",
"ClassList : ClassList ClassDef",
"ClassList : ClassDef",
"VariableDef : Variable ';'",
"Variable : Type IDENTIFIER",
"Type : INT",
"Type : DOUBLE",
"Type : VOID",
"Type : BOOL",
"Type : STRING",
"Type : CLASS IDENTIFIER",
"Type : Type '[' ']'",
"ClassDef : CLASS IDENTIFIER ExtendsClause '{' FieldList '}'",
"ExtendsClause : EXTENDS IDENTIFIER",
"ExtendsClause :",
"FieldList : FieldList VariableDef",
"FieldList : FieldList FunctionDef",
"FieldList :",
"Formals : VariableList",
"Formals :",
"VariableList : VariableList ',' Variable",
"VariableList : Variable",
"FunctionDef : STATIC Type IDENTIFIER '(' Formals ')' StmtBlock",
"FunctionDef : Type IDENTIFIER '(' Formals ')' StmtBlock",
"StmtBlock : '{' StmtList '}'",
"StmtList : StmtList Stmt",
"StmtList :",
"Stmt : VariableDef",
"Stmt : SimpleStmt ';'",
"Stmt : IfStmt",
"Stmt : WhileStmt",
"Stmt : RepeatStmt",
"Stmt : ForStmt",
"Stmt : ReturnStmt ';'",
"Stmt : PrintStmt ';'",
"Stmt : BreakStmt ';'",
"Stmt : StmtBlock",
"SimpleStmt : LValue '=' Expr",
"SimpleStmt : Call",
"SimpleStmt :",
"Receiver : Expr '.'",
"Receiver :",
"LValue : Receiver IDENTIFIER",
"LValue : Expr '[' Expr ']'",
"Call : Receiver IDENTIFIER '(' Actuals ')'",
"Expr : LValue",
"Expr : Call",
"Expr : Constant",
"Expr : Expr '+' Expr",
"Expr : Expr '-' Expr",
"Expr : Expr '*' Expr",
"Expr : Expr '/' Expr",
"Expr : Expr '%' Expr",
"Expr : Expr EQUAL Expr",
"Expr : Expr NOT_EQUAL Expr",
"Expr : Expr '<' Expr",
"Expr : Expr '>' Expr",
"Expr : Expr LESS_EQUAL Expr",
"Expr : Expr GREATER_EQUAL Expr",
"Expr : Expr AND Expr",
"Expr : Expr OR Expr",
"Expr : '(' Expr ')'",
"Expr : '-' Expr",
"Expr : '!' Expr",
"Expr : READ_INTEGER '(' ')'",
"Expr : READ_LINE '(' ')'",
"Expr : THIS",
"Expr : NEW IDENTIFIER '(' ')'",
"Expr : NEW Type '[' Expr ']'",
"Expr : '(' CLASS IDENTIFIER ')' Expr",
"Constant : LITERAL",
"Constant : NULL",
"Actuals : ExprList",
"Actuals :",
"ExprList : ExprList ',' Expr",
"ExprList : Expr",
"WhileStmt : WHILE '(' Expr ')' Stmt",
"ForStmt : FOR '(' SimpleStmt ';' Expr ';' SimpleStmt ')' Stmt",
"BreakStmt : BREAK",
"IfStmt : IF '(' Expr ')' Stmt ElseClause",
"ElseClause : ELSE Stmt",
"ElseClause :",
"ReturnStmt : RETURN Expr",
"ReturnStmt : RETURN",
"PrintStmt : PRINT '(' ExprList ')'",
"RepeatStmt : REPEAT Stmt UNTIL '(' Expr ')'",
};

//#line 433 "Parser.y"
    
	/**
	 * 打印当前归约所用的语法规则<br>
	 * 请勿修改。
	 */
    public boolean onReduce(String rule) {
		if (rule.startsWith("$$"))
			return false;
		else
			rule = rule.replaceAll(" \\$\\$\\d+", "");

   	    if (rule.endsWith(":"))
    	    System.out.println(rule + " <empty>");
   	    else
			System.out.println(rule);
		return false;
    }
    
    public void diagnose() {
		addReduceListener(this);
		yyparse();
	}
//#line 576 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    //if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      //if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        //if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        //if (yychar < 0)    //it it didn't work/error
        //  {
        //  yychar = 0;      //change it to default string (no -1!)
          //if (yydebug)
          //  yylexdebug(yystate,yychar);
        //  }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        //if (yydebug)
          //debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      //if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0 || valptr<0)   //check for under & overflow here
            {
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            //if (yydebug)
              //debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            //if (yydebug)
              //debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0 || valptr<0)   //check for under & overflow here
              {
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        //if (yydebug)
          //{
          //yys = null;
          //if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          //if (yys == null) yys = "illegal-symbol";
          //debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          //}
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    //if (yydebug)
      //debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    if (reduceListener == null || reduceListener.onReduce(yyrule[yyn])) // if intercepted!
      switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 1:
//#line 53 "Parser.y"
{
						tree = new Tree.TopLevel(val_peek(0).clist, val_peek(0).loc);
					}
break;
case 2:
//#line 62 "Parser.y"
{
						yyval.clist.add(val_peek(0).cdef);
					}
break;
case 3:
//#line 66 "Parser.y"
{
                		yyval.clist = new ArrayList<Tree.ClassDef>();
                		yyval.clist.add(val_peek(0).cdef);
                	}
break;
case 5:
//#line 76 "Parser.y"
{
						yyval.vdef = new Tree.VarDef(val_peek(0).ident, val_peek(1).type, val_peek(0).loc);
					}
break;
case 6:
//#line 82 "Parser.y"
{
						yyval.type = new Tree.TypeIdent(Tree.INT, val_peek(0).loc);
					}
break;
case 7:
//#line 86 "Parser.y"
{
						yyval.type = new Tree.TypeIdent(Tree.DOUBLE, val_peek(0).loc);
					}
break;
case 8:
//#line 90 "Parser.y"
{
                		yyval.type = new Tree.TypeIdent(Tree.VOID, val_peek(0).loc);
                	}
break;
case 9:
//#line 94 "Parser.y"
{
                		yyval.type = new Tree.TypeIdent(Tree.BOOL, val_peek(0).loc);
                	}
break;
case 10:
//#line 98 "Parser.y"
{
                		yyval.type = new Tree.TypeIdent(Tree.STRING, val_peek(0).loc);
                	}
break;
case 11:
//#line 102 "Parser.y"
{
                		yyval.type = new Tree.TypeClass(val_peek(0).ident, val_peek(1).loc);
                	}
break;
case 12:
//#line 106 "Parser.y"
{
                		yyval.type = new Tree.TypeArray(val_peek(2).type, val_peek(2).loc);
                	}
break;
case 13:
//#line 112 "Parser.y"
{
						yyval.cdef = new Tree.ClassDef(val_peek(4).ident, val_peek(3).ident, val_peek(1).flist, val_peek(5).loc);
					}
break;
case 14:
//#line 118 "Parser.y"
{
						yyval.ident = val_peek(0).ident;
					}
break;
case 15:
//#line 122 "Parser.y"
{
                		yyval = new SemValue();
                	}
break;
case 16:
//#line 128 "Parser.y"
{
						yyval.flist.add(val_peek(0).vdef);
					}
break;
case 17:
//#line 132 "Parser.y"
{
						yyval.flist.add(val_peek(0).fdef);
					}
break;
case 18:
//#line 136 "Parser.y"
{
                		yyval = new SemValue();
                		yyval.flist = new ArrayList<Tree>();
                	}
break;
case 20:
//#line 144 "Parser.y"
{
                		yyval = new SemValue();
                		yyval.vlist = new ArrayList<Tree.VarDef>(); 
                	}
break;
case 21:
//#line 151 "Parser.y"
{
						yyval.vlist.add(val_peek(0).vdef);
					}
break;
case 22:
//#line 155 "Parser.y"
{
                		yyval.vlist = new ArrayList<Tree.VarDef>();
						yyval.vlist.add(val_peek(0).vdef);
                	}
break;
case 23:
//#line 162 "Parser.y"
{
						yyval.fdef = new MethodDef(true, val_peek(4).ident, val_peek(5).type, val_peek(2).vlist, (Block) val_peek(0).stmt, val_peek(4).loc);
					}
break;
case 24:
//#line 166 "Parser.y"
{
						yyval.fdef = new MethodDef(false, val_peek(4).ident, val_peek(5).type, val_peek(2).vlist, (Block) val_peek(0).stmt, val_peek(4).loc);
					}
break;
case 25:
//#line 172 "Parser.y"
{
						yyval.stmt = new Block(val_peek(1).slist, val_peek(2).loc);
					}
break;
case 26:
//#line 178 "Parser.y"
{
						yyval.slist.add(val_peek(0).stmt);
					}
break;
case 27:
//#line 182 "Parser.y"
{
                		yyval = new SemValue();
                		yyval.slist = new ArrayList<Tree>();
                	}
break;
case 28:
//#line 189 "Parser.y"
{
						yyval.stmt = val_peek(0).vdef;
					}
break;
case 29:
//#line 194 "Parser.y"
{
                		if (yyval.stmt == null) {
                			yyval.stmt = new Tree.Skip(val_peek(0).loc);
                		}
                	}
break;
case 38:
//#line 210 "Parser.y"
{
						yyval.stmt = new Tree.Assign(val_peek(2).lvalue, val_peek(0).expr, val_peek(1).loc);
					}
break;
case 39:
//#line 214 "Parser.y"
{
                		yyval.stmt = new Tree.Exec(val_peek(0).expr, val_peek(0).loc);
                	}
break;
case 40:
//#line 218 "Parser.y"
{
                		yyval = new SemValue();
                	}
break;
case 42:
//#line 225 "Parser.y"
{
                		yyval = new SemValue();
                	}
break;
case 43:
//#line 231 "Parser.y"
{
						yyval.lvalue = new Tree.Ident(val_peek(1).expr, val_peek(0).ident, val_peek(0).loc);
						if (val_peek(1).loc == null) {
							yyval.loc = val_peek(0).loc;
						}
					}
break;
case 44:
//#line 238 "Parser.y"
{
                		yyval.lvalue = new Tree.Indexed(val_peek(3).expr, val_peek(1).expr, val_peek(3).loc);
                	}
break;
case 45:
//#line 244 "Parser.y"
{
						yyval.expr = new Tree.CallExpr(val_peek(4).expr, val_peek(3).ident, val_peek(1).elist, val_peek(3).loc);
						if (val_peek(4).loc == null) {
							yyval.loc = val_peek(3).loc;
						}
					}
break;
case 46:
//#line 253 "Parser.y"
{
						yyval.expr = val_peek(0).lvalue;
					}
break;
case 49:
//#line 259 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.PLUS, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 50:
//#line 263 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.MINUS, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 51:
//#line 267 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.MUL, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 52:
//#line 271 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.DIV, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 53:
//#line 275 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.MOD, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 54:
//#line 279 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.EQ, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 55:
//#line 283 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.NE, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 56:
//#line 287 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.LT, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 57:
//#line 291 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.GT, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 58:
//#line 295 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.LE, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 59:
//#line 299 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.GE, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 60:
//#line 303 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.AND, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 61:
//#line 307 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.OR, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 62:
//#line 311 "Parser.y"
{
                		yyval = val_peek(1);
                	}
break;
case 63:
//#line 315 "Parser.y"
{
                		yyval.expr = new Tree.Unary(Tree.NEG, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 64:
//#line 319 "Parser.y"
{
                		yyval.expr = new Tree.Unary(Tree.NOT, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 65:
//#line 323 "Parser.y"
{
                		yyval.expr = new Tree.ReadIntExpr(val_peek(2).loc);
                	}
break;
case 66:
//#line 327 "Parser.y"
{
                		yyval.expr = new Tree.ReadLineExpr(val_peek(2).loc);
                	}
break;
case 67:
//#line 331 "Parser.y"
{
                		yyval.expr = new Tree.ThisExpr(val_peek(0).loc);
                	}
break;
case 68:
//#line 335 "Parser.y"
{
                		yyval.expr = new Tree.NewClass(val_peek(2).ident, val_peek(3).loc);
                	}
break;
case 69:
//#line 339 "Parser.y"
{
                		yyval.expr = new Tree.NewArray(val_peek(3).type, val_peek(1).expr, val_peek(4).loc);
                	}
break;
case 70:
//#line 343 "Parser.y"
{
                		yyval.expr = new Tree.TypeCast(val_peek(2).ident, val_peek(0).expr, val_peek(0).loc);
                	}
break;
case 71:
//#line 349 "Parser.y"
{
						yyval.expr = new Tree.Literal(val_peek(0).typeTag, val_peek(0).literal, val_peek(0).loc);
					}
break;
case 72:
//#line 353 "Parser.y"
{
						yyval.expr = new Null(val_peek(0).loc);
					}
break;
case 74:
//#line 360 "Parser.y"
{
                		yyval = new SemValue();
                		yyval.elist = new ArrayList<Tree.Expr>();
                	}
break;
case 75:
//#line 367 "Parser.y"
{
						yyval.elist.add(val_peek(0).expr);
					}
break;
case 76:
//#line 371 "Parser.y"
{
                		yyval.elist = new ArrayList<Tree.Expr>();
						yyval.elist.add(val_peek(0).expr);
                	}
break;
case 77:
//#line 378 "Parser.y"
{
						yyval.stmt = new Tree.WhileLoop(val_peek(2).expr, val_peek(0).stmt, val_peek(4).loc);
					}
break;
case 78:
//#line 384 "Parser.y"
{
						yyval.stmt = new Tree.ForLoop(val_peek(6).stmt, val_peek(4).expr, val_peek(2).stmt, val_peek(0).stmt, val_peek(8).loc);
					}
break;
case 79:
//#line 390 "Parser.y"
{
						yyval.stmt = new Tree.Break(val_peek(0).loc);
					}
break;
case 80:
//#line 396 "Parser.y"
{
						yyval.stmt = new Tree.If(val_peek(3).expr, val_peek(1).stmt, val_peek(0).stmt, val_peek(5).loc);
					}
break;
case 81:
//#line 402 "Parser.y"
{
						yyval.stmt = val_peek(0).stmt;
					}
break;
case 82:
//#line 406 "Parser.y"
{
						yyval = new SemValue();
					}
break;
case 83:
//#line 412 "Parser.y"
{
						yyval.stmt = new Tree.Return(val_peek(0).expr, val_peek(1).loc);
					}
break;
case 84:
//#line 416 "Parser.y"
{
                		yyval.stmt = new Tree.Return(null, val_peek(0).loc);
                	}
break;
case 85:
//#line 422 "Parser.y"
{
						yyval.stmt = new Print(val_peek(1).elist, val_peek(3).loc);
					}
break;
case 86:
//#line 428 "Parser.y"
{
						yyval.stmt = new Tree.RepeatLoop(val_peek(1).expr, val_peek(4).stmt, val_peek(5).loc);
					}
break;
//#line 1169 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    //if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      //if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        //if (yychar<0) yychar=0;  //clean, if necessary
        //if (yydebug)
          //yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      //if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
//## The -Jnorun option was used ##
//## end of method run() ########################################



//## Constructors ###############################################
//## The -Jnoconstruct option was used ##
//###############################################################



}
//################### END OF CLASS ##############################
