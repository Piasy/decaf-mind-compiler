package decaf.typecheck;

import java.util.List;
import java.util.Stack;

import decaf.Driver;
import decaf.Location;
import decaf.tree.Tree;
import decaf.tree.Tree.Expr;
import decaf.error.BadArgCountError;
import decaf.error.BadArgTypeError;
import decaf.error.BadArrElementError;
import decaf.error.BadLengthArgError;
import decaf.error.BadLengthError;
import decaf.error.BadNewArrayLength;
import decaf.error.BadPrintArgError;
import decaf.error.BadReturnTypeError;
import decaf.error.BadTestExpr;
import decaf.error.BreakOutOfLoopError;
import decaf.error.ClassNotFoundError;
import decaf.error.DecafError;
import decaf.error.FieldNotAccessError;
import decaf.error.FieldNotFoundError;
import decaf.error.IncompatBinOpError;
import decaf.error.IncompatUnOpError;
import decaf.error.NotArrayError;
import decaf.error.NotClassError;
import decaf.error.NotClassFieldError;
import decaf.error.NotClassMethodError;
import decaf.error.RefNonStaticError;
import decaf.error.SubNotIntError;
import decaf.error.ThisInStaticFuncError;
import decaf.error.UndeclVarError;
import decaf.frontend.Parser;
import decaf.scope.ClassScope;
import decaf.scope.FormalScope;
import decaf.scope.Scope;
import decaf.scope.ScopeStack;
import decaf.scope.Scope.Kind;
import decaf.symbol.Class;
import decaf.symbol.Function;
import decaf.symbol.Symbol;
import decaf.symbol.Variable;
import decaf.type.*;

public class TypeCheck extends Tree.Visitor {

	private ScopeStack table;

	private Stack<Tree> breaks;

	private Function currentFunction;

	public TypeCheck(ScopeStack table) {
		this.table = table;
		breaks = new Stack<Tree>();
	}

	public static void checkType(Tree.TopLevel tree) {
		new TypeCheck(Driver.getDriver().getTable()).visitTopLevel(tree);
	}

	@Override
	public void visitBinary(Tree.Binary expr) {
		expr.type = checkBinaryOp(expr.left, expr.right, expr.tag, expr.loc);
	}

	@Override
	public void visitUnary(Tree.Unary expr) {
		expr.expr.accept(this);
		if(expr.tag == Tree.NEG){
//////////////////////////////////////////////////////////////////////////////////////////////			
			if (expr.expr.type.equal(BaseType.ERROR)
					|| expr.expr.type.equal(BaseType.INT) || expr.expr.type.equal(BaseType.DOUBLE)) 
			{
				expr.type = expr.expr.type;
			}
			else 
			{
				issueError(new IncompatUnOpError(expr.getLocation(), "-",
						expr.expr.type.toString()));
				expr.type = BaseType.ERROR;
			}
//////////////////////////////////////////////////////////////////////////////////////////////
		}
		else{
			if (!(expr.expr.type.equal(BaseType.BOOL) || expr.expr.type
					.equal(BaseType.ERROR))) {
				issueError(new IncompatUnOpError(expr.getLocation(), "!",
						expr.expr.type.toString()));
			}
			expr.type = BaseType.BOOL;
		}
	}

	@Override
	public void visitLiteral(Tree.Literal literal) {
		switch (literal.typeTag) {
		case Tree.INT:
			literal.type = BaseType.INT;
			break;
		case Tree.BOOL:
			literal.type = BaseType.BOOL;
			break;
		case Tree.STRING:
			literal.type = BaseType.STRING;
			break;
		////////////////////////////////////////////////////////////////////////////////////////
		case Tree.DOUBLE:
			literal.type = BaseType.DOUBLE;
		////////////////////////////////////////////////////////////////////////////////////////
		}
	}

	@Override
	public void visitNull(Tree.Null nullExpr) {
		nullExpr.type = BaseType.NULL;
	}

	@Override
	public void visitReadIntExpr(Tree.ReadIntExpr readIntExpr) {
		readIntExpr.type = BaseType.INT;
	}

