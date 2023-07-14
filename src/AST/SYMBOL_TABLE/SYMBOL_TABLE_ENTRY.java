/***********/
/* PACKAGE */
/***********/
package SYMBOL_TABLE;

/*******************/
/* PROJECT IMPORTS */
/*******************/
import TYPES.*;

/**********************/
/* SYMBOL TABLE ENTRY */
/**********************/
public class SYMBOL_TABLE_ENTRY
{
	/*********/
	/* index */
	/*********/
	int index;
	
	/********/
	/* name */
	/********/
	public String name;

	/******************/
	/* TYPE value ... */
	/******************/
	public TYPE type;

	/*********************************************/
	/* prevtop and next symbol table entries ... */
	/*********************************************/
	public SYMBOL_TABLE_ENTRY prevtop;
	public SYMBOL_TABLE_ENTRY next;

	/****************************************************/
	/* The prevtop_index is just for debug purposes ... */
	/****************************************************/
	public int prevtop_index;

	/****************************************************/
	/* param vs local and the index in the scope 	... */
	/****************************************************/
	public PARAM_LOCAL paramLocal;

	/****************************************************/
	/* count how many locals in scope
	(will be maintained only for SCOPE-BOUNDARIES) 	... */
	/****************************************************/
	int localsCounter = 0;


	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public SYMBOL_TABLE_ENTRY(
		String name,
		TYPE type,
		int index,
		SYMBOL_TABLE_ENTRY next,
		SYMBOL_TABLE_ENTRY prevtop,
		int prevtop_index) {
		this.index = index;
		this.name = name;
		this.type = type;
		this.next = next;
		this.prevtop = prevtop;
		this.prevtop_index = prevtop_index;
	}
	public SYMBOL_TABLE_ENTRY(
			String name,
			TYPE type,
			int index,
			SYMBOL_TABLE_ENTRY next,
			SYMBOL_TABLE_ENTRY prevtop,
			int prevtop_index,
			PARAM_LOCAL paramLocal) {
		this(name, type, index, next, prevtop, prevtop_index);
		this.paramLocal = paramLocal;
	}

	public void increaseLocalsCounter(){
		++localsCounter;
	}
	public int getLocalsCounter(){
		return localsCounter;
	}
}
