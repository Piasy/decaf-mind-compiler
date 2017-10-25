/*
 * 本文件是构造Decaf编译器所需要的JFlex输入脚本。
 * 在第一阶段，你需要完成这个脚本的内容，请参考"JFlex Manual"中关于如何编写JFlex脚本的说明。
 *
 * 注意：在UNIX系统下你需要保证这个文件使用UNIX文本格式，可使用dos2unix命令进行文本各式转换。
 */

package decaf.frontend;

import decaf.Location;
import decaf.error.*;
import decaf.utils.MiscUtils;
 
%%
%public
%class Lexer
%extends BaseLexer
%byaccj
%line
%column
%switch
%unicode
	
%{
	private Location sloc = null;
	private StringBuilder buffer = new StringBuilder();

	public Location getLocation() {
		return new decaf.Location(yyline + 1, yycolumn + 1);
	}
%}

NEWLINE				= (\r|\n|\r\n)
DIGIT 				= ([0-9])
HEX_DIGIT 			= ([0-9A-Fa-f])
HEX_INTEGER			= (0[Xx]{HEX_DIGIT}+)
DEC_INTEGER			= ({DIGIT}+)
INTEGER				= ({HEX_INTEGER}|{DEC_INTEGER})
IDENTIFIER			= ([A-Za-z][_0-9A-Za-z]*)
SIMPLE_OPERATOR		= ("+"|"-"|"*"|"/"|"%"|"="|"<"|">"|"."|","|";"|"!"|"("|")"|"["|"]"|"{"|"}")
S_COMMENT			= ("//"[^\r\n]*{NEWLINE})
WHITESPACE			= ([ \t]+)

DOUBLE_NUM			= ({DIGIT}+\.{DIGIT}*)
DOUBLE_SCI			= ({DOUBLE_NUM}[Ee][\+\-]{DIGIT}+)
DOUBLE_VALUE		= ({DOUBLE_NUM}|{DOUBLE_SCI})

	// 开始条件S表示字符串，根据需要添加
%x S
%x BLOCK_COMMENT
	//%x BLOCK_COMMENT_END

%%
	// 识别注释和空白字符的规则
{WHITESPACE}		{ /* Just ignore */    }
{NEWLINE}			{ /* Just ignore */    }
{S_COMMENT}			{ /* Just ignore */    }


	// 识别关键字的规则 ， 以下部分请根据decaf语言规范以及实验新特性添加
"bool"				{ return keyword(Parser.BOOL);			}
"break"				{ return keyword(Parser.BREAK);			}
"class"				{ return keyword(Parser.CLASS);			}
"else"				{ return keyword(Parser.ELSE);			}
"extends"			{ return keyword(Parser.EXTENDS);		}
"for"				{ return keyword(Parser.FOR);			}
"if"				{ return keyword(Parser.IF);			}
"int"				{ return keyword(Parser.INT);			}
"new"				{ return keyword(Parser.NEW);			}
"null"				{ return keyword(Parser.NULL);			}
"return"			{ return keyword(Parser.RETURN);		}
"string"			{ return keyword(Parser.STRING);		}
"this"				{ return keyword(Parser.THIS);			}
"void"				{ return keyword(Parser.VOID);			}
"while"				{ return keyword(Parser.WHILE);			}
"static"			{ return keyword(Parser.STATIC);		}
"Print"				{ return keyword(Parser.PRINT);			}
"ReadInteger"		{ return keyword(Parser.READ_INTEGER);	}
"ReadLine"			{ return keyword(Parser.READ_LINE);		}

"repeat"			{ return keyword(Parser.REPEAT);		}
"until"				{ return keyword(Parser.UNTIL);			}
"double"			{ return keyword(Parser.DOUBLE);		}


	// 识别操作符的规则
