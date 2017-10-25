package decaf.dataflow;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import decaf.machdesc.Asm;
import decaf.machdesc.Register;
import decaf.tac.Label;
import decaf.tac.Tac;
import decaf.tac.Temp;

public class BasicBlock {
	public int bbNum;

	public enum EndKind {
		BY_BRANCH, BY_BEQZ, BY_BNEZ, BY_RETURN
	}

	public EndKind endKind;

	public int inDegree;

	public Tac tacList;

	public Label label;

	public Temp var;

	public Register varReg;

	public int[] next;

	public boolean cancelled;

	public boolean mark;

	public Set<Temp> def;

	public Set<Temp> liveUse;

	public Set<Temp> liveIn;

	public Set<Temp> liveOut;

	public Set<Temp> saves;

	private List<Asm> asms;

	public BasicBlock() {
		def = new TreeSet<Temp>(Temp.ID_COMPARATOR);
		liveUse = new TreeSet<Temp>(Temp.ID_COMPARATOR);
		liveIn = new TreeSet<Temp>(Temp.ID_COMPARATOR);
		liveOut = new TreeSet<Temp>(Temp.ID_COMPARATOR);
		next = new int[2];
		asms = new ArrayList<Asm>();
	}

	public void computeDefAndLiveUse() {
		// TODO
	}

	public void analyzeLiveness() {
		// TODO:
	}

	public void printTo(PrintWriter pw) {
		pw.println("BASIC BLOCK " + bbNum + " : ");
		for (Tac t = tacList; t != null; t = t.next) {
			pw.println("    " + t);
		}
		switch (endKind) {
		case BY_BRANCH:
			pw.println("END BY BRANCH, goto " + next[0]);
			break;
		case BY_BEQZ:
			pw.println("END BY BEQZ, if " + var.name + " = ");
			pw.println("    0 : goto " + next[0] + "; 1 : goto " + next[1]);
			break;
		case BY_BNEZ:
			pw.println("END BY BGTZ, if " + var.name + " = ");
			pw.println("    1 : goto " + next[0] + "; 0 : goto " + next[1]);
			break;
		case BY_RETURN:
			if (var != null) {
				pw.println("END BY RETURN, result = " + var.name);
			} else {
				pw.println("END BY RETURN, void result");
			}
			break;
		}
	}

	public void printLivenessTo(PrintWriter pw) {
		pw.println("BASIC BLOCK " + bbNum + " : ");
		pw.println("  Def     = " + toString(def));
		pw.println("  liveUse = " + toString(liveUse));
		pw.println("  liveIn  = " + toString(liveIn));
		pw.println("  liveOut = " + toString(liveOut));

		for (Tac t = tacList; t != null; t = t.next) {
			pw.println("    " + t + " " + toString(t.liveOut));
		}

		switch (endKind) {
		case BY_BRANCH:
			pw.println("END BY BRANCH, goto " + next[0]);
			break;
		case BY_BEQZ:
			pw.println("END BY BEQZ, if " + var.name + " = ");
			pw.println("    0 : goto " + next[0] + "; 1 : goto " + next[1]);
			break;
		case BY_BNEZ:
			pw.println("END BY BGTZ, if " + var.name + " = ");
			pw.println("    1 : goto " + next[0] + "; 0 : goto " + next[1]);
			break;
		case BY_RETURN:
			if (var != null) {
				pw.println("END BY RETURN, result = " + var.name);
			} else {
				pw.println("END BY RETURN, void result");
			}
			break;
		}
	}

	public String toString(Set<Temp> set) {
		StringBuilder sb = new StringBuilder("[ ");
		for (Temp t : set) {
			sb.append(t.name + " ");
		}
		sb.append(']');
		return sb.toString();
	}

	public void insertBefore(Tac insert, Tac base) {
		if (base == tacList) {
			tacList = insert;
		} else {
			base.prev.next = insert;
		}
		insert.prev = base.prev;
		base.prev = insert;
		insert.next = base;
	}

	public void insertAfter(Tac insert, Tac base) {
		if (tacList == null) {
			tacList = insert;
			insert.next = null;
			return;
		}
		if (base.next != null) {
			base.next.prev = insert;
		}
		insert.prev = base;
		insert.next = base.next;
		base.next = insert;
	}

	public void appendAsm(Asm asm) {
		asms.add(asm);
	}

	public List<Asm> getAsms() {
		return asms;
	}
}
