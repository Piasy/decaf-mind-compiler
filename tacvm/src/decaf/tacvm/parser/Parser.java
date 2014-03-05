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






//#line 2 "Parser.y"
package decaf.tacvm.parser;

import java.util.*;
//#line 22 "Parser.java"
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
public final static short EQU=257;
public final static short NEQ=258;
public final static short GEQ=259;
public final static short LEQ=260;
public final static short LAND=261;
public final static short LOR=262;
public final static short BRANCH=263;
public final static short PARM=264;
public final static short CALL=265;
public final static short RETURN=266;
public final static short IF=267;
public final static short LABEL=268;
public final static short EMPTY=269;
public final static short VTABLE=270;
public final static short FUNC=271;
public final static short TEMP=272;
public final static short ENTRY=273;
public final static short INT_CONST=274;
public final static short STRING_CONST=275;
public final static short VTBL=276;
public final static short IDENT=277;
public final static short MEMO=278;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    1,    1,    3,    3,    3,    3,    4,    4,    2,
    2,    5,    8,    6,   10,    6,    9,    9,    7,    7,
   11,   11,   11,   11,   11,   11,   11,   11,   11,   11,
   11,   11,   11,   11,   11,   11,   11,   11,   11,   11,
   11,   11,   11,   11,   11,   11,   11,   11,   11,   11,
   11,   11,   11,   11,   11,   11,
};
final static short yylen[] = {                            2,
    2,    2,    1,    9,    9,    8,    7,    3,    0,    2,
    1,    3,    0,   12,    0,   11,    4,    0,    2,    0,
    7,    7,    7,    7,    7,    7,    7,    7,    7,    7,
    7,    7,    7,    4,    4,    3,    3,    3,    8,    8,
    8,    8,    4,    2,    4,    4,    2,    2,    6,    2,
    8,    8,    2,    2,    2,    2,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    3,    0,    0,    0,    2,   11,   20,
    0,    0,   10,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   12,    0,   19,    0,   13,   15,
   50,   53,   44,   48,   47,   55,   54,    0,   56,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   36,   37,
   38,    0,    0,    0,    0,    0,    0,    9,    9,    0,
    0,    7,    0,    0,    0,    0,   43,   46,   45,    0,
    0,   35,    0,   34,    0,    0,    0,    0,    6,    8,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    5,    4,   18,    0,    0,    0,   49,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   30,
   31,   29,   32,   26,   27,   21,   22,   23,   24,   25,
   28,   33,    0,    0,    0,    0,    0,    0,    0,   51,
   52,   39,   40,   41,   42,    0,    0,   16,   17,   14,
};
final static short yydgoto[] = {                          2,
    3,    7,    4,   44,    9,   10,   14,   45,  126,   46,
   27,
};
final static short yysindex[] = {                      -257,
  -18,    0, -230,    0, -252,  -12, -238,    0,    0,    0,
    3, -242,    0,  -39,  -78,    5,    7, -218, -221, -258,
 -249,   12,   -5,   -4,    0,   14,    0, -259,    0,    0,
    0,    0,    0,    0,    0,    0,    0, -217,    0,  -33,
 -216, -219, -214, -123,  -64,  -63, -215, -256,    0,    0,
    0,    1, -210, -208,   25, -206,   -7,    0,    0, -121,
    8,    0, -209, -207, -204, -202,    0,    0,    0, -203,
  -13,    0, -199,    0, -198, -197, -120, -119,    0,    0,
   29,   36,   37,   38,   18, -191, -190, -189, -188, -187,
 -185, -184, -183, -182, -181, -180, -179, -178,   -6,   54,
   55,    0,    0,    0,   58, -165, -164,    0,   59,   60,
   61,   62,   63,   64,   65,   66,   67,   68,   69,   70,
   71, -161, -160,   56,   57,  -38, -162, -152, -149,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   79,   80, -150, -148,   72, -147,   73,    0,
    0,    0,    0,    0,    0, -151,   74,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,  125,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0, -117,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0, -117,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,    0,  124,  -32,  121,    0,    0,    0,    0,    0,
    0,
};
final static int YYTABLESIZE=249;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         56,
  148,   62,   26,   79,  102,  103,   53,    9,   55,   42,
   60,   54,    1,   33,   34,   67,   68,   43,   35,   36,
   69,    5,   37,   96,   11,   77,   78,   12,   94,   92,
   16,   93,    6,   95,   17,   75,  122,   76,  123,    1,
    6,   65,   66,   15,   28,   29,   98,   30,   97,   31,
   32,   38,   39,   41,   47,   57,   40,   58,   63,   64,
   70,   71,   59,   72,   73,   74,   80,  104,   81,   83,
   82,   84,   99,   85,  105,  100,  101,  106,  107,  108,
  109,  110,  111,  112,  113,   25,  114,  115,  116,  117,
  118,  119,  120,  121,  124,  125,  127,  128,  129,  130,
  131,  132,  133,  134,  135,  136,  137,  138,  139,  140,
  141,  142,  143,  144,  149,  150,  145,  146,  151,  152,
  153,  154,  159,  155,    1,  157,    8,   13,    0,  156,
  158,  160,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   61,
    0,   61,   61,   61,    0,    9,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   18,   19,   20,   21,   22,   23,    0,
    0,   48,   24,  147,    0,    0,    0,    0,   49,    0,
   50,   51,   52,   86,   87,   88,   89,   90,   91,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         33,
   39,  125,   42,  125,  125,  125,   40,  125,   42,  269,
   43,   45,  270,  272,  273,  272,  273,  277,  277,  269,
  277,   40,  272,   37,  277,   58,   59,   40,   42,   43,
  273,   45,  271,   47,  277,   43,   43,   45,   45,  270,
  271,  257,  258,   41,  123,   41,   60,   41,   62,  268,
  272,   40,   58,   40,  272,  272,   61,  277,  123,  123,
   60,  272,  277,  272,   40,  272,   59,   39,  278,  274,
  278,  274,  272,  277,   39,  274,  274,   41,   41,   62,
  272,  272,  272,  272,  272,  125,  272,  272,  272,  272,
  272,  272,  272,  272,   41,   41,   39,  263,  263,   41,
   41,   41,   41,   41,   41,   41,   41,   41,   41,   41,
   41,   41,  274,  274,  277,  268,   61,   61,  268,   41,
   41,  272,  274,  272,    0,  273,    3,    7,   -1,   58,
   58,   58,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  273,
   -1,  273,  273,  273,   -1,  273,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  263,  264,  265,  266,  267,  268,   -1,
   -1,  265,  272,  272,   -1,   -1,   -1,   -1,  272,   -1,
  274,  275,  276,  257,  258,  259,  260,  261,  262,
};
}
final static short YYFINAL=2;
final static short YYMAXTOKEN=278;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,"'!'",null,null,null,"'%'",null,"'\\''","'('","')'","'*'","'+'",
null,"'-'",null,"'/'",null,null,null,null,null,null,null,null,null,null,"':'",
"';'","'<'","'='","'>'",null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,"'{'",null,"'}'",null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,"EQU","NEQ","GEQ","LEQ","LAND","LOR",
"BRANCH","PARM","CALL","RETURN","IF","LABEL","EMPTY","VTABLE","FUNC","TEMP",
"ENTRY","INT_CONST","STRING_CONST","VTBL","IDENT","MEMO",
};
final static String yyrule[] = {
"$accept : Program",
"Program : VTables Funcs",
"VTables : VTables VTable",
"VTables : VTable",
"VTable : VTABLE '(' IDENT ')' '{' IDENT IDENT Entrys '}'",
"VTable : VTABLE '(' IDENT ')' '{' EMPTY IDENT Entrys '}'",
"VTable : VTABLE '(' IDENT ')' '{' IDENT Entrys '}'",
"VTable : VTABLE '(' IDENT ')' '{' Entrys '}'",
"Entrys : Entrys ENTRY ';'",
"Entrys :",
"Funcs : Funcs Func",
"Funcs : Func",
"Func : FuncHeader Tacs '}'",
"$$1 :",
"FuncHeader : FUNC '(' ENTRY ')' $$1 '{' MEMO '\\'' Params '\\'' ENTRY ':'",
"$$2 :",
"FuncHeader : FUNC '(' IDENT ')' $$2 '{' MEMO '\\'' '\\'' IDENT ':'",
"Params : Params TEMP ':' INT_CONST",
"Params :",
"Tacs : Tacs Tac",
"Tacs :",
"Tac : TEMP '=' '(' TEMP '+' TEMP ')'",
"Tac : TEMP '=' '(' TEMP '-' TEMP ')'",
"Tac : TEMP '=' '(' TEMP '*' TEMP ')'",
"Tac : TEMP '=' '(' TEMP '/' TEMP ')'",
"Tac : TEMP '=' '(' TEMP '%' TEMP ')'",
"Tac : TEMP '=' '(' TEMP LAND TEMP ')'",
"Tac : TEMP '=' '(' TEMP LOR TEMP ')'",
"Tac : TEMP '=' '(' TEMP '>' TEMP ')'",
"Tac : TEMP '=' '(' TEMP GEQ TEMP ')'",
"Tac : TEMP '=' '(' TEMP EQU TEMP ')'",
"Tac : TEMP '=' '(' TEMP NEQ TEMP ')'",
"Tac : TEMP '=' '(' TEMP LEQ TEMP ')'",
"Tac : TEMP '=' '(' TEMP '<' TEMP ')'",
"Tac : TEMP '=' '!' TEMP",
"Tac : TEMP '=' '-' TEMP",
"Tac : TEMP '=' TEMP",
"Tac : TEMP '=' INT_CONST",
"Tac : TEMP '=' STRING_CONST",
"Tac : TEMP '=' '*' '(' TEMP '+' INT_CONST ')'",
"Tac : TEMP '=' '*' '(' TEMP '-' INT_CONST ')'",
"Tac : '*' '(' TEMP '+' INT_CONST ')' '=' TEMP",
"Tac : '*' '(' TEMP '-' INT_CONST ')' '=' TEMP",
"Tac : TEMP '=' CALL TEMP",
"Tac : CALL TEMP",
"Tac : TEMP '=' CALL IDENT",
"Tac : TEMP '=' CALL ENTRY",
"Tac : CALL IDENT",
"Tac : CALL ENTRY",
"Tac : TEMP '=' VTBL '<' IDENT '>'",
"Tac : BRANCH LABEL",
"Tac : IF '(' TEMP EQU INT_CONST ')' BRANCH LABEL",
"Tac : IF '(' TEMP NEQ INT_CONST ')' BRANCH LABEL",
"Tac : PARM TEMP",
"Tac : RETURN TEMP",
"Tac : RETURN EMPTY",
"Tac : LABEL ':'",
};