"<="				{ return operator(Parser.LESS_EQUAL);	}
">="				{ return operator(Parser.GREATER_EQUAL);}
"=="				{ return operator(Parser.EQUAL);		}
"!="				{ return operator(Parser.NOT_EQUAL);	}
"&&"				{ return operator(Parser.AND);			}
"||"				{ return operator(Parser.OR);			}
{SIMPLE_OPERATOR}	{ return operator(yycharat(0));	}




	// 识别常数的规则
"true"				{ return boolConst(true);				}
"false"				{ return boolConst(false);				}
{INTEGER}			{ return intConst(yytext());			}

{DOUBLE_VALUE}		{ return doubleConst(yytext());			}

	//<YYINITIAL>是每次词法分析器扫描开始的状态，下面的表达式表示扫描开始立即遇到双引号，即字符串常量的开始
<YYINITIAL>\"		{ sloc = getLocation();		  
					  yybegin(S);
					  buffer = new StringBuilder();			}
					  
	//<S>表示字符串，下面的表达式表示字符串中出现换行符（在再次出现双引号之前），即出错
<S>{NEWLINE}		{ issueError(new NewlineInStrError(sloc, MiscUtils.quote(buffer.toString())));	}
	//字符串在再次出现双引号之前文件结束，即出错
<S><<EOF>>			{ issueError(new UntermStrError(sloc, MiscUtils.quote(buffer.toString())));
					  yybegin(YYINITIAL);					}
	//字符串之后再次遇到双引号，即正确的字符串常量
<S>\"				{ yybegin(YYINITIAL);
					  return StringConst(buffer.toString(), sloc);									}
	//字符串中出现转义换行符，将其append到buffer中，这意味着，处理完这个动作之后，词法分析器的状态处在转义换行符之后的那个位置，即n后的位置
<S>"\\n"			{ buffer.append('\n');					}
	//字符串中出现转义tab，即一个反斜杠，一个t这两个字符
<S>"\\t"			{ buffer.append('\t'); 					}
	//字符串中出现转义双引号
<S>"\\\""			{ buffer.append('"');					}
	//字符串中出现转义反斜杠
<S>"\\\\"			{ buffer.append('\\'); 					}
	//遇到换行符之外的任意字符，将目前扫描到的内容（字符串/字符）append到buffer中，这意味着，<S>每次有效匹配的应该只是一个字符，
	//这样就可以保证只支持想要的几种转义了，其他的情况，单个字符不可能构成转义字符。妙。
	//而且上述所有匹配规则是按从上到下顺序进行，一旦完成一个匹配，则本次匹配结束。实际上这是通过switch来完成的。
<S>.				{ buffer.append(yytext()); 				}

	// 识别标识符的规则
{IDENTIFIER}		{ return identifier(yytext());			}

	//必须定义好遇到每种字符应该做什么处理，否则会出错
<YYINITIAL>"/*"		{ 	sloc = getLocation();	
						yybegin(BLOCK_COMMENT);				}
<BLOCK_COMMENT>{NEWLINE}		{ /*do nothing*/			}
<BLOCK_COMMENT><<EOF>>			{ issueError(new BlockCommentError(sloc));
								  yybegin(YYINITIAL);		}
<BLOCK_COMMENT>"*/"	{ yybegin(YYINITIAL);					}								  
	//<BLOCK_COMMENT>"*/"	{ yybegin(BLOCK_COMMENT_END);			}
<BLOCK_COMMENT>.	{ /*do nothing*/						}

	//<BLOCK_COMMENT_END><<EOF>>		{ issueError(new BlockCommentError(sloc));
	//							  yybegin(YYINITIAL);		}
	//<BLOCK_COMMENT_END>.			{ yybegin(YYINITIAL);		}
	//<BLOCK_COMMENT_END>{NEWLINE}	{ yybegin(YYINITIAL);		}
	//<S>确实是匹配的字符串。。。
	
	// 上面规则不能识别的字符怎么处理
.					{ issueError(new UnrecogCharError(getLocation(), yycharat(0))); 				}