	@Override
	public void visitReadLineExpr(Tree.ReadLineExpr readStringExpr) {
		readStringExpr.type = BaseType.STRING;
	}

	@Override
	public void visitIndexed(Tree.Indexed indexed) {
		indexed.lvKind = Tree.LValue.Kind.ARRAY_ELEMENT;
		indexed.array.accept(this);
		if (!indexed.array.type.isArrayType()) {
			issueError(new NotArrayError(indexed.array.getLocation()));
			indexed.type = BaseType.ERROR;
		} else {
			indexed.type = ((ArrayType) indexed.array.type)
					.getElementType();
		}
		
		
//		System.out.println("visit index");
		indexed.index.accept(this);
//		System.out.println("after visit index");
//		System.out.println(indexed.getLocation() + " " + indexed.index.getLocation() + " " + indexed.index.type.toString());
		
		if (!indexed.index.type.equal(BaseType.INT)) {
			issueError(new SubNotIntError(indexed.getLocation()));
		}
	}

	private void checkCallExpr(Tree.CallExpr callExpr, Symbol f) {
		Type receiverType = callExpr.receiver == null ? ((ClassScope) table
				.lookForScope(Scope.Kind.CLASS)).getOwner().getType()
				: callExpr.receiver.type;
		if (f == null) {
			issueError(new FieldNotFoundError(callExpr.getLocation(),
					callExpr.method, receiverType.toString()));
			callExpr.type = BaseType.ERROR;
		} else if (!f.isFunction()) {
			issueError(new NotClassMethodError(callExpr.getLocation(),
					callExpr.method, receiverType.toString()));
			callExpr.type = BaseType.ERROR;
		} else {
			Function func = (Function) f;
			callExpr.symbol = func;
			callExpr.type = func.getReturnType();
			if (callExpr.receiver == null && currentFunction.isStatik()
					&& !func.isStatik()) {
				issueError(new RefNonStaticError(callExpr.getLocation(),
						currentFunction.getName(), func.getName()));
			}
			// TO-DO: Add code here.
			/////////////////////////////////////////////////////////////////////////////////////////////////////////////
			else
			{
				//FieldNotAccessError
				if (callExpr.receiver != null && callExpr.receiver.isClass && !func.isStatik())
				{
					issueError(new NotClassFieldError(callExpr.getLocation(), func.getName(), 
							callExpr.receiver.type.toString()));
					callExpr.type = BaseType.ERROR;
				}
				else
				{
					//BadArgCountError
					boolean goodArgCount = (func.isStatik() && func.getType().numOfParams() == callExpr.actuals.size()) || 
										   (!func.isStatik() && func.getType().numOfParams() == callExpr.actuals.size() + 1);
					if (!goodArgCount)
					{
						issueError(new BadArgCountError(callExpr.getLocation(), func.getName(), 
								func.getType().numOfParams(), callExpr.actuals.size()));
						callExpr.type = BaseType.ERROR;
					}
					else
					{
						int argsCount = func.getType().numOfParams();
						List<Type> formals = func.getType().getArgList();
						List<Expr> actuals = callExpr.actuals;
						
						for (int i = 0; i < actuals.size(); i ++)
							actuals.get(i).accept(this);
						
						//BadArgTypeError
						if (func.isStatik())
						{
							for (int i = 0; i < argsCount; i ++)
							{
								boolean goodArgType = actuals.get(i).type.compatible(formals.get(i)) ||
										(formals.get(i).isClassType() && actuals.get(i).type.equal(BaseType.NULL));
								if (!goodArgType)
								{
									issueError(new BadArgTypeError(actuals.get(i).getLocation(), i + 1, 
											actuals.get(i).type.toString(), formals.get(i).toString()));
									callExpr.type = BaseType.ERROR;
									break;
								}
							}
						}
						else
						{
							for (int i = 1; i < argsCount; i ++)
							{
								boolean goodArgType = actuals.get(i - 1).type.compatible(formals.get(i)) ||
										(formals.get(i).isClassType() && actuals.get(i - 1).type.equal(BaseType.NULL));
								if (!goodArgType)
								{
									issueError(new BadArgTypeError(actuals.get(i - 1).getLocation(), i, 
											actuals.get(i - 1).type.toString(), formals.get(i).toString()));
									callExpr.type = BaseType.ERROR;
									break;
								}
							}
						}

					}	
				}
			}
			/////////////////////////////////////////////////////////////////////////////////////////////////////////////
		}
	}