//#line 242 "Parser.y"
    
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
//#line 367 "Parser.java"
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
case 4:
//#line 30 "Parser.y"
{
					createVTable(val_peek(6).sVal, val_peek(3).sVal, val_peek(2).sVal, val_peek(1).entrys);
				}
break;
case 5:
//#line 34 "Parser.y"
{
					createVTable(val_peek(6).sVal, null, val_peek(2).sVal, val_peek(1).entrys);
				}
break;
case 6:
//#line 39 "Parser.y"
{
					createVTable(val_peek(5).sVal, null, val_peek(2).sVal, val_peek(1).entrys);
				}
break;
case 7:
//#line 43 "Parser.y"
{
					createVTable(val_peek(4).sVal, null, "xjlxjl", val_peek(1).entrys);
				}
break;
case 8:
//#line 50 "Parser.y"
{
					Entry e = new Entry();
					e.name = val_peek(1).sVal;
					e.offset = -1;
					val_peek(2).entrys.add(e);
				}
break;
case 9:
//#line 57 "Parser.y"
{
					yyval.entrys = new ArrayList<Entry>();
				}
break;
case 12:
//#line 67 "Parser.y"
{
					endFunc();
				}
break;
case 13:
//#line 73 "Parser.y"
{
					enterFunc(val_peek(3).loc, val_peek(1).sVal);
				}
