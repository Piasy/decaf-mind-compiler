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
    0,    1,    1,    2,    3,    3,    4,    4,    4,    5,
    7,    8,    8,    8,    8,    8,    8,    8,    6,    6,
    9,    9,   11,   11,   10,   12,   12,   13,   13,   13,
   13,   13,   13,   13,   13,   13,   13,   14,   14,   14,
   22,   22,   25,   25,   23,   23,   23,   23,   23,   23,
   23,   23,   23,   23,   23,   23,   23,   23,   23,   23,
   23,   23,   23,   23,   23,   23,   23,   23,   23,   24,
   27,   27,   28,   28,   26,   26,   18,   29,   16,   21,
   15,   30,   30,   19,   19,   20,   17,
};
final static short yylen[] = {                            2,
    1,    2,    1,    6,    2,    0,    2,    2,    0,    2,
    2,    1,    1,    1,    1,    1,    2,    3,    7,    6,
    1,    0,    3,    1,    3,    2,    0,    1,    2,    1,
    1,    1,    1,    2,    2,    2,    1,    3,    1,    0,
    2,    4,    2,    0,    1,    1,    1,    3,    3,    3,
    3,    3,    2,    2,    3,    3,    3,    3,    3,    3,
    3,    3,    1,    3,    3,    4,    5,    5,    3,    5,
    1,    0,    3,    1,    1,    1,    9,    1,    5,    1,
    6,    2,    0,    2,    1,    4,    6,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    3,    0,    2,    0,    0,    5,    9,
    0,   16,   14,   12,   15,    0,   13,    0,    4,    7,
    8,    0,    0,   17,    0,   10,    0,    0,    0,    0,
   18,    0,   24,    0,    0,    0,    0,   11,    0,    0,
    0,   27,   20,   23,   19,    0,   76,   63,    0,    0,
    0,    0,   80,    0,    0,    0,    0,   75,    0,    0,
    0,    0,   25,   28,   37,   26,    0,   30,   31,   32,
   33,    0,    0,    0,    0,    0,    0,    0,   47,    0,
    0,    0,   45,    0,   46,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   29,   34,   35,   36,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   43,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   64,   65,    0,    0,   69,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   66,    0,    0,
   86,    0,    0,   42,    0,    0,   79,    0,    0,    0,
   67,    0,    0,   68,   70,    0,    0,   81,   87,    0,
   82,    0,   77,
};
final static short yydgoto[] = {                          2,
    3,    4,    8,   11,   64,   21,   22,   34,   35,   65,
   36,   46,   66,   67,   68,   69,   70,   71,   72,   73,
   74,   83,   76,   85,   78,   79,  155,  123,  159,  168,
};
final static short yysindex[] = {                      -253,
 -255,    0, -253,    0, -236,    0, -248,  -62,    0,    0,
  450,    0,    0,    0,    0, -241,    0,  420,    0,    0,
    0,   15,  -88,    0,  -87,    0,   41,   -9,   45,  420,
    0,  420,    0,  -85,   46,   42,   47,    0,  -23,  420,
  -23,    0,    0,    0,    0,  297,    0,    0,   58,   61,
   77,   -3,    0,  266,   79,   80,   83,    0,  221,   -3,
   -3,  606,    0,    0,    0,    0,   66,    0,    0,    0,
    0,   67,   70,   76,   85,  262,    0, -136,    0,   -3,
   -3,   -3,    0,  262,    0,  107,   57,   -3,  108,  110,
 -129,   90,  -25, -123,   97,    0,    0,    0,    0,   -3,
   -3,   -3,   -3,   -3,   -3,   -3,   -3,   -3,   -3,   -3,
   -3,   -3,   -3,    0,   -3,  116,  124,   99,  154,  119,
  623,  262,  -17,    0,    0,  122,  127,    0,  262,  415,
  408,  161,  161,  470,  470,   90,   90,  -25,  -25,  -25,
  161,  161,  324,   -3,  221,   -3,  221,    0,  351,   -3,
    0,   -3,   -3,    0,  132,  130,    0,  262,  104,  -94,
    0,  262,  135,    0,    0,   -3,  221,    0,    0,  137,
    0,  221,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,  179,    0,   59,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  121,    0,    0,  142,
    0,  142,    0,    0,    0,  144,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  -58,    0,    0,    0,    0,
    0,  -57,    0,    0,    0,    0,    0,    0,  -58,  -90,
  -90,  -90,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  381,    0,   -2,    0,    0,  -90,
  -58,  -90,    0,  134,    0,    0,    0,  -90,    0,    0,
    0,  460,    9,    0,    0,    0,    0,    0,    0,  -90,
  -90,  -90,  -90,  -90,  -90,  -90,  -90,  -90,  -90,  -90,
  -90,  -90,  -90,    0,  -90,  -28,    0,    0,    0,    0,
  -90,   18,    0,    0,    0,    0,    0,    0,  -36,  405,
   52,  533,  535,  583,  611,  490,  552,   35,   62,   71,
  547,  579,    0,  -31,  -58,  -90,  -58,    0,    0,  -90,
    0,  -90,  -90,    0,    0,  153,    0,  -21,    0,  -33,
    0,   26,    0,    0,    0,  -30,  -58,    0,    0,    0,
    0,  -58,    0,
};
final static short yygindex[] = {                         0,
    0,  199,    0,    0,  194,    0,   43,   37,  177,    8,
    0,    0,    5,  -56,    0,    0,    0,    0,    0,    0,
    0,  615,  814,  643,    0,    0,    0,   68,   65,    0,
};
final static int YYTABLESIZE=967;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         83,
   40,   85,   28,   28,   38,   28,   83,    1,   41,   72,
   40,   83,   41,   41,   41,   41,   41,   41,   41,   78,
  114,    5,   38,  151,  118,   83,  150,    7,    9,   61,
   41,   41,   41,   41,   46,   24,   62,   78,   39,   46,
   46,   60,   46,   46,   46,   54,   43,   23,   45,   54,
   54,   54,   54,   54,   25,   54,   39,   46,   74,   46,
   10,   74,   41,   91,   41,  115,   73,   54,   54,   73,
   54,   50,   33,   26,   33,   50,   50,   50,   50,   50,
   30,   50,   44,   31,   32,   40,   39,   41,   46,   83,
   87,   83,   62,   50,   50,   62,   50,   80,   51,   42,
   81,   54,   51,   51,   51,   51,   51,   52,   51,  170,
   62,   52,   52,   52,   52,   52,   82,   52,   88,   89,
   51,   51,   90,   51,   96,   97,  111,   50,   98,   52,
   52,  109,   52,  111,   99,  114,  110,  128,  109,  107,
  116,  108,  114,  110,   62,  100,  120,  121,  124,  157,
  125,  160,  126,  127,   51,  144,  113,  146,  112,  148,
  111,  152,  166,   52,  145,  109,  107,  153,  108,  114,
  110,  171,  165,  150,  167,  169,  173,  172,    1,   11,
  115,    6,   22,  113,   21,  112,   44,  115,   27,   29,
  111,   38,   84,   71,  147,  109,  107,  111,  108,  114,
  110,    6,  109,  107,   20,  108,  114,  110,   37,    0,
    0,  156,    0,  113,  115,  112,  163,    0,   44,   44,
    0,    0,    0,   83,   83,   83,   83,   83,   83,   83,
    0,   83,   83,   83,   83,    0,   83,   83,   83,   83,
   83,   83,   83,   83,  115,   44,   44,   83,   83,   41,
   41,  115,    0,   61,   41,   41,   41,   41,    0,   47,
   62,   48,    0,    0,    0,   60,    0,    0,   54,    0,
   56,   57,   58,    0,    0,   46,   46,    0,    0,    0,
   46,   46,   46,   46,    0,    0,   54,   54,    0,    0,
    0,   54,   54,   54,   54,    0,    0,    0,  111,    0,
    0,    0,    0,  109,  107,    0,  108,  114,  110,    0,
    0,    0,   50,   50,    0,    0,    0,   50,   50,   50,
   50,  113,    0,  112,    0,    0,    0,    0,    0,   61,
   62,    0,    0,    0,    0,    0,   62,    0,    0,   51,
   51,   60,    0,   42,   51,   51,   51,   51,   52,   52,
    0,    0,  115,   52,   52,   52,   52,    0,    0,    0,
  111,    0,    0,    0,    0,  109,  107,    0,  108,  114,
  110,    0,    0,    0,  101,  102,    0,    0,    0,  103,
  104,  105,  106,  113,    0,  112,    0,  111,    0,    0,
    0,    0,  109,  107,    0,  108,  114,  110,    0,    0,
    0,  101,  102,    0,    0,    0,  103,  104,  105,  106,
  113,    0,  112,    0,  115,    0,  154,   45,    0,   42,
    0,   63,   45,   45,    0,   45,   45,   45,    0,    0,
    0,  101,  102,    0,    0,    0,  103,  104,  105,  106,
   45,  115,   45,  161,  111,   61,    0,    0,   61,  109,
  107,  111,  108,  114,  110,    0,  109,  107,    0,  108,
  114,  110,    0,   61,    0,    0,    0,  113,    0,  112,
    0,   45,    0,    0,  113,    0,  112,   12,   13,   14,
   15,   16,   17,   47,    0,   48,   49,   50,   51,    0,
   52,   53,   54,   55,   56,   57,   58,   61,  115,    0,
   53,   59,   53,   53,   53,  115,  111,    0,    0,    0,
    0,  109,  107,    0,  108,  114,  110,    0,   53,   53,
    0,   53,   12,   13,   14,   15,   16,   17,    0,  113,
   48,  112,   48,   48,   48,    0,    0,    0,    0,  101,
  102,    0,   86,    0,  103,  104,  105,  106,   48,   48,
    0,   48,   53,   12,   13,   14,   15,   16,   17,   47,
  115,   48,   49,   50,   51,    0,   52,   53,   54,   55,
   56,   57,   58,   56,   19,   58,   56,   59,   58,    0,
    0,    0,   48,    0,    0,    0,    0,   57,    0,    0,
   57,   56,   49,   58,   49,   49,   49,    0,    0,    0,
    0,  101,  102,    0,    0,   57,  103,  104,  105,  106,
   49,   49,    0,   49,    0,    0,    0,    0,    0,   55,
    0,    0,   55,   59,    0,   56,   59,   58,  101,  102,
    0,    0,    0,  103,  104,  105,  106,   55,   61,   57,
    0,   59,    0,    0,   49,   62,    0,    0,    0,    0,
   60,   60,    0,    0,   60,   61,    0,    0,   45,   45,
   75,    0,   62,   45,   45,   45,   45,   60,    0,   60,
    0,   55,    0,   75,    0,   59,   12,   13,   14,   15,
   16,   17,   61,   61,    0,  101,    0,    0,   77,    0,
  103,  104,  105,  106,    0,   75,    0,  103,  104,  105,
  106,   77,    0,   60,    0,    0,   12,   13,   14,   15,
   16,   17,    0,    0,    0,   31,    0,    0,    0,    0,
    0,    0,    0,   77,    0,    0,    0,    0,    0,   18,
    0,    0,    0,    0,    0,    0,    0,   53,   53,    0,
    0,    0,   53,   53,   53,   53,    0,    0,    0,    0,
    0,    0,  103,  104,    0,    0,    0,    0,    0,   75,
    0,   75,    0,    0,    0,    0,    0,   48,   48,    0,
    0,    0,   48,   48,   48,   48,    0,    0,    0,    0,
   75,   75,    0,    0,    0,    0,   75,   77,    0,   77,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   77,   77,
   56,   56,   58,   58,   77,    0,    0,   56,   56,   58,
   58,    0,    0,    0,   57,   57,    0,    0,    0,   49,
   49,   57,   57,    0,   49,   49,   49,   49,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   55,   55,    0,    0,
   59,   59,    0,   55,   55,   84,   94,    0,   47,    0,
   48,    0,    0,   92,   93,   95,    0,   54,    0,   56,
   57,   58,    0,    0,    0,   47,    0,   48,   60,   60,
    0,    0,    0,  117,   54,  119,   56,   57,   58,    0,
    0,  122,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  129,  130,  131,  132,  133,  134,  135,
  136,  137,  138,  139,  140,  141,  142,    0,  143,    0,
    0,    0,    0,    0,  149,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  122,    0,  158,
    0,    0,    0,  162,    0,  158,  164,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         33,
   59,   59,   91,   91,   41,   91,   40,  261,   37,   41,
   41,   45,   41,   42,   43,   44,   45,   46,   47,   41,
   46,  277,   59,   41,   81,   59,   44,  264,  277,   33,
   59,   60,   61,   62,   37,  277,   40,   59,   41,   42,
   43,   45,   45,   46,   47,   37,   39,   11,   41,   41,
   42,   43,   44,   45,   18,   47,   59,   60,   41,   62,
  123,   44,   91,   59,   93,   91,   41,   59,   60,   44,
   62,   37,   30,   59,   32,   41,   42,   43,   44,   45,
   40,   47,   40,   93,   40,   44,   41,   41,   91,  123,
   54,  125,   41,   59,   60,   44,   62,   40,   37,  123,
   40,   93,   41,   42,   43,   44,   45,   37,   47,  166,
   59,   41,   42,   43,   44,   45,   40,   47,   40,   40,
   59,   60,   40,   62,   59,   59,   37,   93,   59,   59,
   60,   42,   62,   37,   59,   46,   47,   41,   42,   43,
  277,   45,   46,   47,   93,   61,   40,   91,   41,  145,
   41,  147,  282,  277,   93,   40,   60,   59,   62,   41,
   37,   40,   59,   93,   41,   42,   43,   41,   45,   46,
   47,  167,   41,   44,  269,   41,  172,   41,    0,   59,
   91,  123,   41,   60,   41,   62,  277,   91,  277,  277,
   37,  277,   59,   41,   41,   42,   43,   37,   45,   46,
   47,    3,   42,   43,   11,   45,   46,   47,   32,   -1,
   -1,  144,   -1,   60,   91,   62,  152,   -1,  277,  277,
   -1,   -1,   -1,  257,  258,  259,  260,  261,  262,  263,
   -1,  265,  266,  267,  268,   -1,  270,  271,  272,  273,
  274,  275,  276,  277,   91,  277,  277,  281,  282,  278,
  279,   91,   -1,   33,  283,  284,  285,  286,   -1,  263,
   40,  265,   -1,   -1,   -1,   45,   -1,   -1,  272,   -1,
  274,  275,  276,   -1,   -1,  278,  279,   -1,   -1,   -1,
  283,  284,  285,  286,   -1,   -1,  278,  279,   -1,   -1,
   -1,  283,  284,  285,  286,   -1,   -1,   -1,   37,   -1,
   -1,   -1,   -1,   42,   43,   -1,   45,   46,   47,   -1,
   -1,   -1,  278,  279,   -1,   -1,   -1,  283,  284,  285,
  286,   60,   -1,   62,   -1,   -1,   -1,   -1,   -1,   33,
  279,   -1,   -1,   -1,   -1,   -1,   40,   -1,   -1,  278,
  279,   45,   -1,  123,  283,  284,  285,  286,  278,  279,
   -1,   -1,   91,  283,  284,  285,  286,   -1,   -1,   -1,
   37,   -1,   -1,   -1,   -1,   42,   43,   -1,   45,   46,
   47,   -1,   -1,   -1,  278,  279,   -1,   -1,   -1,  283,
  284,  285,  286,   60,   -1,   62,   -1,   37,   -1,   -1,
   -1,   -1,   42,   43,   -1,   45,   46,   47,   -1,   -1,
   -1,  278,  279,   -1,   -1,   -1,  283,  284,  285,  286,
   60,   -1,   62,   -1,   91,   -1,   93,   37,   -1,  123,
   -1,  125,   42,   43,   -1,   45,   46,   47,   -1,   -1,
   -1,  278,  279,   -1,   -1,   -1,  283,  284,  285,  286,
   60,   91,   62,   93,   37,   41,   -1,   -1,   44,   42,
   43,   37,   45,   46,   47,   -1,   42,   43,   -1,   45,
   46,   47,   -1,   59,   -1,   -1,   -1,   60,   -1,   62,
   -1,   91,   -1,   -1,   60,   -1,   62,  257,  258,  259,
  260,  261,  262,  263,   -1,  265,  266,  267,  268,   -1,
  270,  271,  272,  273,  274,  275,  276,   93,   91,   -1,
   41,  281,   43,   44,   45,   91,   37,   -1,   -1,   -1,
   -1,   42,   43,   -1,   45,   46,   47,   -1,   59,   60,
   -1,   62,  257,  258,  259,  260,  261,  262,   -1,   60,
   41,   62,   43,   44,   45,   -1,   -1,   -1,   -1,  278,
  279,   -1,  277,   -1,  283,  284,  285,  286,   59,   60,
   -1,   62,   93,  257,  258,  259,  260,  261,  262,  263,
   91,  265,  266,  267,  268,   -1,  270,  271,  272,  273,
  274,  275,  276,   41,  125,   41,   44,  281,   44,   -1,
   -1,   -1,   93,   -1,   -1,   -1,   -1,   41,   -1,   -1,
   44,   59,   41,   59,   43,   44,   45,   -1,   -1,   -1,
   -1,  278,  279,   -1,   -1,   59,  283,  284,  285,  286,
   59,   60,   -1,   62,   -1,   -1,   -1,   -1,   -1,   41,
   -1,   -1,   44,   41,   -1,   93,   44,   93,  278,  279,
   -1,   -1,   -1,  283,  284,  285,  286,   59,   33,   93,
   -1,   59,   -1,   -1,   93,   40,   -1,   -1,   -1,   -1,
   45,   41,   -1,   -1,   44,   33,   -1,   -1,  278,  279,
   46,   -1,   40,  283,  284,  285,  286,   45,   -1,   59,
   -1,   93,   -1,   59,   -1,   93,  257,  258,  259,  260,
  261,  262,  278,  279,   -1,  278,   -1,   -1,   46,   -1,
  283,  284,  285,  286,   -1,   81,   -1,  283,  284,  285,
  286,   59,   -1,   93,   -1,   -1,  257,  258,  259,  260,
  261,  262,   -1,   -1,   -1,   93,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   81,   -1,   -1,   -1,   -1,   -1,  280,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  278,  279,   -1,
   -1,   -1,  283,  284,  285,  286,   -1,   -1,   -1,   -1,
   -1,   -1,  283,  284,   -1,   -1,   -1,   -1,   -1,  145,
   -1,  147,   -1,   -1,   -1,   -1,   -1,  278,  279,   -1,
   -1,   -1,  283,  284,  285,  286,   -1,   -1,   -1,   -1,
  166,  167,   -1,   -1,   -1,   -1,  172,  145,   -1,  147,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  166,  167,
  278,  279,  278,  279,  172,   -1,   -1,  285,  286,  285,
  286,   -1,   -1,   -1,  278,  279,   -1,   -1,   -1,  278,
  279,  285,  286,   -1,  283,  284,  285,  286,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  278,  279,   -1,   -1,
  278,  279,   -1,  285,  286,   52,  261,   -1,  263,   -1,
  265,   -1,   -1,   60,   61,   62,   -1,  272,   -1,  274,
  275,  276,   -1,   -1,   -1,  263,   -1,  265,  278,  279,
   -1,   -1,   -1,   80,  272,   82,  274,  275,  276,   -1,
   -1,   88,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  100,  101,  102,  103,  104,  105,  106,
  107,  108,  109,  110,  111,  112,  113,   -1,  115,   -1,
   -1,   -1,   -1,   -1,  121,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  144,   -1,  146,
   -1,   -1,   -1,  150,   -1,  152,  153,
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
"ClassDef : CLASS IDENTIFIER ExtendsClause '{' FieldList '}'",
"ExtendsClause : EXTENDS IDENTIFIER",
"ExtendsClause :",
"FieldList : FieldList VariableDef",
"FieldList : FieldList FunctionDef",
"FieldList :",
"VariableDef : Variable ';'",
"Variable : Type IDENTIFIER",
"Type : INT",
"Type : DOUBLE",
"Type : BOOL",
"Type : STRING",
"Type : VOID",
"Type : CLASS IDENTIFIER",
"Type : Type '[' ']'",
"FunctionDef : STATIC Type IDENTIFIER '(' Formals ')' StmtBlock",
"FunctionDef : Type IDENTIFIER '(' Formals ')' StmtBlock",
"Formals : VariableList",
"Formals :",
"VariableList : VariableList ',' Variable",
"VariableList : Variable",
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
"LValue : Receiver IDENTIFIER",
"LValue : Expr '[' Expr ']'",
"Receiver : Expr '.'",
"Receiver :",
"Expr : LValue",
"Expr : Call",
"Expr : Constant",
"Expr : Expr '+' Expr",
"Expr : Expr '-' Expr",
"Expr : Expr '*' Expr",
"Expr : Expr '/' Expr",
"Expr : Expr '%' Expr",
"Expr : '-' Expr",
"Expr : '!' Expr",
"Expr : Expr '<' Expr",
"Expr : Expr LESS_EQUAL Expr",
"Expr : Expr '>' Expr",
"Expr : Expr GREATER_EQUAL Expr",
"Expr : Expr EQUAL Expr",
"Expr : Expr NOT_EQUAL Expr",
"Expr : Expr AND Expr",
"Expr : Expr OR Expr",
"Expr : THIS",
"Expr : READ_INTEGER '(' ')'",
"Expr : READ_LINE '(' ')'",
"Expr : NEW IDENTIFIER '(' ')'",
"Expr : NEW Type '[' Expr ']'",
"Expr : '(' CLASS IDENTIFIER ')' Expr",
"Expr : '(' Expr ')'",
"Call : Receiver IDENTIFIER '(' Actuals ')'",
"Actuals : ExprList",
"Actuals :",
"ExprList : ExprList ',' Expr",
"ExprList : Expr",
"Constant : LITERAL",
"Constant : NULL",
"ForStmt : FOR '(' SimpleStmt ';' BoolExpr ';' SimpleStmt ')' Stmt",
"BoolExpr : Expr",
"WhileStmt : WHILE '(' Expr ')' Stmt",
"BreakStmt : BREAK",
"IfStmt : IF '(' Expr ')' Stmt ElseClause",
"ElseClause : ELSE Stmt",
"ElseClause :",
"ReturnStmt : RETURN Expr",
"ReturnStmt : RETURN",
"PrintStmt : PRINT '(' ExprList ')'",
"RepeatStmt : REPEAT Stmt UNTIL '(' BoolExpr ')'",
};