	@Override
	public void visitCallExpr(Tree.CallExpr callExpr) {
		if (callExpr.receiver == null) {
			ClassScope cs = (ClassScope) table.lookForScope(Kind.CLASS);
			checkCallExpr(callExpr, cs.lookupVisible(callExpr.method));
			return;
		}
		callExpr.receiver.usedForRef = true;
		callExpr.receiver.accept(this);

		if (callExpr.receiver.type.equal(BaseType.ERROR)) {
			callExpr.type = BaseType.ERROR;
			return;
		}
		if (callExpr.method.equals("length")) {
			if (callExpr.receiver.type.isArrayType()) {
				if (callExpr.actuals.size() > 0) {
					issueError(new BadLengthArgError(callExpr.getLocation(),
							callExpr.actuals.size()));
				}
				callExpr.type = BaseType.INT;
				callExpr.isArrayLength = true;
				return;
			} else if (!callExpr.receiver.type.isClassType()) {
				issueError(new BadLengthError(callExpr.getLocation()));
				callExpr.type = BaseType.ERROR;
				return;
			}
		}

		if (!callExpr.receiver.type.isClassType()) {
			issueError(new NotClassFieldError(callExpr.getLocation(),
					callExpr.method, callExpr.receiver.type.toString()));
			callExpr.type = BaseType.ERROR;
			return;
		}

		ClassScope cs = ((ClassType) callExpr.receiver.type)
				.getClassScope();
		checkCallExpr(callExpr, cs.lookupVisible(callExpr.method));
	}

	@Override
	public void visitExec(Tree.Exec exec){
		exec.expr.accept(this);
	}
	
	@Override
	public void visitNewArray(Tree.NewArray newArrayExpr) {
		// TO-DO
		newArrayExpr.elementType.accept(this);
		if (newArrayExpr.elementType.type.equal(BaseType.ERROR))
			newArrayExpr.type = BaseType.ERROR;
		else
			newArrayExpr.type = new ArrayType(newArrayExpr.elementType.type);
		
		newArrayExpr.length.accept(this);
		if (!newArrayExpr.length.type.equal(BaseType.INT))
		{
			issueError(new BadNewArrayLength(newArrayExpr.length.getLocation()));
			newArrayExpr.type = BaseType.ERROR;
		}		
	}

	@Override
	public void visitNewClass(Tree.NewClass newClass) {
		Class c = table.lookupClass(newClass.className);
		newClass.symbol = c;
		if (c == null) {
			issueError(new ClassNotFoundError(newClass.getLocation(),
					newClass.className));
			newClass.type = BaseType.ERROR;
		} else {
			newClass.type = c.getType();
		}
	}

	@Override
	public void visitThisExpr(Tree.ThisExpr thisExpr) {
		if (currentFunction.isStatik()) {
			issueError(new ThisInStaticFuncError(thisExpr.getLocation()));
			thisExpr.type = BaseType.ERROR;
		} else {
			thisExpr.type = ((ClassScope) table.lookForScope(Scope.Kind.CLASS))
					.getOwner().getType();
		}
		
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////
//	@Override
//	public void visitTypeTest(Tree.TypeTest instanceofExpr) {
//		instanceofExpr.instance.accept(this);
//		if (!instanceofExpr.instance.type.isClassType()) {
//			issueError(new NotClassError(instanceofExpr.instance.type
//					.toString(), instanceofExpr.getLocation()));
//		}
//		Class c = table.lookupClass(instanceofExpr.className);
//		instanceofExpr.symbol = c;
//		instanceofExpr.type = BaseType.BOOL;
//		if (c == null) {
//			issueError(new ClassNotFoundError(instanceofExpr.getLocation(),
//					instanceofExpr.className));
//		}
//	}
	///////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public void visitTypeCast(Tree.TypeCast cast) {
		cast.expr.accept(this);
		if (!cast.expr.type.isClassType()) {
			issueError(new NotClassError(cast.expr.type.toString(),
					cast.getLocation()));
		}
		Class c = table.lookupClass(cast.className);
		cast.symbol = c;
		if (c == null) {
			issueError(new ClassNotFoundError(cast.getLocation(),
					cast.className));
			cast.type = BaseType.ERROR;
		} else {
			cast.type = c.getType();
		}
	}