break;
case 15:
//#line 78 "Parser.y"
{
					enterFunc(val_peek(3).loc, val_peek(1).sVal);
				}
break;
case 17:
//#line 85 "Parser.y"
{
					addParam(val_peek(2).sVal, val_peek(0).iVal);
				}
break;
case 21:
//#line 96 "Parser.y"
{
					genAdd(val_peek(2).loc, val_peek(6).sVal, val_peek(3).sVal, val_peek(1).sVal);
				}
break;
case 22:
//#line 100 "Parser.y"
{
					genSub(val_peek(2).loc, val_peek(6).sVal, val_peek(3).sVal, val_peek(1).sVal);
				}
break;
case 23:
//#line 104 "Parser.y"
{
					genMul(val_peek(2).loc, val_peek(6).sVal, val_peek(3).sVal, val_peek(1).sVal);
				}
break;
case 24:
//#line 108 "Parser.y"
{
					genDiv(val_peek(2).loc, val_peek(6).sVal, val_peek(3).sVal, val_peek(1).sVal);
				}
break;
case 25:
//#line 112 "Parser.y"
{
					genMod(val_peek(2).loc, val_peek(6).sVal, val_peek(3).sVal, val_peek(1).sVal);
				}
break;
case 26:
//#line 116 "Parser.y"
{
					genLAnd(val_peek(2).loc, val_peek(6).sVal, val_peek(3).sVal, val_peek(1).sVal);
				}
break;
case 27:
//#line 120 "Parser.y"
{
					genLOr(val_peek(2).loc, val_peek(6).sVal, val_peek(3).sVal, val_peek(1).sVal);
				}
break;
case 28:
//#line 124 "Parser.y"
{
					genGtr(val_peek(2).loc, val_peek(6).sVal, val_peek(3).sVal, val_peek(1).sVal);
				}
