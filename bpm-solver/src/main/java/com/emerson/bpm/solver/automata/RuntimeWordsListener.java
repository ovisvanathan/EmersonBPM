package com.emerson.bpm.solver.automata;

// Generated from RuntimeWords.g4 by ANTLR 4.8
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link RuntimeWordsParser}.
 */
public interface RuntimeWordsListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link RuntimeWordsParser#parse}.
	 * @param ctx the parse tree
	 */
	void enterParse(RuntimeWordsParser.ParseContext ctx);
	/**
	 * Exit a parse tree produced by {@link RuntimeWordsParser#parse}.
	 * @param ctx the parse tree
	 */
	void exitParse(RuntimeWordsParser.ParseContext ctx);
}