	@Override
	public void visitIdent(Tree.Ident ident) {
		if (ident.owner == null) 
		{
			Symbol v = table.lookupBeforeLocation(ident.name, ident.getLocation());
			if (v == null)
			{
				issueError(new UndeclVarError(ident.getLocation(), ident.name));
				ident.type = BaseType.ERROR;
			}
			else 
			{
				if (v.isVariable()) 
				{
				// TO-DO: Add code here
					ident.type = v.getType();
					if ((!v.getScope().isLocalScope() && !v.getScope().isFormalScope()) &&
							currentFunction.isStatik())
						issueError(new RefNonStaticError(ident.getLocation(), currentFunction.getName(), ident.name));
				}
				else 
				{
				// TO-DO: Add code here
				//
					ident.type = v.getType();
					if (v.isClass())
						ident.isClass = true;
				}
			}
		}
		else 
		{
			ident.owner.usedForRef = true;
			ident.owner.accept(this);
			if (!ident.owner.type.equal(BaseType.ERROR)) {
				if (ident.owner.isClass || !ident.owner.type.isClassType()) {
					issueError(new NotClassFieldError(ident.getLocation(),
							ident.name, ident.owner.type.toString()));
					ident.type = BaseType.ERROR;
				} else {
					ClassScope cs = ((ClassType) ident.owner.type)
							.getClassScope();
					Symbol v = cs.lookupVisible(ident.name);
					if (v == null) {
						issueError(new FieldNotFoundError(ident.getLocation(),
								ident.name, ident.owner.type.toString()));
						ident.type = BaseType.ERROR;
					} else if (v.isVariable()) {
						ClassType thisType = ((ClassScope) table
								.lookForScope(Scope.Kind.CLASS)).getOwner()
								.getType();
						ident.type = v.getType();
////////////////////////////////////////////////////////////////////////////////////////////////////////
						//所有的成员变量都不会是public的，所以，一旦用owner.var方式访问，必须在该类（及子类）的内部，即this和owner是
						//兼容的，否则就报告访问权限错误
////////////////////////////////////////////////////////////////////////////////////////////////////////
						if (!thisType.compatible(ident.owner.type)) {
							issueError(new FieldNotAccessError(ident
									.getLocation(), ident.name,
									ident.owner.type.toString()));
						} else {
							ident.symbol = (Variable) v;
							ident.lvKind = Tree.LValue.Kind.MEMBER_VAR;
						}
					} else {
						ident.type = v.getType();
					}
				}
			} else {
				ident.type = BaseType.ERROR;
			}
		}
	}

	@Override
	public void visitClassDef(Tree.ClassDef classDef) {
		table.open(classDef.symbol.getAssociatedScope());
		for (Tree f : classDef.fields) {
			f.accept(this);
		}
		table.close();
	}

	@Override
	public void visitMethodDef(Tree.MethodDef func) {
		this.currentFunction = func.symbol;
		table.open(func.symbol.getAssociatedScope());
		func.body.accept(this);
		table.close();
	}

	@Override
	public void visitTopLevel(Tree.TopLevel program) {
		table.open(program.globalScope);
		for (Tree.ClassDef cd : program.classes) {
			cd.accept(this);
		}
		table.close();
	}