//#line 443 "Parser.y"
    
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
//#line 575 "Parser.java"
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
case 4:
//#line 74 "Parser.y"
{
						yyval.cdef = new Tree.ClassDef(val_peek(4).ident, val_peek(3).ident, val_peek(1).flist, val_peek(5).loc);
					}
break;
case 5:
//#line 80 "Parser.y"
{
						yyval.ident = val_peek(0).ident;
					}
break;
case 6:
//#line 84 "Parser.y"
{
                		yyval = new SemValue();
                	}
break;
case 7:
//#line 90 "Parser.y"
{
						yyval.flist.add(val_peek(0).vdef);
					}
break;
case 8:
//#line 94 "Parser.y"
{
						yyval.flist.add(val_peek(0).fdef);
					}
break;
case 9:
//#line 98 "Parser.y"
{
                		yyval = new SemValue();
                		yyval.flist = new ArrayList<Tree>();
                	}
break;
case 11:
//#line 108 "Parser.y"
{
						yyval.vdef = new Tree.VarDef(val_peek(0).ident, val_peek(1).type, val_peek(0).loc);
					}
break;
case 12:
//#line 114 "Parser.y"
{
						yyval.type = new Tree.TypeIdent(Tree.INT, val_peek(0).loc);
					}
break;
case 13:
//#line 119 "Parser.y"
{
						yyval.type = new Tree.TypeIdent(Tree.DOUBLE, val_peek(0).loc);
					}