break;
case 29:
//#line 128 "Parser.y"
{
					genGeq(val_peek(2).loc, val_peek(6).sVal, val_peek(3).sVal, val_peek(1).sVal);
				}
break;
case 30:
//#line 132 "Parser.y"
{
					genEqu(val_peek(2).loc, val_peek(6).sVal, val_peek(3).sVal, val_peek(1).sVal);
				}
break;
case 31:
//#line 136 "Parser.y"
{
					genNeq(val_peek(2).loc, val_peek(6).sVal, val_peek(3).sVal, val_peek(1).sVal);
				}
break;
case 32:
//#line 140 "Parser.y"
{
					genLeq(val_peek(2).loc, val_peek(6).sVal, val_peek(3).sVal, val_peek(1).sVal);
				}
break;
case 33:
//#line 144 "Parser.y"
{
					genLes(val_peek(2).loc, val_peek(6).sVal, val_peek(3).sVal, val_peek(1).sVal);
				}
break;
case 34:
//#line 148 "Parser.y"
{
					genLNot(val_peek(1).loc, val_peek(3).sVal, val_peek(0).sVal);
				}
break;
case 35:
//#line 152 "Parser.y"
{
					genNeg(val_peek(1).loc, val_peek(3).sVal, val_peek(0).sVal);
				}
break;
case 36:
//#line 156 "Parser.y"
{
					genAssign(val_peek(1).loc, val_peek(2).sVal, val_peek(0).sVal);
				}
break;
case 37:
//#line 160 "Parser.y"
{
					genLoadImm4(val_peek(1).loc, val_peek(2).sVal, val_peek(0).iVal);
				}
break;
case 38:
//#line 164 "Parser.y"
{
					genLoadStr(val_peek(1).loc, val_peek(2).sVal, val_peek(0).sVal);
				}
break;
case 39:
//#line 168 "Parser.y"
{
					genLoad(val_peek(6).loc, val_peek(7).sVal, val_peek(3).sVal, val_peek(1).iVal);
				}
break;
case 40:
//#line 172 "Parser.y"
{
					genLoad(val_peek(6).loc, val_peek(7).sVal, val_peek(3).sVal, -val_peek(1).iVal);
				}
break;
case 41:
//#line 176 "Parser.y"
{
					genStore(val_peek(1).loc, val_peek(0).sVal, val_peek(5).sVal, val_peek(3).iVal);
				}
break;
case 42:
//#line 180 "Parser.y"
{
					genStore(val_peek(1).loc, val_peek(0).sVal, val_peek(5).sVal, -val_peek(3).iVal);
				}
break;
case 43:
//#line 184 "Parser.y"
{
					genIndirectCall(val_peek(1).loc, val_peek(3).sVal, val_peek(0).sVal);
				}
break;
case 44:
//#line 188 "Parser.y"
{
					genIndirectCall(val_peek(1).loc, null, val_peek(0).sVal);
				}
break;
case 45:
//#line 192 "Parser.y"
{
					genDirectCall(val_peek(1).loc, val_peek(3).sVal, val_peek(0).sVal);
				}
break;
case 46:
//#line 196 "Parser.y"
{
					genDirectCall(val_peek(1).loc, val_peek(3).sVal, val_peek(0).sVal);
				}
break;
case 47:
//#line 200 "Parser.y"
{
					genDirectCall(val_peek(1).loc, null, val_peek(0).sVal);
				}
break;
case 48:
//#line 204 "Parser.y"
{
					genDirectCall(val_peek(1).loc, null, val_peek(0).sVal);
				}
break;
case 49:
//#line 208 "Parser.y"
{
					genLoadVtbl(val_peek(4).loc, val_peek(5).sVal, val_peek(1).sVal);
				}
break;
case 50:
//#line 212 "Parser.y"
{
					genBranch(val_peek(1).loc, val_peek(0).sVal);
				}
break;
case 51:
//#line 216 "Parser.y"
{
					genBeqz(val_peek(7).loc, val_peek(5).sVal, val_peek(0).sVal);
				}
break;
case 52:
//#line 220 "Parser.y"
{
					genBnez(val_peek(7).loc, val_peek(5).sVal, val_peek(0).sVal);
				}
break;
case 53:
//#line 224 "Parser.y"
{
					genParm(val_peek(1).loc, val_peek(0).sVal);
				}
break;
case 54:
//#line 228 "Parser.y"
{
					genReturn(val_peek(1).loc, val_peek(0).sVal);
				}
break;
case 55:
//#line 232 "Parser.y"
{
					genReturn(val_peek(1).loc, null);
				}
break;
case 56:
//#line 236 "Parser.y"
{
					markLabel(val_peek(1).sVal);
				}
break;
//#line 792 "Parser.java"
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