	@Override
	public void visitBlock(Tree.Block block) {
		table.open(block.associatedScope);
		for (Tree s : block.block) {
			s.accept(this);
		}
		table.close();
	}

	@Override
	public void visitAssign(Tree.Assign assign) {
		// TO-DO: Add code here.
		assign.expr.accept(this);
		assign.left.accept(this);
		
		assign.type = assign.expr.type;
		
		boolean goodAssign = assign.expr.type.compatible(assign.left.type) ||
				(assign.left.type.isClassType() && assign.expr.type.equal(BaseType.NULL));
		if (!goodAssign)
			issueError(new IncompatBinOpError(assign.getLocation(), assign.left.type.toString(),
					"=", assign.expr.type.toString()));	

		
	}

	@Override
	public void visitBreak(Tree.Break breakStmt) {
		//TO-DO
		if (breaks.empty()) 
		{
			issueError(new BreakOutOfLoopError(breakStmt.getLocation()));
		}
	}
	
	@Override
	public void visitForLoop(Tree.ForLoop forLoop) {
		if (forLoop.init != null) {
			forLoop.init.accept(this);
		}
		checkTestExpr(forLoop.condition);
		if (forLoop.update != null) {
			forLoop.update.accept(this);
		}
		breaks.add(forLoop);
		if (forLoop.loopBody != null) {
			forLoop.loopBody.accept(this);
		}
		breaks.pop();
	}

	@Override
	public void visitRepeatLoop(Tree.RepeatLoop repeatLoop) {
		// TO-DO
		// repeat循环。参考visitWhileLoop，自行修改Tree，
		checkTestExpr(repeatLoop.condition);
		breaks.add(repeatLoop);
		if (repeatLoop.loopBody != null) 
		{
			repeatLoop.loopBody.accept(this);
		}
		breaks.pop();
	}

	@Override
	public void visitIf(Tree.If ifStmt) {
		checkTestExpr(ifStmt.condition);
		if (ifStmt.trueBranch != null) {
			ifStmt.trueBranch.accept(this);
		}
		if (ifStmt.falseBranch != null) {
			ifStmt.falseBranch.accept(this);
		}
	}

	@Override
	public void visitPrint(Tree.Print printStmt) {
		int i = 0;
		for (Tree.Expr e : printStmt.exprs) {
			e.accept(this);
			i++;
/////////////////////////////////////////////////////////////////////////////////////////////////////
			if (!e.type.equal(BaseType.ERROR) && !e.type.equal(BaseType.BOOL)
					&& !e.type.equal(BaseType.INT)
					&& !e.type.equal(BaseType.STRING) && !e.type.equal(BaseType.DOUBLE)) 
			{
				issueError(new BadPrintArgError(e.getLocation(), Integer
						.toString(i), e.type.toString()));
			}
/////////////////////////////////////////////////////////////////////////////////////////////////////
		}
	}

	@Override
	public void visitReturn(Tree.Return returnStmt) {
		Type returnType = ((FormalScope) table
				.lookForScope(Scope.Kind.FORMAL)).getOwner().getReturnType();
		if (returnStmt.expr != null) {
			returnStmt.expr.accept(this);
		}
		// TO-DO: 检查返回值类型
		boolean validReturn = (returnStmt.expr == null && returnType.equal(BaseType.VOID)) ||
				(returnStmt.expr != null && returnStmt.expr.type.compatible(returnType));
		if (!validReturn)
			issueError(new BadReturnTypeError(returnStmt.getLocation(), returnType.toString(), 
					returnStmt.expr.type.toString()));
	}

	@Override
	public void visitWhileLoop(Tree.WhileLoop whileLoop) {
		checkTestExpr(whileLoop.condition);
		breaks.add(whileLoop);
		if (whileLoop.loopBody != null) {
			whileLoop.loopBody.accept(this);
		}
		breaks.pop();
	}