break;
case 14:
//#line 123 "Parser.y"
{
						yyval.type = new Tree.TypeIdent(Tree.BOOL, val_peek(0).loc);
					}
break;
case 15:
//#line 127 "Parser.y"
{
						yyval.type = new Tree.TypeIdent(Tree.STRING, val_peek(0).loc);
					}
break;
case 16:
//#line 131 "Parser.y"
{
						yyval.type = new Tree.TypeIdent(Tree.VOID, val_peek(0).loc);
					}
break;
case 17:
//#line 135 "Parser.y"
{
						/*$2.loc ??*/
						yyval.type = new Tree.TypeClass(val_peek(0).ident, val_peek(0).loc);
					}
break;
case 18:
//#line 140 "Parser.y"
{
						/*$1.loc ??*/
						yyval.type = new Tree.TypeArray(val_peek(2).type, val_peek(2).loc);
					}
break;
case 19:
//#line 147 "Parser.y"
{
						yyval.fdef = new MethodDef(true, val_peek(4).ident, val_peek(5).type, val_peek(2).vlist, (Block) val_peek(0).stmt, val_peek(4).loc);
					}
break;
case 20:
//#line 151 "Parser.y"
{
						yyval.fdef = new MethodDef(false, val_peek(4).ident, val_peek(5).type, val_peek(2).vlist, (Block) val_peek(0).stmt, val_peek(4).loc);
					}