	// visiting types
	@Override
	public void visitTypeIdent(Tree.TypeIdent type) {
		switch (type.typeTag) {
		case Tree.VOID:
			type.type = BaseType.VOID;
			break;
		case Tree.INT:
			type.type = BaseType.INT;
			break;
		case Tree.BOOL:
			type.type = BaseType.BOOL;
			break;
//////////////////////////////////////////////////////////////////////////////////////////////
		case Tree.DOUBLE:
			type.type = BaseType.DOUBLE;
			break;
//////////////////////////////////////////////////////////////////////////////////////////////			
		default:
			type.type = BaseType.STRING;
		}
	}

	@Override
	public void visitTypeClass(Tree.TypeClass typeClass) {
		Class c = table.lookupClass(typeClass.name);
		if (c == null) {
			issueError(new ClassNotFoundError(typeClass.getLocation(),
					typeClass.name));
			typeClass.type = BaseType.ERROR;
		} else {
			typeClass.type = c.getType();
		}
	}

	@Override
	public void visitTypeArray(Tree.TypeArray typeArray) {
		// TO-DO
		typeArray.elementType.accept(this);
		if (typeArray.elementType.type.equal(BaseType.VOID))
		{
			issueError(new BadArrElementError(typeArray.elementType.getLocation()));
			typeArray.type = BaseType.ERROR;
		}
		else
			typeArray.type = new ArrayType(typeArray.elementType.type);
	}

	private void issueError(DecafError error) {
		Driver.getDriver().issueError(error);
	}

	private Type checkBinaryOp(Tree.Expr left, Tree.Expr right, int op, Location location) {
		left.accept(this);
		right.accept(this);
		
		if (left.type.equal(BaseType.ERROR) || right.type.equal(BaseType.ERROR)) {
			switch (op) {
			case Tree.PLUS:
			case Tree.MINUS:
			case Tree.MUL:
			case Tree.DIV:
				return left.type;
			case Tree.MOD:
				return BaseType.INT;
			default:
				return BaseType.BOOL;
			}
		}

		boolean compatible = false;
		Type returnType = BaseType.ERROR;
		switch (op) {
		case Tree.PLUS:
		case Tree.MINUS:
		case Tree.MUL:
		case Tree.DIV:
			compatible = left.type.equals(BaseType.INT)
					&& left.type.equal(right.type);
			if (!compatible)
				returnType = BaseType.ERROR;
			else
				returnType = left.type;
			//System.out.println(left.getLocation() + " " + compatible + " " + left.type.toString() + " " + right.type.toString());
			break;
		case Tree.GT:
		case Tree.GE:
		case Tree.LT:
		case Tree.LE:
			compatible = (left.type.equal(BaseType.INT) || left.type.equal(BaseType.DOUBLE)) && left.type.equal(right.type);
			if (!compatible)
				returnType = BaseType.ERROR;
			else
				returnType = BaseType.BOOL;
			break;
		case Tree.MOD:
			compatible = left.type.equal(BaseType.INT) && left.type.equal(right.type);
			if (!compatible)
				returnType = BaseType.ERROR;
			else
				returnType = BaseType.INT;
			break;
		case Tree.EQ:
		case Tree.NE:
			compatible = left.type.compatible(right.type) || right.type.compatible(left.type);
			if (!compatible)
				returnType = BaseType.ERROR;
			else
				returnType = BaseType.BOOL;
			break;
		case Tree.AND:
		case Tree.OR:
			compatible = left.type.equal(BaseType.BOOL) && left.type.equal(right.type);
			if (!compatible)
				returnType = BaseType.ERROR;
			else
				returnType = BaseType.BOOL;
			break;
		// TO-DO: 为上面每个case添加代码
		default:
			break;
		}

		if (!compatible) {
			issueError(new IncompatBinOpError(location, left.type.toString(),
					Parser.opStr(op), right.type.toString()));
		}
		return returnType;
	}

	private void checkTestExpr(Tree.Expr expr) {
		expr.accept(this);
		if (!expr.type.equal(BaseType.ERROR) && !expr.type.equal(BaseType.BOOL)) {
			issueError(new BadTestExpr(expr.getLocation()));
		}
	}

}