break;
case 22:
//#line 158 "Parser.y"
{
                		yyval = new SemValue();
                		yyval.vlist = new ArrayList<Tree.VarDef>(); 
                	}
break;
case 23:
//#line 165 "Parser.y"
{
						yyval.vlist.add(val_peek(0).vdef);
					}
break;
case 24:
//#line 169 "Parser.y"
{
                		yyval.vlist = new ArrayList<Tree.VarDef>();
						yyval.vlist.add(val_peek(0).vdef);
                	}
break;
case 25:
//#line 176 "Parser.y"
{
						yyval.stmt = new Block(val_peek(1).slist, val_peek(2).loc);
					}
break;
case 26:
//#line 182 "Parser.y"
{
						yyval.slist.add(val_peek(0).stmt);
					}
break;
case 27:
//#line 186 "Parser.y"
{
                		yyval = new SemValue();
                		yyval.slist = new ArrayList<Tree>();
                	}
break;
case 28:
//#line 193 "Parser.y"
{
						yyval.stmt = val_peek(0).vdef;
					}
break;
case 29:
//#line 198 "Parser.y"
{
                		if (yyval.stmt == null) {
                			yyval.stmt = new Tree.Skip(val_peek(0).loc);
                		}
                	}
break;
case 38:
//#line 214 "Parser.y"
{
						yyval.stmt = new Tree.Assign(val_peek(2).lvalue, val_peek(0).expr, val_peek(1).loc);
					}
break;
case 39:
//#line 218 "Parser.y"
{
                		yyval.stmt = new Tree.Exec(val_peek(0).expr, val_peek(0).loc);
                	}
break;
case 40:
//#line 222 "Parser.y"
{
                		yyval = new SemValue();
                	}
break;
case 41:
//#line 228 "Parser.y"
{
						yyval.lvalue = new Tree.Ident(val_peek(1).expr, val_peek(0).ident, val_peek(0).loc);
						if (val_peek(1).loc == null) {
							yyval.loc = val_peek(0).loc;
						}
					}
break;
case 42:
//#line 235 "Parser.y"
{
                		yyval.lvalue = new Tree.Indexed(val_peek(3).expr, val_peek(1).expr, val_peek(3).loc);
                	}
break;
case 44:
//#line 242 "Parser.y"
{
                		yyval = new SemValue();
                	}
break;
case 45:
//#line 248 "Parser.y"
{
						yyval.expr = val_peek(0).lvalue;
					}
break;
case 48:
//#line 254 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.PLUS, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 49:
//#line 258 "Parser.y"
{
						yyval.expr = new Tree.Binary(Tree.MINUS, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
					}
break;
case 50:
//#line 262 "Parser.y"
{
						yyval.expr = new Tree.Binary(Tree.MUL, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
					}
break;
case 51:
//#line 266 "Parser.y"
{
						yyval.expr = new Tree.Binary(Tree.DIV, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
					}
break;
case 52:
//#line 270 "Parser.y"
{
						yyval.expr = new Tree.Binary(Tree.MOD, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
					}
break;
case 53:
//#line 274 "Parser.y"
{
						yyval.expr = new Tree.Unary(Tree.NEG, val_peek(0).expr, val_peek(1).loc);
					}
break;
case 54:
//#line 278 "Parser.y"
{
						yyval.expr = new Tree.Unary(Tree.NOT, val_peek(0).expr, val_peek(1).loc);
					}
break;
case 55:
//#line 282 "Parser.y"
{
						yyval.expr = new Tree.Binary(Tree.LT, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
					}
break;
case 56:
//#line 286 "Parser.y"
{
						yyval.expr = new Tree.Binary(Tree.LE, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
					}
break;
case 57:
//#line 290 "Parser.y"
{
						yyval.expr = new Tree.Binary(Tree.GT, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
					}
break;
case 58:
//#line 294 "Parser.y"
{
						yyval.expr = new Tree.Binary(Tree.GE, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
					}
break;
case 59:
//#line 298 "Parser.y"
{
						yyval.expr = new Tree.Binary(Tree.EQ, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
					}
break;
case 60:
//#line 302 "Parser.y"
{
						yyval.expr = new Tree.Binary(Tree.NE, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
					}
break;
case 61:
//#line 306 "Parser.y"
{
						yyval.expr = new Tree.Binary(Tree.AND, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
					}
break;
case 62:
//#line 310 "Parser.y"
{
						yyval.expr = new Tree.Binary(Tree.OR, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
					}
break;
case 63:
//#line 314 "Parser.y"
{
						yyval.expr = new Tree.ThisExpr(val_peek(0).loc);
					}
break;
case 64:
//#line 318 "Parser.y"
{
						yyval.expr = new Tree.ReadIntExpr(val_peek(2).loc);
					}
break;
case 65:
//#line 322 "Parser.y"
{
						yyval.expr = new Tree.ReadLineExpr(val_peek(2).loc);
					}
break;
case 66:
//#line 326 "Parser.y"
{
						yyval.expr = new Tree.NewClass(val_peek(2).ident, val_peek(3).loc);
					}
break;
case 67:
//#line 330 "Parser.y"
{
						yyval.expr = new Tree.NewArray(val_peek(3).type, val_peek(1).expr, val_peek(4).loc);
					}
break;
case 68:
//#line 334 "Parser.y"
{
						yyval.expr = new Tree.TypeCast(val_peek(2).ident, val_peek(0).expr, val_peek(4).loc);
					}
break;
case 69:
//#line 338 "Parser.y"
{
						yyval.expr = val_peek(1).expr;
					}
break;
case 70:
//#line 344 "Parser.y"
{
						yyval.expr = new Tree.CallExpr(val_peek(4).expr, val_peek(3).ident, val_peek(1).elist, val_peek(3).loc);
						if (val_peek(4).loc == null) {
							yyval.loc = val_peek(3).loc;
						}
					}
break;
case 72:
//#line 354 "Parser.y"
{
                		yyval = new SemValue();
                		yyval.elist = new ArrayList<Tree.Expr>();
                	}
break;
case 73:
//#line 361 "Parser.y"
{
						yyval.elist.add(val_peek(0).expr);
					}
break;
case 74:
//#line 365 "Parser.y"
{
                		yyval.elist = new ArrayList<Tree.Expr>();
						yyval.elist.add(val_peek(0).expr);
                	}
break;
case 75:
//#line 372 "Parser.y"
{
						yyval.expr = new Tree.Literal(val_peek(0).typeTag, val_peek(0).literal, val_peek(0).loc);
					}
break;
case 76:
//#line 376 "Parser.y"
{
						yyval.expr = new Null(val_peek(0).loc);
					}
break;
case 77:
//#line 382 "Parser.y"
{
						yyval.stmt = new Tree.ForLoop(val_peek(6).stmt, val_peek(4).expr, val_peek(2).stmt, val_peek(0).stmt, val_peek(8).loc);
					}
break;
case 78:
//#line 387 "Parser.y"
{
						yyval.expr = val_peek(0).expr;
					}
break;
case 79:
//#line 392 "Parser.y"
{
						yyval.stmt = new Tree.WhileLoop(val_peek(2).expr, val_peek(0).stmt, val_peek(4).loc);
					}
break;
case 80:
//#line 399 "Parser.y"
{
						yyval.stmt = new Tree.Break(val_peek(0).loc);
					}
break;
case 81:
//#line 405 "Parser.y"
{
						yyval.stmt = new Tree.If(val_peek(3).expr, val_peek(1).stmt, val_peek(0).stmt, val_peek(5).loc);
					}
break;
case 82:
//#line 411 "Parser.y"
{
						yyval.stmt = val_peek(0).stmt;
					}
break;
case 83:
//#line 415 "Parser.y"
{
						yyval = new SemValue();
					}
break;
case 84:
//#line 421 "Parser.y"
{
						yyval.stmt = new Tree.Return(val_peek(0).expr, val_peek(1).loc);
					}
break;
case 85:
//#line 425 "Parser.y"
{
                		yyval.stmt = new Tree.Return(null, val_peek(0).loc);
                	}
break;
case 86:
//#line 431 "Parser.y"
{
						yyval.stmt = new Print(val_peek(1).elist, val_peek(3).loc);
					}
break;
case 87:
//#line 437 "Parser.y"
{
						yyval.stmt = new Tree.RepeatUntilLoop(val_peek(1).expr, val_peek(4).stmt, val_peek(5).loc);
					}
break;
//#line 1176 "Parser.java"
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
